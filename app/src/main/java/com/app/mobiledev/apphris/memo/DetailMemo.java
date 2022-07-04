package com.app.mobiledev.apphris.memo;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.app.mobiledev.apphris.R;
import com.app.mobiledev.apphris.api.api;
import com.app.mobiledev.apphris.helperPackage.helper;
import com.app.mobiledev.apphris.memo.peserta.adapterPesertaMemo;
import com.app.mobiledev.apphris.memo.peserta.list_peserta;
import com.app.mobiledev.apphris.memo.peserta.modelPesertaMemo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DetailMemo extends AppCompatActivity {

    private String jenis_memo, no_memo, kepada, dari, hal, tgl, tanggal, isi, waktu, tempat, key_memo, pdf, video;
    private TextView tv_jenis_memo, tv_no_memo, tx_kepada, tx_dari, tx_hal, tx_tgl, tv_containe, tgl_notulen, waktu_notulen, tempat_notulen, agenda_notulen, tx_peserta, tx_lampiran, tx_lampiran_video;
    private Toolbar mToolbar;
    private LinearLayout lin_internal_memo, lin_notulen;
    private AlertDialog.Builder dialog;
    private LayoutInflater inflater;
    private LinearLayout lin_detail_memo;
    private View dialogView;
    List<modelPesertaMemo> mlistPeserta;

    DownloadManager downloadManager;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_memo);
        tv_jenis_memo = findViewById(R.id.tv_jenis_memo);
        tv_no_memo = findViewById(R.id.tv_no_memo);
        tx_kepada = findViewById(R.id.tx_kepada);
        tx_dari = findViewById(R.id.tx_dari);
        tx_hal = findViewById(R.id.tx_hal);
        tx_tgl = findViewById(R.id.tx_tgl);
        tv_containe = findViewById(R.id.tv_containe);
        lin_detail_memo = findViewById(R.id.lin_detail_memo);
        lin_internal_memo = findViewById(R.id.lin_internal_memo);
        tx_lampiran = findViewById(R.id.tx_lampiran);
        tx_lampiran_video = findViewById(R.id.tx_lampiran_video);

        /*form notulen1234
         * */
        tx_lampiran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pdf.equals("0")) {
                    helper.snackBar(lin_detail_memo, "link lampiran tidak tersedia");
                } else {
                    Intent intent = new Intent(DetailMemo.this, DetailLampiranMemo.class);
                    Bundle x = new Bundle();
                    x.putString("file_hal", hal);
                    x.putString("file_lampiran", pdf);
                    intent.putExtras(x);
                    startActivity(intent);
                }

            }
        });


        lin_notulen = findViewById(R.id.lin_notulen);
        tgl_notulen = findViewById(R.id.tgl_notulen);
        waktu_notulen = findViewById(R.id.waktu_notulen);
        tempat_notulen = findViewById(R.id.tempat_notulen);
        agenda_notulen = findViewById(R.id.agenda_notulen);
        tx_peserta = findViewById(R.id.tx_peserta);
        dialog = new AlertDialog.Builder(DetailMemo.this);
        mlistPeserta = new ArrayList<>();
        getExtra_value();
        tx_lampiran_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (video.equals("0")) {
                    helper.snackBar(lin_detail_memo, "link lampian video tidak tersedia");
                } else {
                    Intent intent = new Intent(DetailMemo.this, DetailLampiranVideo.class);
                    Bundle x = new Bundle();
                    x.putString("file_lampiran_video", video);
                    intent.putExtras(x);
                    startActivity(intent);
                }

            }
        });

        tx_peserta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(DetailMemo.this, list_peserta.class);
                    Bundle x = new Bundle();
                    x.putString("key_memo", key_memo);
                    intent.putExtras(x);
                    startActivity(intent);
                } catch (NullPointerException e) {
                    Log.d("CEK_ERROR", "onClick: " + e);
                    helper.snackBar(lin_detail_memo, "peserta masih kosong");
                }

            }
        });


        // tv_containe.setText(Html.fromHtml(Html.fromHtml(isi).toString()));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tv_containe.setText(Html.fromHtml(isi, Html.FROM_HTML_MODE_LEGACY));

        } else {
            tv_containe.setText(Html.fromHtml(isi));
        }


        mToolbar = findViewById(R.id.toolbar_abs);
        mToolbar.setTitle("Memo");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        AndroidNetworking.initialize(getApplicationContext());

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        //Log.d("TAG_getE", "onCreate: "+judul+tanggal);

    }


    private void getExtra_value() {
        jenis_memo = getIntent().getExtras().getString("jenis_memo");
        pdf = getIntent().getExtras().getString("pdf");
        video = getIntent().getExtras().getString("video");


        if (jenis_memo.equals("Internal Memo")) {
            isi = getIntent().getExtras().getString("isi");
            no_memo = getIntent().getExtras().getString("no_memo");
            kepada = getIntent().getExtras().getString("kepada");
            dari = getIntent().getExtras().getString("dari");
            hal = getIntent().getExtras().getString("hal");
            tgl = getIntent().getExtras().getString("tgl");
            key_memo = getIntent().getExtras().getString("key_memo");

            tv_jenis_memo.setText(jenis_memo);
            tv_no_memo.setText("No. " + no_memo);
            tx_kepada.setText(kepada);
            tx_dari.setText(dari);
            tx_hal.setText(hal);
            tx_tgl.setText(tgl);
            lin_notulen.setVisibility(View.GONE);

        } else {
            isi = getIntent().getExtras().getString("isi");
            no_memo = getIntent().getExtras().getString("no_memo");
            kepada = getIntent().getExtras().getString("kepada");
            dari = getIntent().getExtras().getString("dari");
            hal = getIntent().getExtras().getString("hal");
            tgl = getIntent().getExtras().getString("tgl");
            waktu = getIntent().getExtras().getString("waktu");
            tempat = getIntent().getExtras().getString("tempat");
            key_memo = getIntent().getExtras().getString("key_memo");

            tgl_notulen.setText("" + tgl);
            waktu_notulen.setText("" + waktu);
            tempat_notulen.setText("" + tempat);
            agenda_notulen.setText("" + hal);
            lin_internal_memo.setVisibility(View.GONE);

        }


    }


    public void DialogForm(String noMemo) {
        try {
            inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            ;
            dialogView = inflater.inflate(R.layout.dialog_list_peserta_memo, null);
            RecyclerView rcKaryawan = dialogView.findViewById(R.id.rcKaryawan);
            Log.d("CEK_DETAIL_MEMO", "onResponse: " + noMemo);
            detMemo(noMemo, rcKaryawan);
            dialog.setView(dialogView);
            dialog.setCancelable(true);
            dialog.show();
        } catch (NullPointerException e) {
            Log.d("NULLPointer", "DialogForm: " + e);
        }

    }


    private void detMemo(String no_memo, final RecyclerView rc) {
        AndroidNetworking.post(api.URL_detMemo)
                .addBodyParameter("key", api.key)
                .addBodyParameter("no_memo", no_memo)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            Boolean success = response.getBoolean("success");

                            Log.d("CEK_DETAIL_MEMO", "onResponse: " + success);
                            if (success) {
                                mlistPeserta.clear();
                                JSONArray jsonArray = response.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject data = jsonArray.getJSONObject(i);
                                    modelPesertaMemo model = new modelPesertaMemo();
                                    model.setMmANo(data.getString("MmANo"));
                                    model.setKyano(data.getString("kyano"));
                                    model.setPeserta(data.getString("peserta"));
                                    // Log.d("CEK_DETAIL_MEMO", "onResponse: "+data.getString("peserta"));
                                    mlistPeserta.add(model);
                                }
                                adapterPesertaMemo mAdapter = new adapterPesertaMemo(mlistPeserta, DetailMemo.this);
                                rc.setLayoutManager(new LinearLayoutManager(DetailMemo.this));
                                rc.setItemAnimator(new DefaultItemAnimator());
                                rc.setAdapter(mAdapter);
                                mAdapter.notifyDataSetChanged();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("JSONEEVENTT", "onResponse: " + e);
                            helper.showMsg(DetailMemo.this, "Peringatan", "" + helper.PESAN_SERVER, helper.ERROR_TYPE);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("EROOR_EVENT", "onError: " + anError);
                        helper.showMsg(DetailMemo.this, "Peringatan", "" + helper.PESAN_KONEKSI, helper.ERROR_TYPE);
                    }
                });

    }

}
