package com.app.mobiledev.apphris.kasbon;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class kasbon_karyawan extends AppCompatActivity {
    RecyclerView rcKasbon;
    List<com.app.mobiledev.apphris.kasbon.modelKasbon> modelKasbon;
    List<model_kasbonMingguan> modelKasbonMingguan;
    private SessionManager sessionmanager;
    private String kyano,nama,namaLengkap,cekStaff,bulan;
    LinearLayout rcLinear;
    private Toolbar mToolbar;
    private TextView date;
    private TextView txNominal,txUm;
    private DatePickerDialog datePickerDialog;
    private ImageButton btnTgl;
    private SimpleDateFormat dateFormatter;
    private SimpleDateFormat dateFormatter2;
    private ProgressDialog mProgressDialog;
    private Button btnCari;
    private CoordinatorLayout lmenu;
    private FloatingActionButton btnPinjaman;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kasbon_karyawan);
        rcKasbon=findViewById(R.id.rcKasbon);
        rcLinear=findViewById(R.id.rcLinear);
        btnCari=findViewById(R.id.btnCari);
        lmenu=findViewById(R.id.lmenu);
        mToolbar = findViewById(R.id.toolbar_abs);
        btnPinjaman=findViewById(R.id.tambahPinjaman);
        sessionmanager = new SessionManager(kasbon_karyawan.this);
        kyano=sessionmanager.getIdUser();
        nama=sessionmanager.getUsername();
        namaLengkap=sessionmanager.getNamaLEngkap();
        cekStaff=sessionmanager.getCekStaff();
        Log.d("CEK_STAFF", "onCreate: "+cekStaff);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Loading ...");

        date=findViewById(R.id.date);
        btnTgl=findViewById(R.id.btnTgl);
        mToolbar.setTitle("Kasbon");

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        AndroidNetworking.initialize(getApplicationContext());

        modelKasbon = new ArrayList<>();
        modelKasbonMingguan = new ArrayList<>();
        txNominal=findViewById(R.id.txNominal);
        txUm=findViewById(R.id.txUm);
        date.setText(getDateNow());
        mProgressDialog.show();
        cekSaldo(kyano,getDate());
        cekJmlNominalHutang(getDate(),kyano);

        batasUM2();
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        btnTgl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog();
            }
        });

        btnCari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressDialog.show();
                Log.d("BULAN_DATA", "onClick: "+bulan);

                try {
                    mProgressDialog.show();
                    if(bulan.equals(null)||bulan.equals("")){
                        helper.showMsg(kasbon_karyawan.this,"informasi","bulan dipilih",helper.ERROR_TYPE);
                    }else{
                        cekSaldo(kyano,bulan);
                        cekJmlNominalHutang(bulan,kyano);
                    }

                }catch (NullPointerException e){
                    helper.showMsg(kasbon_karyawan.this,"informasi2","bulan belum dipilih",helper.ERROR_TYPE);
                    mProgressDialog.dismiss();
                }


            }
        });

        btnPinjaman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(kasbon_karyawan.this, tambahKasbon.class);
                startActivity(intent);
                finish();
            }
        });




    }


    private void cekSaldo(final String kyano,final  String getbulan){
        AndroidNetworking.post(api.URL_kasbonMingguan)
                .addBodyParameter("kyano", kyano)
                .addBodyParameter("key", api.key)
                .addBodyParameter("bulan", getbulan)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Boolean success = response.getBoolean("success");
                            if (success) {
                                JSONArray jsonArray = response.getJSONArray("data");
                                modelKasbonMingguan.clear();
                                rcLinear.setVisibility(View.VISIBLE);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject data = jsonArray.getJSONObject(i);
                                    model_kasbonMingguan model = new model_kasbonMingguan();
                                    model.setKbmJenis(data.getString("KbmJenis"));
                                    model.setKbmTgl(data.getString("KbmTgl"));
                                    model.setKbmPeriode(data.getString("KbmTgl"));
                                    model.setKyano(data.getString("kyano"));
                                    model.setKbmNominal(helper.rp(Double.valueOf(data.getString("KbmNominal"))));
                                    model.setKbmKet(data.getString("KbmKet"));
                                    model.setKbmAlasan(data.getString("KbmAlasan"));
                                    model.setKbmStatus(data.getString("KbmStatus"));
                                    modelKasbonMingguan.add(model);
                                    Log.d("DATA_MODEL", "onResponse: "+data.getString("KbmAlasan"));

                                }

                                adapterKasbonMingguan mAdapter;
                                mAdapter = new adapterKasbonMingguan(modelKasbonMingguan, kasbon_karyawan.this);
                                mAdapter.notifyDataSetChanged();
                                rcKasbon.setLayoutManager(new LinearLayoutManager(kasbon_karyawan.this));
                                rcKasbon.setItemAnimator(new DefaultItemAnimator());
                                rcKasbon.setAdapter(mAdapter);
                                mProgressDialog.dismiss();

                            }else{
                               rcLinear.setVisibility(View.GONE);
                               helper.snackBar(lmenu,"riwayat kasbon belum ada");
                                Log.d("DATA_BOOLEAN", "onResponse: "+success);
                                mProgressDialog.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("JSONERORABSEN", "onResponse: "+e);
                            helper.showMsg(kasbon_karyawan.this, "Peringatan", ""+helper.PESAN_SERVER, helper.ERROR_TYPE);
                            mProgressDialog.dismiss();
                        }catch (NumberFormatException e){
                            Log.d("NUMBER_FORMAT", "onResponse: "+e);
                            mProgressDialog.dismiss();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        helper.showMsg(kasbon_karyawan.this, "Peringatan", ""+helper.PESAN_KONEKSI, helper.ERROR_TYPE);
                        Log.d("EROOR_EXCEPTION", "onError: "+anError);


                    }
                });

    }
    private void cekJmlNominalHutang(final String getbulan,final String kyano){
        AndroidNetworking.post(api.URL_getSaldoMingguan)
                .addBodyParameter("kyano", kyano)
                .addBodyParameter("tgl", getbulan)
                .addBodyParameter("key", api.key)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Boolean success = response.getBoolean("success");
                            if (success) {
                                String  data = response.getString("data");
                                txNominal.setText(""+helper.rp(Double.valueOf(data)));

                            }else{

                                txNominal.setText("0");

                            }
                            mProgressDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("JSONERORABSEN", "onResponse: "+e);
                            helper.showMsg(kasbon_karyawan.this, "Peringatan", ""+helper.PESAN_SERVER, helper.ERROR_TYPE);
                            mProgressDialog.dismiss();
                        }catch (NumberFormatException e){
                            Log.d("Number Format", "onResponse: "+e);
                        }


                    }

                    @Override
                    public void onError(ANError anError) {
                        helper.showMsg(kasbon_karyawan.this, "Peringatan", ""+helper.PESAN_KONEKSI, helper.ERROR_TYPE);
                        Log.d("EROOR_EXCEPTION", "onError: "+anError);
                        mProgressDialog.dismiss();


                    }
                });

    }
    private void showDateDialog(){
        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);

                dateFormatter = new SimpleDateFormat("yyyy-MM");
                dateFormatter2 = new SimpleDateFormat("MM");
                bulan=""+dateFormatter2.format(newDate.getTime());
                date.setText(""+dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
    @Override
    public void onBackPressed() {
        finish();
    }
    private void batasUM2(){
                 AndroidNetworking.post(api.URL_getNominalUM)
                .addBodyParameter("key", api.key)
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
                                    String cekStaff1=data.getString("setano");
                                    if(cekStaff.toUpperCase().equals(cekStaff1.toUpperCase())){
                                        txUm.setText(""+helper.rp(Double.valueOf(data.getString("setchar"))));
                                    }

                                }


                            }else{
                                helper.showMsg(kasbon_karyawan.this,"Informasi","Data tidak ditemukan",helper.WARNING_TYPE);
                            }
                            mProgressDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("JSONERORABSEN", "onResponse: "+e);
                            helper.showMsg(kasbon_karyawan.this, "Peringatan", ""+helper.PESAN_SERVER, helper.ERROR_TYPE);
                            mProgressDialog.dismiss();
                        }catch (NumberFormatException e){
                            Log.d("Number Format", "onResponse: "+e);
                        }


                    }

                    @Override
                    public void onError(ANError anError) {
                        helper.showMsg(kasbon_karyawan.this, "Peringatan", ""+helper.PESAN_KONEKSI, helper.ERROR_TYPE);
                        Log.d("EROOR_EXCEPTION", "onError: "+anError);
                        mProgressDialog.dismiss();


                    }
                });
    }
    public String getDate(){
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("MM");
        String formattedDate = df.format(c);
        return  formattedDate;
    }
    public String getDateNow(){
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");
        String formattedDate = df.format(c);
        return  formattedDate;
    }






}
