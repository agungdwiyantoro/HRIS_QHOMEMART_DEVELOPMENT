package com.app.mobiledev.apphris.profile.alamat;

import android.app.ProgressDialog;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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

public class Kecamatan extends AppCompatActivity {

    private ProgressDialog progressDialog;

    private List<ModelAlamat> modelAlamats;
    private RecyclerView rvAlamat;

    String alamatLengkap, idAlamat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kecamatan);

        Bundle x = getIntent().getExtras();
        idAlamat = x.getString("idAlamat");
        String namaAlamat = x.getString("namaAlamat");
        String namaProvinsiKabupaten = x.getString("aL");

        alamatLengkap = namaAlamat+", "+namaProvinsiKabupaten;

        rvAlamat = findViewById(R.id.rvAlamat);

        modelAlamats = new ArrayList<>();

        getKecamatan(idAlamat);
    }

    private void getKecamatan(String value){
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES)
                .writeTimeout(5, TimeUnit.MINUTES)
                .build();
        AndroidNetworking.initialize(Kecamatan.this, okHttpClient);
        AndroidNetworking.get("https://dev.farizdotid.com/api/daerahindonesia/kecamatan?id_kota="+value)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        modelAlamats.clear();
                        try {
                            JSONArray jsonArray = response.getJSONArray("kecamatan");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject data = jsonArray.getJSONObject(i);
                                ModelAlamat model = new ModelAlamat();
                                int idAlamat = data.getInt("id");
                                String namaAlamat = data.getString("nama");
                                model.setId(idAlamat);
                                model.setNama(namaAlamat);

                                modelAlamats.add(model);
                            }
                            AdapterAlamat.AdapterAlamatKecamatan mAdapter;
                            mAdapter = new AdapterAlamat.AdapterAlamatKecamatan(Kecamatan.this, modelAlamats, alamatLengkap);
                            mAdapter.notifyDataSetChanged();
                            rvAlamat.setLayoutManager(new LinearLayoutManager(Kecamatan.this));
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
                            helper.showMsg(Kecamatan.this, "Peringatan", ""+helper.PESAN_SERVER, helper.ERROR_TYPE);
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
                        helper.showMsg(Kecamatan.this, "Peringatan", ""+helper.PESAN_KONEKSI, helper.ERROR_TYPE);
                        Log.d("ANERROR_EXCEPTION", "onError: "+anError);
                        //progressDialog.dismiss();
                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(Kecamatan.this, Provinsi.class);

        Bundle x = new Bundle();
        x.putString("idAlamat", String.valueOf(idAlamat));

        intent.putExtras(x);

        startActivity(intent);

    }

}