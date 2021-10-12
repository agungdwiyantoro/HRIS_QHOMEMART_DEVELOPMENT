package com.app.mobiledev.apphris.pinjaman;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.app.mobiledev.apphris.R;
import com.app.mobiledev.apphris.api.api;
import com.app.mobiledev.apphris.helperPackage.helper;
import com.app.mobiledev.apphris.sesion.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class pinjamanUang extends AppCompatActivity {

    private TextView txpinjaman;
    private TextView txTotal;
    private RecyclerView rvpinjaman;
    List<model_pinjaman> modelPinjaman;
    private SessionManager sessionmanager;
    private ProgressDialog mProgressDialog;
    private Toolbar mToolbar;
    private LinearLayout rcLinear;
    private String kyano,nama,namaLengkap,cekStaff,bulan;
    private Spinner spinTipe;
    private ImageButton refresh;
    private String[] plants = new String[]{"Tunai", "Barang",};
    private FloatingActionButton tambahPinjaman;
    private ImageView ivInfo;
    private AlertDialog.Builder dialog;
    private LayoutInflater inflater;
    private View dialogView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pinjaman_uang);
        rvpinjaman=findViewById(R.id.rcPinjaman);
        txTotal=findViewById(R.id.txTotal);
        spinTipe=findViewById(R.id.spinTipe);
        txpinjaman=findViewById(R.id.txPinjaman);
        mToolbar = findViewById(R.id.toolbar_abs);
        refresh=findViewById(R.id.refresh);
        rcLinear=findViewById(R.id.rcLinear);
        tambahPinjaman=findViewById(R.id.tambahPinjaman);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Loading ...");
        sessionmanager = new SessionManager(pinjamanUang.this);
        kyano=sessionmanager.getIdUser();
        Log.d("NOMOR_KARYAWAN", "onCreate: "+kyano);
        nama=sessionmanager.getUsername();
        namaLengkap=sessionmanager.getNamaLEngkap();

        mToolbar.setTitle("Pinjaman");
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

        tambahPinjaman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(pinjamanUang.this, tambahPinjaman.class);
                startActivity(intent);
                finish();

            }
        });
        modelPinjaman = new ArrayList<>();
        mProgressDialog.show();
        cekPinjaman(kyano,"Tunai");
        totalPinjaman("Tunai",kyano);
        totalBayar();
        spitipe();


        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                totalPinjaman(spinTipe.getSelectedItem().toString(),kyano);
                cekPinjaman(kyano,spinTipe.getSelectedItem().toString());
            }
        });
        spinTipe.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                mProgressDialog.show();
                totalPinjaman(spinTipe.getSelectedItem().toString(),kyano);
                cekPinjaman(kyano,spinTipe.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        ivInfo = findViewById(R.id.ivInfo);
        ivInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialoginfo();
            }
        });

    }

    private void getDialoginfo() {
        dialog = new AlertDialog.Builder(pinjamanUang.this);
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.dialog_info, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);;

        dialog.show();

    }

    private void cekPinjaman(final String kyano,final  String jenis){
        AndroidNetworking.post(api.URL_pinjaman)
                .addBodyParameter("kyano", kyano)
                .addBodyParameter("key", api.key)
                .addBodyParameter("jenis", jenis)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            modelPinjaman.clear();
                            Boolean success = response.getBoolean("success");
                            if (success) {
                                JSONArray jsonArray = response.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject data = jsonArray.getJSONObject(i);
                                    model_pinjaman model = new model_pinjaman();
                                    model.setKet(data.getString("ket"));
                                    model.setKbalasan(data.getString("kbalasan"));
                                    model.setKyano(data.getString("kyano"));
                                    model.setTgl(data.getString("tgl"));
                                    model.setK(data.getString("k"));
                                    model.setD(data.getString("d"));
                                    model.setKbstatus(data.getString("kbstatus"));
                                    model.setMut(data.getString("mut"));
                                    model.setKbrpaj(data.getString("kbrpaj"));
                                    modelPinjaman.add(model);


                                }

                                adapaterPinjaman mAdapter;
                                mAdapter = new adapaterPinjaman(modelPinjaman, pinjamanUang.this);
                                mAdapter.notifyDataSetChanged();
                                rvpinjaman.setLayoutManager(new LinearLayoutManager(pinjamanUang.this));
                                rvpinjaman.setItemAnimator(new DefaultItemAnimator());
                                rvpinjaman.setAdapter(mAdapter);



                            }else{
                                Log.d("DATA_BOOLEAN", "onResponse: "+success);
                            }
                            mProgressDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("JSONERORABSEN", "onResponse: "+e);
                            helper.showMsg(pinjamanUang.this, "Peringatan", ""+helper.PESAN_SERVER, helper.ERROR_TYPE);
                            mProgressDialog.dismiss();
                        }


                    }

                    @Override
                    public void onError(ANError anError) {
                        helper.showMsg(pinjamanUang.this, "Peringatan", ""+helper.PESAN_KONEKSI, helper.ERROR_TYPE);
                        Log.d("EROOR_EXCEPTION", "onError: "+anError);
                        mProgressDialog.dismiss();


                    }
                });

    }

    @Override
    public void onBackPressed() {
        finish();
    }


    private void totalPinjaman(String jenis,String kyano){
        AndroidNetworking.post(api.URL_totalPinjaman)
                .addBodyParameter("kyano", kyano)
                .addBodyParameter("key", api.key)
                .addBodyParameter("jenis", jenis)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            Boolean success = response.getBoolean("success");
                            if (success) {
                                JSONArray jsonArray = response.getJSONArray("data");

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject data = jsonArray.getJSONObject(i);
                                    String Total=data.getString("total");
                                    if(Total.equals(null)||Total.equals("null")){
                                        txpinjaman.setText(""+0);
                                    }else{
                                        txpinjaman.setText(helper.rp(Double.valueOf(Total)));
                                    }
                                }
                            }else{
                                Log.d("DATA_BOOLEAN", "onResponse: "+success);
                            }
                            mProgressDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("JSONERORABSEN", "onResponse: "+e);
                            helper.showMsg(pinjamanUang.this, "Informasi", ""+helper.PESAN_SERVER + e.toString(), helper.ERROR_TYPE);
                            mProgressDialog.dismiss();
                        }catch (NullPointerException e){
                            Log.d("JSONERORABSEN", "onResponse: "+e);
                        }catch (NumberFormatException e){
                            Log.d("JSONERORABSEN", "onResponse: "+e);
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        helper.showMsg(pinjamanUang.this, "Peringatan", ""+helper.PESAN_KONEKSI + anError, helper.ERROR_TYPE);
                        Log.d("EROOR_EXCEPTION", "onError: "+anError);
                        mProgressDialog.dismiss();


                    }
                });

    }

    private void totalBayar(){
        AndroidNetworking.post(api.URL_totalBayarPinjaman)
                .addBodyParameter("kyano", kyano)
                .addBodyParameter("key", api.key)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            Boolean success = response.getBoolean("success");
                            if (success) {
                                String data = response.getString("data");
                                txTotal.setText(""+helper.rp(Double.valueOf(data)));
                            }else{

                                Log.d("DATA_BOOLEAN", "onResponse: "+success);
                            }
                            mProgressDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("JSONERORABSEN", "onResponse: "+e);
                            helper.showMsg(pinjamanUang.this, "Peringatan", "" +helper.PESAN_SERVER, helper.ERROR_TYPE);
                            mProgressDialog.dismiss();
                        }catch (NullPointerException e){
                            Log.d("JSONERORABSEN", "onResponse: "+e);
                        }catch (NumberFormatException e){
                            Log.d("JSONERORABSEN", "onResponse: "+e);
                        }


                    }

                    @Override
                    public void onError(ANError anError) {
                        helper.showMsg(pinjamanUang.this, "Peringatan", ""+helper.PESAN_KONEKSI, helper.ERROR_TYPE);
                        Log.d("EROOR_EXCEPTION", "onError: "+anError);
                        mProgressDialog.dismiss();


                    }
                });
    }


    private void spitipe(){
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this,R.layout.spinner_item,plants
        );
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinTipe.setAdapter(spinnerArrayAdapter);
    }

}
