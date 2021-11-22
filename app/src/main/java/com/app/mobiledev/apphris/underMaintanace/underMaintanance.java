package com.app.mobiledev.apphris.underMaintanace;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.app.mobiledev.apphris.R;

public class underMaintanance extends AppCompatActivity {

    private TextView txInfo;
    private TextView txtCloses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_under_maintanance);
        txInfo=findViewById(R.id.txInfo);
        txtCloses=findViewById(R.id.txtCloses);
        Intent intent= getIntent();

        try {
             String pesan =intent.getStringExtra("pesan");
             txInfo.setText(pesan);


        }catch (NullPointerException e){
            Log.d("CEKK_UNDER_MAINTANCE", "onCreate: "+e);
        }

        txtCloses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                moveTaskToBack(true);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
            }
        });


    }
}