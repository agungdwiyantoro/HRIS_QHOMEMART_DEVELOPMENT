package com.app.mobiledev.apphris.visitor;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.app.mobiledev.apphris.R;
import com.app.mobiledev.apphris.sesion.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class AddMobilMotorVisit extends AppCompatActivity {

    TextView tvMobil, tvMotor;
    FloatingActionButton fabAdd, fabRemove;
    Toast toast;
    Button btnAddMob, btnRemoveMob, btnAddMot, btnRemoveMot, btnCOMobMot;

    private SessionManager sessionmanager;
    private String kyano="", token="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_mobil_visit);

        tvMobil = findViewById(R.id.txtMobil);
        tvMotor = findViewById(R.id.txtMotor);
        btnAddMob = findViewById(R.id.btnAddMobil);
        btnRemoveMob = findViewById(R.id.btnRemoveMobil);
        btnAddMot = findViewById(R.id.btnAddMotor);
        btnRemoveMot = findViewById(R.id.btnRemoveMotor);
        btnCOMobMot = findViewById(R.id.btnCOMobMot);

        sessionmanager = new SessionManager(AddMobilMotorVisit.this);
        kyano = sessionmanager.getIdUser();
        token = sessionmanager.getToken();

        getMobil(token);
        //fabAdd = findViewById(R.id.fabAdd);
        //fabRemove = findViewById(R.id.fabRemove);

        //getMobil();

        btnAddMob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateAddMobil();
            }
        });

        btnRemoveMob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateRemoveMobil();
            }
        });

        btnAddMot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateAddMotor();
            }
        });

        btnRemoveMot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateRemoveMotor();
            }
        });

        btnCOMobMot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        AddMobilMotorVisit.this);

                alertDialogBuilder.setTitle("Konfirmasi");
                alertDialogBuilder
                        .setMessage("Apakah anda yakin selesai sekarang ?")
                        .setCancelable(false)
                        .setPositiveButton("Ya",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // if this button is clicked, close
                                // current activity
                                /*String currentTime = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
                                sessionmanager.createCutOff(currentTime);*/

                                postCutOff();
                            }
                        })
                        .setNegativeButton("Tidak",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // if this button is clicked, just close
                                // the dialog box and do nothing
                                dialog.cancel();
                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();

                //postCutOff();
            }
        });

    }

    private void updateAddMobil() {
        Log.d("TAG_CEK_TOKEN_KYANO", "getTOKEN: "+token+" KYANO: "+kyano);
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .build();
        AndroidNetworking.post("https://api.qhomedata.id/report/traffic")
                .addHeaders("Content-Type","application/json")
                .addHeaders("Authorization","Bearer "+token)
                .addHeaders("X-QH","wX0EtKTEbA3nR85MUzdOc0CqdlF1ORS1DRqICHIG3Ny2t-TwuwPD4942tb5U0f-RD58FiGOFIbIfPmA8jXSwipip7Z_zx-_450efUNVPl7KPZx3hZ5_LNw~~")
                .addBodyParameter("kyano", kyano)
                .addBodyParameter("type", "mobil")
                .addBodyParameter("action", "add")
                .setPriority(Priority.HIGH)
                .setOkHttpClient(okHttpClient)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            //JSONObject jsonObject = new JSONObject(response);
                            boolean success = Boolean.parseBoolean(response.getString("status"));
                            String message = response.getString("message");
                            String dataMobil = response.getString("data");
                            Log.d("TAG_TESTRESPONSE", "onResponse: "+message+" "+dataMobil+" "+success);
                            if (success) {
                                toast = Toast.makeText(AddMobilMotorVisit.this, message, Toast.LENGTH_LONG);
                                tvMobil.setText(""+dataMobil);
                                Log.d("TAG_MOBIL", "onResponse: "+dataMobil);
                            } else {
                                toast = Toast.makeText(AddMobilMotorVisit.this, "gagal", Toast.LENGTH_LONG);
                            }
                            toast.show();

                        }catch (JSONException e){
                            e.printStackTrace();
                            Log.d("JSONException", "onResponse: "+e);
                        }catch (NumberFormatException e){
                            e.printStackTrace();
                            Log.d("JSONPROduk", "onResponse: " + e);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("EROOR_PRODUK", "onError: " + anError);
                        toast = Toast.makeText(AddMobilMotorVisit.this, "Koneksi Internet Bermasalah", Toast.LENGTH_LONG);
                        toast.show();
                    }
                });

    }

    private void updateRemoveMobil() {
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .build();
        AndroidNetworking.post("https://api.qhomedata.id/report/traffic")
                .addHeaders("Content-Type","application/json")
                .addHeaders("Authorization","Bearer "+token)
                .addHeaders("X-QH","wX0EtKTEbA3nR85MUzdOc0CqdlF1ORS1DRqICHIG3Ny2t-TwuwPD4942tb5U0f-RD58FiGOFIbIfPmA8jXSwipip7Z_zx-_450efUNVPl7KPZx3hZ5_LNw~~")
                .addBodyParameter("kyano", kyano)
                .addBodyParameter("type", "mobil")
                .addBodyParameter("action", "sub")
                .setPriority(Priority.HIGH)
                .setOkHttpClient(okHttpClient)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            //JSONObject jsonObject = new JSONObject(response);
                            boolean success = Boolean.parseBoolean(response.getString("status"));
                            String message = response.getString("message");
                            String dataMobil = response.getString("data");
                            Log.d("TAG_TESTRESPONSE", "onResponse: "+message+" "+dataMobil+" "+success);
                            if (success) {
                                toast = Toast.makeText(AddMobilMotorVisit.this, message, Toast.LENGTH_LONG);
                                tvMobil.setText(""+dataMobil);
                                Log.d("TAG_MOBIL", "onResponse: "+dataMobil);
                            } else {
                                toast = Toast.makeText(AddMobilMotorVisit.this, "gagal", Toast.LENGTH_LONG);
                            }
                            toast.show();

                        }catch (JSONException e){
                            e.printStackTrace();
                            Log.d("JSONException", "onResponse: "+e);
                        }catch (NumberFormatException e){
                            e.printStackTrace();
                            Log.d("JSONPROduk", "onResponse: " + e);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("EROOR_PRODUK", "onError: " + anError);
                        toast = Toast.makeText(AddMobilMotorVisit.this, "Koneksi Internet Bermasalah", Toast.LENGTH_LONG);
                        toast.show();
                    }
                });

    }

    private void updateAddMotor() {
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .build();
        AndroidNetworking.post("https://api.qhomedata.id/report/traffic")
                .addHeaders("Content-Type","application/json")
                .addHeaders("Authorization","Bearer "+token)
                .addHeaders("X-QH","wX0EtKTEbA3nR85MUzdOc0CqdlF1ORS1DRqICHIG3Ny2t-TwuwPD4942tb5U0f-RD58FiGOFIbIfPmA8jXSwipip7Z_zx-_450efUNVPl7KPZx3hZ5_LNw~~")
                .addBodyParameter("kyano", kyano)
                .addBodyParameter("type", "motor")
                .addBodyParameter("action", "add")
                .setPriority(Priority.HIGH)
                .setOkHttpClient(okHttpClient)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            //JSONObject jsonObject = new JSONObject(response);
                            boolean success = Boolean.parseBoolean(response.getString("status"));
                            String message = response.getString("message");
                            String dataMotor = response.getString("data");
                            Log.d("TAG_TESTRESPONSE", "onResponse: "+message+" "+dataMotor+" "+success);
                            if (success) {
                                toast = Toast.makeText(AddMobilMotorVisit.this, message, Toast.LENGTH_LONG);
                                tvMotor.setText(""+dataMotor);
                                Log.d("TAG_MOBIL", "onResponse: "+dataMotor);
                            } else {
                                toast = Toast.makeText(AddMobilMotorVisit.this, "gagal", Toast.LENGTH_LONG);
                            }
                            toast.show();

                        }catch (JSONException e){
                            e.printStackTrace();
                            Log.d("JSONException", "onResponse: "+e);
                        }catch (NumberFormatException e){
                            e.printStackTrace();
                            Log.d("JSONPROduk", "onResponse: " + e);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("EROOR_PRODUK", "onError: " + anError);
                        toast = Toast.makeText(AddMobilMotorVisit.this, "Koneksi Internet Bermasalah", Toast.LENGTH_LONG);
                        toast.show();
                    }
                });

    }

    private void updateRemoveMotor() {
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .build();
        AndroidNetworking.post("https://api.qhomedata.id/report/traffic")
                .addHeaders("Content-Type","application/json")
                .addHeaders("Authorization","Bearer "+token)
                .addHeaders("X-QH","wX0EtKTEbA3nR85MUzdOc0CqdlF1ORS1DRqICHIG3Ny2t-TwuwPD4942tb5U0f-RD58FiGOFIbIfPmA8jXSwipip7Z_zx-_450efUNVPl7KPZx3hZ5_LNw~~")
                .addBodyParameter("kyano", kyano)
                .addBodyParameter("type", "motor")
                .addBodyParameter("action", "sub")
                .setPriority(Priority.HIGH)
                .setOkHttpClient(okHttpClient)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            //JSONObject jsonObject = new JSONObject(response);
                            boolean success = Boolean.parseBoolean(response.getString("status"));
                            String message = response.getString("message");
                            String dataMotor = response.getString("data");
                            Log.d("TAG_TESTRESPONSE", "onResponse: "+message+" "+dataMotor+" "+success);
                            if (success) {
                                toast = Toast.makeText(AddMobilMotorVisit.this, message, Toast.LENGTH_LONG);
                                tvMotor.setText(""+dataMotor);
                                Log.d("TAG_MOBIL", "onResponse: "+dataMotor);
                            } else {
                                toast = Toast.makeText(AddMobilMotorVisit.this, "gagal", Toast.LENGTH_LONG);
                            }
                            toast.show();

                        }catch (JSONException e){
                            e.printStackTrace();
                            Log.d("JSONException", "onResponse: "+e);
                        }catch (NumberFormatException e){
                            e.printStackTrace();
                            Log.d("JSONPROduk", "onResponse: " + e);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("EROOR_PRODUK", "onError: " + anError);
                        toast = Toast.makeText(AddMobilMotorVisit.this, "Koneksi Internet Bermasalah", Toast.LENGTH_LONG);
                        toast.show();
                    }
                });

    }

    public void getMobil(String token) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String currentDate = sdf.format(new Date());

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .build();
        //AndroidNetworking.get("https://api.qhomedata.id/report/traffic/"+currentDate+"?type=history")
        AndroidNetworking.get("https://api.qhomedata.id/report/traffic/"+currentDate+"?type=history")
                .addHeaders("Content-Type","application/json")
                .addHeaders("Authorization","Bearer "+ token)
                .addHeaders("X-QH","wX0EtKTEbA3nR85MUzdOc0CqdlF1ORS1DRqICHIG3Ny2t-TwuwPD4942tb5U0f-RD58FiGOFIbIfPmA8jXSwipip7Z_zx-_450efUNVPl7KPZx3hZ5_LNw~~")
                .setPriority(Priority.HIGH)
                .setOkHttpClient(okHttpClient)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            boolean status = Boolean.parseBoolean(response.getString("status"));
                            String message = response.getString("message");
                            if (status) {
                                JSONObject data = response.getJSONObject("data");
                                String jmlMobil = data.getString("jumlahMobil");
                                String jmlMotor = data.getString("jumlahMotor");

                                tvMobil.setText(""+jmlMobil);
                                tvMotor.setText(""+jmlMotor);

                                Log.d("TEST_JUMLAH ", "MOBIL: "+jmlMobil+" MOTOR: "+jmlMotor);
                            } else {
                                toast = Toast.makeText(AddMobilMotorVisit.this, message, Toast.LENGTH_LONG);
                                toast.show();
                            }

                        }catch (JSONException e){
                            e.printStackTrace();
                            Log.d("JSONException", "onResponse: "+e);
                        }catch (NumberFormatException e){
                            e.printStackTrace();
                            Log.d("JSONPROduk", "onResponse: " + e);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("EROOR_PRODUK", "onError: " + anError);
                        toast = Toast.makeText(AddMobilMotorVisit.this, "Koneksi Internet Bermasalah", Toast.LENGTH_LONG);
                        toast.show();
                    }
                });
    }

    private void postCutOff() {
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .build();
        AndroidNetworking.post("https://api.qhomedata.id/report/traffic")
                .addHeaders("Content-Type","application/json")
                .addHeaders("Authorization","Bearer "+token)
                .addHeaders("X-QH","wX0EtKTEbA3nR85MUzdOc0CqdlF1ORS1DRqICHIG3Ny2t-TwuwPD4942tb5U0f-RD58FiGOFIbIfPmA8jXSwipip7Z_zx-_450efUNVPl7KPZx3hZ5_LNw~~")
                .addBodyParameter("kyano", kyano)
                //.addBodyParameter("type", "customer")
                .addBodyParameter("action", "cutoff")
                .setPriority(Priority.HIGH)
                .setOkHttpClient(okHttpClient)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            boolean success = Boolean.parseBoolean(response.getString("status"));
                            String message = response.getString("message");
                            String dataPengunjung = response.getString("data");
                            Log.d("TAG_TESTRESPONSE", "onResponse: "+message+" "+dataPengunjung+" "+success);
                            if (success) {
                                toast = Toast.makeText(AddMobilMotorVisit.this, "Hitung Mobil & Motor berhasil disimpan ", Toast.LENGTH_LONG);
                                toast.show();
                                startActivity(new Intent(AddMobilMotorVisit.this, Visitor.class));
                                Log.d("TAG_MOBMOT", "onResponse: "+dataPengunjung);
                            } else {
                                toast = Toast.makeText(AddMobilMotorVisit.this, "gagal", Toast.LENGTH_LONG);
                            }
                            toast.show();

                        }catch (JSONException e){
                            e.printStackTrace();
                            Log.d("JSONException", "onResponse: "+e);
                        }catch (NumberFormatException e){
                            e.printStackTrace();
                            Log.d("JSONPROduk", "onResponse: " + e);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("EROOR_PRODUK", "onError: " + anError);
                        toast = Toast.makeText(AddMobilMotorVisit.this, "Koneksi Internet Bermasalah", Toast.LENGTH_LONG);
                        toast.show();
                    }
                });

    }

}