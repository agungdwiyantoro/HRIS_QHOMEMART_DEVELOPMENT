package com.app.mobiledev.apphris.training;

import android.content.Intent;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.app.mobiledev.apphris.R;
import com.app.mobiledev.apphris.api.api;
import com.app.mobiledev.apphris.helperPackage.helper;
import com.app.mobiledev.apphris.sesion.SessionManager;
import com.app.mobiledev.apphris.training.info_training.list_info_training;
import com.app.mobiledev.apphris.training.soal_training.mulai_soal;

import org.json.JSONException;
import org.json.JSONObject;

public class menu_training extends AppCompatActivity {
    private CardView cvSoal,cvInfo;
    private Toolbar mToolbar;
    private SessionManager sessionmanager;
    private String kyano="",jabatan="",idtraining="",no_train="";
    private ConstraintLayout cons_menu_training;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_training);
        cvSoal=findViewById(R.id.cvSoal);
        cvInfo=findViewById(R.id.cvInfo);
        mToolbar = findViewById(R.id.toolbar_menu_training);
        mToolbar.setTitle("Menu Training");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        cons_menu_training=findViewById(R.id.cons_menu_training);
        sessionmanager = new SessionManager(menu_training.this);
        kyano=sessionmanager.getIdUser();
        jabatan=sessionmanager.getCekStaff();
        idtraining=sessionmanager.getIdtraning();
        Log.d("CEK_NO_TRAINING3", "onCreate: "+no_train);



        cvInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(menu_training.this, list_info_training.class));
                //stopForeground(helper2.NOTIF_ID);
            }
        });

        cvSoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cekSoal(idtraining);
            }
        });
    }

    public void cekSoal(String notrain){
        AndroidNetworking.post(api.URL_cekSoal)
                .addBodyParameter("key", api.key)
                .addBodyParameter("no_training", notrain)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Boolean success = response.getBoolean("success");
                            String data = response.getString("data");
                            if (success && !data.equals("0")) {
                                if(idtraining.equals("")){
                                    helper.snackBar(cons_menu_training,"anda belum dikirimi soal oleh trainer....");
                                }else{
                                    startActivity(new Intent(menu_training.this, mulai_soal.class));
                                }
                            } else {
                                helper.snackBar(cons_menu_training,"anda belum dikirimi soal oleh trainer....");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            helper.showMsg(menu_training.this, "Peringatan",  helper.PESAN_SERVER, helper.ERROR_TYPE);

                        }catch (NumberFormatException e){
                            Log.d("JSON_JWBAN2", "onResponse: "+e);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        helper.showMsg(menu_training.this, "Peringatan",  helper.PESAN_KONEKSI, helper.ERROR_TYPE);
                    }
                });

    }
}
