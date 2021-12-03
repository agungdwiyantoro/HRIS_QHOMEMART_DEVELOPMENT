package com.app.mobiledev.apphris.newIzin.izinSakit;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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

import java.util.ArrayList;
import java.util.List;

public class statusApproveIzinSakit extends AppCompatActivity {
    private RecyclerView recyler_status_approve;


    private List<modelapproveIzinSakit> modelapproveIzinSakits;
    private TextView text_keterangan;
    private ImageView img_back;
    private Bundle bundle;
    private String idDetailIzin="";
    private String approveStatusHrd="";
    private String approveStatusHead="";
    private String status_approve="";
    private SessionManager msession;
    private String token;
    private TextView tx_nama;
    private TextView tx_indikasi_sakit;
    private TextView tx_catatan;
    private TextView tx_selengkapnya;
    private String nama="";
    String dataKeterangan[];
    String dataJudul[];






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_approve);
        recyler_status_approve=findViewById(R.id.recyler_status_approve);
        modelapproveIzinSakits = new ArrayList<>();
        text_keterangan=findViewById(R.id.text_keterangan);
        img_back=findViewById(R.id.img_back);
        msession=new SessionManager(this);
        token=msession.getToken();
        text_keterangan.setText("Dalam Proses");
        tx_nama=findViewById(R.id.tx_nama);
        tx_indikasi_sakit=findViewById(R.id.tx_indikasi_sakit);
        tx_catatan=findViewById(R.id.tx_catatan);
        tx_selengkapnya=findViewById(R.id.tx_selengkapnya);
        bundle = getIntent().getExtras();
        idDetailIzin=bundle.getString("id");
        approveStatusHrd=bundle.getString("status_approve_hrd");
        approveStatusHead=bundle.getString("status_approve_head");
        status_approve=bundle.getString("status_approve");
        nama=msession.getNamaLEngkap();
        tx_nama.setText(nama);
        getStatusApprove();
        getRiwayatStatusApprove(idDetailIzin);


        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public void getStatusApprove(){
        dataJudul     =new String[]{status_approve,"Human Resource Development","Head/ Supervisor"};
        dataKeterangan=new String []{status_approve,approveStatusHrd,approveStatusHead};



        for(int i=0;i<dataJudul.length;i++){
            modelapproveIzinSakit model = new modelapproveIzinSakit();
            model.setKeterangan(dataKeterangan[i]);
            model.setJudul(dataJudul[i]);
            modelapproveIzinSakits.add(model);
        }

        adapterApproveIzinSakit mAdapter;
        mAdapter = new adapterApproveIzinSakit(modelapproveIzinSakits, statusApproveIzinSakit.this);
        mAdapter.notifyDataSetChanged();
        recyler_status_approve.setLayoutManager(new LinearLayoutManager(statusApproveIzinSakit.this));
        recyler_status_approve.setItemAnimator(new DefaultItemAnimator());
        recyler_status_approve.setAdapter(mAdapter);
    }

    private void getRiwayatStatusApprove(String id){
        AndroidNetworking.get(api.URL_IzinSakit+"?id="+id)
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
                                    modelapproveIzinSakit model = new modelapproveIzinSakit();
                                    modelapproveIzinSakits.add(model);
                                    tx_indikasi_sakit.setText(data.getString("indikasi_sakit"));
                                    tx_catatan.setText(data.getString("catatan"));

                                }


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
