package com.app.mobiledev.apphris.test.SecurityVerifikasi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.app.mobiledev.apphris.R;

public class JenisVerifikasi extends AppCompatActivity {

    CardView cvJenisMT, cvJenisDinas;
    ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jenis_verifikasi);

        cvJenisMT = findViewById(R.id.cvJenisVerMT);
        cvJenisDinas = findViewById(R.id.cvJenisVerDinas);

        ivBack = findViewById(R.id.ivBackJenisVerifikasi);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        cvJenisMT.setOnClickListener(v -> {
            Intent intent= new Intent(JenisVerifikasi.this, RiwayatVerifikasiSecurity.class);
            startActivity(intent);
        });

        cvJenisDinas.setOnClickListener(v -> {
            Intent intent= new Intent(JenisVerifikasi.this, RiwayatVerifikasiSecurity.class);
            startActivity(intent);
        });

    }
}