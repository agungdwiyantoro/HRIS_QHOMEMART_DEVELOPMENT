package com.app.mobiledev.apphris.profile.PerjanjianKerja;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.app.mobiledev.apphris.Model.modelPerjanjianKerja;
import com.app.mobiledev.apphris.R;
import com.app.mobiledev.apphris.api.api;
import com.app.mobiledev.apphris.sesion.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PerjanjianKerja extends AppCompatActivity {

    private Toolbar toolbar;

    private List<modelPerjanjianKerja> modelPerjanjianKerjaList;
    private RecyclerView rvKontrak;
    private String token;
    private SessionManager sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perjanjian_kerja);

        sp = new SessionManager(this);
        token = sp.getToken();
        Log.d("TAG_GET_TOKEN_KONTRAK", "onCreate: "+token);

        toolbar = findViewById(R.id.toolbar_kontrak);
        toolbar.setTitle("Daftar Perjanjian Kerja");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rvKontrak = findViewById(R.id.rvKontrak);
        modelPerjanjianKerjaList = new ArrayList<>();

        getListKontrak();

    }

    private void getListKontrak() {
        //AndroidNetworking.get("https://fakestoreapi.com/users")
        AndroidNetworking.get(api.URL_getListKontrak)
                .addHeaders("Authorization","Bearer "+token)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONArray jsonArray = response.getJSONArray("message");
                            Log.d("TAG_RESULT", "onResponse: "+jsonArray.toString());

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject data = jsonArray.getJSONObject(i);

                                modelPerjanjianKerja model = new modelPerjanjianKerja();
                                model.setMkano(data.getString("mkano"));
                                model.setJudulKontrak(data.getString("judul_kontrak"));
                                model.setMkstatuskerja(data.getString("mkstatuskerja"));
                                model.setMktglDari(data.getString("mktgl_dari"));
                                model.setMktglSampai(data.getString("mktgl_sampai"));
                                model.setFile(data.getString("file"));



                                modelPerjanjianKerjaList.add(model);
                            }

                            adapterPerjanjianKerja mAdapter = new adapterPerjanjianKerja(modelPerjanjianKerjaList,PerjanjianKerja.this);
                            mAdapter.notifyDataSetChanged();
                            rvKontrak.setVisibility(View.VISIBLE);
                            rvKontrak.setLayoutManager(new LinearLayoutManager(PerjanjianKerja.this, LinearLayoutManager.VERTICAL,false));
                            rvKontrak.setItemAnimator(new DefaultItemAnimator());
                            rvKontrak.setAdapter(mAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("TAG_ERROR_EXCEPTION", "onResponse: "+e.toString());
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("TAG_ERROR_RESULT", "onResponse: "+anError.toString());
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }
}