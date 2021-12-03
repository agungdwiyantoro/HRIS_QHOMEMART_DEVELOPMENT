package com.app.mobiledev.apphris.newIzin.izinSakit;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.app.mobiledev.apphris.R;
import com.app.mobiledev.apphris.api.api;
import com.app.mobiledev.apphris.memo.ModelMemo;
import com.app.mobiledev.apphris.sesion.SessionManager;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class listIzinSakit extends AppCompatActivity {


    RecyclerView recyler_izin_sakit;
    private List<modelIzinSakit> modelIzinSakits;
    private String token;
    private ImageView img_back;
    private SessionManager msession;
    String dataDummy[]={"Demam","Demam","Demam","Demam","Demam","Demam","Demam","Demam","Demam","Demam"};
    String dataDummy2[]={"Izin Dokter Istirahat du...","Izin Dokter Istirahat du...","Izin Dokter Istirahat du...","Izin Dokter Istirahat du...","Izin Dokter Istirahat du...","Izin Dokter Istirahat du...","Izin Dokter Istirahat du...","Izin Dokter Istirahat du...","Izin Dokter Istirahat du...","Izin Dokter Istirahat du..."};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_izin_sakit);
        recyler_izin_sakit=findViewById(R.id.recyler_izin_sakit);
        img_back=findViewById(R.id.img_back);
        msession=new SessionManager(listIzinSakit.this);
        modelIzinSakits = new ArrayList<>();
        token=msession.getToken();
        getRiwayatSakit();

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



    }





    private void getRiwayatSakit(){
        AndroidNetworking.get(api.URL_IzinSakit)
                .addHeaders("Authorization", "Bearer "+token)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            Log.d("HASL_RESPONSE", "onResponse: "+response);
                            String status = response.getString("status");

                            if(status.equals("200")){
                                JSONArray jsonArray = response.getJSONArray("message");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject data = jsonArray.getJSONObject(i);
                                    modelIzinSakit model = new modelIzinSakit();
                                    model.setId(data.getString("id"));
                                    model.setKyano(data.getString("kyano"));
                                    model.setIndikasi_sakit(data.getString("indikasi_sakit"));
                                    model.setSelesai_sakit_tanggal(data.getString("mulai_sakit_tanggal"));
                                    model.setMulai_sakit_tanggal(data.getString("selesai_sakit_tanggal"));
                                    model.setCatatan(data.getString("catatan"));
                                    model.setCreated_at(data.getString("created_at"));
                                    model.setUpdated_at(data.getString("updated_at"));
                                    model.setApprove_head(data.getString("approve_head"));
                                    model.setApprove_hrd(data.getString("approve_hrd"));
                                    model.setLampiran_file(data.getString("lampiran_file"));
                                    modelIzinSakits.add(model);
                                }
                                adapterIzinSakit mAdapter;
                                mAdapter = new adapterIzinSakit(modelIzinSakits, listIzinSakit.this);
                                mAdapter.notifyDataSetChanged();
                                recyler_izin_sakit.setLayoutManager(new LinearLayoutManager(listIzinSakit.this));
                                recyler_izin_sakit.setItemAnimator(new DefaultItemAnimator());
                                recyler_izin_sakit.setAdapter(mAdapter);

                            }else{
                                JSONObject object = response.getJSONObject("message");
                                String pesan = object.getString("lampiran_file");

                              //  Toast.makeText(formIzinSakit.this,""+pesan,toast.LENGTH_SHORT).show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("JSON_RIWYAT_IZIN_SAKIT", "onResponse: " + e);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("EROOR_RIWYAT_IZIN_SAKIT", "onError: " + anError.getErrorDetail());
                    }
                });




    }
}
