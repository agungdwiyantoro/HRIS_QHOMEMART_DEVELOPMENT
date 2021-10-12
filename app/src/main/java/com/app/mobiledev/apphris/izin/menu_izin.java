package com.app.mobiledev.apphris.izin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.app.mobiledev.apphris.R;
import com.app.mobiledev.apphris.izin.izinDinas.listIzinDinas;
import com.app.mobiledev.apphris.izin.izinMT.listIzinMt;

public class menu_izin extends AppCompatActivity {
    private CardView cv_meninggalkan_tugas,cvDinas;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_izin);
        cv_meninggalkan_tugas=findViewById(R.id.cv_meninggalkan_tugas);
        cvDinas=findViewById(R.id.cvDinas);
        mToolbar = findViewById(R.id.toolbar_menu_izin);
        mToolbar.setTitle("Menu Izin");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        cv_meninggalkan_tugas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(menu_izin.this, listIzinMt.class));
            }
        });


        cvDinas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(menu_izin.this, form_izin_dinas.class);
//                Bundle x = new Bundle();
//                x.putString("lokasi_tujuan", "");
//                x.putString("lokasi_awal", "");
//                intent.putExtras(x);
//                startActivity(intent);
//
                Intent intent = new Intent(menu_izin.this, listIzinDinas.class);
                startActivity(intent);

            }
        });

    }
}