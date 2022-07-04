package com.app.mobiledev.apphris.visitor;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.app.mobiledev.apphris.R;
import com.app.mobiledev.apphris.sesion.SessionManager;
import com.app.mobiledev.apphris.visitor.adapter.adapterVisit;
import com.app.mobiledev.apphris.visitor.model.modelVisit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class RiwayatVisit extends AppCompatActivity {

    private SessionManager sessionManager;
    CardView cvRMobil, cvRMotor, cvRPengunjung;
    private String token = "";

    List<modelVisit> mlistVisit;
    RecyclerView rvVisit;
    Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riwayat_visit);

        sessionManager = new SessionManager(RiwayatVisit.this);
        token = sessionManager.getToken();

        mlistVisit = new ArrayList<>();
        rvVisit = findViewById(R.id.rvVisit);

        getRiwayat();

    }

    public void getRiwayat() {
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .build();
        AndroidNetworking.get("https://api.qhomedata.id/report/traffic/")
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
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject data = jsonArray.getJSONObject(i);
                                    modelVisit model = new modelVisit();
                                    model.setKynm((data.getString("kynm")));
                                    model.setTanggal((data.getString("tanggal")));
                                    model.setWaktu((data.getString("waktu")));
                                    model.setMobil((data.getString("mobil")));
                                    model.setMotor((data.getString("motor")));
                                    model.setCustomer((data.getString("customer")));
                                    model.setKyano((data.getString("kyano")));
                                    mlistVisit.add(model);
                                }

                                adapterVisit mAdapter = new adapterVisit(mlistVisit, RiwayatVisit.this);
                                mAdapter.notifyDataSetChanged();
                                rvVisit.setVisibility(View.VISIBLE);
                                rvVisit.setLayoutManager(new LinearLayoutManager(RiwayatVisit.this));
                                rvVisit.setItemAnimator(new DefaultItemAnimator());
                                rvVisit.setAdapter(mAdapter);

                            } else {

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("JSONException", "onResponse: " + e);
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                            Log.d("JSONPROduk", "onResponse: " + e);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("EROOR_PRODUK", "onError: " + anError);
                        toast = Toast.makeText(RiwayatVisit.this, "Koneksi Internet Bermasalah", Toast.LENGTH_LONG);
                        toast.show();
                    }


                });
    }
}