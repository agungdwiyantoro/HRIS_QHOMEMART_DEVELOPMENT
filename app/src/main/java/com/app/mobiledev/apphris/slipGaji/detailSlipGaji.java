package com.app.mobiledev.apphris.slipGaji;

import android.app.Dialog;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.app.mobiledev.apphris.R;
import com.app.mobiledev.apphris.api.api;
import com.app.mobiledev.apphris.sesion.SessionManager;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class detailSlipGaji extends AppCompatActivity {
    WebView webSlip;
    Toolbar mToolbar;
    String bulan="";
    String tahun="";
    SessionManager msesion;
    Dialog dialogInfoSlipGaji;
    TextView txtClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_slip_gaji);
        mToolbar = findViewById(R.id.toolbar_detail_slip);

        mToolbar.setTitle("Detail Slip Gaji");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        webSlip=findViewById(R.id.webSlip);
        webSlip.getSettings().setBuiltInZoomControls(true);
        msesion=new SessionManager(detailSlipGaji.this);
        bulan = getIntent().getExtras().getString("bulan");
        tahun = getIntent().getExtras().getString("tahun");


        String postData = null;
        try {
            postData = "bulan=" + URLEncoder.encode(bulan, "UTF-8") + "&tahun=" + URLEncoder.encode(tahun, "UTF-8")+"&nik=" + URLEncoder.encode(msesion.getNik(), "UTF-8");
            webSlip.postUrl(api.URL_getSlipGaji,postData.getBytes());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        notifyDialogSlipGaji();
    }

    private void notifyDialogSlipGaji()  {
        dialogInfoSlipGaji = new Dialog(detailSlipGaji.this);
        dialogInfoSlipGaji.setContentView(R.layout.dialog_infoslipgaji);
        dialogInfoSlipGaji.setCancelable(true);

        dialogInfoSlipGaji.setCanceledOnTouchOutside(false);
        txtClose = (TextView) dialogInfoSlipGaji.findViewById(R.id.txtClose);

        txtClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogInfoSlipGaji.dismiss();
            }
        });
        dialogInfoSlipGaji.show();
    }






}