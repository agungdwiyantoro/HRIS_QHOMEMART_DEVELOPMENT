package com.app.mobiledev.apphris;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.app.mobiledev.apphris.helperPackage.helper;
import com.app.mobiledev.apphris.service_location.LocationUpdate;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class menu_pinjaman extends AppCompatActivity {
    CardView pinjaman,kasbon,kredit;
    TextView tvLokasi;
    private Toolbar toolbar;
    private  double lat=0,lon=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_pinjaman);
        pinjaman=findViewById(R.id.pinjaman);
        kasbon=findViewById(R.id.kasbon);
        kredit=findViewById(R.id.kredit);
        toolbar = findViewById(R.id.toolbar_abs);
        tvLokasi=findViewById(R.id.tvLokasi);
        toolbar.setTitle("Pinjaman");
        setSupportActionBar(toolbar);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        getLokasi();



        pinjaman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // startActivity(new Intent(menu_pinjaman.this, pinjamanUang.class));
                //finish();
                helper.showMsg(menu_pinjaman.this,"Informasi","Menu ini dalam perbaikan");

            }
        });


        kasbon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(menu_pinjaman.this, kasbon_karyawan.class));
//                finish();
                helper.showMsg(menu_pinjaman.this,"Informasi","Menu ini dalam perbaikan");

            }
        });



        kredit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(menu_pinjaman.this, pinjaman.class));
//               finish();
                helper.showMsg(menu_pinjaman.this,"Informasi","Menu ini dalam perbaikan");

            }
        });

    }


    public void getLokasi() {
        LocationUpdate gt = new LocationUpdate(menu_pinjaman.this);
        Location l = gt.getLocation();
        if( l == null){
            Log.d("CEK_LOKASI", "getLokasi: "+l);
        }else {
            lat = l.getLatitude();
            lon = l.getLongitude();
            Log.d("LOKASI", "getLokasi: "+l.getLatitude());
            Geocoder geocoder = new Geocoder(menu_pinjaman.this, Locale.getDefault());
            String result = null;
            try {
                List<Address > addressList = geocoder.getFromLocation(lat, lon, 1);
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
                    tvLokasi.setText(""+address.getAddressLine(0));
                    result = sb.toString();
                }
            } catch (IOException e) {
                Log.e("Location Address Loader", "Unable connect to Geocoder", e);
            }


        }

    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.report:
                    startActivity(new Intent(menu_pinjaman.this, reportAbsensi.class));
                    finish();
                    return true;

                case R.id.home:
                    startActivity(new Intent(menu_pinjaman.this, main_fragment.class));
                    finish();
                    return true;

            }
            return false;
        }
    };

    @Override
    public void onBackPressed() {
        new SweetAlertDialog(menu_pinjaman.this, SweetAlertDialog.WARNING_TYPE)
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

}
