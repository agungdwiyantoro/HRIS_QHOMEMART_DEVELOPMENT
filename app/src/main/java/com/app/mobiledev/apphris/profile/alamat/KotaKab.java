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

public class KotaKab extends AppCompatActivity {

    private ProgressDialog progressDialog;

    private List<ModelAlamat> modelAlamats;
    private RecyclerView rvAlamat;

    String alamatLengkap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kota_kab);

        Bundle x = getIntent().getExtras();
        String idAlamat = x.getString("idAlamat");
        String namaAlamat = x.getString("namaAlamat");
        String namaProvinsi = x.getString("aL");

        alamatLengkap = namaAlamat+", "+namaProvinsi;

        Log.d("TAG_PROVINSI", "onCreate: "+namaAlamat);

        rvAlamat = findViewById(R.id.rvAlamat);

        modelAlamats = new ArrayList<>();

        getKotaKab(idAlamat, namaAlamat);

    }

    private void getKotaKab(String idAlamat, final String nama_Alamat){
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES)
                .writeTimeout(5, TimeUnit.MINUTES)
                .build();
        AndroidNetworking.initialize(KotaKab.this, okHttpClient);
        AndroidNetworking.get("https://dev.farizdotid.com/api/daerahindonesia/kota?id_provinsi="+idAlamat)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        modelAlamats.clear();
                        try {
                            JSONArray jsonArray = response.getJSONArray("kota_kabupaten");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject data = jsonArray.getJSONObject(i);
                                ModelAlamat model = new ModelAlamat();
                                int idAlamat = data.getInt("id");
                                String namaAlamat = data.getString("nama");
                                model.setId(idAlamat);
                                model.setNama(namaAlamat);

                                modelAlamats.add(model);
                            }
                            AdapterAlamat.AdapterAlamatKabupaten mAdapter;
                            mAdapter = new AdapterAlamat.AdapterAlamatKabupaten(KotaKab.this, modelAlamats, alamatLengkap);
                            mAdapter.notifyDataSetChanged();
                            rvAlamat.setLayoutManager(new LinearLayoutManager(KotaKab.this));
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
                            helper.showMsg(KotaKab.this, "Peringatan", ""+helper.PESAN_SERVER, helper.ERROR_TYPE);
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
                        helper.showMsg(KotaKab.this, "Peringatan", ""+helper.PESAN_KONEKSI, helper.ERROR_TYPE);
                        Log.d("ANERROR_EXCEPTION", "onError: "+anError);
                        //progressDialog.dismiss();
                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(KotaKab.this, Provinsi.class);
        startActivity(intent);

    }
}