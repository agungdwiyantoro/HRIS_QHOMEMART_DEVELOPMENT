package com.app.mobiledev.apphris.izin.izinSakit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.mobiledev.apphris.R;

public class dashboardIzin extends AppCompatActivity {

    TextView btn_izin_sakit;
    TextView btn_izin_meninggalkan_tugas;
    TextView btn_izin_dinas;
    TextView btn_izin_cuti;

    TextView btn_tambah_izin_sakit;
    TextView btn_status_izin_sakit;
    TextView btn_tambah_izin_meninggalkan_tugas;
    TextView btn_status_izin_meninggalkan_tugas;
    TextView btn_tambah_izin_dinas;
    TextView btn_status_izin_dinas;
    TextView btn_tambah_izin_cuti;
    TextView btn_status_izin_cuti;
    LinearLayout linear_izin_sakit;
    LinearLayout linear_izin_meninggalkan_tugas;
    LinearLayout linear_izin_dinas;
    LinearLayout linear_izin_cuti;
    RelativeLayout relativeMain;
    Boolean izin_sakit=false;
    Boolean izin_meninggalkan_tugas=false;
    Boolean izin_dinas=false;
    Boolean izin_cuti=false;

    ImageView imStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_izin);

        imStatus = findViewById(R.id.imStatus);

        btn_izin_sakit=findViewById(R.id.btn_izin_sakit);
        btn_izin_meninggalkan_tugas=findViewById(R.id.btn_izin_meninggalkan_tugas);
        btn_izin_dinas=findViewById(R.id.btn_izin_dinas);
        btn_izin_cuti=findViewById(R.id.btn_izin_cuti);

        btn_tambah_izin_sakit=findViewById(R.id.btn_tambah_izin_sakit);
        btn_status_izin_sakit=findViewById(R.id.btn_status_izin_sakit);
        btn_tambah_izin_meninggalkan_tugas=findViewById(R.id.btn_tambah_izin_meninggalkan_tugas);
        btn_status_izin_meninggalkan_tugas=findViewById(R.id.btn_status_izin_meninggalkan_tugas);
        btn_tambah_izin_dinas=findViewById(R.id.btn_tambah_izin_dinas);
        btn_status_izin_dinas=findViewById(R.id.btn_status_izin_dinas);
        btn_tambah_izin_cuti=findViewById(R.id.btn_tambah_izin_cuti);
        btn_status_izin_cuti=findViewById(R.id.btn_status_izin_cuti);
        linear_izin_sakit=findViewById(R.id.linear_izin_sakit);
        linear_izin_meninggalkan_tugas=findViewById(R.id.linear_izin_meninggalkan_tugas);
        linear_izin_dinas=findViewById(R.id.linear_izin_dinas);
        linear_izin_cuti=findViewById(R.id.linear_izin_cuti);

        relativeMain=findViewById(R.id.relativeMain);
        linear_izin_sakit.setVisibility(View.GONE);
        linear_izin_meninggalkan_tugas.setVisibility(View.GONE);
        linear_izin_dinas.setVisibility(View.GONE);
        linear_izin_cuti.setVisibility(View.GONE);

        imStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_izin_sakit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_izin_sakit.setVisibility(View.GONE);
                linear_izin_sakit.setVisibility(View.VISIBLE);
                izin_sakit=true;

            }
        });


        btn_tambah_izin_sakit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(dashboardIzin.this, formIzinSakit.class);
                startActivity(i);

            }
        });

        btn_status_izin_sakit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(dashboardIzin.this, ListInfinitySakitEmp.class);
                startActivity(i);

            }
        });

        btn_izin_meninggalkan_tugas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_izin_meninggalkan_tugas.setVisibility(View.GONE);
                linear_izin_meninggalkan_tugas.setVisibility(View.VISIBLE);
                izin_meninggalkan_tugas=true;
            }
        });

        btn_izin_dinas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_izin_dinas.setVisibility(View.GONE);
                linear_izin_dinas.setVisibility(View.VISIBLE);
                izin_dinas=true;
            }
        });


        btn_izin_cuti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_izin_cuti.setVisibility(View.GONE);
                linear_izin_cuti.setVisibility(View.VISIBLE);
                izin_cuti=true;
            }
        });




        relativeMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(izin_sakit=true){
                    btn_izin_sakit.setVisibility(View.VISIBLE);
                    linear_izin_sakit.setVisibility(View.GONE);

                }

                if(izin_meninggalkan_tugas=true){
                    btn_izin_meninggalkan_tugas.setVisibility(View.VISIBLE);
                    linear_izin_meninggalkan_tugas.setVisibility(View.GONE);

                }

                if(izin_dinas=true){
                    btn_izin_dinas.setVisibility(View.VISIBLE);
                    linear_izin_dinas.setVisibility(View.GONE);

                }

                if(izin_cuti=true){
                    btn_izin_cuti.setVisibility(View.VISIBLE);
                    linear_izin_cuti.setVisibility(View.GONE);
                }

            }
        });
    }
}