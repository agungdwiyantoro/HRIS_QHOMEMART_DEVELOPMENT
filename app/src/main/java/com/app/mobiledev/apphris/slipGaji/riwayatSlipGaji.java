package com.app.mobiledev.apphris.slipGaji;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.app.mobiledev.apphris.R;
import com.app.mobiledev.apphris.api.api;
import com.app.mobiledev.apphris.sesion.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class riwayatSlipGaji extends AppCompatActivity {

    private RecyclerView rvSlipGaji;
    private List<modelSlipGaji> modelSlip;
    private SessionManager mSession;
    private Toolbar mToolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riwayat_slip_gaji);
        mToolbar = findViewById(R.id.toolbar_menu_bonus);
        mToolbar.setTitle("Riwayat Slip Gaji");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        rvSlipGaji=findViewById(R.id.rvSlipGaji);
        modelSlip = new ArrayList<>();
        mSession=new SessionManager(this);
        riwayatSlipGaji();
    }



    private void riwayatSlipGaji(){
        AndroidNetworking.post(api.URL_getRiwayatSlipGaji)
                .addBodyParameter("key", api.key)
                .addBodyParameter("nik", mSession.getNik())
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
//                        modelRiwayatAbsens.clear();
                        try {
                            Boolean success = response.getBoolean("status");
                            if (success) {
                                JSONArray jsonArray = response.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject data = jsonArray.getJSONObject(i);
                                    modelSlipGaji model = new modelSlipGaji();
                                    String bulan=data.getString("bulan");
                                    String tahun=data.getString("tahun");
                                    String status_sent=data.getString("status");
                                    model.setBulan(bulan);
                                    model.setTahun(tahun);
                                    Log.d("JSONERORSLIP", "onResponse: "+bulan);
                                    model.setStatus_sent(status_sent);
                                    modelSlip.add(model);
                                }
                                adapterSlipGaji mAdapter;
                                mAdapter = new adapterSlipGaji(modelSlip, riwayatSlipGaji.this);
                                mAdapter.notifyDataSetChanged();
                                rvSlipGaji.setLayoutManager(new LinearLayoutManager(riwayatSlipGaji.this));
                                rvSlipGaji.setItemAnimator(new DefaultItemAnimator());
                                rvSlipGaji.setAdapter(mAdapter);
                            }else{
                                Log.d("DATA_BOOLEAN", "onResponse: "+success);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("JSONERORSLIP", "onResponse: "+e);


                        }catch (NullPointerException e){
                            Log.d("JSONERORSLIP", "onResponse: "+e);
                        }catch (NumberFormatException e){
                            Log.d("JSONERORSLIP", "onResponse: "+e);
                        }


                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("EROOR_EXCEPTION_slip", "onError: "+anError);
                    }
                });
    }
}