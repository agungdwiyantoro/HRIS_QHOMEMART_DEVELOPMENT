package com.app.mobiledev.apphris.izin.izinDinas;

import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.app.mobiledev.apphris.helperPackage.helper;
import com.app.mobiledev.apphris.izin.izinDinas.adapaterIzinDinas.adapterRiwayatIzinDinas;
import com.app.mobiledev.apphris.izin.izinDinas.modelRiwayatIzinDinas.modelRiwayatIzinDinas;
import com.app.mobiledev.apphris.sesion.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class listIzinDinas extends AppCompatActivity {
    private List<modelRiwayatIzinDinas> modelRiwayatIzinDinass;
    private EditText ed_cari;
    private String kyano;
    SessionManager mSession;
    private RecyclerView rc_riwayat_dinas;
    private FloatingActionButton float_add_izinDinas;
    private ProgressBar progressBar;
    private SwipeRefreshLayout swiperefresh;
    private Toolbar toolbar_izin_dinas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_izin_dinas);
        rc_riwayat_dinas=findViewById(R.id.rc_riwayat_dinas);
        ed_cari=findViewById(R.id.ed_cari);
        float_add_izinDinas=findViewById(R.id.float_add_izinDinas);
        progressBar=findViewById(R.id.progressBar);
        swiperefresh=findViewById(R.id.swiperefresh);
        ed_cari=findViewById(R.id.ed_cari);
        swiperefresh.setColorSchemeResources(R.color.blue);
        modelRiwayatIzinDinass = new ArrayList<>();
        mSession=new SessionManager(listIzinDinas.this);
        kyano=mSession.getIdUser();
        toolbar_izin_dinas = findViewById(R.id.toolbar_izin_dinas);
        setSupportActionBar(toolbar_izin_dinas);
        toolbar_izin_dinas.setTitle("List Izin Dinas");
        setSupportActionBar(toolbar_izin_dinas);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar_izin_dinas.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        float_add_izinDinas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(listIzinDinas.this, form_izin_dinas.class));
            }
        });
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
                getIzinDinas(kyano,cari);


            }
        });
        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override public void run() {
                        swiperefresh.setRefreshing(false);
                        progressBar.setVisibility(View.VISIBLE);
                        getIzinDinas(kyano,"");
                        // getMenu();
                    }
                }, 2000);
            }
        });
        progressBar.setVisibility(View.VISIBLE);
        getIzinDinas(kyano,"");
    }


    public void getIzinDinas(final String kyano, String cari){
        AndroidNetworking.post(api.URL_getRiwayatDinas)
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
                                modelRiwayatIzinDinass.clear();
                                JSONArray jsonArray = response.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject data = jsonArray.getJSONObject(i);
                                    modelRiwayatIzinDinas model = new modelRiwayatIzinDinas();
                                    model.setTdano(data.getString("tdano"));
                                    model.setKyano(data.getString("kyano"));
                                    model.setDvano(data.getString("dvano"));
                                    model.setJbano(data.getString("jbano"));
                                    model.setTgl(data.getString("tgl"));
                                    model.setDari(data.getString("dari"));
                                    model.setSampai(data.getString("sampai"));
                                    model.setDaerah(data.getString("daerah"));
                                    model.setKeperluan(data.getString("keperluan"));
                                    model.setTrans(data.getString("trans"));
                                    model.setAkomodasi(data.getString("akomodasi"));
                                    model.setKet(data.getString("ket"));
                                    model.setAprove_hrd_date(data.getString("aprove_hrd_date"));
                                    model.setAprove_hrd(data.getString("aprove_hrd"));
                                    model.setAprove_date(data.getString("aprove_date"));
                                    model.setAprove_by(data.getString("aprove_by"));
                                    modelRiwayatIzinDinass.add(model);
                                }
                                adapterRiwayatIzinDinas mAdapter;
                                mAdapter = new adapterRiwayatIzinDinas(modelRiwayatIzinDinass, listIzinDinas.this);
                                mAdapter.notifyDataSetChanged();
                                rc_riwayat_dinas.setLayoutManager(new LinearLayoutManager(listIzinDinas.this));
                                rc_riwayat_dinas.setItemAnimator(new DefaultItemAnimator());
                                rc_riwayat_dinas.setAdapter(mAdapter);
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
                        helper.showMsg(listIzinDinas.this, "Peringatan", ""+helper.PESAN_KONEKSI, helper.ERROR_TYPE);
                        Log.d("EROOR_EXCEPTION", "onError: "+anError);
                        progressBar.setVisibility(View.GONE);

                    }
                });

    }

    @Override
    protected void onResume() {
        super.onResume();
        getIzinDinas(kyano,"");

    }
}