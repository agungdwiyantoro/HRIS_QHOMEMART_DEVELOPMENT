package com.app.mobiledev.apphris.approve;

import static com.app.mobiledev.apphris.helperPackage.PaginationListener.PAGE_START;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.app.mobiledev.apphris.R;
import com.app.mobiledev.apphris.api.api;
import com.app.mobiledev.apphris.approve.adminIzinDinas.listIzinDinasApprove;
import com.app.mobiledev.apphris.approve.adminIzinMt.listIzinMtApprove;
import com.app.mobiledev.apphris.approve.adminIzinSakitHRD.ListIzinSakitApproveHRD;
import com.app.mobiledev.apphris.approve.adminIzinSakitHead.ListIzinSakitApproveHead;
import com.app.mobiledev.apphris.helperPackage.helper;
import com.app.mobiledev.apphris.izin.izinSakit.modelIzinSakit;
import com.app.mobiledev.apphris.sesion.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class menu_approve extends AppCompatActivity {
    private CardView card_izin_dinas;
    private CardView card_izin_mt;
    private CardView card_izin_sakit_approve_head;
    private CardView card_izin_sakit_approve_hrd;
    private SessionManager session;
    private String token;
    private ConstraintLayout lmenu;
    private SessionManager msession;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_approve);
        card_izin_dinas=findViewById(R.id.card_izin_dinas);
        card_izin_mt=findViewById(R.id.card_izin_mt);
        card_izin_sakit_approve_head=findViewById(R.id.card_izin_sakit_approve_head);
        card_izin_sakit_approve_hrd=findViewById(R.id.card_izin_sakit_approve_hrd);
        lmenu=findViewById(R.id.lmenu);
        msession=new SessionManager(menu_approve.this);
        token=msession.getToken();
        approveIzinSakitHead();
        approveIzinSakitHrd();



        card_izin_dinas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                helper.snackBar(lmenu,"menu ini belum tersedia....!!!");
//                Intent intent = new Intent(menu_approve.this, listIzinDinasApprove.class);
//                startActivity(intent);

            }
        });


        card_izin_sakit_approve_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(menu_approve.this, ListIzinSakitApproveHead.class);
                startActivity(intent);


            }
        });


        card_izin_sakit_approve_hrd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(menu_approve.this, ListIzinSakitApproveHRD.class);
                startActivity(intent);

            }
        });
    }

    private void approveIzinSakitHrd(){
        AndroidNetworking.get(api.URL_IzinSakit_approve_hrd)
                .addHeaders("Authorization", "Bearer "+token)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String status = response.getString("status");
                            String message = response.getString("message");
                            Log.d("HASL_RESPONSE_HRD", "onResponse: " + status);
                            if (status.equals("200")) {
                                card_izin_sakit_approve_hrd.setVisibility(View.VISIBLE);
                            } else {
                                card_izin_sakit_approve_hrd.setVisibility(View.GONE);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("JSON_RIWYAT_IZIN_SAKIT", "onResponse: " + e);

                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("EROOR_RIWYAT_SAKIT_HRD", "onError: " + anError.getErrorDetail());
                        card_izin_sakit_approve_hrd.setVisibility(View.GONE);

                    }
                });
    }


    private void approveIzinSakitHead(){
        AndroidNetworking.get(api.URL_IzinSakit_approve_head)
                .addHeaders("Authorization", "Bearer "+token)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String status = response.getString("status");
                            Log.d("HASL_RESPONSE_HEAD", "onResponse: " + status);
                            if (status.equals("200")) {
                                card_izin_sakit_approve_head.setVisibility(View.VISIBLE);
                            } else {
                                card_izin_sakit_approve_head.setVisibility(View.GONE);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("JSON_RESPONSE_HEAD", "onResponse: " + e);

                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("EROOR_RESPONSE_HEAD", "onError: " + anError.getErrorDetail());
                        card_izin_sakit_approve_head.setVisibility(View.GONE);

                    }
                });
    }



}