package com.app.mobiledev.apphris.izin.securityVerifikasi;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.app.mobiledev.apphris.R;

public class RiwayatVerifikasiSecurity extends AppCompatActivity {

    ImageView ivBack;
    FloatingActionButton fabQR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riwayat_verifikasi_security);

        ivBack = findViewById(R.id.ivBackRiwayatVerifikasiSecurity);
        fabQR = findViewById(R.id.fabQR);

        fabQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(RiwayatVerifikasiSecurity.this, ScanQR.class);
                startActivity(intent);
            }
        });

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}