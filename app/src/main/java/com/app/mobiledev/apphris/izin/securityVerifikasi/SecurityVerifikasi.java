package com.app.mobiledev.apphris.izin.securityVerifikasi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;

import com.app.mobiledev.apphris.R;

public class SecurityVerifikasi extends AppCompatActivity {

    LinearLayout llSecVer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_verifikasi);

        llSecVer = findViewById(R.id.llSecVer);

        llSecVer.setOnClickListener(v -> {
            Intent intent= new Intent(SecurityVerifikasi.this, JenisVerifikasi.class);
            startActivity(intent);
        });

    }
}