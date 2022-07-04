package com.app.mobiledev.apphris;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.app.mobiledev.apphris.api.api;
import com.app.mobiledev.apphris.helperPackage.helper;
import com.app.mobiledev.apphris.sesion.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class form_cuti extends AppCompatActivity {
    private TextView txnik,txnama,txdivisi,txjabatan,txsisa,txNlaiAngsuran,txNoDivisi,txNoJabatan;
    private EditText edHp,edLamacuti,edSisaCuti,edPeriode,edTglMulai,edTglSelesai,edAlasan;
    private Button btnSimpan;
    private String kyano,nama,namaLengkap,bulan;
    private SessionManager sessionmanager;
    private ImageButton btnBack;
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;
    private ImageButton btnTglMulai,btnTglSelesai;
    private ProgressDialog mProgressDialog;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_cuti);
        txnik=findViewById(R.id.txNik);
        txnama=findViewById(R.id.txNama);
        txdivisi=findViewById(R.id.txDivisi);
        txjabatan=findViewById(R.id.txJabatan);
        txNoJabatan=findViewById(R.id.txNoJabatan);
        txNoDivisi=findViewById(R.id.txNoDivisi);
        edHp=findViewById(R.id.edHp);
        edLamacuti=findViewById(R.id.edLamacuti);
        edSisaCuti=findViewById(R.id.edSisaCuti);
        btnTglMulai=findViewById(R.id.btnTglMulai);
        btnTglSelesai=findViewById(R.id.btnTglSelesai);
        edPeriode=findViewById(R.id.edPeriode);
        edTglMulai=findViewById(R.id.edTglMulai);
        edTglSelesai=findViewById(R.id.edTglSelesai);
        btnBack=findViewById(R.id.btnBack);
        edAlasan=findViewById(R.id.edAlasan);
        btnSimpan=findViewById(R.id.btnSimpan);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Loading ...");
        sessionmanager = new SessionManager(form_cuti.this);
        kyano=sessionmanager.getIdUser();
        nama=sessionmanager.getUsername();
        namaLengkap=sessionmanager.getNamaLEngkap();
        edSisaCuti.setInputType(InputType.TYPE_NULL);
        AndroidNetworking.initialize(form_cuti.this);
        edPeriode.setInputType(InputType.TYPE_NULL);
        edLamacuti.setInputType(InputType.TYPE_NULL);

        getInformasiKaryawan(kyano);
        getSisaCuti();
        getPeriode();
        btnTglMulai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog("mulai");
            }
        });

        btnTglSelesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog("");

            }
        });

        mToolbar = findViewById(R.id.toolbar_abs);
        mToolbar.setTitle("Form Cuti");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        edAlasan.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    String ctmulai=edTglMulai.getText().toString();
                    String ct_selesai=edTglSelesai.getText().toString();
                    if((ctmulai.equals(null)||ctmulai.equals(""))&&(ct_selesai.equals(null)||ct_selesai.equals(""))){
                        Log.d("NULLL", "onClick: Kosong");
                    }else{
                        getLamaCuti(ctmulai,ct_selesai);
                    }
                }
                @Override
                public void afterTextChanged(Editable s) {
                }
                });


                btnSimpan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            String nodivisi = txNoDivisi.getText().toString();
                            String nojabatan = txNoJabatan.getText().toString();
                            String ctmulai = edTglMulai.getText().toString();
                            String ct_selesai = edTglSelesai.getText().toString();
                            String ct_alasan = edAlasan.getText().toString();
                            String ctperiode = edPeriode.getText().toString();
                            String telp = edHp.getText().toString();

                            if (nodivisi.equals("") || nodivisi.equals(null)) {
                                helper.showMsg(form_cuti.this, "informasi", "koneksi internet anda bermasalah", helper.WARNING_TYPE);
                            } else {
                                if (ctmulai.equals(null) || ctmulai.equals("")) {
                                    helper.showMsg(form_cuti.this, "informasi", "tgl cuti mulai belum diisi", helper.WARNING_TYPE);
                                } else if (ct_selesai.equals(null) || ct_selesai.equals("")) {
                                    helper.showMsg(form_cuti.this, "informasi", "tgl cuti selesai belum diisi", helper.WARNING_TYPE);
                                } else if (ct_alasan.equals(null) || ct_alasan.equals("")) {
                                    helper.showMsg(form_cuti.this, "informasi", "field alasan belum diisi", helper.WARNING_TYPE);
                                } else if (telp.equals(null) || telp.equals("")) {
                                    helper.showMsg(form_cuti.this, "informasi", "field telp belum diisi", helper.WARNING_TYPE);
                                } else {
                                    mProgressDialog.show();
                                    formCuti(kyano, nodivisi, nojabatan, ctmulai, ct_selesai, ct_alasan, ctperiode, telp);
                                }
                            }


                        } catch (NullPointerException e) {
                            Log.d("NULLPOINTER", "onClick: " + e);
                        }
                    }
                });

    }

    private void getSisaCuti(){
                 AndroidNetworking.post(api.URL_getCuti)
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
                                String sisaCuti=response.getString("sisa");
                                edSisaCuti.setText(""+sisaCuti);
                                Log.d("SISA_CUTI", "onResponse: "+sisaCuti);


                            }else{
                                Log.d("DATA_BOOLEAN", "onResponse: "+success);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("JSONERORABSEN", "onResponse: "+e);
                            helper.showMsg(form_cuti.this, "Peringatan", ""+helper.PESAN_SERVER, helper.ERROR_TYPE);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        helper.showMsg(form_cuti.this, "Peringatan", ""+helper.PESAN_KONEKSI, helper.ERROR_TYPE);
                        Log.d("EROOR_EXCEPTION", "onError: "+anError);

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
                                    if(!kyhp.equals("")||!kyhp.equals("null")){
                                        edHp.setText(""+kyhp);
                                    }


                                }
                            }else{
                                Log.d("DATA_BOOLEAN", "onResponse: "+success);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("JSONERORABSEN", "onResponse: "+e);
                            helper.showMsg(form_cuti.this, "Peringatan", ""+helper.PESAN_SERVER, helper.ERROR_TYPE);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        helper.showMsg(form_cuti.this, "Peringatan", ""+helper.PESAN_KONEKSI, helper.ERROR_TYPE);
                        Log.d("EROOR_EXCEPTION", "onError: "+anError);

                    }
                });

    }

    private void getLamaCuti(final String ctmulai,final String ctselesai){
                 AndroidNetworking.post(api.URL_getLamaCuti)
                .addBodyParameter("key", api.key)
                .addBodyParameter("ct_mulai", ctmulai)
                .addBodyParameter("ct_selesai", ctselesai)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Boolean success = response.getBoolean("success");
                            String hari = response.getString("hari");
                            if (success) {
                               edLamacuti.setText(""+hari);
                            }else{
                                Log.d("DATA_BOOLEAN", "onResponse: "+success);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("JSONERORABSEN", "onResponse: "+e);
                            helper.showMsg(form_cuti.this, "Peringatan", ""+helper.PESAN_SERVER, helper.ERROR_TYPE);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        helper.showMsg(form_cuti.this, "Peringatan", ""+helper.PESAN_KONEKSI , helper.ERROR_TYPE);
                        Log.d("EROOR_EXCEPTION", "onError: "+anError);

                    }
                });

    }

    private void getPeriode() {
        AndroidNetworking.post(api.URL_getPeriode)
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
                                String status = response.getString("status");
                                String kytgl = response.getString("kytgl");
                                edPeriode.setText(""+helper.format_tgl(kytgl,"").substring(3,10));
                            }
                           // mProgressDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("JSONERORABSEN", "onResponse: "+e);
                            helper.showMsg(form_cuti.this, "Peringatan", ""+helper.PESAN_SERVER, helper.ERROR_TYPE);
                            //mProgressDialog.dismiss();
                        }catch (NullPointerException e){
                            Log.d("JSONERORABSEN", "onResponse: "+e);
                        }catch (NumberFormatException e){
                            Log.d("JSONERORABSEN", "onResponse: "+e);
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        helper.showMsg(form_cuti.this, "Peringatan", ""+helper.PESAN_KONEKSI + anError, helper.ERROR_TYPE);
                        Log.d("EROOR_EXCEPTION", "onError: "+anError);
                        //mProgressDialog.dismiss();


                    }
                });
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void showDateDialog(final String tglcek){
        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);

                dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
                bulan=""+dateFormatter.format(newDate.getTime());
                if(tglcek.equals("mulai")){
                    edTglMulai.setText(""+dateFormatter.format(newDate.getTime()));
                }else{
                    edTglSelesai.setText(""+dateFormatter.format(newDate.getTime()));
                }


            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }



    private void formCuti(String kyano, String no_divisi,String no_jabatan,String ctmulai,String ctselesai,String ctalasan,String ctperiode,String cttelp) {
                 AndroidNetworking.post(api.URL_insertCuti)
                .addBodyParameter("kyano", kyano)
                .addBodyParameter("no_divisi", no_divisi)
                .addBodyParameter("no_jabatan", no_jabatan)
                .addBodyParameter("ct_mulai", ctmulai)
                .addBodyParameter("ct_selesai", ctselesai)
                .addBodyParameter("ct_alasan", ctalasan)
                .addBodyParameter("ct_periode", ctperiode)
                .addBodyParameter("ct_telp", cttelp)
                .addBodyParameter("key", api.key)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Boolean success = response.getBoolean("status");
                            String ket=response.getString("ket");
                            if (success) {
                                new SweetAlertDialog(form_cuti.this, SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("Informasi")
                                        .setContentText(""+ket)
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                finish();
                                            }
                                        })
                                        .show();
                            }else{
                                Log.d("DATA_BOOLEAN", "onResponse: "+ket);
                            }
                            mProgressDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("JSONERORCUTI", "onResponse: "+e);
                            helper.showMsg(form_cuti.this, "Peringatan", ""+helper.PESAN_SERVER, helper.ERROR_TYPE);
                            mProgressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        helper.showMsg(form_cuti.this, "Peringatan", ""+helper.PESAN_KONEKSI, helper.ERROR_TYPE);
                        Log.d("EROOR_CUTI", "onError: "+anError);
                        //mProgressDialog.dismiss();

                    }
                });

    }


}
