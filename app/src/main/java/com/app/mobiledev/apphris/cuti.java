package com.app.mobiledev.apphris;

import android.app.ProgressDialog;
import android.content.Intent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.app.mobiledev.apphris.api.api;
import com.app.mobiledev.apphris.helperPackage.helper;
import com.app.mobiledev.apphris.riwayat_cuti.adapterCuti;
import com.app.mobiledev.apphris.riwayat_cuti.modelCuti;
import com.app.mobiledev.apphris.sesion.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class cuti extends AppCompatActivity {
    private RecyclerView riwayat;
    private List<modelCuti> modelCuti;
    private SessionManager sessionmanager;
    private ProgressDialog mProgressDialog;
    private  String kyano,nama;
    private Toolbar mToolbar;
    private FloatingActionButton form_cuti;
    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuti);
        riwayat=findViewById(R.id.riwayat);
        form_cuti=findViewById(R.id.form_cuti);

        modelCuti = new ArrayList<>();
        sessionmanager = new SessionManager(cuti.this);
        kyano=sessionmanager.getIdUser();
        nama=sessionmanager.getNamaLEngkap();
        mToolbar = findViewById(R.id.toolbar_abs);
        mToolbar.setTitle("Cuti");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        AndroidNetworking.initialize(getApplicationContext());
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        form_cuti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(cuti.this, form_cuti.class);
                startActivity(intent);
                finish();

            }
        });

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Loading ...");
        listRiwayat();

    }



    private void listRiwayat(){
        AndroidNetworking.post(api.URL_getHistoryCuti)
                .addBodyParameter("kyano", kyano)
                .addBodyParameter("key", api.key)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Boolean success = response.getBoolean("success");
                            if (success) {
                                JSONArray jsonArray = response.getJSONArray("data");
                                modelCuti.clear();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject data = jsonArray.getJSONObject(i);
                                    modelCuti model = new modelCuti();
                                    String ctano=data.getString("ctano");
                                    String kyano=data.getString("kyano");
                                    String dvano=data.getString("dvano");
                                    String jbano=data.getString("jbano");
                                    String ctlama=data.getString("ctlama");
                                    String ctmulai=data.getString("ctmulai");
                                    String ctselesai=data.getString("ctselesai");
                                    String ctalasan=data.getString("ctalasan");
                                    String ctstatus=data.getString("status");
                                    String ctket=data.getString("ket");
                                    String ctperiode=data.getString("ctperiode");
                                    String cttelp=data.getString("cttelp");
                                    String ctsisahak=data.getString("ctsisahak");
                                    String ctjenis=data.getString("ctjenis");
                                    String ctgantihari=data.getString("ctgantihari");
                                    String ctgantitgl=data.getString("ctgantitgl");
                                    String ctgambar=data.getString("ctgambar");
                                    String jbanama=data.getString("jbnama");
                                    String dvnama=data.getString("dvnama");


                                    model.setCtano(ctano);
                                    model.setKyano(kyano);
                                    model.setDvano(dvano);
                                    model.setJbano(jbano);
                                    model.setCtlama(ctlama);
                                    model.setCtmulai(ctmulai);
                                    model.setCtselesai(ctselesai);
                                    model.setCtalasan(ctalasan);
                                    model.setStatus(ctstatus);
                                    model.setKet(ctket);
                                    model.setCtperiode(ctperiode);
                                    model.setCttelp(cttelp);
                                    model.setCtsisahak(ctsisahak);
                                    model.setCtjenis(ctjenis);
                                    model.setCtgantihari(ctgantihari);
                                    model.setCtgantitgl(ctgantitgl);
                                    model.setCtgambar(ctgambar);
                                    model.setJbnama(jbanama);
                                    model.setDvnama(dvnama);
                                    modelCuti.add(model);

                                }

                                adapterCuti mAdapter;
                                mAdapter = new adapterCuti(modelCuti, cuti.this);
                                mAdapter.notifyDataSetChanged();
                                riwayat.setLayoutManager(new LinearLayoutManager(cuti.this));
                                riwayat.setItemAnimator(new DefaultItemAnimator());
                                riwayat.setAdapter(mAdapter);



                            }else{

                                Log.d("DATA_BOOLEAN", "onResponse: "+success);
                            }
                            mProgressDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("JSONERORABSEN", "onResponse: "+e);
                            helper.showMsg(cuti.this, "Peringatan", ""+helper.PESAN_SERVER, helper.ERROR_TYPE);
                            mProgressDialog.dismiss();
                        }catch (NullPointerException e){
                            Log.d("JSONERORABSEN", "onResponse: "+e);
                        }catch (NumberFormatException e){
                            Log.d("JSONERORABSEN", "onResponse: "+e);
                        }


                    }

                    @Override
                    public void onError(ANError anError) {
                        helper.showMsg(cuti.this, "Peringatan", ""+helper.PESAN_KONEKSI, helper.ERROR_TYPE);
                        Log.d("EROOR_EXCEPTION", "onError: "+anError);
                        mProgressDialog.dismiss();


                    }
                });
    }






}
