package com.app.mobiledev.apphris.visitor;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
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
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class AddPengunjungVisit extends AppCompatActivity {

    TextView tvPengujung;
    Toast toast;
    Button btnAddPengunjung, btnRemovePengunjung, btnCOPengunjung;
    private SessionManager sessionmanager;
    private String kyano="", token="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pengunjung_visit);

        tvPengujung = findViewById(R.id.txtPengunjung);
        btnAddPengunjung = findViewById(R.id.btnAddPengunjung);
        btnRemovePengunjung = findViewById(R.id.btnRemovePengunjung);
        btnCOPengunjung = findViewById(R.id.btnCOPengunjung);

        sessionmanager = new SessionManager(AddPengunjungVisit.this);
        kyano = sessionmanager.getIdUser();
        token = sessionmanager.getToken();

        getPengunjung(token);

        btnAddPengunjung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateAddPengunjung();
            }
        });

        btnRemovePengunjung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateRemovePengunjung();
            }
        });

        btnCOPengunjung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        AddPengunjungVisit.this);

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

    private void updateAddPengunjung() {
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
                .addBodyParameter("type", "customer")
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
                            String dataPengunjung = response.getString("data");
                            Log.d("TAG_TESTRESPONSE", "onResponse: "+message+" "+dataPengunjung+" "+success);
                            if (success) {
                                toast = Toast.makeText(AddPengunjungVisit.this, message, Toast.LENGTH_LONG);
                                tvPengujung.setText(""+dataPengunjung);
                                Log.d("TAG_PENGUNJUNG", "onResponse: "+dataPengunjung);
                            } else {
                                toast = Toast.makeText(AddPengunjungVisit.this, "gagal", Toast.LENGTH_LONG);
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
                        toast = Toast.makeText(AddPengunjungVisit.this, "Koneksi Internet Bermasalah", Toast.LENGTH_LONG);
                        toast.show();
                    }
                });

    }

    private void updateRemovePengunjung() {
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
                .addBodyParameter("type", "customer")
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
                            String dataPengunjung = response.getString("data");
                            Log.d("TAG_TESTRESPONSE", "onResponse: "+message+" "+dataPengunjung+" "+success);
                            if (success) {
                                toast = Toast.makeText(AddPengunjungVisit.this, message, Toast.LENGTH_LONG);
                                tvPengujung.setText(""+dataPengunjung);
                                Log.d("TAG_PENGUNJUNG", "onResponse: "+dataPengunjung);
                            } else {
                                toast = Toast.makeText(AddPengunjungVisit.this, "gagal", Toast.LENGTH_LONG);
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
                        toast = Toast.makeText(AddPengunjungVisit.this, "Koneksi Internet Bermasalah", Toast.LENGTH_LONG);
                        toast.show();
                    }
                });

    }

    public void getPengunjung(String token) {
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
                                String jmlPengunjung = data.getString("jumlahCustomer");

                                tvPengujung.setText(""+jmlPengunjung);

                                Log.d("TEST_JUMLAH ", "PENGUNJUNG: "+jmlPengunjung);
                            } else {
                                toast = Toast.makeText(AddPengunjungVisit.this, message, Toast.LENGTH_LONG);
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
                        toast = Toast.makeText(AddPengunjungVisit.this, "Koneksi Internet Bermasalah", Toast.LENGTH_LONG);
                        toast.show();

                    }
                });
    }

    private void postCutOffLate() {
        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        String timeCutOff = "23:00:00";

        if (currentTime.equals(timeCutOff)) {
            //postCutOff();
        }
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
                .addBodyParameter("type", "customer")
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
                                toast = Toast.makeText(AddPengunjungVisit.this, "Hitung pengunjung berhasil disimpan ", Toast.LENGTH_LONG);
                                toast.show();
                                startActivity(new Intent(AddPengunjungVisit.this, Visitor.class));
                                Log.d("TAG_PENGUNJUNG", "onResponse: "+dataPengunjung);
                            } else {
                                toast = Toast.makeText(AddPengunjungVisit.this, "gagal", Toast.LENGTH_LONG);
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
                        toast = Toast.makeText(AddPengunjungVisit.this, "Koneksi Internet Bermasalah", Toast.LENGTH_LONG);
                        toast.show();
                    }
                });

    }
}