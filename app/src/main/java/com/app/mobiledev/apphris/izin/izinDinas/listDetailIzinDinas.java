package com.app.mobiledev.apphris.izin.izinDinas;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.app.mobiledev.apphris.R;
import com.app.mobiledev.apphris.api.api;
import com.app.mobiledev.apphris.helperPackage.helper;
import com.app.mobiledev.apphris.izin.izinDinas.adapterListDetailIzinDinas.adapterListDetailIzinDinas;
import com.app.mobiledev.apphris.izin.izinDinas.modelListDetailIzinDinas.modelListDetailIzinDinas;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class listDetailIzinDinas extends AppCompatActivity {
    private List<modelListDetailIzinDinas> modelListDetailIzinDinass;
    private RecyclerView rc_detail_izinDinas;
    private String tdano;
    private FloatingActionButton float_add_izinDinas;
    private ImageView image_no_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_detail_izin_dinas);
        rc_detail_izinDinas=findViewById(R.id.rc_detail_izinDinas);
        float_add_izinDinas=findViewById(R.id.float_add_izinDinas);
        image_no_data=findViewById(R.id.image_no_data);
        modelListDetailIzinDinass = new ArrayList<>();

        float_add_izinDinas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(listDetailIzinDinas.this, formDetailIzinDinas.class);
                Bundle x = new Bundle();
                x.putString("tdano", tdano);
                x.putString("image", "");

                intent.putExtras(x);
                startActivity(intent);

            }
        });
        getId_izin_dinas();
        getDetailIzinDinas(tdano,"");
    }

    private void getDetailIzinDinas(final String tdano, String cari){
        AndroidNetworking.post(api.URL_getRiewayatDetailDinas)
                .addBodyParameter("tdano", tdano)
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
                                modelListDetailIzinDinass.clear();
                                JSONArray jsonArray = response.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject data = jsonArray.getJSONObject(i);
                                    modelListDetailIzinDinas model = new modelListDetailIzinDinas();
                                    model.setTdano(data.getString("tdano"));
                                    model.setKyano(data.getString("kyano"));
                                    model.setDvano(data.getString("dvano"));
                                    model.setJbano(data.getString("jbano"));
                                    model.setImage(data.getString("image"));
                                    model.setLang(data.getString("lang"));
                                    model.setLat(data.getString("lat"));
                                    model.setSTATUS(data.getString("status"));
                                    model.setTgl(data.getString("tanggal"));
                                    model.setKynm(data.getString("kynm"));
                                    model.setKeperluan(data.getString("keperluan"));
                                    modelListDetailIzinDinass.add(model);
                                }
                                adapterListDetailIzinDinas mAdapter;
                                mAdapter = new adapterListDetailIzinDinas(modelListDetailIzinDinass, listDetailIzinDinas.this);
                                mAdapter.notifyDataSetChanged();
                                rc_detail_izinDinas.setLayoutManager(new LinearLayoutManager(listDetailIzinDinas.this));
                                rc_detail_izinDinas.setItemAnimator(new DefaultItemAnimator());
                                rc_detail_izinDinas.setAdapter(mAdapter);
                            }else{
                                Log.d("DATA_BOOLEAN", "onResponse: "+success);
                                image_no_data.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("JSONERORABSEN", "onResponse: "+e);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        helper.showMsg(listDetailIzinDinas.this, "Peringatan", ""+helper.PESAN_KONEKSI, helper.ERROR_TYPE);
                        Log.d("EROOR_EXCEPTION", "onError: "+anError);

                    }
                });

    }


    private void getId_izin_dinas(){
        Bundle bundle = getIntent().getExtras();
        if(bundle == null) {
            tdano= "";
        } else {
            tdano= bundle.getString("tdano");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getDetailIzinDinas(tdano,"");
    }
}