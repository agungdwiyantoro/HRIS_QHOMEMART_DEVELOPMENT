package com.app.mobiledev.apphris.pinjaman;

import android.app.ProgressDialog;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
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

import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class tambahPinjaman extends AppCompatActivity {
    private TextView txnik,txnama,txdivisi,txjabatan,txsisa,txNlaiAngsuran,txNoDivisi,txNoJabatan;
    private EditText edAngsuran,edNominal;
    private SessionManager sessionmanager;
    private String kyano,nama,namaLengkap;
    private Button btnplus, btnmin;
    private EditText edAlasan;
    private ImageButton btnBack;
    private Spinner edJenis;
    private ProgressDialog mProgressDialog;
    private Button btnPengajuan;
    private String cek;
    private LinearLayout linPembayaran;
    private RadioGroup rgPembayaran;
    private String mPembayaran="";
    private String[] data = new String[]{"Tunai", "Barang",};

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_pinjaman);
        txnik=findViewById(R.id.txNik);
        txnama=findViewById(R.id.txNama);
        txdivisi=findViewById(R.id.txDivisi);
        txjabatan=findViewById(R.id.txJabatan);
        linPembayaran=findViewById(R.id.linPembayaran);
        txNoJabatan=findViewById(R.id.txNoJabatan);
        txNoDivisi=findViewById(R.id.txNoDivisi);
        //txsisa=findViewById(R.id.txSisa);
        edAlasan=findViewById(R.id.edAlasan);
        btnBack=findViewById(R.id.btnBack);
        edJenis=findViewById(R.id.edJenis);
        txNlaiAngsuran=findViewById(R.id.txNilaiAngsuran);
        edAngsuran=findViewById(R.id.edAngsuran);
        edNominal=findViewById(R.id.edNominal);
        btnmin = findViewById(R.id.btnminus);
        btnplus = findViewById(R.id.btnplus);
        rgPembayaran = findViewById(R.id.rgPembayaran);
        edAngsuran.setInputType(InputType.TYPE_NULL);
        btnPengajuan=findViewById(R.id.btnPengajuan);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Loading ...");
        sessionmanager = new SessionManager(tambahPinjaman.this);
        kyano=sessionmanager.getIdUser();
        nama=sessionmanager.getUsername();
        namaLengkap=sessionmanager.getNamaLEngkap();

        mToolbar = findViewById(R.id.toolbar_abs);

        setSupportActionBar(mToolbar);
        mToolbar.setTitle("Form Pinjaman");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
        edAngsuran.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if (edAngsuran.getText().toString().equals("")) {
                    edAngsuran.setText("0");
                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        edJenis.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(edJenis.getSelectedItem().toString().equals("Tunai")){
                    linPembayaran.setVisibility(View.GONE);
                }else{
                    linPembayaran.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        btnplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qty = Integer.parseInt(edAngsuran.getText().toString());
                edAngsuran.setText("" + (qty + 1));
                hitungAngsuran();

            }
        });
        btnmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qty = Integer.parseInt(edAngsuran.getText().toString());
                if (qty <= 0) {
                    edAngsuran.setText("0");
                } else {
                    edAngsuran.setText("" + (qty - 1));
                    hitungAngsuran();
                }
            }
        });



        btnPengajuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressDialog.show();
                String nik=txnik.getText().toString();
                String alasan=edAlasan.getText().toString();
                String nilaiAmgsuran=txNlaiAngsuran.getText().toString();

                if(nik.equals("TEST")||nik.equals("")||nik.equals("null")){
                    helper.showMsg(tambahPinjaman.this,"informasi","NIK tidak ditemukan",helper.ERROR_TYPE);
                }
                else if(alasan.equals("")||alasan.equals("null")){
                    helper.showMsg(tambahPinjaman.this,"informasi","field alasan belum diisi",helper.ERROR_TYPE);
                }
                else if(nilaiAmgsuran.equals("0")||nilaiAmgsuran.equals("")){
                    helper.showMsg(tambahPinjaman.this,"informasi","field angsuran belum diisi",helper.ERROR_TYPE);
                }else{

                    if(edJenis.getSelectedItem().toString().equals("Tunai")){
                        mPembayaran="";
                    }else{
                        if(rgPembayaran.getCheckedRadioButtonId() == R.id.rbPotongGaji){
                            mPembayaran="Y";
                        } else if(rgPembayaran.getCheckedRadioButtonId() == R.id.rbCash){
                            mPembayaran="N";
                        }
                    }

                    simpanPengajuan(kyano,edJenis.getSelectedItem().toString(),edAlasan.getText().toString(),txNoDivisi.getText().toString(),txNoJabatan.getText().toString(),edNominal.getText().toString().replace(",","").replace(".",""),edAngsuran.getText().toString(),mPembayaran);
                }
                mProgressDialog.dismiss();

            }
        });
        getInformasiKaryawan(kyano);
        spitipe();



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
                            helper.showMsg(tambahPinjaman.this, "Peringatan", "" + helper.PESAN_SERVER, helper.ERROR_TYPE);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        helper.showMsg(tambahPinjaman.this, "Peringatan", ""+helper.PESAN_KONEKSI, helper.ERROR_TYPE);
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
        startActivity(new Intent(tambahPinjaman.this, pinjamanUang.class));
        finish();
    }


    private void hitungAngsuran(){
        try {
            double  nominal=Double.valueOf(edNominal.getText().toString().replace(",","").replace(".",""));
            int  angsuran=Integer.valueOf(edAngsuran.getText().toString());
            double hasil=nominal/angsuran;
            if (Double.isNaN(hasil)||Double.isInfinite(hasil)) {
                txNlaiAngsuran.setText("0");
            }else{
                txNlaiAngsuran.setText(""+helper.rp(hasil));
            }



        }catch (NumberFormatException e){
            Log.d("", "hitungAngsuran: "+e);

        }catch (NullPointerException e){
            Log.d("", "hitungAngsuran: "+e);
        }


    }

    private void simpanPengajuan(String kyano, String kbjenis,String kbalasan,String no_divisi,String no_jabatan,String nominal,String angsuran,String potonggj) {
        AndroidNetworking.post(api.URL_pengajuanPinjaman)
                .addBodyParameter("kyano", kyano)
                .addBodyParameter("kbjenis", kbjenis)
                .addBodyParameter("kbalasan", kbalasan)
                .addBodyParameter("no_divisi", no_divisi)
                .addBodyParameter("no_jabatan", no_jabatan)
                .addBodyParameter("nominal", nominal)
                .addBodyParameter("potong_gj", potonggj)
                .addBodyParameter("angsuran", angsuran)
                .addBodyParameter("key", api.key)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Boolean success = response.getBoolean("status");
                            if (success) {

                                new SweetAlertDialog(tambahPinjaman.this, SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("Informasi")
                                        .setContentText("Data berhasil disimpan")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                finish();
                                            }
                                        })
                                        .show();
                            }else{
                                Log.d("DATA_BOOLEAN", "onResponse: "+success);
                            }
                            mProgressDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("JSONERORABSEN", "onResponse: "+e);
                            helper.showMsg(tambahPinjaman.this, "Peringatan", "" + helper.PESAN_SERVER, helper.ERROR_TYPE);
                            mProgressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("EROOR_EXCEPTION", "onError: "+anError);
                        helper.showMsg(tambahPinjaman.this, "Peringatan", "" + helper.PESAN_KONEKSI, helper.ERROR_TYPE);

                        mProgressDialog.dismiss();

                    }
                });

    }


}
