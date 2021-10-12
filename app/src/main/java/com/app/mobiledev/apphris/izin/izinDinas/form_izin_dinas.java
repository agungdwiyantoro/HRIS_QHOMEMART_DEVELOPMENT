 package com.app.mobiledev.apphris.izin.izinDinas;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

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

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class form_izin_dinas extends AppCompatActivity {


        private TextView txnik,txnama,txdivisi,txNoDivisi,txNojabatan;
        private  String nik,divisi,nodivisi,kepentingan,nojabatan,transportasi,keperluan,akomodasi,keterangan,daerah,date_time_mulai,date_time_sampai;
        private String tdano="",tgl_mulai,tgl_selesai,jam_mulai,jam_selesai,trans,ket;
        public  EditText edJamMulai,edTglMulai,edLokasi_tujuan,edTglSampai,edjamSampai;
        private Spinner izin;
        private ImageButton btnjamMulai,btnIzin,btnJamSampai,btnTglMulai,btnTglSampai;
        private SessionManager sessionmanager;
        private DatePickerDialog datePickerDialog;
        private SimpleDateFormat dateFormatter;
        private Button btnSimpan,btn_hapus;
        private String bulan,kyano,nama,namaLengkap, encodedimage="";
        private SweetAlertDialog mProgressDialog;
        private List<String> listIzin= new ArrayList<>();
        private int i = 0;
        private LinearLayout lin_form_izin;
        private ImageButton map_lokasi_awal;
        private Toolbar mToolbar;
        private double lat=0;
        private double lon=0;
        private double lat_lok_awal=0;
        private double lang_lok_awal=0;
        private LinearLayout lin_lokasi_awal;
        private EditText edTransportasi;
        private EditText edKeperluan;
        private EditText edAkomodasi;
        private EditText edKeterangan;
        private  TextView tx_detail_izin_dinas;

    private double lat_lok_tujuan=0;
    private double lang_lok_tujuan=0;
    private map_place_picker_izin_lokasi dialog_map_lokasi;
    Context ctx;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_izin);
        txnik=findViewById(R.id.txNik);
        txnama=findViewById(R.id.txNama);
        txdivisi=findViewById(R.id.txDivisi);
        txNojabatan=findViewById(R.id.txNoJabatan);
        edLokasi_tujuan=findViewById(R.id.edLokasi_tujuan);
        lin_form_izin=findViewById(R.id.lin_form_izin);
        lin_lokasi_awal=findViewById(R.id.lin_lokasi_awal);
        izin=findViewById(R.id.izin);
        btnSimpan=findViewById(R.id.btnSimpan);
        map_lokasi_awal=findViewById(R.id.map_lokasi_awal);
        txNoDivisi=findViewById(R.id.txNoDivisi);
        tx_detail_izin_dinas=findViewById(R.id.tx_detail_izin_dinas);
        edJamMulai=findViewById(R.id.edjamMulai);
        edTglMulai=findViewById(R.id.edTglMulai);
        edTglSampai=findViewById(R.id.edTglSampai);
        edjamSampai=findViewById(R.id.edjamSampai);
        edTransportasi=findViewById(R.id.edTransportasi);
        edKeperluan=findViewById(R.id.edKeperluan);
        edAkomodasi=findViewById(R.id.edAkomodasi);
        edKeterangan=findViewById(R.id.edKeterangan);
        edJamMulai.setInputType(InputType.TYPE_NULL);
        edTglMulai.setInputType(InputType.TYPE_NULL);
        edTglSampai.setInputType(InputType.TYPE_NULL);
        edjamSampai.setInputType(InputType.TYPE_NULL);
        btnjamMulai=findViewById(R.id.btnjamMulaii);
        btnJamSampai=findViewById(R.id.btnJamSampai);
        btnTglMulai=findViewById(R.id.btnTglMulai);
        btn_hapus=findViewById(R.id.btn_hapus);
        btnTglSampai=findViewById(R.id.btnTglSampai);
        mProgressDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        mProgressDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        mProgressDialog.setTitleText("Loading");
        mProgressDialog.setCancelable(true);
        listIzin = new ArrayList<String>();
        helper.disabledEditText(edLokasi_tujuan);
        sessionmanager = new SessionManager(form_izin_dinas.this);
        kyano=sessionmanager.getIdUser();
        nama=sessionmanager.getUsername();
        namaLengkap=sessionmanager.getNamaLEngkap();
        dialog_map_lokasi = new map_place_picker_izin_lokasi();


        tx_detail_izin_dinas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(form_izin_dinas.this, listDetailIzinDinas.class);
                Bundle x = new Bundle();
                x.putString("tdano", tdano);
                intent.putExtras(x);
                startActivity(intent);
            }
        });


        btn_hapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SweetAlertDialog(form_izin_dinas.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Konfirmasi")
                        .setContentText("Yakin akan \nhapus izin dinas..?")
                        .setConfirmText("Ya, Lanjutkan")
                        .setCancelText("Tidak")
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                finish();
                            }
                        })
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                                mProgressDialog.show();
                                hapusIzinDinas();
                            }
                        }).show();

            }
        });


        mProgressDialog.show();
        getIzin();
        getInformasiKaryawan(kyano);
        getId_izin_dinas();

        btnTglMulai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog_mulai();
            }
        });
        edTglMulai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDialog_mulai();
            }
        });
        edTglSampai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDialog_sampai();
            }
        });
        btnTglSampai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog_sampai();
            }
        });
        edJamMulai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeLiniearDialogMulai();
            }
        });
        btnjamMulai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               timeLiniearDialogMulai();
            }
        });
        edjamSampai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeLiniearDialogSampai();
            }
        });
        btnJamSampai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeLiniearDialogSampai();
            }
        });
        map_lokasi_awal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                opendialog_map_lokasi();
            }
        });

        mToolbar = findViewById(R.id.toolbar_abs);
        setSupportActionBar(mToolbar);
        mToolbar.setTitle("Form Izin Dinas");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                date_time_mulai=edTglMulai.getText().toString()+" "+edJamMulai.getText().toString();
                date_time_sampai=edTglSampai.getText().toString()+" "+edjamSampai.getText().toString();

                transportasi=edTransportasi.getText().toString();
                keperluan=edKeperluan.getText().toString();
                akomodasi=edAkomodasi.getText().toString();
                keterangan=edKeterangan.getText().toString();
                daerah=edLokasi_tujuan.getText().toString();
                i=0;
                i++;
                Handler handler = new Handler();
                Runnable r = new Runnable() {
                    @Override
                    public void run() { i = 0; }
                };

                try {
                    if(kyano.equals("")){
                        helper.snackBar(lin_form_izin,"nomor karyawan belum ditemukan");
                    }else if(edTglMulai.getText().toString().equals("")){
                        helper.snackBar(lin_form_izin,"tgl berangkat belum diisi...");
                    }else if(edJamMulai.getText().toString().equals("")){
                        helper.snackBar(lin_form_izin,"jam berangkat belum diisi...");
                    }else if(edTglSampai.getText().toString().equals("")){
                        helper.snackBar(lin_form_izin,"tgl datang belum diisi...");
                    }else if(edjamSampai.getText().toString().equals("")){
                        helper.snackBar(lin_form_izin,"jam datang belum diisi...");
                    }
                    else if(daerah.equals("")){
                        helper.snackBar(lin_form_izin,"lokasi tujuan belum diisi..");
                    }
                    else if(transportasi.equals("")){
                        helper.snackBar(lin_form_izin,"transportasi belum diisi...");
                    }else if(keperluan.equals("")){
                        helper.snackBar(lin_form_izin,"keperluan  belum diisi...");
                    }else if(akomodasi.equals("")){
                        helper.snackBar(lin_form_izin,"akomodasi  belum diisi...");
                    }
                    else if(keterangan.equals("")){
                        helper.snackBar(lin_form_izin,"keterangan belum diisi...");
                    }else{
                        if (i == 1) {
                            // formCuti(kyano,nodivisi,nojabatan,tgl,jamMulai,jamSelesai,kepentingan);
                            Log.d("CEK_VALUE_FORM", "onClick: tgl="+" jamMulai"+tdano+" "+transportasi+" kep"+keperluan+" akomodasi"+akomodasi+" ket"+keterangan+" daerah"+daerah);
                            mProgressDialog.show();
                            if(tdano.equals("")){

                                insertIzinDinas(kyano,nodivisi,nojabatan,date_time_mulai,daerah,transportasi,keperluan,akomodasi, keterangan,date_time_sampai);
                            }else{
                                updateIzinDinas(date_time_mulai,daerah,transportasi,keperluan,akomodasi, keterangan,date_time_sampai);

                            }


                            handler.postDelayed(r, 250);
                        } else if (i == 2) {
                            i = 0;
                        }

                    }


                }catch (NullPointerException e){
                    Log.d("null_value", "onClick: "+e);
                    helper.snackBar(lin_form_izin,"data gagal disimpan");
                    mProgressDialog.dismiss();
                }
            }
        });




    }




    private void getIzin(){
        AndroidNetworking.post(api.URL_getIzin)
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
                                    listIzin.add(data.getString("alasan"));

                                }
                                spitipe();
                            } else{
                                Log.d("DATA_BOOLEAN", "onResponse: ");
                            }
                            mProgressDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("JSONERORCUTI", "onResponse: "+e);
                            helper.showMsg(form_izin_dinas.this, "Peringatan", ""+helper.PESAN_SERVER, helper.ERROR_TYPE);
                            mProgressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        helper.showMsg(form_izin_dinas.this, "Peringatan", ""+helper.PESAN_KONEKSI, helper.ERROR_TYPE);
                        Log.d("EROOR_EXCEPTION", "onError: "+anError);

                    }
                });

    }


    private void spitipe(){
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this,R.layout.spinner_item,listIzin
        );
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        izin.setAdapter(spinnerArrayAdapter);
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
                                    nojabatan=jbano;
                                    nodivisi=dvano;
                                mProgressDialog.dismiss();
                                }
                            }else{
                                mProgressDialog.dismiss();
                                Log.d("DATA_BOOLEAN", "onResponse: "+success);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("JSONERORABSEN", "onResponse: "+e);
                            helper.showMsg(form_izin_dinas.this, "Peringatan", ""+helper.PESAN_SERVER, helper.ERROR_TYPE);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        helper.showMsg(form_izin_dinas.this, "Peringatan", ""+helper.PESAN_KONEKSI, helper.ERROR_TYPE);
                        Log.d("EROOR_EXCEPTION", "onError: "+anError);
                        mProgressDialog.dismiss();

                    }
                });

    }
    private void timeLiniearDialogMulai(){
        Calendar mcurrentTime = Calendar.getInstance();
        final int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        final int min = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog Tp = new TimePickerDialog(form_izin_dinas.this,android.R.style.Theme_Holo_Light_Dialog, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                if(minute<10){
                    edJamMulai.setText(hourOfDay+ ":0"+minute);
                }else{
                    edJamMulai.setText(hourOfDay+ ":"+minute);
                }

            }
        },hour,min,false);
        Tp.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Tp.show();
    }

    private void timeLiniearDialogSampai(){
        Calendar mcurrentTime = Calendar.getInstance();
        final int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        final int min = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog Tp = new TimePickerDialog(form_izin_dinas.this,android.R.style.Theme_Holo_Light_Dialog, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                if(minute<10){
                    edjamSampai.setText(hourOfDay+ ":0"+minute);
                }else{
                   edjamSampai.setText(hourOfDay+ ":"+minute);
                }

            }
        },hour,min,false);
        Tp.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Tp.show();
    }

    private void showDateDialog_mulai(){
        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
                bulan=""+dateFormatter.format(newDate.getTime());
                edTglMulai.setText(""+dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void showDateDialog_sampai(){
        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);

                dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
                bulan=""+dateFormatter.format(newDate.getTime());
                edTglSampai.setText(""+dateFormatter.format(newDate.getTime()));


            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }



    private void insertIzinDinas(String kyano,String no_divisi,String no_jabatan,String waktu_mulai,String daerah,String trans,String keperluan,String akomodasi,String ket,String waktu_selesai){
        AndroidNetworking.post(api.URL_insertIzinDinas)
                .addBodyParameter("kyano", kyano)
                .addBodyParameter("no_divisi", no_divisi)
                .addBodyParameter("no_jabatan", no_jabatan)
                .addBodyParameter("waktu_mulai", waktu_mulai)
                .addBodyParameter("waktu_selesai", waktu_selesai)
                .addBodyParameter("daerah", daerah)
                .addBodyParameter("trans", trans)
                .addBodyParameter("keperluan", keperluan)
                .addBodyParameter("akomodasi", akomodasi)
                .addBodyParameter("keterangan", ket)
                .addBodyParameter("key", api.key)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Boolean success = response.getBoolean("success");
                            String ket = response.getString("ket");


                            if (success) {
                                new SweetAlertDialog(form_izin_dinas.this, SweetAlertDialog.SUCCESS_TYPE)
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
                                helper.snackBar(lin_form_izin,""+ket);
                            }
                            mProgressDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("JSONERRORDINAS", "onResponse: "+e);
                            helper.showMsg(form_izin_dinas.this, "Peringatan", ""+helper.PESAN_SERVER, helper.ERROR_TYPE);
                            mProgressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        helper.showMsg(form_izin_dinas.this, "Peringatan", ""+helper.PESAN_KONEKSI, helper.ERROR_TYPE);
                        Log.d("EROOR_EXCEPTION", "onError: "+anError);
                        mProgressDialog.dismiss();

                    }
                });


    }
    private void updateIzinDinas(String waktu_mulai,String daerah,String trans,String keperluan,String akomodasi,String ket,String waktu_selesai){
        AndroidNetworking.post(api.URL_updateInsertTrDinas)

                .addBodyParameter("waktu_mulai", waktu_mulai)
                .addBodyParameter("waktu_selesai", waktu_selesai)
                .addBodyParameter("daerah", daerah)
                .addBodyParameter("trans", trans)
                .addBodyParameter("keperluan", keperluan)
                .addBodyParameter("akomodasi", akomodasi)
                .addBodyParameter("keterangan", ket)
                .addBodyParameter("tdano", tdano)
                .addBodyParameter("key", api.key)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Boolean success = response.getBoolean("success");
                            String ket = response.getString("ket");
                            if (success) {
                                new SweetAlertDialog(form_izin_dinas.this, SweetAlertDialog.SUCCESS_TYPE)
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
                                String  cek_pesan = response.getString("success2");
                                Log.d("DATA_BOOLEAN", "onResponse: "+cek_pesan);
                                helper.snackBar(lin_form_izin,""+ket);
                            }
                            mProgressDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("JSONERRORDINAS", "onResponse: "+e);
                            helper.showMsg(form_izin_dinas.this, "Peringatan", ""+helper.PESAN_SERVER, helper.ERROR_TYPE);
                            mProgressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        helper.showMsg(form_izin_dinas.this, "Peringatan", ""+helper.PESAN_KONEKSI, helper.ERROR_TYPE);
                        Log.d("EROOR_EXCEPTION", "onError: "+anError);
                        mProgressDialog.dismiss();

                    }
                });


    }


    public void getLokasi(double llat,double llon,String lokasi) {
        try {
            if(llat == 0){
                Log.d("CEK_LOKASI", "getLokasi: "+llat);
            }else {
                lat = llat;
                lon = llon;
                Log.d("LOKASI", "getLokasi: "+llat);
                Geocoder geocoder = new Geocoder(form_izin_dinas.this, Locale.getDefault());
                String result = null;

                List<Address> addressList = geocoder.getFromLocation(lat, lon, 1);
                if (addressList != null && addressList.size() > 0) {
                    Address address = addressList.get(0);
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                        sb.append(address.getAddressLine(i)); //.append("\n");
                    }
                    sb.append(address.getLocality()).append("\n");
                    sb.append(address.getPostalCode()).append("\n");
                    sb.append(address.getCountryName());
                    Log.d("getLOKASI", "getLokasi: "+address.getAddressLine(0));
                    if(lokasi.equals("awal")){
                        edLokasi_tujuan.setText(address.getAddressLine(0));
                    }
                 //   tvLokasi.setText(""+address.getAddressLine(0));
                    result = sb.toString();
                }
            }
        } catch (IOException e) {
            Log.e("Location Address Loader", "Unable connect to Geocoder", e);
        }catch (NullPointerException e){
            Log.e("Location Address Loader", "Unable connect to Geocoder", e);
        }catch (NumberFormatException e){
            Log.d("NULL_POINTER", "getExtraLokasi_awal: "+e);
        }
    }





    private void getExtraLokasi_awal(String lokasi_awals){
        try {
            if(lokasi_awals.equals("")){
                lokasi_awals="";
            }else{
                String lokasis_awal=replace_string(lokasi_awals);
                Log.d("CEK_LOKASI_EXTRA", "getExtraLokasi_awal: "+lokasis_awal);
                String[] arr_lokasi = lokasis_awal.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                lat_lok_awal=Double.parseDouble(arr_lokasi[0]);
                lang_lok_awal=Double.parseDouble(arr_lokasi[1]);
                getLokasi(lat_lok_awal,lang_lok_awal,"awal");
            }
        }catch (NullPointerException e){
            Log.d("NULL_POINTER", "getExtraLokasi_awal: "+e);
        }catch (NumberFormatException e){
            Log.d("NULL_POINTER", "getExtraLokasi_awal: "+e);
        }


    }


    private void getExtraLokasi_tujuan(String lokasi_tujuans){
        try {
            if(lokasi_tujuans.equals("")){
                lokasi_tujuans="";
            }else{
                String lokasis_tujuan=replace_string(lokasi_tujuans);
                String[] arr_lokasi = lokasis_tujuan.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                lat_lok_tujuan=Double.parseDouble(arr_lokasi[0]);
                lang_lok_tujuan=Double.parseDouble(arr_lokasi[1]);
                getLokasi(lat_lok_tujuan,lang_lok_tujuan,"tujuan");
            }

        }catch (NullPointerException e){
            Log.d("NULL_POINTER", "getExtraLokasi_tujuan: "+e);
        }catch (NumberFormatException e){
            Log.d("NULL_POINTER", "getExtraLokasi_awal: "+e);
        }


    }

    public String replace_string(String str) {
        String s = str;
        s = s.replace("(", "");
        s = s.replace(")", "");
        s = s.replace("lat/lng:", "");
        // Add here some other replacements
        return s;
    }

    private void opendialog_map_lokasi() {
        if (!dialog_map_lokasi.isAdded()){
        dialog_map_lokasi.ctx = form_izin_dinas.this;
        Bundle bundl = new Bundle();
        bundl.putString("lokasi_awal","lokasi_awal");
        bundl.putString("lokasi_tujuan","0");
        dialog_map_lokasi.setArguments(bundl);
        dialog_map_lokasi.show(getSupportFragmentManager(), null);
        }

    }

    private void openDialog_map_lokasi_tujuan() {
        if (!dialog_map_lokasi.isAdded()){
            dialog_map_lokasi.ctx = form_izin_dinas.this;
            Bundle bundl = new Bundle();
            bundl.putString("lokasi_tujuan","lokasi_tujuan");
            bundl.putString("lokasi_awal","0");
            dialog_map_lokasi.setArguments(bundl);
            dialog_map_lokasi.show(getSupportFragmentManager(), null);
        }
    }

    public void getData(String cek_map,String koordinat){
        try {
            if(cek_map.equals("awal")){
                Log.d("CEK_KOORDINAT1", "getData: "+koordinat);
                getExtraLokasi_awal(koordinat);
            }else{
                Log.d("CEK_KOORDINAT2", "getData: "+koordinat);
                getExtraLokasi_tujuan(koordinat);
            }
        }catch (NullPointerException e){
            Log.d("NULL_POINTER", "getData: "+e);
        }
    }


    private void getId_izin_dinas(){
        Bundle bundle = getIntent().getExtras();
        if(bundle == null) {
            tdano= "";
            tx_detail_izin_dinas.setVisibility(View.GONE);
            btn_hapus.setVisibility(View.GONE);
        } else {
            tgl_mulai= bundle.getString("tgl_mulai");
            tgl_selesai= bundle.getString("tgl_selesai");
            jam_mulai= bundle.getString("jam_mulai");
            jam_selesai= bundle.getString("jam_selesai");
            keperluan= bundle.getString("keperluan");
            trans= bundle.getString("trans");
            akomodasi= bundle.getString("akomodasi");
            ket= bundle.getString("ket");
            tdano= bundle.getString("tdano");
            daerah= bundle.getString("daerah");
            edTglMulai.setText(tgl_mulai);
            edTglSampai.setText(tgl_selesai);
            edJamMulai.setText(jam_mulai);
            edjamSampai.setText(jam_selesai);
            edKeperluan.setText(keperluan);
            edTransportasi.setText(trans);
            edLokasi_tujuan.setText(daerah);
            edAkomodasi.setText(akomodasi);

            edKeterangan.setText(ket);



        }
        mProgressDialog.dismiss();
    }



    private void hapusIzinDinas(){
        AndroidNetworking.post(api.URL_deleteIzinDinas)
                .addBodyParameter("tdano", tdano)
                .addBodyParameter("key", api.key)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Boolean success = response.getBoolean("success");
                            if (success) {
                                new SweetAlertDialog(form_izin_dinas.this, SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("Informasi")
                                        .setContentText("izin dinas berhasil dihapus")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                finish();
                                            }
                                        })
                                        .show();

                            }else{
                                helper.snackBar(lin_form_izin,"gagal hapus izin dinas..!!");
                            }
                            mProgressDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("JSONERRORDINAS", "onResponse: "+e);
                            helper.showMsg(form_izin_dinas.this, "Peringatan", ""+helper.PESAN_SERVER, helper.ERROR_TYPE);
                            mProgressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        helper.showMsg(form_izin_dinas.this, "Peringatan", ""+helper.PESAN_KONEKSI, helper.ERROR_TYPE);
                        Log.d("EROOR_EXCEPTION", "onError: "+anError);
                        mProgressDialog.dismiss();

                    }
                });


    }

}
