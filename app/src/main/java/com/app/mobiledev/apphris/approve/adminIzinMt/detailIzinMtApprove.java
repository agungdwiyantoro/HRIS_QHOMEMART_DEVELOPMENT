package com.app.mobiledev.apphris.approve.adminIzinMt;

import android.graphics.Color;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.app.mobiledev.apphris.R;
import com.app.mobiledev.apphris.api.api;
import com.app.mobiledev.apphris.helperPackage.helper;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class detailIzinMtApprove extends AppCompatActivity {
    private TextView txnik,txnama,txdivisi,txNoDivisi,txNojabatan;
    private Button btnApprove;
    private ImageView image;
    private String tjano,kyano_bundle,dvano,jbano,tgl_bundle,jam,sampai,kepentingan_bundle,status,image_bundle,aprove,approve_by,approve_date;
    private String kepentingan;
    private EditText edKepentingan;
    private EditText izin;
    private EditText edjamMulai;
    private EditText edjamSelesai;
    private EditText edTgl;
    private Toolbar toolbar_izin_meninggalkan_tugas;
    private CoordinatorLayout coor_detail_mtApprove;
    private SweetAlertDialog mProgressDialog;
    private String nojabatan,nodivisi;
    private Button btnUnApprove;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_izin_mt);
        txnik=findViewById(R.id.txNik);
        coor_detail_mtApprove=findViewById(R.id.coor_detail_mtApprove);
        txnama=findViewById(R.id.txNama);
        txdivisi=findViewById(R.id.txDivisi);
        toolbar_izin_meninggalkan_tugas=findViewById(R.id.toolbar_izin_meninggalkan_tugas);
        setSupportActionBar(toolbar_izin_meninggalkan_tugas);
        toolbar_izin_meninggalkan_tugas.setTitle("Detail Approve Izin Meninggalkan Tugas");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar_izin_meninggalkan_tugas.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        txNoDivisi=findViewById(R.id.txNoDivisi);
        image=findViewById(R.id.image);
        edKepentingan=findViewById(R.id.edKepentingan);
        izin=findViewById(R.id.izin);
        edjamMulai=findViewById(R.id.edjamMulai);
        edjamSelesai=findViewById(R.id.edjamSelesai);
        edTgl=findViewById(R.id.edTgl);
        mProgressDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        mProgressDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        mProgressDialog.setTitleText("Loading");
        mProgressDialog.setCancelable(true);
        btnApprove=findViewById(R.id.btnApprove);
        btnUnApprove=findViewById(R.id.btnUnApprove);

        helper.disabledEditText(edKepentingan);
        helper.disabledEditText(edjamMulai);
        helper.disabledEditText(edjamSelesai);
        helper.disabledEditText(edTgl);
        getId_izin_Mt();

        btnApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProgressDialog.show();
                setApproveIzinMt(kyano_bundle,tjano);
            }
        });

        btnUnApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProgressDialog.show();
                setApproveIzinMt("0",tjano);
            }
        });




    }


    private void setApproveIzinMt(String kyanos,String tjano){
        AndroidNetworking.post(api.URL_approve_izinMt)
                .addBodyParameter("approve_by", kyanos)
                .addBodyParameter("tjano", tjano)
                .addBodyParameter("key", api.key)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Boolean success = response.getBoolean("success");
                            String ket = response.getString("ket");
                            if (success) {
                                new SweetAlertDialog(detailIzinMtApprove.this, SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("Informasi")
                                        .setContentText(""+ket)
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                finish();
                                                sweetAlertDialog.dismiss();
                                            }
                                        })
                                        .show();
                            }else{
                                mProgressDialog.dismiss();
                                helper.snackBar(coor_detail_mtApprove,""+ket);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("JSONERORABSEN", "onResponse: "+e);
                            helper.snackBar(coor_detail_mtApprove,""+helper.PESAN_SERVER);
                        }
                        mProgressDialog.dismiss();
                    }

                    @Override
                    public void onError(ANError anError) {
                        helper.snackBar(coor_detail_mtApprove,""+helper.PESAN_KONEKSI);
                        Log.d("EROOR_EXCEPTION", "onError: "+anError);
                        mProgressDialog.dismiss();

                    }
                });

    }

    private void getId_izin_Mt(){

        Bundle bundle = getIntent().getExtras();
        if(bundle == null) {
            tjano= "";
        } else {

            tjano=bundle.getString("tjano");
            Log.d("TJOANO", "getId_izin_dinas: "+tjano);
            kyano_bundle=bundle.getString("kyano");
            dvano=bundle.getString("dvano");
            jbano=bundle.getString("jbano");
            tgl_bundle=bundle.getString("tgl");
            jam=bundle.getString("jam");
            sampai=bundle.getString("sampai");
            kepentingan=bundle.getString("kepentingan");
            status=bundle.getString("status");
            image_bundle=bundle.getString("image");
            aprove=bundle.getString("aprove");
            approve_by=bundle.getString("aproveBy");
            approve_date=bundle.getString("aproveDate");
            edjamMulai.setText(jam);
            edjamSelesai.setText(sampai);
            edTgl.setText(tgl_bundle);
            izin.setText(status);
            edKepentingan.setText(kepentingan);
            RequestOptions requestOptions = new RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true);

            Glide.with(detailIzinMtApprove.this).load(api.URL_foto_izin+"/"+image_bundle).thumbnail(Glide.with(detailIzinMtApprove.this).load(R.drawable.loading)).apply(requestOptions).into(image);
            getInformasiKaryawan(kyano_bundle);
        }
        mProgressDialog.dismiss();
    }


    private void getInformasiKaryawan(final String kyano){
        AndroidNetworking.post(api.URL_informasiKaryawan)
                .addBodyParameter("kyano", kyano)
                .addBodyParameter("key", api.key)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Boolean success = response.getBoolean("success");
                            if (success) {
                                JSONArray jsonArray = response.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject data = jsonArray.getJSONObject(i);
                                    String kyano=data.getString("kyano");
                                    String nik=data.getString("nik");
                                    String kynm=data.getString("kynm");
                                    String kyjk=data.getString("kyjk");
                                    String kyagama=data.getString("kyagama");
                                    String kytgllahir=data.getString("kytgllhr");
                                    String kystatus_kerja=data.getString("kystatus_kerja");
                                    String kyalamat=data.getString("kyalamat");
                                    String kyhp=data.getString("kyhp");
                                    String jbnama=data.getString("jbnama");
                                    String dvnama=data.getString("dvnama");
                                    String jbano=data.getString("jbano");
                                    String dvano=data.getString("dvano");
                                    txnik.setText(""+nik);
                                    txnama.setText(""+kynm);
                                    txdivisi.setText(""+dvnama);
                                    nojabatan=jbano;
                                    nodivisi=dvano;
                                    mProgressDialog.dismiss();
                                }
                            }else{
                                mProgressDialog.dismiss();
                                Log.d("DATA_BOOLEAN", "onResponse: "+success);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("JSONERORABSEN", "onResponse: "+e);
                            helper.showMsg(detailIzinMtApprove.this, "Peringatan", ""+helper.PESAN_SERVER, helper.ERROR_TYPE);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        helper.showMsg(detailIzinMtApprove.this, "Peringatan", ""+helper.PESAN_KONEKSI, helper.ERROR_TYPE);
                        Log.d("EROOR_EXCEPTION", "onError: "+anError);
                        mProgressDialog.dismiss();

                    }
                });

    }


}