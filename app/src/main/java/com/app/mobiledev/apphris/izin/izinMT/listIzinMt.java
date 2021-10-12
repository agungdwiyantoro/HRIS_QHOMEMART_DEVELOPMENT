package com.app.mobiledev.apphris.izin.izinMT;

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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.app.mobiledev.apphris.R;
import com.app.mobiledev.apphris.api.api;
import com.app.mobiledev.apphris.helperPackage.helper;
import com.app.mobiledev.apphris.izin.izinMT.adapterizinMt.adapterizinMt;
import com.app.mobiledev.apphris.izin.izinMT.modelizinMt.modelizinMt;
import com.app.mobiledev.apphris.sesion.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class listIzinMt extends AppCompatActivity {
    private List<modelizinMt> modelizinMt;
    private RecyclerView rc_riwayat_mt;
    private TextView ed_cari;
    private ProgressBar progressBar;
    private SessionManager mSession;
    private String kyano="";
    private FloatingActionButton float_add_izinMt;
    private Toolbar toolbar_izin_mt;
    private SwipeRefreshLayout swiperefresh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_izin_mt);
        rc_riwayat_mt=findViewById(R.id.rc_riwayat_mt);
        float_add_izinMt=findViewById(R.id.float_add_izinMt);
        ed_cari=findViewById(R.id.ed_cari);
        modelizinMt = new ArrayList<>();
        swiperefresh=findViewById(R.id.swiperefresh);
        swiperefresh.setColorSchemeResources(R.color.blue);
        progressBar=findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        mSession=new SessionManager(this);
        kyano=mSession.getIdUser();
        toolbar_izin_mt = findViewById(R.id.toolbar_izin_mt);
        setSupportActionBar(toolbar_izin_mt);
        toolbar_izin_mt.setTitle("List Izin Meninggalkan Tugas");
        setSupportActionBar(toolbar_izin_mt);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar_izin_mt.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        float_add_izinMt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(listIzinMt.this, form_izin_meninggalkan_tugas.class));
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

                String cari= ed_cari.getText().toString();
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

        getIzinDinas(kyano,"");


    }

    public void getIzinDinas(final String kyano, String cari){
        AndroidNetworking.post(api.URL_getRiwayatIzin_mt)
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
                                modelizinMt.clear();
                                JSONArray jsonArray = response.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject data = jsonArray.getJSONObject(i);
                                    modelizinMt model = new modelizinMt();
                                    model.setTjano(data.getString("tjano"));
                                    model.setKyano(data.getString("kyano"));
                                    model.setDvano(data.getString("dvano"));
                                    model.setJbano(data.getString("jbano"));
                                    model.setTgl(data.getString("tgl"));
                                    model.setJam(data.getString("jam"));
                                    model.setSampai(data.getString("sampai"));
                                    model.setKepentingan(data.getString("kepentingan"));
                                    model.setCatatan(data.getString("catatan"));
                                    model.setStatus(data.getString("status"));
                                    model.setImage(data.getString("image"));

                                    model.setAprove(data.getString("aprove"));
                                    model.setAproveBy(data.getString("aproveBy"));
                                    model.setAproveDate(data.getString("aproveDate"));


                                    modelizinMt.add(model);
                                }
                                adapterizinMt mAdapter;
                                mAdapter = new adapterizinMt(modelizinMt, listIzinMt.this);
                                mAdapter.notifyDataSetChanged();
                                rc_riwayat_mt.setLayoutManager(new LinearLayoutManager(listIzinMt.this));
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
                        helper.showMsg(listIzinMt.this, "Peringatan", ""+helper.PESAN_KONEKSI, helper.ERROR_TYPE);
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