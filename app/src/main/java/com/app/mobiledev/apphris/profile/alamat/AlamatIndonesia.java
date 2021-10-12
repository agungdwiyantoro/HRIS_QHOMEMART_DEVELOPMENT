package com.app.mobiledev.apphris.profile.alamat;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.app.mobiledev.apphris.R;
import com.app.mobiledev.apphris.api.api;
import com.app.mobiledev.apphris.sesion.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AlamatIndonesia extends AppCompatActivity {

    SessionManager sessionManager;

    Spinner spProvinsi, spKabupatenKota, spKecamatan, spKelurahan;
    ArrayList<String> listProvinsi, listProvinsiKode, listKabupatenKota, listKabupatenKotaKode, listKecamatan, listKecamatanKode, listKelurahan, listKelurahanKode;

    String token,urlProv, urlKabKot, urlKec, urlKel, kdProv, kdKabKot, kdKec, kdKel, nmProv, nmKabKot, nmKec, nmKel, alamatLengkap;
    TextView tvAlamatTemp, tvAlamatLengkap;
    EditText etAlamatLengkap;
    TextInputLayout tilAlamatLengkap;

    Button btnGetAlamat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alamat_indonesia);

        sessionManager = new SessionManager(this);

        spProvinsi = findViewById(R.id.spProvinsi);
        spKabupatenKota = findViewById(R.id.spKabupatenKota);
        spKecamatan = findViewById(R.id.spKecamatan);
        spKelurahan = findViewById(R.id.spKelurahan);

        tvAlamatTemp = findViewById(R.id.tvAlamatTemp);
        tvAlamatLengkap = findViewById(R.id.tvAlamatLengkap);
        etAlamatLengkap = findViewById(R.id.etAlamatLengkap);
        btnGetAlamat = findViewById(R.id.btnSimpanAlamat);

        tilAlamatLengkap = findViewById(R.id.tilAlamatLengkap);

        urlProv = api.URL_getProvinsi;
        urlKabKot = api.URL_getKabKot;
        urlKec = api.URL_getKecamatan;
        urlKel = api.URL_getKelurahan;

        token = sessionManager.getToken();

        Log.d("TAG_TOKEN_SP", "onCreate: "+token);
        getProvinsi();

        etAlamatLengkap.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String i = etAlamatLengkap.getText().toString();
                tvAlamatLengkap.setText(i+", "+alamatLengkap);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btnGetAlamat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String al = tvAlamatLengkap .getText().toString();

                if(is_valid()){
                    //mProgressDialog.show();
                    //insert_data_kk(kyano,nik,jbano,tempat_lahir,tgl_lahir,tgl_masuk_kerja,alamat_ktp,alamat_now,no_hp,email,npwp,nama,compressedImage_ktp,compressedImage_kk,compressedImage_npwp);

                    if (sessionManager.getStatusAlamat().equals("now")) {
                        sessionManager.putAlamatNow(al);
                        onBackPressed();
                    } else if (sessionManager.getStatusAlamat().equals("ktp")){
                        sessionManager.putAlamatKtp(al);
                        onBackPressed();
                    }

                }else{
                    //mProgressDialog.dismiss();
                }

            }
        });
    }

    private boolean is_valid() {
        boolean valid = true;
        if (etAlamatLengkap.getText().toString().isEmpty()) {
            tilAlamatLengkap.setError("Lengkapi alamat kamu");
            tilAlamatLengkap.requestFocus();
            valid = false;
        }

        return valid;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void getProvinsi() {
        AndroidNetworking.get(urlProv)
                .addHeaders("Authorization", "Bearer "+token)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // do anything with response
                        Log.d("TAG_PROVINSI", "onResponse: "+ response);

                        try {
                            listProvinsi = new ArrayList<>();
                            listProvinsiKode = new ArrayList<>();

                            for (int i = 0; i < response.length(); i++) {
                                JSONObject objectData = response.getJSONObject(i);

                                String kode = objectData.getString("kode");
                                String nama = objectData.getString("nama");

                                listProvinsi.add(nama);
                                listProvinsiKode.add(kode);

                                Log.d("TAG_PROVINSI", "onResponse: "+ kode +nama);

                            }

                            spProvinsi.setAdapter(new ArrayAdapter<String>(AlamatIndonesia.this, android.R.layout.simple_spinner_dropdown_item, listProvinsi));
                            spProvinsi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    nmProv = spProvinsi.getSelectedItem().toString();
                                    int kd = spProvinsi.getSelectedItemPosition();
                                    kdProv = listProvinsiKode.get(kd);

                                    getKabupatenKota(kdProv);
                                    Log.d("TAG_KDPROV", "onItemSelected: "+kdProv+" "+nmProv);

                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("TAG_JSONException", "onResponse: "+e);
                        }

                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                    }
                });
    }

    private void getKabupatenKota(String kode) {
        AndroidNetworking.get(urlKabKot+kode)
                .addHeaders("Authorization", "Bearer "+token)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // do anything with response
                        Log.d("TAG_KabupatenKota", "onResponse: "+ response);

                        try {
                            listKabupatenKota = new ArrayList<>();
                            listKabupatenKotaKode = new ArrayList<>();

                            for (int i = 0; i < response.length(); i++) {
                                JSONObject objectData = response.getJSONObject(i);

                                String kode = objectData.getString("kode");
                                String nama = objectData.getString("nama");

                                listKabupatenKota.add(nama);
                                listKabupatenKotaKode.add(kode);

                                Log.d("TAG_KabupatenKota", "onResponse: "+ kode +nama);
                            }

                            //spProvinsi = dialogHubungan.findViewById(R.id.spHubungan);
                            spKabupatenKota.setAdapter(new ArrayAdapter<String>(AlamatIndonesia.this, android.R.layout.simple_spinner_dropdown_item, listKabupatenKota));
                            spKabupatenKota.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    nmKabKot = spKabupatenKota.getSelectedItem().toString();
                                    int kd = spKabupatenKota.getSelectedItemPosition();
                                    kdKabKot = listKabupatenKotaKode.get(kd);

                                    getKecamatan(kdKabKot);
                                    Log.d("TAG_KDKABKOT", "onItemSelected: "+kdKabKot+" "+nmKabKot);
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("TAG_JSONException", "onResponse: "+e);
                        }

                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                    }
                });
    }

    private void getKecamatan(String kode) {
        AndroidNetworking.get(urlKec+kode)
                .addHeaders("Authorization", "Bearer "+token)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // do anything with response
                        Log.d("TAG_Kecamatan", "onResponse: "+ response);

                        try {
                            listKecamatan = new ArrayList<>();
                            listKecamatanKode = new ArrayList<>();

                            for (int i = 0; i < response.length(); i++) {
                                JSONObject objectData = response.getJSONObject(i);

                                String kode = objectData.getString("kode");
                                String nama = objectData.getString("nama");

                                listKecamatan.add(nama);
                                listKecamatanKode.add(kode);

                                Log.d("TAG_Kecamatan", "onResponse: "+ kode +nama);
                            }

                            spKecamatan.setAdapter(new ArrayAdapter<String>(AlamatIndonesia.this, android.R.layout.simple_spinner_dropdown_item, listKecamatan));
                            spKecamatan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    nmKec = spKecamatan.getSelectedItem().toString();
                                    int kd = spKecamatan.getSelectedItemPosition();
                                    kdKec = listKecamatanKode.get(kd);

                                    getKelurahan(kdKec);
                                    Log.d("TAG_KDKEC", "onItemSelected: "+kdKec+" "+nmKec);
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("TAG_JSONException", "onResponse: "+e);
                        }

                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                    }
                });
    }

    private void getKelurahan(String kode) {
        AndroidNetworking.get(urlKel+kode)
                .addHeaders("Authorization", "Bearer "+token)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // do anything with response
                        Log.d("TAG_Kelurahan", "onResponse: "+ response);

                        try {
                            listKelurahan = new ArrayList<>();
                            listKelurahanKode = new ArrayList<>();

                            for (int i = 0; i < response.length(); i++) {
                                JSONObject objectData = response.getJSONObject(i);

                                String kode = objectData.getString("kode");
                                String nama = objectData.getString("nama");

                                listKelurahan.add(nama);
                                listKelurahanKode.add(kode);

                                Log.d("TAG_Kelurahan", "onResponse: "+ kode +nama);
                            }

                            //spProvinsi = dialogHubungan.findViewById(R.id.spHubungan);
                            spKelurahan.setAdapter(new ArrayAdapter<String>(AlamatIndonesia.this, android.R.layout.simple_spinner_dropdown_item, listKelurahan));
                            spKelurahan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    nmKel = spKelurahan.getSelectedItem().toString();

                                    alamatLengkap = nmKel+", "+ nmKec+", "+ nmKabKot+", " + nmProv;

                                    tvAlamatLengkap.setText(alamatLengkap);

                                    Log.d("TAG_AL", "onItemSelected: "+alamatLengkap);
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("TAG_JSONException", "onResponse: "+e);
                        }

                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                    }
                });
    }
}