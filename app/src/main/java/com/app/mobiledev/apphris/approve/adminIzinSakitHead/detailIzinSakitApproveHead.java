package com.app.mobiledev.apphris.approve.adminIzinSakitHead;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.app.mobiledev.apphris.R;
import com.app.mobiledev.apphris.api.api;
import com.app.mobiledev.apphris.helperPackage.helper;
import com.app.mobiledev.apphris.sesion.SessionManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class detailIzinSakitApproveHead extends AppCompatActivity {

    private String id, name,indikasi_sakit, kyano, mulai_sakit_tanggal, selesai_sakit_tanggal, catatan, created_at, updated_at;
    private String approve_head, approve_hrd, lampiran_file, head_kyano, head_approve_date, hrd_approve_date, head_name,hrd_kyano;
    private String status;
    private TextView txNama_title, tx_jenisIzin_title, tx_tanggal_title, tx_bulan_tahun_title;
    private TextView tx_nama, tx_indikasi_sakit, tx_catatan, tx_tgl_pengajuan,tx_link_lihat_dokumen;
    private  TextView tx_status,tx_tgl_status;
    private TextView tx_nama_dialog,btn_close_dialog,btn_setuju_dialog,tx_info_dialog,tx_jenis_izin_dialog;
    private ImageView ivImageName;
    private LinearLayout linearOption, linearKeputusan;
    private String token;
    private SessionManager msession;
    private  Dialog dialogApprove;
    private Button btn_setuju;
    private SimpleDateFormat dateFormat_day;
    private SimpleDateFormat dateFormat_month_year;
    private SimpleDateFormat dateFormatSources;
    private Date dateSource;
    private CardView card_status_approve;
    private LinearLayout lin_approve_btn;
    private LinearLayout lin_status_approve;
    private ImageView imStatus;
    private Bundle bundle;
    private Dialog dialogFoto;
    private ImageView img_izin_sakit;
    private TextView txtClose;
    private ImageView img_back;
    private Button btn_tolak;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_izin_sakit_approve);
        txNama_title = findViewById(R.id.txNama_title);
        tx_jenisIzin_title = findViewById(R.id.tx_jenisIzin_title);
        tx_tanggal_title = findViewById(R.id.tx_tanggal_title);
        tx_bulan_tahun_title = findViewById(R.id.tx_bulan_tahun_title);
        tx_nama = findViewById(R.id.tx_nama);
        tx_indikasi_sakit = findViewById(R.id.tx_indikasi_sakit);
        tx_catatan = findViewById(R.id.tx_catatan);
        card_status_approve=findViewById(R.id.card_status_approve);
        lin_approve_btn=findViewById(R.id.lin_approve_btn);
        btn_tolak=findViewById(R.id.btn_tolak);
        tx_tgl_pengajuan = findViewById(R.id.tx_tgl_pengajuan);
        tx_link_lihat_dokumen = findViewById(R.id.tx_link_lihat_dokumen);
        lin_status_approve=findViewById(R.id.lin_status_approve);
        tx_status=findViewById(R.id.tx_status);
        tx_tgl_status=findViewById(R.id.tx_tgl_status);
        imStatus=findViewById(R.id.imStatus);
        btn_setuju=findViewById(R.id.btn_setuju);
        img_back=findViewById(R.id.img_back);
        msession=new SessionManager(this);
        token=msession.getToken();
        dateFormatSources = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat_day = new SimpleDateFormat("dd");
        dateFormat_month_year = new SimpleDateFormat("MMM-yyyy");
        bundle = getIntent().getExtras();
        id=bundle.getString("id");
        card_status_approve.setVisibility(View.GONE);




        linearOption = findViewById(R.id.lLDetOption);
        linearKeputusan = findViewById(R.id.lLDetKeputusanAprove);
        getDetailSakitApprove(id);
        Log.d("CEK_URL_APPROVE", "onCreate: "+api.URL_IzinSakit_approve_head+"?id="+id);
        Log.d("CEK_URL_STATUS", "onCreate: "+status);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getDetailSakitApprove(String _id) {
        AndroidNetworking.get(api.URL_IzinSakit_approve_head+"?id="+_id)
                .addHeaders("Authorization", "Bearer "+token)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void onResponse(JSONObject response) {
                       try {
                            status=response.getString("status");
                            if(status.equals("200")){
                                Log.d("RESULT_DET_PENG_SAKIT", "onResponse: " + response.toString());
                                JSONArray dataArray = response.getJSONArray("message");

                                Log.d("RESULT_DET_PENG_SAKIT0", "onResponse: " + dataArray.getJSONObject(0).getString("name"));
                                name=dataArray.getJSONObject(0).getString("name");
                                kyano=dataArray.getJSONObject(0).getString("kyano");
                                indikasi_sakit=dataArray.getJSONObject(0).getString("indikasi_sakit");
                                mulai_sakit_tanggal=dataArray.getJSONObject(0).getString("mulai_sakit_tanggal");
                                selesai_sakit_tanggal=dataArray.getJSONObject(0).getString("selesai_sakit_tanggal");
                                catatan=dataArray.getJSONObject(0).getString("catatan");
                                created_at=dataArray.getJSONObject(0).getString("created_at");
                                updated_at=dataArray.getJSONObject(0).getString("updated_at");
                                approve_head=dataArray.getJSONObject(0).getString("approve_head");
                                approve_hrd=dataArray.getJSONObject(0).getString("approve_hrd");
                                lampiran_file=dataArray.getJSONObject(0).getString("lampiran_file");
                                head_kyano=dataArray.getJSONObject(0).getString("head_kyano");
                                hrd_kyano=dataArray.getJSONObject(0).getString("hrd_kyano");
                                head_approve_date=dataArray.getJSONObject(0).getString("head_approve_date");
                                hrd_approve_date=dataArray.getJSONObject(0).getString("hrd_approve_date");
                                head_name=dataArray.getJSONObject(0).getString("head_name");

                                txNama_title.setText(name);
                                dateSource = dateFormatSources.parse(created_at);
                                tx_tanggal_title.setText(dateFormat_day.format(dateSource));
                                tx_bulan_tahun_title.setText(dateFormat_month_year.format(dateSource));
                                tx_nama.setText(name);
                                tx_indikasi_sakit.setText(indikasi_sakit);
                                tx_catatan.setText(catatan);
                                tx_tgl_pengajuan.setText(created_at);

                                if(approve_head.equals("1")&&approve_hrd.equals("0")){
                                    tx_status.setText("Ditolak");
                                    tx_tgl_status.setText(hrd_approve_date);
                                    lin_approve_btn.setVisibility(View.GONE);
                                    card_status_approve.setVisibility(View.VISIBLE);
                                    lin_status_approve.setBackgroundResource(R.color.transparentOranye);
                                    imStatus.setImageResource(R.drawable.ic_dot_oranye);
                                }
                                else if(approve_head.equals("0")){
                                    tx_status.setText("Ditolak");
                                    tx_tgl_status.setText(head_approve_date);
                                    lin_approve_btn.setVisibility(View.GONE);
                                    card_status_approve.setVisibility(View.VISIBLE);
                                    lin_status_approve.setBackgroundResource(R.color.transparentRed);
                                    imStatus.setImageResource(R.drawable.ic_dot_red);
                                }else if(approve_head.equals("1")){
                                    tx_status.setText("Diterima");
                                    tx_tgl_status.setText(head_approve_date);
                                    lin_approve_btn.setVisibility(View.GONE);
                                    card_status_approve.setVisibility(View.VISIBLE);
                                    lin_status_approve.setBackgroundResource(R.color.transparentGreen);
                                    imStatus.setImageResource(R.drawable.ic_dot_sukses);
                                }else{
                                    imStatus.setImageResource(R.drawable.ic_dot_point_abu_abu);
                                    card_status_approve.setVisibility(View.GONE);
                                    lin_approve_btn.setVisibility(View.VISIBLE);
                                }
                                tx_link_lihat_dokumen.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialogFoto();
                                    }
                                });
                                btn_setuju.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        notifDialog("Apakah Anda yakin menyetujui izin",name,"1");

                                    }
                                });

                                btn_tolak.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        notifDialog("Apakah Anda yakin menolak izin",name,"0");
                                    }
                                });



                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("JSON_RIWYAT_IZIN_SAKIT", "onResponse: " + e);
                        } catch (ParseException e) {
                           e.printStackTrace();
                       }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("ERROR_DET_SAKIT_APRVE", "onError: " + anError.getErrorDetail());
                    }
                });


    }
    private void notifDialog(String pesan,String nama,String _value) {
        dialogApprove = new Dialog(detailIzinSakitApproveHead.this);
        dialogApprove.setContentView(R.layout.dialog_persetujuan_izin);
        tx_nama_dialog=dialogApprove.findViewById(R.id.tx_nama_dialog);
        btn_close_dialog=dialogApprove.findViewById(R.id.btn_close);
        btn_setuju_dialog=dialogApprove.findViewById(R.id.btn_setuju);
        tx_jenis_izin_dialog=dialogApprove.findViewById(R.id.tx_jenis_izin);
        tx_info_dialog=dialogApprove.findViewById(R.id.tx_info);
        tx_nama_dialog.setText(nama);
        tx_jenis_izin_dialog.setText("Izin Sakit");
        
        dialogApprove.setCancelable(true);
        dialogApprove.setTitle("Update data diri");
        tx_info_dialog.setText(pesan);
        btn_close_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogApprove.dismiss();
            }
        });

        btn_setuju_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateApprove(id,_value);
                dialogApprove.dismiss();
            }
        });
        dialogApprove.show();
    }

    private void dialogFoto()  {
        dialogFoto = new Dialog(detailIzinSakitApproveHead.this);
        dialogFoto.setContentView(R.layout.dialog_foto_izin_sakit);
        dialogFoto.setCancelable(true);

        dialogFoto.setCanceledOnTouchOutside(false);
        img_izin_sakit = (ImageView) dialogFoto.findViewById(R.id.img_izin_sakit);
        txtClose = (TextView) dialogFoto.findViewById(R.id.txtClose);
        RequestOptions requestOptions = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true);
        Glide.with(detailIzinSakitApproveHead.this).load(api.URL_foto_izinsakit+""+lampiran_file).thumbnail(Glide.with(detailIzinSakitApproveHead.this).load(R.drawable.loading)).apply(requestOptions).into(img_izin_sakit);

        txtClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogFoto.dismiss();
            }
        });
        dialogFoto.show();
    }

    private void updateApprove(String _id,String value){
        AndroidNetworking.put(api.URL_IzinSakit_approve_head)
                .addHeaders("Authorization", "Bearer "+token)
                .addBodyParameter("id",_id)
                .addBodyParameter("status",value)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            status=response.getString("status");
                            if(status.equals("200")){
                                Log.d("APPROVE_SUKSES", "onResponse: " + response.toString());
                                finish();
                            }else{
                                helper.messageToast(detailIzinSakitApproveHead.this,"izin gagal approve..!!");
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("JSON_RIWYAT_IZIN_SAKIT", "onResponse: " + e);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("ERROR_DET_SAKIT_APRVE", "onError: " + anError.getErrorDetail());
                    }
                });

    }
}