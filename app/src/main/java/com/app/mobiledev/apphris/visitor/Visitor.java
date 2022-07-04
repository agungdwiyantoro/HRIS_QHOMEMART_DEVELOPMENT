package com.app.mobiledev.apphris.visitor;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.app.mobiledev.apphris.R;
import com.app.mobiledev.apphris.sesion.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class Visitor extends AppCompatActivity {

    private CardView cvMobil, cvPengunjung, cvRiwayat;
    private SessionManager sessionManager;
    private Toast toast;
    private String cekCOPengunjung = "", token = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visitor);

        sessionManager = new SessionManager(Visitor.this);
        //cekCOPengunjung = sessionManager.getCutOff();

        cvMobil = findViewById(R.id.cvMobilMotor);
        cvPengunjung = findViewById(R.id.cvPengunjung);
        cvRiwayat = findViewById(R.id.cvRiwayat);

        token = sessionManager.getToken();

        cvMobil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Visitor.this, AddMobilMotorVisit.class));

            }
        });

        cvPengunjung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Visitor.this, AddPengunjungVisit.class));

            }
        });

        cvRiwayat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Visitor.this, RiwayatVisit.class));

            }
        });

        //getCekCutOff();

    }

    public void getCekCutOff() {
        String currentDate = new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(new Date());

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .build();
        AndroidNetworking.get("https://api.qhomedata.id/report/traffic/"+currentDate)
                .addHeaders("Content-Type","application/json")
                .addHeaders("Authorization","Bearer "+token)
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
                                JSONArray jsonArray = response.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++){
                                    JSONObject data = jsonArray.getJSONObject(i);
                                    final String tanggal = data.getString("tanggal");
                                    Log.d("TAG_CEK_TANGGAL", "onResponse: "+tanggal);
                                    cvPengunjung.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            String currentTime = new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(new Date());
                                            if (tanggal.equals(currentTime)) {
                                                toast = Toast.makeText(Visitor.this, "Hitung pengunjung telah selesai pada hari ini ", Toast.LENGTH_LONG);
                                                toast.show();
                                            } else {
                                                startActivity(new Intent(Visitor.this, AddPengunjungVisit.class));
                                            }
                                        }
                                    });
                                }

                            } else {
                                toast = Toast.makeText(Visitor.this, message, Toast.LENGTH_LONG);
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

                    }
                });
    }
}