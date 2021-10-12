package com.app.mobiledev.apphris.approve.adminIzinDinas;

import android.graphics.Color;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

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

import cn.pedant.SweetAlert.SweetAlertDialog;

public class detailListDinasApprove extends AppCompatActivity {
    private EditText edTransportasi;
    private EditText edKeperluan;
    private EditText edAkomodasi;
    private EditText edKeterangan;
    private  EditText edJamMulai,edTglMulai,edLokasi_tujuan,edTglSampai,edjamSampai;
    private SessionManager mSession;
    private TextView txnik,txnama,txdivisi,txNoDivisi,txNojabatan;
    private SweetAlertDialog mProgressDialog;
    private  String kyano,nik,divisi,nodivisi,kepentingan,nojabatan,transportasi,keperluan,akomodasi,keterangan,daerah,date_time_mulai,date_time_sampai;
    private String tdano="",tgl_mulai,tgl_selesai,jam_mulai,jam_selesai,trans,ket,kyano_izin;
    private TextView txNik;
    private CoordinatorLayout coor_detail_dinas;
    private Button btnApprove;
    private Button btnunapprove;
    private Toolbar toolbar_detail_izin_dinas_approve;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_list_dinas_approve);
        toolbar_detail_izin_dinas_approve = findViewById(R.id.toolbar_detail_izin_dinas_approve);
        setSupportActionBar(toolbar_detail_izin_dinas_approve);
        toolbar_detail_izin_dinas_approve.setTitle("Detail Approve Izin Dinas");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar_detail_izin_dinas_approve.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        txnik=findViewById(R.id.txNik);
        txnama=findViewById(R.id.txNama);
        txdivisi=findViewById(R.id.txDivisi);
        txNojabatan=findViewById(R.id.txNoJabatan);
        edJamMulai=findViewById(R.id.edjamMulai);
        edTglMulai=findViewById(R.id.edTglMulai);
        edTglSampai=findViewById(R.id.edTglSampai);
        edjamSampai=findViewById(R.id.edjamSampai);
        edTransportasi=findViewById(R.id.edTransportasi);
        edKeperluan=findViewById(R.id.edKeperluan);
        edAkomodasi=findViewById(R.id.edAkomodasi);
        edKeterangan=findViewById(R.id.edKeterangan);
        edLokasi_tujuan=findViewById(R.id.edLokasi_tujuan);
        coor_detail_dinas=findViewById(R.id.coor_detail_dinas);
        btnApprove=findViewById(R.id.btnApprove);
        btnunapprove=findViewById(R.id.btnunapprove);
        mProgressDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        mProgressDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        mProgressDialog.setTitleText("Loading");
        mProgressDialog.setCancelable(true);
        mSession=new SessionManager(this);
        kyano=mSession.getIdUser();

        helper.disabledEditText(edJamMulai);
        helper.disabledEditText(edTglMulai);
        helper.disabledEditText(edTglSampai);
        helper.disabledEditText(edjamSampai);

        helper.disabledEditText(edTransportasi);
        helper.disabledEditText(edKeperluan);
        helper.disabledEditText(edKeterangan);
        helper.disabledEditText(edLokasi_tujuan);
        helper.disabledEditText(edAkomodasi);
        getId_izin_dinas();

        btnApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProgressDialog.show();
                setApproveIzinDinas(kyano,tdano);


            }
        });


        btnunapprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProgressDialog.show();
                setApproveIzinDinas("0",tdano);

            }
        });
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
                            helper.showMsg(detailListDinasApprove.this, "Peringatan", ""+helper.PESAN_SERVER, helper.ERROR_TYPE);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        helper.showMsg(detailListDinasApprove.this, "Peringatan", ""+helper.PESAN_KONEKSI, helper.ERROR_TYPE);
                        Log.d("EROOR_EXCEPTION", "onError: "+anError);
                        mProgressDialog.dismiss();

                    }
                });

    }

    private void getId_izin_dinas(){
        Bundle bundle = getIntent().getExtras();
        if(bundle == null) {
            tdano= "";
        } else {

            tgl_mulai= bundle.getString("tgl_mulai");
            tgl_selesai= bundle.getString("tgl_selesai");
            jam_mulai= bundle.getString("jam_mulai");
            jam_selesai= bundle.getString("jam_selesai");
            keperluan= bundle.getString("keperluan");
            trans= bundle.getString("trans");
            akomodasi= bundle.getString("akomodasi");
            ket= bundle.getString("ket");
            tdano= bundle.getString("tdano");
            kyano_izin=bundle.getString("kyano_izin");
            daerah= bundle.getString("daerah");
            edTglMulai.setText(tgl_mulai);
            edTglSampai.setText(tgl_selesai);
            edJamMulai.setText(jam_mulai);
            edjamSampai.setText(jam_selesai);
            edKeperluan.setText(keperluan);
            edTransportasi.setText(trans);
            edLokasi_tujuan.setText(daerah);
            edAkomodasi.setText(akomodasi);
            edKeterangan.setText(ket);
            getInformasiKaryawan(kyano_izin);



        }
        mProgressDialog.dismiss();
    }


    private void setApproveIzinDinas(String kyanos,String tdanos){
        AndroidNetworking.post(api.URL_approve_izinDinas)
                .addBodyParameter("approve_by", kyanos)
                .addBodyParameter("tdano", tdano)
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
                                new SweetAlertDialog(detailListDinasApprove.this, SweetAlertDialog.SUCCESS_TYPE)
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
                                helper.snackBar(coor_detail_dinas,""+ket);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("JSONERORABSEN", "onResponse: "+e);
                            helper.snackBar(coor_detail_dinas,""+helper.PESAN_SERVER);
                        }
                        mProgressDialog.dismiss();
                    }

                    @Override
                    public void onError(ANError anError) {
                        helper.snackBar(coor_detail_dinas,""+helper.PESAN_KONEKSI);
                        Log.d("EROOR_EXCEPTION", "onError: "+anError);
                        mProgressDialog.dismiss();

                    }
                });

    }
}