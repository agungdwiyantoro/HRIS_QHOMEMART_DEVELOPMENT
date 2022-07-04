package com.app.mobiledev.apphris.kasbon;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class tambahKasbon extends AppCompatActivity {
    private EditText edNominal,edAlasan;
    private TextView txnik,txnama,txdivisi,txjabatan,txLimit,txNoDivisi,txNoJabatan,txBatasUm,txPersenSaldo;
    private SessionManager sessionmanager;
    private String kyano1,nama,namaLengkap;
    private ProgressDialog mProgressDialog;
    private Spinner edJenis;
    private Button btnPengajuan;
    private String cek,cekStaff;
    private String[] data = new String[]{"Tidak Rutin"};

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_kasbon);
        txnik=findViewById(R.id.txNik);
        txnama=findViewById(R.id.txNama);
        txdivisi=findViewById(R.id.txDivisi);
        txjabatan=findViewById(R.id.txJabatan);
        txNoJabatan=findViewById(R.id.txNoJabatan);
        txNoDivisi=findViewById(R.id.txNoDivisi);
        txBatasUm=findViewById(R.id.txBatasUm);
        txLimit=findViewById(R.id.txLimit);
        edJenis=findViewById(R.id.edJenis);
        txPersenSaldo=findViewById(R.id.txPersenSaldo);
        edAlasan=findViewById(R.id.edAlasan);
        edNominal=findViewById(R.id.edNominal);

        sessionmanager = new SessionManager(tambahKasbon.this);
        kyano1=sessionmanager.getIdUser();
        nama=sessionmanager.getUsername();
        cekStaff=sessionmanager.getCekStaff();
        namaLengkap=sessionmanager.getNamaLEngkap();
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Loading ...");
        btnPengajuan=findViewById(R.id.btnPengajuan);

        mToolbar = findViewById(R.id.toolbar_abs);
        mToolbar.setTitle("Form Pinjaman Kasbon");
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

        btnPengajuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mProgressDialog.show();
                    String nominal=edNominal.getText().toString();
                    String alasan=edAlasan.getText().toString();


                    if(nominal.equals("0")){
                        helper.showMsg(tambahKasbon.this,"Informasi","Field nominal belum diisi",helper.ERROR_TYPE);
                    }else if(alasan.equals("")){
                        helper.showMsg(tambahKasbon.this,"Informasi","Field alasan belum diisi",helper.ERROR_TYPE);
                    }
                   else{
                        String kbmJenis="";
                        if(edJenis.getSelectedItem().toString().equals("Rutin")){
                            kbmJenis="R";
                            if(cekPengajuanKasbon()){
                                simpanPengajuanKasbon(kbmJenis,kyano1,txNoDivisi.getText().toString(),txNoJabatan.getText().toString(),edAlasan.getText().toString(),edNominal.getText().toString().replace(",","").replace(".",""),"");
                            }else{
                                helper.showMsg(tambahKasbon.this,"Informasi","Nominal harus kurang dari 20 %",helper.WARNING_TYPE);
                            }
                        }else{
                            kbmJenis="TR";
                            double limit=Double.valueOf(txLimit.getText().toString().replace(",","").replace(".",""));
                            double nominall=Double.valueOf(edNominal.getText().toString().replace(",","").replace(".",""));
                            if(nominall>limit){
                                helper.showMsg(tambahKasbon.this,"informasi","pinjaman yang diajukan melebihi limit",helper.ERROR_TYPE);
                            }else{
                                simpanPengajuanKasbon(kbmJenis,kyano1,txNoDivisi.getText().toString(),txNoJabatan.getText().toString(),edAlasan.getText().toString(),edNominal.getText().toString().replace(",","").replace(".",""),"");
                            }

                        }



                    }
                    mProgressDialog.dismiss();
                }catch (NullPointerException e){
                    Log.d("NULL POINTER", "onClick: "+e);
                    mProgressDialog.dismiss();
                }catch (NumberFormatException e){
                    Log.d("NULL POINTER", "onClick: "+e);
                }


            }
        });

        edNominal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(cek)) {
                    edNominal.removeTextChangedListener(this);
                    Locale local = new Locale("id", "id");
                    String replaceable = String.format("[Rp,.\\s]",
                            java.text.NumberFormat.getCurrencyInstance().getCurrency()
                                    .getSymbol(local));
                    String cleanString = s.toString().replaceAll(replaceable,
                            "");

                    double parsed;
                    try {
                        parsed = Double.parseDouble(cleanString);
                    } catch (NumberFormatException e) {
                        parsed = 0.00;
                    }

                    java.text.NumberFormat formatter = java.text.NumberFormat
                            .getCurrencyInstance(local);
                    formatter.setMaximumFractionDigits(0);
                    formatter.setParseIntegerOnly(true);
                    String formatted = formatter.format((parsed));

                    String replace = String.format("[Rp\\s]",
                            java.text.NumberFormat.getCurrencyInstance().getCurrency()
                                    .getSymbol(local));
                    String clean = formatted.replaceAll(replace, "");

                    cek= formatted;
                    edNominal.setText(clean);
                    edNominal.setSelection(clean.length());
                    edNominal.addTextChangedListener(this);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (edNominal.getText().toString().equals("")) {
                    edNominal.setText("0");
                }


            }
        });

    batasUM2();
    getInformasiKaryawan(kyano1);
    spitipe();
    mProgressDialog.show();

    cekJmlNominalHutang(getDate(),kyano1);
    cekPersenRutin();
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
                                        Double total=Double.valueOf(data);
                                        String txUM=txBatasUm.getText().toString().replace(",","").replace(".","");
                                        Double btsUM=Double.valueOf(txUM);
                                        Double mTotal=btsUM-total;
                                        txLimit.setText(""+helper.rp(mTotal));

                            }else{
                                String txUM=txBatasUm.getText().toString();
                                txLimit.setText(""+txUM);
                            }
                            mProgressDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("JSONERORABSEN", "onResponse: "+e);
                            helper.showMsg(tambahKasbon.this, "Peringatan", ""+helper.PESAN_SERVER, helper.ERROR_TYPE);
                            mProgressDialog.dismiss();
                        }catch (NumberFormatException e){
                            Log.d("Number Format", "onResponse: "+e);
                        }


                    }

                    @Override
                    public void onError(ANError anError) {
                        helper.showMsg(tambahKasbon.this, "Peringatan", ""+helper.PESAN_KONEKSI, helper.ERROR_TYPE);
                        Log.d("EROOR_EXCEPTION", "onError: "+anError);
                        mProgressDialog.dismiss();
                    }
                });

    }


    private void getInformasiKaryawan(final String kyano){
        AndroidNetworking.post(api.URL_informasiKaryawan)
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
                                JSONArray jsonArray = response.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject data = jsonArray.getJSONObject(i);
                                    String kyano=data.getString("kyano");
                                    String nik=data.getString("nik");
                                    String kynm=data.getString("kynm");
                                    String kyjk=data.getString("kyjk");
                                    String kyagama=data.getString("kyagama");
                                    String kytgllahir=data.getString("kytgllhr");
                                    String kystatus_kerja=data.getString("kystatus_kerja");
                                    String kyalamat=data.getString("kyalamat");
                                    String kyhp=data.getString("kyhp");
                                    String jbnama=data.getString("jbnama");
                                    String dvnama=data.getString("dvnama");
                                    String jbano=data.getString("jbano");
                                    String dvano=data.getString("dvano");

                                    txnik.setText(""+nik);
                                    txnama.setText(""+kynm);
                                    txdivisi.setText(""+dvnama);
                                    txjabatan.setText(""+jbnama);
                                    txNoDivisi.setText(""+dvano);
                                    txNoJabatan.setText(""+jbano);

                                }
                            }else{
                                Log.d("DATA_BOOLEAN", "onResponse: "+success);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("JSONERORABSEN", "onResponse: "+e);
                            helper.showMsg(tambahKasbon.this, "Peringatan", ""+helper.PESAN_SERVER, helper.ERROR_TYPE);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        helper.showMsg(tambahKasbon.this, "Peringatan", ""+helper.PESAN_KONEKSI, helper.ERROR_TYPE);
                        Log.d("EROOR_EXCEPTION", "onError: "+anError);

                    }
                });

    }

    private void spitipe(){
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter(this,R.layout.spinner_item,data);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        edJenis.setAdapter(spinnerArrayAdapter);
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(tambahKasbon.this, kasbon_karyawan.class));
        finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void simpanPengajuanKasbon(String kbmjenis, String kyano, String noDivisi, String noJabatan, String alasan, String nominal, String keterangan) {
                 AndroidNetworking.post(api.URL_pengajuanKasbon)
                .addBodyParameter("kbmjenis", kbmjenis)
                .addBodyParameter("ip", helper.getDeviceIMEI(tambahKasbon.this))
                .addBodyParameter("kyano", kyano)
                .addBodyParameter("nominal", nominal)
                .addBodyParameter("alasan", alasan)
                .addBodyParameter("key", api.key)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Boolean success = response.getBoolean("status");
                            String ket = response.getString("ket");
                            if (success) {
                                new SweetAlertDialog(tambahKasbon.this, SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("Informasi")
                                        .setContentText("Data berhasil disimpan")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                finish();
                                            }
                                        })
                                        .show();
                                edNominal.setText("0");
                                edAlasan.setText("");
                                cekJmlNominalHutang(getDate(),kyano1);

                            }else{
                                Log.d("DATA_BOOLEAN", "onResponse: "+success);
                                helper.showMsg(tambahKasbon.this, "Informasi", ""+ket, helper.WARNING_TYPE);
                            }
                            mProgressDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("JSONERORABSEN", "onResponse: "+e);
                            helper.showMsg(tambahKasbon.this, "Peringatan", ""+helper.PESAN_SERVER, helper.ERROR_TYPE);
                            mProgressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        helper.showMsg(tambahKasbon.this, "Peringatan", ""+helper.PESAN_KONEKSI, helper.ERROR_TYPE);
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
                                        txBatasUm.setText(""+helper.rp(Double.valueOf(data.getString("setchar"))));
                                    }
                                }
                            }else{
                                helper.showMsg(tambahKasbon.this,"Informasi","Data tidak ditemukan",helper.WARNING_TYPE);
                            }
                            mProgressDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("JSONERORABSEN", "onResponse: "+e);
                            helper.showMsg(tambahKasbon.this, "Peringatan", ""+helper.PESAN_SERVER, helper.ERROR_TYPE);
                            mProgressDialog.dismiss();
                        }catch (NumberFormatException e){
                            Log.d("Number Format", "onResponse: "+e);
                        }


                    }

                    @Override
                    public void onError(ANError anError) {
                        helper.showMsg(tambahKasbon.this, "Peringatan", ""+helper.PESAN_KONEKSI, helper.ERROR_TYPE);
                        Log.d("EROOR_EXCEPTION", "onError: "+anError);
                        mProgressDialog.dismiss();


                    }
                });
    }


    private void cekPersenRutin(){
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
                                    String cekStaff=data.getString("setano");
                                    if(cekStaff.equals("potongan")){
                                        txPersenSaldo.setText(""+data.getString("setchar"));
                                    }
                                }
                            }else{
                                helper.showMsg(tambahKasbon.this,"Informasi","Data tidak ditemukan",helper.WARNING_TYPE);
                            }
                            mProgressDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("JSONERORABSEN", "onResponse: "+e);
                            helper.showMsg(tambahKasbon.this, "Peringatan", ""+helper.PESAN_SERVER, helper.ERROR_TYPE);
                            mProgressDialog.dismiss();
                        }catch (NumberFormatException e){
                            Log.d("Number Format", "onResponse: "+e);
                        }


                    }

                    @Override
                    public void onError(ANError anError) {
                        helper.showMsg(tambahKasbon.this, "Peringatan", ""+helper.PESAN_KONEKSI , helper.ERROR_TYPE);
                        Log.d("EROOR_EXCEPTION", "onError: "+anError);
                        mProgressDialog.dismiss();


                    }
                });
    }


    private Boolean cekPengajuanKasbon(){
        Boolean cek=false;
        try {
            double nominal=Double.valueOf(edNominal.getText().toString().replace(",","").replace(".",""));
            double batsUm=Double.valueOf(txBatasUm.getText().toString());
            double persen=Double.valueOf(txPersenSaldo.getText().toString());
            double hasil=(batsUm*persen)/100;
            if(nominal<=hasil){
                cek=true;
            }else{
                cek=false;
            }

        }catch (NumberFormatException e){
            Log.d("NumberFormat", "cekPengajuanKasbon: "+e);
        }
        return  cek;
    }
}
