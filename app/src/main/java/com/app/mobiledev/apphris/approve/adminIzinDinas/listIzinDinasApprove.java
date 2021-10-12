package com.app.mobiledev.apphris.approve.adminIzinDinas;

import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
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
import com.app.mobiledev.apphris.approve.adminIzinDinas.adapterApproveDinas.adapterApproveDinas;
import com.app.mobiledev.apphris.approve.adminIzinDinas.modelApproveDinas.modelApproveDinas;
import com.app.mobiledev.apphris.helperPackage.helper;
import com.app.mobiledev.apphris.sesion.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class listIzinDinasApprove extends AppCompatActivity {
    private List<modelApproveDinas> modelApproveDinass;
    private EditText ed_cari;
    private String kyano;
    private String no_divisi;
    private SessionManager mSession;
    public static RecyclerView rc_riwayat_dinas;
    private ProgressBar progressBar;
    private SwipeRefreshLayout swiperefresh;
    private Toolbar toolbar_izin_dinas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_izin_dinas_approve);
        toolbar_izin_dinas = findViewById(R.id.toolbar_izin_dinas_approve);
        setSupportActionBar(toolbar_izin_dinas);
        toolbar_izin_dinas.setTitle("List Izin Dinas Approve");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar_izin_dinas.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        rc_riwayat_dinas=findViewById(R.id.rc_riwayat_dinas);
        ed_cari=findViewById(R.id.ed_cari);
        progressBar=findViewById(R.id.progressBar);
        swiperefresh=findViewById(R.id.swiperefresh);
        ed_cari=findViewById(R.id.ed_cari);
        swiperefresh.setColorSchemeResources(R.color.blue);
        modelApproveDinass = new ArrayList<>();
        mSession=new SessionManager(listIzinDinasApprove.this);
        kyano=mSession.getIdUser();
        no_divisi=mSession.getNodivisi();
        Log.d("CEK_JABATAN", "onClick: "+mSession.getJabatan());


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
                getIzinDinas(no_divisi,cari);


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
                        getIzinDinas(no_divisi,"");
                        // getMenu();
                    }
                }, 2000);
            }
        });
        progressBar.setVisibility(View.VISIBLE);
        getIzinDinas(no_divisi, "");
    }

    public void getIzinDinas(final String no_divisi, String cari){
        AndroidNetworking.post(api.URL_getRiwayatDinas_approve)
                .addBodyParameter("no_divisi", no_divisi)
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
                                modelApproveDinass.clear();
                                JSONArray jsonArray = response.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject data = jsonArray.getJSONObject(i);
                                    modelApproveDinas model = new modelApproveDinas();
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
                                    model.setApprove_hrd_date(data.getString("approve_hrd_date"));
                                    model.setApprove_hrd(data.getString("approve_hrd"));
                                    model.setApprove_head(data.getString("approve_head"));
                                    model.setKynm(data.getString("kynm"));
                                    modelApproveDinass.add(model);
                                }
                                adapterApproveDinas mAdapter;
                                mAdapter = new adapterApproveDinas(modelApproveDinass, listIzinDinasApprove.this);
                                mAdapter.notifyDataSetChanged();
                                rc_riwayat_dinas.setLayoutManager(new LinearLayoutManager(listIzinDinasApprove.this));
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
                        helper.showMsg(listIzinDinasApprove.this, "Peringatan", ""+helper.PESAN_KONEKSI, helper.ERROR_TYPE);
                        Log.d("EROOR_EXCEPTION", "onError: "+anError);
                        progressBar.setVisibility(View.GONE);

                    }
                });

    }

    @Override
    public void onResume() {
        super.onResume();
        progressBar.setVisibility(View.VISIBLE);
        getIzinDinas(no_divisi, "");
    }
}