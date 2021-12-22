package com.app.mobiledev.apphris.approve;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;

import com.app.mobiledev.apphris.R;
import com.app.mobiledev.apphris.approve.adminIzinDinas.listIzinDinasApprove;
import com.app.mobiledev.apphris.approve.adminIzinMt.listIzinMtApprove;
import com.app.mobiledev.apphris.approve.adminIzinSakit.ListIzinSakitApprove;
import com.app.mobiledev.apphris.sesion.SessionManager;

public class menu_approve extends AppCompatActivity {
    private CardView card_izin_dinas;
    private CardView card_izin_mt;
    private CardView card_izin_sakit;
    private CardView card_izin_cuti;
    private SessionManager session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_approve);
        card_izin_dinas=findViewById(R.id.card_izin_dinas);
        card_izin_mt=findViewById(R.id.card_izin_mt);
        card_izin_sakit=findViewById(R.id.card_izin_sakit);
        card_izin_cuti=findViewById(R.id.card_izin_cuti);



        card_izin_dinas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(menu_approve.this, listIzinDinasApprove.class);
                startActivity(intent);

            }
        });


        card_izin_sakit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(menu_approve.this, ListIzinSakitApprove.class);
                startActivity(intent);


            }
        });


        card_izin_cuti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(menu_approve.this, listIzinMtApprove.class);
                startActivity(intent);

            }
        });


    }
}