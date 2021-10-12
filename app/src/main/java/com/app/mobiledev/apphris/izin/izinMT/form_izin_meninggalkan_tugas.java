package com.app.mobiledev.apphris.izin.izinMT;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.app.mobiledev.apphris.R;
import com.app.mobiledev.apphris.api.api;
import com.app.mobiledev.apphris.helperPackage.helper;
import com.app.mobiledev.apphris.sesion.SessionManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.mindorks.paracamera.Camera;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class form_izin_meninggalkan_tugas extends AppCompatActivity {

    private String tjano,kyano_bundle,dvano,jbano,tgl_bundle,jam,sampai,kepentingan_bundle,status,image_bundle,aprove,approve_by,approve_date;
    private TextView txnik,txnama,txdivisi,txNoDivisi,txNojabatan;
    private  String nik,divisi,nodivisi,jamMulai,jamSelesai,tgl,kepentingan,nojabatan;
    public EditText edjamSelesai,edJamMulai,edTgl,edKepentingan,edLokasi_akhir,edLokasi_awal;
    private Spinner izin;
    private ImageButton btnjamSelesai,btnjamMulai,btnTgl,btnIzin;
    private SessionManager sessionmanager;
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;
    private Button btnSimpan,btnHapus;
    private String bulan,kyano,nama,namaLengkap, encodedimage="";
    private String lokasi_awal,lokasi_tujuan;
    private SweetAlertDialog mProgressDialog;
    private List<String> listIzin= new ArrayList<>();
    private Camera camera;
    private ImageView image;
    private Bitmap imageFoto;
    private ImageButton btnBack;
    private int i = 0;
    private LinearLayout lin_form_izin;
    private ImageButton map_lokasi_awal;
    private ImageButton map_lokasi_tujuan;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_izin_meninggalkan_tugas);
        txnik=findViewById(R.id.txNik);
        txnama=findViewById(R.id.txNama);
        txdivisi=findViewById(R.id.txDivisi);
        btnBack=findViewById(R.id.btnBack);
        edTgl=findViewById(R.id.edTgl);
        lin_form_izin=findViewById(R.id.lin_form_izin);
        image=findViewById(R.id.image);
        btnTgl=findViewById(R.id.btnTgl);
        izin=findViewById(R.id.izin);
        btnSimpan=findViewById(R.id.btnSimpan);
        map_lokasi_awal=findViewById(R.id.map_lokasi_awal);
        txNoDivisi=findViewById(R.id.txNoDivisi);
        txNojabatan=findViewById(R.id.txNoJabatan);
        edJamMulai=findViewById(R.id.edjamMulai);
        edKepentingan=findViewById(R.id.edKepentingan);
        edjamSelesai=findViewById(R.id.edjamSelesai);
        edJamMulai.setInputType(InputType.TYPE_NULL);
        edjamSelesai.setInputType(InputType.TYPE_NULL);
        edTgl.setInputType(InputType.TYPE_NULL);
        btnjamMulai=findViewById(R.id.btnJamMulai);
        btnjamSelesai=findViewById(R.id.btnJamSelesai);
        btnHapus=findViewById(R.id.btn_hapus);
        mProgressDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        mProgressDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        mProgressDialog.setTitleText("Loading");
        mProgressDialog.setCancelable(true);
        listIzin = new ArrayList<String>();
        sessionmanager = new SessionManager(form_izin_meninggalkan_tugas.this);
        kyano=sessionmanager.getIdUser();
        nama=sessionmanager.getUsername();
        namaLengkap=sessionmanager.getNamaLEngkap();



        mProgressDialog.show();
        getIzin();
        getInformasiKaryawan(kyano);
        getId_izin_Mt();

        btnTgl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog();
            }
        });


        btnjamMulai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeLiniearDialogMulai();
            }
        });


        btnjamSelesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeLiniearDialogSelesai();

            }
        });

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera = new Camera.Builder()
                        .resetToCorrectOrientation(true)
                        .setTakePhotoRequestCode(1)
                        .setDirectory("pics")
                        .setName("ali_" + System.currentTimeMillis())
                        .setImageFormat(Camera.IMAGE_JPEG)
                        .setCompression(75)
                        .setImageHeight(1000)
                        .build(form_izin_meninggalkan_tugas.this);
                try {
                    camera.takePicture();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        mToolbar = findViewById(R.id.toolbar_izin_meninggalkan_tugas);
        setSupportActionBar(mToolbar);
        mToolbar.setTitle("Form Izin Meninggalkan Tugas");
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
                mProgressDialog.show();
                kepentingan=edKepentingan.getText().toString();
                tgl=edTgl.getText().toString();
                jamMulai=edJamMulai.getText().toString();
                jamSelesai=edjamSelesai.getText().toString();
                status=izin.getSelectedItem().toString();
                kepentingan=edKepentingan.getText().toString();
                try {
                    if(kyano.equals("")){
                        helper.snackBar(lin_form_izin,"nomor karyawan belum ditemukan");
                    }else if(tgl.equals("")){
                        helper.snackBar(lin_form_izin,"field tanggal belum diisi...");
                    }else if(jamMulai.equals("")){
                        helper.snackBar(lin_form_izin,"field jam mulai belum diisi...");
                    }
                    else if(jamSelesai.equals("")){
                        helper.snackBar(lin_form_izin,"field jam selesai belum diisi...");
                    }else if(kepentingan.equals("")){
                        helper.snackBar(lin_form_izin,"field kepentingan  belum diisi...");
                    }else{
                        if(imageFoto==null){
                            helper.showMsg(form_izin_meninggalkan_tugas.this,"Informasi","foto belum diiisi");
                            mProgressDialog.dismiss();
                        }else{
                            i=0;
                            i++;
                            Handler handler = new Handler();
                            Runnable r = new Runnable() {
                                @Override
                                public void run() { i = 0; }
                            };
                            if (i == 1) {
                                formCuti(kyano,nodivisi,nojabatan,tgl,jamMulai,jamSelesai,kepentingan,status);
                                handler.postDelayed(r, 250);
                            } else if (i == 2) {
                                i = 0;
                            }
                        }


                    }
                }catch (NullPointerException e){
                    Log.d("null_value", "onClick: "+e);
                    helper.snackBar(lin_form_izin,"data gagal disimpan");
                    mProgressDialog.dismiss();
                }
            }
        });


        btnHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProgressDialog.show();
              hapusIzinDinas(tjano);


            }
        });



      
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == Camera.REQUEST_TAKE_PHOTO) {
                Bitmap bitmap = camera.getCameraBitmap();
                if (bitmap != null ) {
                    image.setImageBitmap(bitmap);
                    imageFoto=bitmap;
                    imageFoto=Bitmap.createScaledBitmap(imageFoto, 500, 500, false);
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    imageFoto.compress(Bitmap.CompressFormat.PNG, 50, bytes);
                    encodedimage = Base64.encodeToString(bytes.toByteArray(), Base64.DEFAULT);
                }
                else { Toast.makeText(this.getApplicationContext(), "Picture not taken!", Toast.LENGTH_SHORT).show(); }
            }
        }catch (Exception e){
            Log.d("TAKE_CAMERA", "onActivityResult: "+e);
        }

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
                            helper.showMsg(form_izin_meninggalkan_tugas.this, "Peringatan", ""+helper.PESAN_SERVER, helper.ERROR_TYPE);
                            mProgressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        helper.showMsg(form_izin_meninggalkan_tugas.this, "Peringatan", ""+helper.PESAN_KONEKSI, helper.ERROR_TYPE);
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
                            helper.showMsg(form_izin_meninggalkan_tugas.this, "Peringatan", ""+helper.PESAN_SERVER, helper.ERROR_TYPE);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        helper.showMsg(form_izin_meninggalkan_tugas.this, "Peringatan", ""+helper.PESAN_KONEKSI, helper.ERROR_TYPE);
                        Log.d("EROOR_EXCEPTION", "onError: "+anError);
                        mProgressDialog.dismiss();

                    }
                });

    }
    private void timeLiniearDialogMulai(){
        Calendar mcurrentTime = Calendar.getInstance();
        final int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        final int min = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog Tp = new TimePickerDialog(form_izin_meninggalkan_tugas.this,android.R.style.Theme_Holo_Light_Dialog, new TimePickerDialog.OnTimeSetListener() {
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

    private void timeLiniearDialogSelesai(){
        Calendar mcurrentTime = Calendar.getInstance();
        final int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        final int min = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog Tp = new TimePickerDialog(form_izin_meninggalkan_tugas.this,android.R.style.Theme_Holo_Light_Dialog, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                if(minute<10){
                    edjamSelesai.setText(hourOfDay+ ":0"+minute);
                }else{
                    edjamSelesai.setText(hourOfDay+ ":"+minute);
                }

            }
        },hour,min,false);
        Tp.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Tp.show();
    }

    private void showDateDialog(){
        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);

                dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
                bulan=""+dateFormatter.format(newDate.getTime());
                edTgl.setText(""+dateFormatter.format(newDate.getTime()));


            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
    private void formCuti(String kyano, String no_divisi,String no_jabatan,String tgl,String jam_mulai,String jam_selesai,String kepentingan,String statuss) {
        AndroidNetworking.post(api.URL_uploadIzin)
                .addBodyParameter("kyano", kyano)
                .addBodyParameter( "img", "data:image/png;base64,"+encodedimage)
                .addBodyParameter("no_devisi", no_divisi)
                .addBodyParameter("no_jabatan", no_jabatan)
                .addBodyParameter("tgl", tgl)
                .addBodyParameter("jam_mulai", jam_mulai)
                .addBodyParameter("jam_selesai", jam_selesai)
                .addBodyParameter("kepentingan", kepentingan)
                .addBodyParameter("status", statuss)
                .addBodyParameter("key", api.key)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Boolean success = response.getBoolean("success");
                            if (success) {
                                new SweetAlertDialog(form_izin_meninggalkan_tugas.this, SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("Informasi")
                                        .setContentText("form izin meninggalkan tugas berhasil disimpan")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                finish();
                                            }
                                        })
                                        .show();

                            }else{
                                Log.d("DATA_BOOLEAN", "onResponse: ");
                            }
                            mProgressDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("JSONERORCUTI", "onResponse: "+e);
                            helper.showMsg(form_izin_meninggalkan_tugas.this, "Peringatan", ""+helper.PESAN_SERVER, helper.ERROR_TYPE);
                            mProgressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        helper.showMsg(form_izin_meninggalkan_tugas.this, "Peringatan", ""+helper.PESAN_KONEKSI, helper.ERROR_TYPE);
                        Log.d("EROOR_EXCEPTION", "onError: "+anError);
                        mProgressDialog.dismiss();

                    }
                });

    }

    private void getId_izin_Mt(){

        Bundle bundle = getIntent().getExtras();
        if(bundle == null) {
            tjano= "";
//            tx_detail_izin_dinas.setVisibility(View.GONE);
            btnHapus.setVisibility(View.GONE);
            btnSimpan.setVisibility(View.VISIBLE);
        } else {

            tjano=bundle.getString("tjano");
            Log.d("TJOANO", "getId_izin_dinas: "+tjano);
            kyano_bundle=bundle.getString("kyano");
            dvano=bundle.getString("dvano");
            jbano=bundle.getString("jbano");
            tgl_bundle=bundle.getString("tgl");
            jam=bundle.getString("jam");
            sampai=bundle.getString("sampai");
            kepentingan=bundle.getString("kepentingan");
            status=bundle.getString("status");
            image_bundle=bundle.getString("image");
            aprove=bundle.getString("aprove");
            approve_by=bundle.getString("aproveBy");
            approve_date=bundle.getString("aproveDate");
            edJamMulai.setText(jam);
            edjamSelesai.setText(sampai);
            edTgl.setText(tgl_bundle);
            edKepentingan.setText(kepentingan);
            RequestOptions requestOptions = new RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true);
            Log.d("CEK_URL", "getId_izin_Mt: "+api.URL_foto_izin+"/"+image_bundle);
            Glide.with(form_izin_meninggalkan_tugas.this).load(api.URL_foto_izin+"/"+image_bundle).thumbnail(Glide.with(form_izin_meninggalkan_tugas.this).load(R.drawable.loading)).apply(requestOptions).into(image);
            btnHapus.setVisibility(View.VISIBLE);
            btnSimpan.setVisibility(View.GONE);

        }
        mProgressDialog.dismiss();
    }


    private void hapusIzinDinas(String tjanos){
        AndroidNetworking.post(api.URL_deleteIzinMt)
                .addBodyParameter("tjano", tjanos)
                .addBodyParameter("key", api.key)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Boolean success = response.getBoolean("success");
                            if (success) {
                                new SweetAlertDialog(form_izin_meninggalkan_tugas.this, SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("Informasi")
                                        .setContentText("izin meninggalkan tugas berhasil dihapus")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                finish();
                                            }
                                        })
                                        .show();

                            }else{
                                helper.snackBar(lin_form_izin,"gagal hapus izin..!!");
                            }
                            mProgressDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("JSONERRORDINAS", "onResponse: "+e);
                            helper.showMsg(form_izin_meninggalkan_tugas.this, "Peringatan", ""+helper.PESAN_SERVER, helper.ERROR_TYPE);
                            mProgressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        helper.showMsg(form_izin_meninggalkan_tugas.this, "Peringatan", ""+helper.PESAN_KONEKSI, helper.ERROR_TYPE);
                        Log.d("EROOR_EXCEPTION", "onError: "+anError);
                        mProgressDialog.dismiss();

                    }
                });


    }

}