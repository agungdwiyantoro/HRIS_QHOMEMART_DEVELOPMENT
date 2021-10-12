package com.app.mobiledev.apphris;

import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;

import com.androidnetworking.AndroidNetworking;
import com.app.mobiledev.apphris.api.api;
import com.app.mobiledev.apphris.api.set_ip;
import com.app.mobiledev.apphris.helperPackage.helper;
import com.app.mobiledev.apphris.sesion.SessionManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class reportAbsensi extends AppCompatActivity {
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;
    private EditText tgl1,tgl2;
    private ImageButton btnCalender1,btnCalender2;
    private Button btnOpen;
    private Toolbar mToolbar;
    private SessionManager sessionmanager;
    private String nik="";

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_absensi);
        btnCalender1=findViewById(R.id.btnCalender1);
        btnCalender2=findViewById(R.id.btnCalender2);
        tgl1=findViewById(R.id.tgl1);
        tgl2=findViewById(R.id.tgl2);
        btnOpen=findViewById(R.id.btnOpen);
        mToolbar = findViewById(R.id.toolbar_abs_masuk);
        tgl1.setInputType(InputType.TYPE_NULL);
        tgl2.setInputType(InputType.TYPE_NULL);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        sessionmanager = new SessionManager(reportAbsensi.this);
        nik=sessionmanager.getNik();
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        mToolbar.setTitle("Laporan Absensi");
        setSupportActionBar(mToolbar);
        AndroidNetworking.initialize(getApplicationContext());


        btnOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                if(tgl1.getText().toString().equals("")||tgl1.getText().toString().equals(null)){
                    helper.showMsg(reportAbsensi.this,"informasi","Tanggal mulai belum diisi");
                }else if(tgl2.getText().toString().equals("")||tgl2.getText().toString().equals(null)){
                    helper.showMsg(reportAbsensi.this,"informasi","Tanggal selesai belum diisi");
                } else{
                    Intent i = new Intent("android.intent.action.MAIN");
                    i.setComponent(ComponentName.unflattenFromString("com.android.chrome/com.android.chrome.Main"));
                    i.addCategory("android.intent.category.LAUNCHER");
                    Log.d("NOMOR KTP2", "onResponse: "+nik);
                    i.setData(Uri.parse( getUrl(tgl1.getText().toString(),tgl2.getText().toString(),nik)));
                    startActivity(i);
                    System.exit(0);
                }


                }
                catch(ActivityNotFoundException e) {
                    // Chrome is not installed
                    Intent viewIntent =
                            new Intent("android.intent.action.VIEW",
                                    Uri.parse(getUrl(tgl1.getText().toString(),tgl2.getText().toString(),nik)));
                    startActivity(viewIntent);
                }
            }
        });


        btnCalender1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog1();
            }
        });

        btnCalender2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog2();
            }
        });



    }


    private void showDateDialog2(){

        /**
         * Calendar untuk mendapatkan tanggal sekarang
         */
        Calendar newCalendar = Calendar.getInstance();

        /**
         * Initiate DatePicker dialog
         */
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                /**
                 * Method ini dipanggil saat kita selesai memilih tanggal di DatePicker
                 */

                /**
                 * Set Calendar untuk menampung tanggal yang dipilih
                 */
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);

                /**
                 * Update TextView dengan tanggal yang kita pilih
                 */
                tgl2.setText(""+dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        /**
         * Tampilkan DatePicker dialog
         */
        datePickerDialog.show();
    }
    private void showDateDialog1(){

        /**
         * Calendar untuk mendapatkan tanggal sekarang
         */
        Calendar newCalendar = Calendar.getInstance();

        /**
         * Initiate DatePicker dialog
         */
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                /**
                 * Method ini dipanggil saat kita selesai memilih tanggal di DatePicker
                 */

                /**
                 * Set Calendar untuk menampung tanggal yang dipilih
                 */
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);

                /**
                 * Update TextView dengan tanggal yang kita pilih
                 */
                tgl1.setText(""+dateFormatter.format(newDate.getTime()));
                Log.d("SET_TGL1", "onDateSet: "+dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        /**
         * Tampilkan DatePicker dialog
         */
        datePickerDialog.show();
    }


    private String getUrl(String tgl1,String tgl2,String nik){
         set_ip ip = new set_ip();
         String url= api.URL_generatePdf+"?nik="+nik+"&tgl1="+tgl1+"&tgl2="+tgl2;
        Log.d("DATA_URL", "getUrl: "+url);
         return url;

    }
    @Override
    public void onBackPressed() {
        new SweetAlertDialog(reportAbsensi.this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Konfirmasi keluar")
                .setContentText("Yakin keluar aplikasi?")
                .setConfirmText("Ya, keluar")
                .setCancelText("Tidak")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        finish();
                        moveTaskToBack(true);
                        android.os.Process.killProcess(android.os.Process.myPid());
                        System.exit(1);
                    }
                }).show();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.home :
                    startActivity(new Intent(reportAbsensi.this, main_fragment.class));
                    finish();
                    return true;

                case R.id.pinjaman :
                    startActivity(new Intent(reportAbsensi.this,menu_pinjaman.class));
                    finish();
                    return true;

            }
            return false;
        }
    };

}
