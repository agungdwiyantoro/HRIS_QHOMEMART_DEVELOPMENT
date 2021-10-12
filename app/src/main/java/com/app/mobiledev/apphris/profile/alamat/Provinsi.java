package com.app.mobiledev.apphris.profile.alamat;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.app.mobiledev.apphris.R;
import com.app.mobiledev.apphris.helperPackage.helper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class Provinsi extends AppCompatActivity {

    private ProgressDialog progressDialog;

    private List<ModelAlamat> modelAlamats;
    private RecyclerView rvAlamat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provinsi);
        rvAlamat = findViewById(R.id.rvAlamat);
        modelAlamats = new ArrayList<>();
        getProvinsi();
    }

    private void getProvinsi(){
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES)
                .writeTimeout(5, TimeUnit.MINUTES)
                .build();
        AndroidNetworking.initialize(Provinsi.this, okHttpClient);
        AndroidNetworking.get("https://dev.farizdotid.com/api/daerahindonesia/provinsi")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        modelAlamats.clear();
                        try {
                            JSONArray jsonArray = response.getJSONArray("provinsi");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject data = jsonArray.getJSONObject(i);
                                ModelAlamat model = new ModelAlamat();
                                int idAlamat = data.getInt("id");
                                String namaAlamat = data.getString("nama");
                                model.setId(idAlamat);
                                model.setNama(namaAlamat);

                                modelAlamats.add(model);
                            }
                            AdapterAlamat.AdapterAlamatProvinsi mAdapter;
                            mAdapter = new AdapterAlamat.AdapterAlamatProvinsi(Provinsi.this, modelAlamats);
                            mAdapter.notifyDataSetChanged();
                            rvAlamat.setLayoutManager(new LinearLayoutManager(Provinsi.this));
                            rvAlamat.setItemAnimator(new DefaultItemAnimator());
                            rvAlamat.setAdapter(mAdapter);
                            /*if (success) {

                            }else{
                                Log.d("DATA_BOOLEAN", "onResponse: "+success);
                            }*/
                            //progressDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("JSON_Exception", "onResponse: "+e);
                            helper.showMsg(Provinsi.this, "Peringatan", ""+helper.PESAN_SERVER, helper.ERROR_TYPE);
                            //progressDialog.dismiss();
                        }catch (NullPointerException e){
                            Log.d("JSON_NullPointer", "onResponse: "+e);
                        }catch (NumberFormatException e){
                            Log.d("JSONFormatException", "onResponse: "+e);
                        }

                        //progressDialog.dismiss();
                    }

                    @Override
                    public void onError(ANError anError) {
                        helper.showMsg(Provinsi.this, "Peringatan", ""+helper.PESAN_KONEKSI, helper.ERROR_TYPE);
                        Log.d("ANERROR_EXCEPTION", "onError: "+anError);
                        //progressDialog.dismiss();
                    }
                });
    }
}