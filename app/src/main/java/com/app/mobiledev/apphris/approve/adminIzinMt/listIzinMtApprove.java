package com.app.mobiledev.apphris.approve.adminIzinMt;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.app.mobiledev.apphris.R;
import com.app.mobiledev.apphris.api.api;
import com.app.mobiledev.apphris.approve.adminIzinMt.adapterAprroveMt.adapterApproveMt;
import com.app.mobiledev.apphris.approve.adminIzinMt.modelApproveMt.modelApproveMt;
import com.app.mobiledev.apphris.helperPackage.helper;
import com.app.mobiledev.apphris.sesion.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class listIzinMtApprove extends AppCompatActivity {
    private EditText ed_cari;
    private RecyclerView rc_riwayat_mt;
    private List<modelApproveMt> modelizinApproveMt;
    private ProgressBar progressBar;
    private SessionManager mSession;
    private String kyano;
    private Toolbar toolbar_izin_mt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_approve_izin_mt);
        progressBar=findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        toolbar_izin_mt=findViewById(R.id.toolbar_izin_mt);
        rc_riwayat_mt=findViewById(R.id.rc_riwayat_mt);
        ed_cari=findViewById(R.id.ed_cari);
        modelizinApproveMt = new ArrayList<>();
        mSession=new SessionManager(this);
        kyano=mSession.getIdUser();
        getIzinMt(kyano,"");

        ed_cari.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String cari=ed_cari.getText().toString();
                getIzinMt(kyano,cari);
            }
        });
    }


    public void getIzinMt(final String kyano, String cari){
        AndroidNetworking.post(api.URL_getRiwayatMt_approve)
                .addBodyParameter("kyano", kyano)
                .addBodyParameter("key", api.key)
                .addBodyParameter("cari", cari)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Boolean success = response.getBoolean("success");
                            if (success) {
                                modelizinApproveMt.clear();
                                JSONArray jsonArray = response.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject data = jsonArray.getJSONObject(i);
                                    modelApproveMt model = new modelApproveMt();
                                    model.setTjano(data.getString("tjano"));
                                    model.setKyano(data.getString("kyano"));
                                    model.setDvano(data.getString("dvano"));
                                    model.setJbano(data.getString("jbano"));
                                    model.setJam(data.getString("jam"));
                                    model.setTgl(data.getString("tgl"));
                                    model.setSampai(data.getString("sampai"));
                                    model.setKepentingan(data.getString("kepentingan"));
                                    model.setCatatan(data.getString("catatan"));
                                    model.setStatus(data.getString("status"));
                                    model.setImage(data.getString("image"));
                                    model.setAprove(data.getString("aprove"));
                                    model.setAprove_by(data.getString("aprove_by"));
                                    model.setAprove_date(data.getString("aprove_date"));
                                    model.setAprove_hrd(data.getString("aprove_hrd"));
                                    model.setAprove_hrd_date(data.getString("aprove_hrd_date"));

                                    modelizinApproveMt.add(model);
                                }
                                adapterApproveMt mAdapter;
                                mAdapter = new adapterApproveMt(modelizinApproveMt, listIzinMtApprove.this);
                                mAdapter.notifyDataSetChanged();
                                rc_riwayat_mt.setLayoutManager(new LinearLayoutManager(listIzinMtApprove.this));
                                rc_riwayat_mt.setItemAnimator(new DefaultItemAnimator());
                                rc_riwayat_mt.setAdapter(mAdapter);
                            }else{
                                Log.d("DATA_BOOLEAN", "onResponse: "+success);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("JSONERORABSEN", "onResponse: "+e);
                        }
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(ANError anError) {
                        helper.showMsg(listIzinMtApprove.this, "Peringatan", ""+helper.PESAN_KONEKSI, helper.ERROR_TYPE);
                        Log.d("EROOR_EXCEPTION", "onError: "+anError);
                        progressBar.setVisibility(View.GONE);

                    }
                });

    }

    @Override
    public void onResume() {
        super.onResume();
        progressBar.setVisibility(View.VISIBLE);
        getIzinMt(kyano, "");
    }

}