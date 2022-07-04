package com.app.mobiledev.apphris.memo.peserta;

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
import com.app.mobiledev.apphris.helperPackage.helper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class list_peserta extends AppCompatActivity {
    private RecyclerView rcKaryawan;
    private List<modelPesertaMemo> mlistPeserta;
    private Toolbar mToolbar;
    private String key_memo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_peserta);

        mToolbar = findViewById(R.id.toolbar_list_peserta);
        mToolbar.setTitle("Memo List Peserta");
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
        mlistPeserta = new ArrayList<>();
        rcKaryawan=findViewById(R.id.rcKaryawan);
        getExtra_value();
        detMemo(key_memo);

    }
    private void getExtra_value(){
        try {
            key_memo= getIntent().getExtras().getString("key_memo");
        }catch (NullPointerException e){
            Log.d("NULL", "getExtra_value: "+e);
        }



    }

    private void detMemo(String no_memo){
        AndroidNetworking.post(api.URL_detMemo)
                .addBodyParameter("key", api.key)
                .addBodyParameter("no_memo", no_memo)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            Boolean success = response.getBoolean("success");

                            Log.d("CEK_DETAIL_MEMO", "onResponse: "+success);
                            if (success) {
                                mlistPeserta.clear();
                                JSONArray jsonArray = response.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject data = jsonArray.getJSONObject(i);
                                    modelPesertaMemo model = new modelPesertaMemo();
                                    model.setMmANo(data.getString("MmANo"));
                                    model.setKyano(data.getString("kyano"));
                                    model.setPeserta(data.getString("peserta"));
                                    // Log.d("CEK_DETAIL_MEMO", "onResponse: "+data.getString("peserta"));
                                    mlistPeserta.add(model);
                                }
                                adapterPesertaMemo mAdapter = new adapterPesertaMemo(mlistPeserta, list_peserta.this);
                                rcKaryawan.setLayoutManager(new LinearLayoutManager(list_peserta.this));
                                rcKaryawan.setItemAnimator(new DefaultItemAnimator());
                                rcKaryawan.setAdapter(mAdapter);
                                mAdapter.notifyDataSetChanged();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("JSONEEVENTT", "onResponse: "+e);
                            helper.showMsg(list_peserta.this, "Peringatan", ""+ helper.PESAN_SERVER, helper.ERROR_TYPE);
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        Log.d("EROOR_EVENT", "onError: "+anError);
                        helper.showMsg(list_peserta.this, "Peringatan", ""+ helper.PESAN_KONEKSI, helper.ERROR_TYPE);
                    }
                });

    }
}
