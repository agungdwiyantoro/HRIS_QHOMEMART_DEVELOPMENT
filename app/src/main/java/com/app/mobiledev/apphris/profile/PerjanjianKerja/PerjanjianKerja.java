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
import com.app.mobiledev.apphris.memo.ModelMemo;
import com.app.mobiledev.apphris.memo.adapterMemoList;
import com.app.mobiledev.apphris.memo.listMemo.memo_list;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PerjanjianKerja extends AppCompatActivity {

    private Toolbar toolbar;

    private List<modelPerjanjianKerja> modelPerjanjianKerjaList;
    private RecyclerView rvKontrak;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perjanjian_kerja);

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
        AndroidNetworking.get("https://fakestoreapi.com/users")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {

                            //JSONArray jsonArray = response.getJSONArray();
                            Log.d("TAG_RESULT", "onResponse: "+response.toString());

                            for (int i = 0; i < response.length(); i++) {
                                JSONObject data = response.getJSONObject(i);

                                modelPerjanjianKerja model = new modelPerjanjianKerja();
                                model.setUsername(data.getString("username"));
                                model.setEmail(data.getString("email"));
                                model.setPhone(data.getString("phone"));

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