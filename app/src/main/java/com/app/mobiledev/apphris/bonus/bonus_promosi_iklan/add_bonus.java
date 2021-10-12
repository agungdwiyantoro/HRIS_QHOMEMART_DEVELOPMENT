package com.app.mobiledev.apphris.bonus.bonus_promosi_iklan;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
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
import android.widget.Spinner;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.app.mobiledev.apphris.R;
import com.app.mobiledev.apphris.api.api;
import com.app.mobiledev.apphris.helperPackage.helper;
import com.app.mobiledev.apphris.sesion.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class add_bonus extends AppCompatActivity {
    private ImageView image;
    private Spinner keterangan;
    private EditText tgl1;
    private Button btnOk;
    private Uri resultUri;
    private Bitmap image_bmap;
    private ImageButton btnCalender;
    private String encodedimage="";
    private SweetAlertDialog mProgressDialog;
    private SessionManager sessionmanager;
    private String kyano;
    private DatePickerDialog datePickerDialog;
    int i = 0;
    private SimpleDateFormat dateFormatter;
    private String[] sosmed = new String[]{"Twitter", "Facebook","Instagram","Whatsapp","dll"};

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bonus);
        image=findViewById(R.id.image);
        tgl1=findViewById(R.id.tgl);
        keterangan=findViewById(R.id.keterangan);
        btnOk=findViewById(R.id.btnOk);
        btnCalender=findViewById(R.id.btnCalender1);
        mProgressDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        mProgressDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        mProgressDialog.setTitleText("Loading");
        mProgressDialog.setCancelable(true);
        tgl1.setInputType(InputType.TYPE_NULL);
        sessionmanager = new SessionManager(add_bonus.this);
        kyano=sessionmanager.getIdUser();
        spitipe();
        Log.d("IDENTITAS_KYANO", "onCreate: "+kyano);
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        mToolbar = findViewById(R.id.toolbar_abs);
        mToolbar.setTitle("Form Bonus");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });


        btnCalender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog();
            }
        });





        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressDialog.show();
                String ket=keterangan.getSelectedItem().toString();
                String tgl=tgl1.getText().toString();


                try {
                    if(image_bmap==null){
                        helper.showMsg(add_bonus.this,"Informasi","gambar belum diiisi");
                        mProgressDialog.dismiss();
                    }
                    else if(ket.equals("")){
                        helper.showMsg(add_bonus.this,"Informasi","keterangan belum diiisi");
                        mProgressDialog.dismiss();
                    }
                    else if(tgl.equals("")){
                        helper.showMsg(add_bonus.this,"Informasi","tanggal belum diiisi");
                        mProgressDialog.dismiss();
                    }
                    else{

                            i=0;
                            i++;
                            Handler handler = new Handler();
                            Runnable r = new Runnable() {
                                @Override
                                public void run() { i = 0; }
                            };
                            if (i == 1) {
                                insertBonus(""+ket);
                                handler.postDelayed(r, 250);
                            } else if (i == 2) {
                                i = 0;
                            }

                        }


                }catch (NullPointerException e){
                    helper.showMsg(add_bonus.this,"","Lokasi anda belum  terdeteksi \n pastikan gps anda aktif\n coba restart aplikasi",helper.WARNING_TYPE);
                    mProgressDialog.dismiss();
                }



            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if(requestCode == 1 && resultCode == Activity.RESULT_OK){
                    final Uri imageUri = data.getData();
                    resultUri = imageUri;
                    image.setImageURI(resultUri);
                    Log.d("IMAGE_UPLOAD", "onActivityResult: "+resultUri);
                    image_bmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    image_bmap.compress(Bitmap.CompressFormat.PNG, 50, bytes);
                    encodedimage= Base64.encodeToString(bytes.toByteArray(), Base64.DEFAULT);
            }


        }catch (Exception e){
            Log.d("TAKE_GALERI", "onActivityResult: "+e);
        }


    }

    private void insertBonus(final String ket){
                AndroidNetworking.post(api.URL_insertBonus)
                .addBodyParameter( "img_screenshoot", "data:image/png;base64,"+encodedimage)
                .addBodyParameter("ket",ket)
                .addBodyParameter("status","N")
                .addBodyParameter("kyano", kyano)
                .addBodyParameter("tgl", getDateNow())
                .addBodyParameter("key", api.key)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Boolean success = response.getBoolean("status");
                            String data = response.getString("ket");
                            if (success) {
                                new SweetAlertDialog(add_bonus.this, SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("Informasi")
                                        .setContentText(""+data)
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                finish();
                                            }
                                        })
                                        .show();


                            } else {

                                helper.showMsg(add_bonus.this,"Informasi",""+data,helper.WARNING_TYPE);
                            }
                            mProgressDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("JSONERORABSEN", "onResponse: "+e);
                            helper.showMsg(add_bonus.this, "Peringatan", "" + helper.PESAN_SERVER, helper.ERROR_TYPE);
                            mProgressDialog.dismiss();
                        }


                    }

                    @Override
                    public void onError(ANError anError) {
                        helper.showMsg(add_bonus.this, "Peringatan", "" + helper.PESAN_KONEKSI, helper.ERROR_TYPE);
                        Log.d("EROOR_EXCEPTION", "onError: "+anError);
                        mProgressDialog.dismiss();

                    }
                });

    }

    public String getDateNow(){
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c);
        return  formattedDate;
    }

    private void showDateDialog(){
        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(add_bonus.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                tgl1.setText(""+dateFormatter.format(newDate.getTime()));
                Log.d("SET_TGL1", "onDateSet: "+dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
    private void spitipe(){
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter(this,R.layout.spinner_item,sosmed);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        keterangan.setAdapter(spinnerArrayAdapter);
    }


}
