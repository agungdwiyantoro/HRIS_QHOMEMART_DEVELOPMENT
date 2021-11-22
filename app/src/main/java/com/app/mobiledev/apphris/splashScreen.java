package com.app.mobiledev.apphris;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.app.mobiledev.apphris.api.api;
import com.app.mobiledev.apphris.helperPackage.helper;
import com.app.mobiledev.apphris.sesion.SessionManager;
import com.app.mobiledev.apphris.underMaintanace.underMaintanance;

import org.json.JSONException;
import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class splashScreen extends AppCompatActivity {


    helper newVer = new helper() {
        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            Nv(o);
        }
    };

    private static int LamaTampilSplash = 3000;
    private SessionManager sessionmanager;
    private Boolean cek_update=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        newVer.execute();

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);
        helper.requestPermissions(splashScreen.this);
        sessionmanager =new SessionManager(splashScreen.this);
        helper.getMsetProg(splashScreen.this,"no_hp_admi");


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // to do auto generated stub
                if (!checkInternet()){
                    new SweetAlertDialog(splashScreen.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Tidak ada koneksi internet")
                            .setContentText("Silakan hidupkan koneksi anda dan coba lagi")
                            .setConfirmText("Coba Lagi")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    startActivity(getIntent());
                                    finish();
                                }
                            }).show();
                } else {
                   // helper2.update("1.13",splashScreen.this);//

                    if(cek_update==false){
                        chekingUnderMainTance();
                        //sessionmanager.checkLogin();
                    }

                    this.selesai();
                }
            }
            private void selesai() {
                finish();
            }
        },LamaTampilSplash);


    };


    public boolean checkInternet(){
        boolean connectStatus;
        ConnectivityManager ConnectionManager=(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo= ConnectionManager != null ? ConnectionManager.getActiveNetworkInfo() : null;
        connectStatus = networkInfo != null && networkInfo.isConnected();
        return connectStatus;
    }

    private void Nv(Object n) {
        try {

//            String newV = n.toString();
//            PackageInfo pInfo = splashScreen.this.getPackageManager().getPackageInfo(getPackageName(), 0);
//            String version = pInfo.versionName;
//            Log.d("GET_VERNEW", "getNEWVer: "+newV+" OLD VER "+version);
//            if (!version.equals(newV)) {
//                Intent load1 = new Intent(splashScreen.this, update_layout.class);
//                startActivity(load1);
//                cek_update=true;
//                finish();
//            }
        } catch (Exception e) {
            Toast.makeText(splashScreen.this, "aaError : "+e, Toast. LENGTH_SHORT).show();
        }


    }

    private void chekingUnderMainTance(){
        AndroidNetworking.get(api.URL_API_QHOME_ID)
                .addHeaders("Content-Type", "application/json")
                .addHeaders("X-QH", "wX0EtKTEbA3nR85MUzdOc0CqdlF1ORS1DRqICHIG3Ny2t-TwuwPD4942tb5U0f-RD58FiGOFIbIfPmA8jXSwipip7Z_zx-_450efUNVPl7KPZx3hZ5_LNw~~")
                .addHeaders("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhaWQiOiIzLjE2MzcxMTg4MzQiLCJnaWQiOlsiNiJdfQ.UcB3T4aGbNVcdPC6zFhCqKv-aUqhCU10ZQVllsAcVxA")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            boolean success = response.getBoolean("status");
                            if (success) {
                                Intent i = new Intent(splashScreen.this, underMaintanance.class);
                                i.putExtra("pesan",response.getString("message"));
                                startActivity(i);
                                finish();
                            }else{
                                sessionmanager.checkLogin();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("UNDERMAINTANCE", "onResponse: "+e);
                        }


                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("EROOR_UNDERMAINTANCE", "onError: "+anError);
                    }
                });

    }

}