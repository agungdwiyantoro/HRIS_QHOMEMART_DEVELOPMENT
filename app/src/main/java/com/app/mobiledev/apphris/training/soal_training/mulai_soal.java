package com.app.mobiledev.apphris.training.soal_training;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.app.mobiledev.apphris.R;
import com.app.mobiledev.apphris.api.api;
import com.app.mobiledev.apphris.helperPackage.helper;
import com.app.mobiledev.apphris.sesion.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class mulai_soal extends AppCompatActivity {
    private Toolbar mToolbar;
    private Button btnMulai;
    private TextView txtgl,txNamatest,txWaktu,txJmlsoal,txToken;
    private SessionManager sessionmanager;
    private String kyano;
    private CheckBox ckbox;
    private Boolean agreement=false;
    private ConstraintLayout cons_mulaisoal;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mulai_soal);
        mToolbar=findViewById(R.id.toolbar_mulai_soal);
        mToolbar.setTitle("Mulai Soal");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        btnMulai=findViewById(R.id.btnMulai);
        txtgl=findViewById(R.id.txtgl);
        txJmlsoal=findViewById(R.id.txJML);
        cons_mulaisoal=findViewById(R.id.cons_mulaisoal);
        ckbox=findViewById(R.id.ckbox);
        txNamatest=findViewById(R.id.txNamatest);
        txToken=findViewById(R.id.txToken);
        txWaktu=findViewById(R.id.txWaktu);
        sessionmanager=new SessionManager(mulai_soal.this);
        kyano=sessionmanager.getIdUser();
        btnMulai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(agreement){
                    startActivity(new Intent(mulai_soal.this, Latihan.class));
                    finish();
                }else{
                    helper.snackBar(cons_mulaisoal,"anda belum ceklis persetujuan tata tertib");
                }
            }
        });
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((CheckBox) v).isChecked();
                if (checked){
                    agreement=true;
                }
                else{
                    agreement=false;
                }
            }
        });
        getinfosoal();

    }




    private void getinfosoal(){
        AndroidNetworking.post(api.URL_getinfosoal)
                .addBodyParameter("key", api.key)
                .addBodyParameter("kyano", kyano)
                .addBodyParameter("waktu", "10:43:00")
                .addBodyParameter("tgl", "25-09-2020")
                .addBodyParameter("no_training", "HR004")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Boolean success = response.getBoolean("success");
                            String jml = response.getString("jml");

                            if (success) {
                                    JSONArray jsonArray = response.getJSONArray("data");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject data = jsonArray.getJSONObject(i);
                                        txNamatest.setText(""+data.getString("materi"));
                                        txtgl.setText(""+data.getString("tntgl"));
                                        txWaktu.setText(data.getString("tnwktmulai")+"--"+data.getString("tnwktselesai"));
                                        txNamatest.setText(""+data.getString("materi"));
                                        txJmlsoal.setText(""+jml);
                                    }

                            } else {
                                helper.showMsg(mulai_soal.this,"informasi","");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("JSON_JWBAN1", "onResponse: "+e);
                            helper.showMsg(mulai_soal.this, "Peringatan",  helper.PESAN_SERVER, helper.ERROR_TYPE);

                        }catch (NumberFormatException e){
                            Log.d("JSON_JWBAN2", "onResponse: "+e);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        helper.showMsg(mulai_soal.this, "Peringatan",  helper.PESAN_KONEKSI, helper.ERROR_TYPE);
                        Log.d("JSON_JWBAN3", "onError: "+anError);
                    }
                });
    }
}
