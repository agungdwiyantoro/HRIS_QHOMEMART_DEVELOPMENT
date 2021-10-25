package com.app.mobiledev.apphris.slipGaji;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.app.mobiledev.apphris.R;
import com.app.mobiledev.apphris.api.api;
import com.app.mobiledev.apphris.sesion.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class detailSlipGaji extends AppCompatActivity {
    WebView webSlip;
    Toolbar mToolbar;
    String bulan="";
    String tahun="";
    SessionManager msesion;

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
            webSlip.postUrl(api.URL_getSlipGaji2,postData.getBytes());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }






}