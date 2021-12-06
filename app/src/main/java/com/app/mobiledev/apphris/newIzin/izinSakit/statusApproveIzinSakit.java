package com.app.mobiledev.apphris.newIzin.izinSakit;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.app.mobiledev.apphris.R;
import com.app.mobiledev.apphris.api.api;
import com.app.mobiledev.apphris.login;
import com.app.mobiledev.apphris.main_fragment;
import com.app.mobiledev.apphris.sesion.SessionManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class statusApproveIzinSakit extends AppCompatActivity {

    private List<modelapproveIzinSakit> modelapproveIzinSakits;
    private TextView text_keterangan;
    private ImageView img_back;
    private Bundle bundle;
    private String idDetailIzin="";
    private String kodeStatus="";
    private String status_approve="";
    private SessionManager msession;
    private String token;
    private TextView text_status;
    private TextView text_alasan;
    private TextView text_status_hrd;
    private TextView text_status_head;
    private TextView tx_nama;
    private TextView tx_indikasi_sakit;
    private TextView tx_catatan;
    private TextView tx_selengkapnya;
    private TextView text_head;
    private TextView text_hrd;
    private LinearLayout lin_hrd;
    private LinearLayout lin_head;
    private LinearLayout lin_result;
    private ImageView dot_hrd;
    private ImageView dot_head;
    private ImageView dot_result;
    private View view_hrd;
    private View view_head;
    private View view_result;
    private ImageView img_status;
    //dialog
    private Dialog dialogFoto;
    private ImageView img_izin_sakit;
    private TextView txtClose;

    private String nama="";
    private String lampiran_file;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_approve);
        modelapproveIzinSakits = new ArrayList<>();
        text_keterangan=findViewById(R.id.text_keterangan);
        text_status=findViewById(R.id.text_status);
        text_alasan=findViewById(R.id.text_alasan);
        text_status_hrd=findViewById(R.id.text_status_hrd);
        text_status_head=findViewById(R.id.text_status_head);
        text_head=findViewById(R.id.text_head);
        text_hrd=findViewById(R.id.text_hrd);
        lin_hrd=findViewById(R.id.lin_hrd);
        lin_head=findViewById(R.id.lin_head);
        dot_hrd=findViewById(R.id.dot_hrd);
        dot_head=findViewById(R.id.dot_head);
        view_head=findViewById(R.id.view_head);
        view_hrd=findViewById(R.id.view_hrd);
        img_status=findViewById(R.id.img_status);
        lin_result=findViewById(R.id.lin_result);
        view_result=findViewById(R.id.view_result);
        dot_result=findViewById(R.id.dot_result);





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
        kodeStatus=bundle.getString("kode_status");
        status_approve=bundle.getString("status_approve");
        text_head.setText("Head/Supervisor");
        text_hrd.setText("Human Resource Development");
        nama=msession.getNamaLEngkap();
        tx_nama.setText(nama);
        getRiwayatStatusApprove(idDetailIzin);


        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        tx_selengkapnya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //URL_foto_izinsakit

                dialogFotoIzinSakit();
            }
        });
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
                                    if(kodeStatus.equals("")){
                                        text_status.setText("Diproses");
                                        text_keterangan.setText("Dalam Proses");
                                        img_status.setBackgroundResource(R.drawable.onprogress);
                                        lin_result.setBackgroundResource(R.drawable.ic_boxtext_gray);
                                        dot_result.setBackgroundResource(R.drawable.ic_dot_point_abu_abu);
                                        view_result.setBackgroundResource(R.color.abu_abu);
                                        text_status.setTextColor(Color.BLACK);
                                        text_alasan.setTextColor(Color.BLACK);
                                    }
                                    else if(kodeStatus.equals("0")){
                                        text_status.setText("Ditolak");
                                        text_alasan.setText(""+status_approve);
                                        text_keterangan.setText("Ditolak");
                                        img_status.setBackgroundResource(R.drawable.rejected);
                                        lin_result.setBackgroundResource(R.drawable.ic_boxtext_result_red);
                                        dot_result.setBackgroundResource(R.drawable.ic_dot_red);
                                        view_result.setBackgroundResource(R.color.red_btn_bg_pressed_color);
                                    }else{
                                        text_status.setText("Diterima");
                                        text_alasan.setText(""+status_approve);
                                        text_keterangan.setText("Diterima");
                                        img_status.setBackgroundResource(R.drawable.success);
                                        lin_result.setBackgroundResource(R.drawable.ic_boxtext_result_green);
                                        dot_result.setBackgroundResource(R.drawable.ic_dot_sukses);
                                        view_result.setBackgroundResource(R.color.greennew);
                                    }

                                    data.getString("id");
                                    data.getString("kyano");
                                    data.getString("indikasi_sakit");
                                    data.getString("catatan");
                                    data.getString("mulai_sakit_tanggal");
                                    data.getString("selesai_sakit_tanggal");
                                    data.getString("created_at");
                                    data.getString("updated_at");
                                    if(data.getString("approve_head").equals("null")){
                                        text_status_head.setText("----");
                                        lin_head.setBackgroundResource(R.drawable.ic_boxtext_gray);
                                        dot_head.setBackgroundResource(R.drawable.ic_dot_point_abu_abu);
                                        view_head.setBackgroundResource(R.color.abu_abu);


                                    }else if(data.getString("approve_head").equals("0")){
                                        lin_head.setBackgroundResource(R.drawable.ic_boxtext_red);
                                        dot_head.setBackgroundResource(R.drawable.ic_dot_red);
                                        view_head.setBackgroundResource(R.color.red_btn_bg_pressed_color);
                                        text_status_head.setText(data.getString("approve_head"));
                                        lin_result.setBackgroundResource(R.drawable.ic_boxtext_result_red);
                                    }else{
                                        lin_head.setBackgroundResource(R.drawable.ic_boxtext_green);
                                        dot_head.setBackgroundResource(R.drawable.ic_dot_sukses);
                                        view_head.setBackgroundResource(R.color.greennew);
                                        text_status_head.setText(data.getString("head_name"));


                                    }
                                    if(data.getString("approve_hrd").equals("null")){
                                        text_status_hrd.setText("----");
                                        lin_hrd.setBackgroundResource(R.drawable.ic_boxtext_gray);
                                        dot_hrd.setBackgroundResource(R.drawable.ic_dot_point_abu_abu);
                                        view_hrd.setBackgroundResource(R.color.abu_abu);


                                    }else if(data.getString("approve_hrd").equals("0")){
                                        lin_hrd.setBackgroundResource(R.drawable.ic_boxtext_red);
                                        dot_hrd.setBackgroundResource(R.drawable.ic_dot_red);
                                        view_hrd.setBackgroundResource(R.color.red_btn_bg_pressed_color);
                                        text_status_hrd.setText(data.getString("approve_hrd"));


                                    }else{
                                        lin_hrd.setBackgroundResource(R.drawable.ic_boxtext_green);
                                        dot_hrd.setBackgroundResource(R.drawable.ic_dot_sukses);
                                        view_hrd.setBackgroundResource(R.color.greennew);
                                        text_status_hrd.setText(data.getString("approve_hrd"));




                                    }


                                    data.getString("approve_hrd");
                                    lampiran_file=data.getString("lampiran_file");
                                    data.getString("head_kyano");
                                    data.getString("hrd_kyano");
                                    data.getString("hrd_approve_date");
                                    data.getString("hrd_approve_date");


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

    private void dialogFotoIzinSakit()  {
        dialogFoto = new Dialog(statusApproveIzinSakit.this);
        dialogFoto.setContentView(R.layout.dialog_foto_izin_sakit);
        dialogFoto.setCancelable(true);

        dialogFoto.setCanceledOnTouchOutside(false);
        img_izin_sakit = (ImageView) dialogFoto.findViewById(R.id.img_izin_sakit);
        txtClose = (TextView) dialogFoto.findViewById(R.id.txtClose);
        RequestOptions requestOptions = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true);
        Glide.with(statusApproveIzinSakit.this).load(api.URL_foto_izinsakit+""+lampiran_file).thumbnail(Glide.with(statusApproveIzinSakit.this).load(R.drawable.loading)).apply(requestOptions).into(img_izin_sakit);

        txtClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogFoto.dismiss();
            }
        });
        dialogFoto.show();
    }


}
