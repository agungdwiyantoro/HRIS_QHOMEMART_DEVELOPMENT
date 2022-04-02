package com.app.mobiledev.apphris.approve.approveSakitNew;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.ColorRes;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.github.chrisbanes.photoview.PhotoView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DetailIzinSakitApprove extends AppCompatActivity {

    private String id, access, name, indikasi_sakit, kyano,
            mulai_sakit_tanggal, selesai_sakit_tanggal,
            lampiran_file, status, token, comment,
            catatan, created_at, updated_at, select_date;
    private String
            head_kyano, head_name, approve_head, head_approve_date,
            exec_kyano, exec_name, approve_exec, exec_approve_date,
            dir_kyano, dir_name, approve_dir, dir_approve_date,
            hrd_kyano, hrd_name, approve_hrd, hrd_approve_date;
    private TextView
            tx_nama, tx_indikasi_sakit, tx_catatan, tx_tgl_pengajuan, tx_link_lihat_dokumen,
            tx_status, tx_tgl_status, tx_waktu_sakit, tx_nama_dialog, tvCancelDialog,
            tvSubmitDialog, tx_info_dialog, tx_jenis_izin_dialog, tx_alasan, tvTitleDetailApp,
            tvCheckHead, tvCheckExec, tvCheckDir, tvCheckHRD;
    private ImageView ivImageName, img_back, icCheckHead, icCheckExec, icCheckDir, icCheckHRD;
    private LinearLayout lin_approve_btn, lin_status_approve, llAlasan, llRiwayatPengajuan,
            llCheckHead, llCheckExec, llCheckDir, llCheckHRD;
    private SessionManager msession;
    private Dialog dialogApprove;
    private Button btn_setuju, btn_tolak;
    private SimpleDateFormat dateFormat_day, dateFormat_month_year, dateFormat_standart, dateFormatSources;
    private Date dateSource, dateSource1;
    private CardView card_status_approve, cvSubmitDialog, cvCancelDialog;
    private Bundle bundle;
    private Dialog dialogFoto;
    private PhotoView img_izin_sakit;
    private DownloadManager downloadManager;
    private FloatingActionButton fabDownloadIzin;
    private TextInputLayout tilAlasan;
    private EditText etAlasan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_izin_sakit_approve);

        msession = new SessionManager(this);
        token = msession.getToken();

        bundle = getIntent().getExtras();
        id = bundle.getString("id");
        access = bundle.getString("access");

        tvTitleDetailApp = findViewById(R.id.tvTitleDetailApp);
        tvTitleDetailApp.setText("Detail Persetujuan");

        tx_waktu_sakit = findViewById(R.id.tx_waktu_sakit);
        tx_nama = findViewById(R.id.tx_nama);
        tx_indikasi_sakit = findViewById(R.id.tx_indikasi_sakit);
        tx_catatan = findViewById(R.id.tx_catatan);
        tx_tgl_pengajuan = findViewById(R.id.tx_tgl_pengajuan);
        tx_link_lihat_dokumen = findViewById(R.id.tx_link_lihat_dokumen);
        tx_status = findViewById(R.id.tx_status);
        tx_tgl_status = findViewById(R.id.tx_tgl_status);
        tx_alasan = findViewById(R.id.tx_alasan);

        card_status_approve = findViewById(R.id.card_status_approve);

        img_back = findViewById(R.id.img_back);

        dateFormatSources = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat_day = new SimpleDateFormat("dd");
        dateFormat_month_year = new SimpleDateFormat("MMM-yyyy");
        dateFormat_standart = new SimpleDateFormat("dd-MMM-yyyy");

        card_status_approve.setVisibility(View.GONE);
        downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);

        llAlasan = findViewById(R.id.llAlasan);
        lin_approve_btn = findViewById(R.id.lin_approve_btn);
        lin_status_approve = findViewById(R.id.lin_status_approve);
        llRiwayatPengajuan = findViewById(R.id.llRiwayatPengajuan);

        btn_tolak = findViewById(R.id.btn_tolak);
        btn_setuju = findViewById(R.id.btn_setuju);

        /*
        * Initialize Riwayat Pengajuan START
        * */

        llCheckHead = findViewById(R.id.llCheckHead);
        icCheckHead = findViewById(R.id.icCheckHead);
        tvCheckHead = findViewById(R.id.tvCheckHead);

        llCheckExec = findViewById(R.id.llCheckExec);
        icCheckExec = findViewById(R.id.icCheckExec);
        tvCheckExec = findViewById(R.id.tvCheckExec);

        llCheckDir = findViewById(R.id.llCheckDir);
        icCheckDir = findViewById(R.id.icCheckDir);
        tvCheckDir = findViewById(R.id.tvCheckDir);

        llCheckHRD = findViewById(R.id.llCheckHRD);
        icCheckHRD = findViewById(R.id.icCheckHRD);
        tvCheckHRD = findViewById(R.id.tvCheckHRD);

        /*
         * Initialize Riwayat Pengajuan END
         * */

        getDetailSakitApprove(id);
        Log.d("CEK_URL_APPROVE", "onCreate: " + api.URL_IzinSakit_approve + "?id=" + id);
        Log.d("CEK_BUNDLE_STRING", "onCreate: " + id + access + token);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getDetailSakitApprove(String _id) {
        //http://192.168.50.24/all/hris_ci_3/api/approvesakit?id=7
        AndroidNetworking.get(api.URL_IzinSakit_approve + "?id=" + _id)
                /*AndroidNetworking.get("http://192.168.50.24/all/hris_ci_3/api/approvesakit?id=7")*/
                .addHeaders("Authorization", "Bearer " + token)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @SuppressLint({"ResourceAsColor", "SetTextI18n"})
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            String _status = response.getString("status");
                            if (_status.equals("200")) {
                                Log.d("RESULT_DET_PENG_SAKIT", "onResponse: " + response.toString());
                                JSONArray dataArray = response.getJSONArray("message");

                                Log.d("RESULT_DET_PENG_SAKIT0", "onResponse: " + dataArray.getJSONObject(0).getString("name"));

                                /*
                                 * GET DATA TO STRING
                                 */

                                name = dataArray.getJSONObject(0).getString("name");
                                id = dataArray.getJSONObject(0).getString("id");
                                kyano = dataArray.getJSONObject(0).getString("kyano");
                                indikasi_sakit = dataArray.getJSONObject(0).getString("indikasi_sakit");
                                mulai_sakit_tanggal = dataArray.getJSONObject(0).getString("mulai_sakit_tanggal");
                                selesai_sakit_tanggal = dataArray.getJSONObject(0).getString("selesai_sakit_tanggal");
                                select_date = dataArray.getJSONObject(0).getString("select_date");

                                catatan = dataArray.getJSONObject(0).getString("catatan");
                                created_at = dataArray.getJSONObject(0).getString("created_at");
                                updated_at = dataArray.getJSONObject(0).getString("updated_at");
                                status = dataArray.getJSONObject(0).getString("status");
                                comment = dataArray.getJSONObject(0).getString("comment");

                                head_kyano = dataArray.getJSONObject(0).getString("head_kyano");
                                head_name = dataArray.getJSONObject(0).getString("head_name");
                                approve_head = dataArray.getJSONObject(0).getString("approve_head");
                                head_approve_date = dataArray.getJSONObject(0).getString("head_approve_date");

                                exec_kyano = dataArray.getJSONObject(0).getString("executiv_kyano");
                                exec_name = dataArray.getJSONObject(0).getString("executiv");
                                approve_exec = dataArray.getJSONObject(0).getString("approve_executiv");
                                exec_approve_date = dataArray.getJSONObject(0).getString("executiv_approve_date");

                                dir_kyano = dataArray.getJSONObject(0).getString("directur_kyano");
                                //dir_name = dataArray.getJSONObject(0).getString("dir");
                                approve_dir = dataArray.getJSONObject(0).getString("approve_directur");
                                dir_approve_date = dataArray.getJSONObject(0).getString("directur_approve_date");

                                hrd_kyano = dataArray.getJSONObject(0).getString("hrd_kyano");
                                hrd_name = dataArray.getJSONObject(0).getString("hrd_name");
                                approve_hrd = dataArray.getJSONObject(0).getString("approve_hrd");
                                hrd_approve_date = dataArray.getJSONObject(0).getString("hrd_approve_date");

                                lampiran_file = dataArray.getJSONObject(0).getString("lampiran_file");


                                switch (status) {
                                    case "TOLAK":
                                        Log.d("TAG_GET_STATUS", "onResponse: "+status);
                                        if (approve_head.equals("0")) {
                                            tx_status.setText("Ditolak oleh " + head_name);
                                            tx_tgl_status.setText(head_approve_date);
                                            lin_approve_btn.setVisibility(View.GONE);
                                            card_status_approve.setVisibility(View.VISIBLE);
                                            lin_status_approve.setBackgroundResource(R.color.transparentRed);

                                            icCheckHead.setImageResource(R.drawable.ic_baseline_highlight_off_24_end);
                                            icCheckExec.setVisibility(View.GONE);
                                            icCheckDir.setVisibility(View.GONE);
                                            icCheckHRD.setVisibility(View.GONE);

                                            tvCheckHead.setText("Ditolak " + "Head/SPV");
                                            tvCheckExec.setVisibility(View.GONE);
                                            tvCheckDir.setVisibility(View.GONE);
                                            tvCheckHRD.setVisibility(View.GONE);

                                            llAlasan.setVisibility(View.VISIBLE);
                                            tx_alasan.setText(comment);
                                            llRiwayatPengajuan.setVisibility(View.VISIBLE);

                                        } else if (approve_head.equals("1") && approve_exec.equals("0")) {
                                            tx_status.setText("Ditolak oleh " + exec_name);
                                            tx_tgl_status.setText(exec_approve_date);
                                            lin_approve_btn.setVisibility(View.GONE);
                                            card_status_approve.setVisibility(View.VISIBLE);
                                            lin_status_approve.setBackgroundResource(R.color.transparentRed);

                                            icCheckHead.setImageResource(R.drawable.ic_baseline_check_box_24_green);
                                            icCheckExec.setImageResource(R.drawable.ic_baseline_highlight_off_24_end);
                                            icCheckDir.setVisibility(View.GONE);
                                            icCheckHRD.setVisibility(View.GONE);

                                            tvCheckHead.setText("Disetujui " + "Head/SPV");
                                            tvCheckExec.setText("Ditolak " + "Eksekutif");
                                            tvCheckDir.setVisibility(View.GONE);
                                            tvCheckHRD.setVisibility(View.GONE);

                                            llAlasan.setVisibility(View.VISIBLE);
                                            tx_alasan.setText(comment);
                                            llRiwayatPengajuan.setVisibility(View.VISIBLE);

                                        } else if (approve_head.equals("1") && approve_exec.equals("1") && approve_dir.equals("0")) {
                                            tx_status.setText("Ditolak oleh " + dir_name);
                                            tx_tgl_status.setText(dir_approve_date);
                                            lin_approve_btn.setVisibility(View.GONE);
                                            card_status_approve.setVisibility(View.VISIBLE);
                                            lin_status_approve.setBackgroundResource(R.color.transparentRed);

                                            icCheckHead.setImageResource(R.drawable.ic_baseline_check_box_24_green);
                                            icCheckExec.setImageResource(R.drawable.ic_baseline_check_box_24_green);
                                            icCheckDir.setImageResource(R.drawable.ic_baseline_highlight_off_24_end);
                                            icCheckHRD.setVisibility(View.GONE);

                                            tvCheckHead.setText("Disetujui " + "Head/SPV");
                                            tvCheckExec.setText("Disetujui " + "Eksekutif");
                                            tvCheckDir.setText("Ditolak " + "Direktur");
                                            tvCheckHRD.setVisibility(View.GONE);

                                            llAlasan.setVisibility(View.VISIBLE);
                                            tx_alasan.setText(comment);
                                            llRiwayatPengajuan.setVisibility(View.VISIBLE);

                                        } else if (approve_head.equals("1") && approve_exec.equals("1") && approve_dir.equals("1") && approve_hrd.equals("0")) {
                                            tx_status.setText("Ditolak oleh " + hrd_name);
                                            tx_tgl_status.setText(hrd_approve_date);
                                            lin_approve_btn.setVisibility(View.GONE);
                                            card_status_approve.setVisibility(View.VISIBLE);
                                            lin_status_approve.setBackgroundResource(R.color.transparentRed);

                                            icCheckHead.setImageResource(R.drawable.ic_baseline_check_box_24_green);
                                            icCheckExec.setImageResource(R.drawable.ic_baseline_check_box_24_green);
                                            icCheckDir.setImageResource(R.drawable.ic_baseline_check_box_24_green);
                                            icCheckHRD.setImageResource(R.drawable.ic_baseline_highlight_off_24_end);

                                            tvCheckHead.setText("Disetujui " + "Head/SPV");
                                            tvCheckExec.setText("Disetujui " + "Eksekutif");
                                            tvCheckDir.setText("Disetujui " + "Direktur");
                                            tvCheckHRD.setText("Ditolak " + "HRD");

                                            llAlasan.setVisibility(View.VISIBLE);
                                            tx_alasan.setText(comment);
                                            llRiwayatPengajuan.setVisibility(View.VISIBLE);

                                        } else {

                                            card_status_approve.setVisibility(View.GONE);
                                            lin_approve_btn.setVisibility(View.VISIBLE);
                                            llRiwayatPengajuan.setVisibility(View.GONE);
                                        }

                                        break;
                                    case "SELESAI":
                                        Log.d("TAG_GET_STATUS", "onResponse: "+status);

                                        tx_status.setText("Disetujui oleh " + hrd_name);
                                        tx_tgl_status.setText(hrd_approve_date);
                                        lin_approve_btn.setVisibility(View.GONE);
                                        card_status_approve.setVisibility(View.VISIBLE);
                                        lin_status_approve.setBackgroundResource(R.color.greennew);

                                        icCheckHead.setImageResource(R.drawable.ic_baseline_check_box_24_green);
                                        icCheckExec.setImageResource(R.drawable.ic_baseline_check_box_24_green);
                                        icCheckDir.setImageResource(R.drawable.ic_baseline_check_box_24_green);
                                        icCheckHRD.setImageResource(R.drawable.ic_baseline_check_box_24_green);

                                        tvCheckHead.setText("Disetujui " + "Head/SPV");
                                        tvCheckExec.setText("Disetujui " + "Eksekutif");
                                        tvCheckDir.setText("Disetujui " + "Direktur");
                                        tvCheckHRD.setText("Disetujui " + "HRD");

                                        llRiwayatPengajuan.setVisibility(View.VISIBLE);
                                        break;
                                    case "ON PROGRESS":
                                        card_status_approve.setVisibility(View.GONE);
                                        llRiwayatPengajuan.setVisibility(View.VISIBLE);
                                        lin_status_approve.setBackgroundResource(R.color.blue);

                                        if (access.equals("HEAD") && (approve_head.equals("null"))) {
                                            Log.d("TAG_ACC_BTN", "onResponse: "+access);
                                            lin_approve_btn.setVisibility(View.VISIBLE);
                                        } else if (access.equals("EXECUTIV") && (approve_exec.equals("null"))) {
                                            Log.d("TAG_ACC_BTN", "onResponse: "+access);
                                            if (approve_head.equals("null")) {
                                                lin_approve_btn.setVisibility(View.GONE);
                                            } else {
                                                lin_approve_btn.setVisibility(View.VISIBLE);
                                            }
                                        } else if (access.equals("DIRECTUR") && (approve_dir.equals("null"))) {
                                            Log.d("TAG_ACC_BTN", "onResponse: "+access);
                                            if (approve_exec.equals("null") || approve_head.equals("null")) {
                                                lin_approve_btn.setVisibility(View.GONE);
                                            } else {
                                                lin_approve_btn.setVisibility(View.VISIBLE);
                                            }
                                        } else if (access.equals("HRD") && (approve_hrd.equals("null"))) {
                                            Log.d("TAG_ACC_BTN", "onResponse: "+access);
                                            if (approve_dir.equals("null") || approve_exec.equals("null") || approve_head.equals("null")) {
                                                lin_approve_btn.setVisibility(View.GONE);
                                            } else {
                                                lin_approve_btn.setVisibility(View.VISIBLE);
                                            }

                                        }

                                        Log.d("TAG_GET_STATUS", "onResponse: "+status);

                                        boolean staff = true;

                                        if (approve_head.equals("1") && approve_dir.equals("null")) {
                                            staff = false;
                                        } else {
                                            staff = true;
                                        }

                                        if (approve_head.equals("1") && approve_exec.equals("null") && approve_dir.equals("null") && approve_hrd.equals("null")) {
                                            icCheckHead.setImageResource(R.drawable.ic_baseline_check_box_24_blue);
                                            icCheckDir.setVisibility(View.GONE);
                                            icCheckHRD.setVisibility(View.GONE);

                                            tvCheckHead.setText("Disetujui " + "Head/SPV");

                                            tvCheckDir.setVisibility(View.GONE);
                                            tvCheckHRD.setVisibility(View.GONE);

                                            card_status_approve.setVisibility(View.VISIBLE);
                                            lin_status_approve.setBackgroundResource(R.color.greennew);
                                            tx_status.setText("Disetujui oleh " + head_name);
                                            tx_tgl_status.setText(head_approve_date);

                                            Log.d("TAG1", "onResponse: ");

                                        } else if (approve_head.equals("1") && approve_exec.equals("null") && approve_dir.equals("1") && approve_hrd.equals("null")) {
                                            icCheckHead.setImageResource(R.drawable.ic_baseline_check_box_24_blue);
                                            icCheckDir.setVisibility(View.GONE);
                                            icCheckHRD.setVisibility(View.GONE);

                                            tvCheckHead.setText("Disetujui " + "Head/SPV");

                                            tvCheckDir.setVisibility(View.GONE);
                                            tvCheckHRD.setVisibility(View.GONE);

                                            card_status_approve.setVisibility(View.VISIBLE);
                                            lin_status_approve.setBackgroundResource(R.color.greennew);
                                            tx_status.setText("Disetujui oleh " + head_name);
                                            tx_tgl_status.setText(head_approve_date);

                                            Log.d("TAG2", "onResponse: ");

                                        } else if (approve_head.equals("1") && approve_exec.equals("1") && approve_dir.equals("null") && approve_hrd.equals("null")) {
                                            icCheckHead.setImageResource(R.drawable.ic_baseline_check_box_24_green);
                                            icCheckExec.setImageResource(R.drawable.ic_baseline_check_box_24_blue);
                                            icCheckDir.setVisibility(View.GONE);
                                            icCheckHRD.setVisibility(View.GONE);

                                            tvCheckHead.setText("Disetujui " + "Head/SPV");
                                            tvCheckExec.setText("Disetujui " + "Eksekutif");

                                            tvCheckDir.setVisibility(View.GONE);
                                            tvCheckHRD.setVisibility(View.GONE);

                                            card_status_approve.setVisibility(View.VISIBLE);
                                            lin_status_approve.setBackgroundResource(R.color.greennew);
                                            tx_status.setText("Disetujui oleh " + exec_name);
                                            tx_tgl_status.setText(exec_approve_date);

                                            Log.d("TAG3", "onResponse: ");

                                        } else if (approve_head.equals("1") && approve_exec.equals("1") && approve_dir.equals("1") && approve_hrd.equals("null")) {
                                            icCheckHead.setImageResource(R.drawable.ic_baseline_check_box_24_green);
                                            icCheckExec.setImageResource(R.drawable.ic_baseline_check_box_24_green);
                                            icCheckDir.setImageResource(R.drawable.ic_baseline_check_box_24_blue);

                                            tvCheckHead.setText("Disetujui " + "Head/SPV");
                                            tvCheckExec.setText("Disetujui " + "Eksekutif");
                                            tvCheckDir.setText("Disetujui " + "Direktur");

                                            if (approve_dir.equals("1")) {
                                                dir_name = "Henky";
                                                tx_status.setText("Disetujui oleh " + dir_name);
                                            }

                                            card_status_approve.setVisibility(View.VISIBLE);
                                            lin_status_approve.setBackgroundResource(R.color.greennew);
                                            tx_status.setText("Disetujui oleh " + dir_name);
                                            tx_tgl_status.setText(dir_approve_date);

                                            Log.d("TAG4", "onResponse: ");

                                        } else if (approve_head.equals("1") && approve_exec.equals("1") && approve_dir.equals("1") && approve_hrd.equals("1")) {
                                            icCheckHead.setImageResource(R.drawable.ic_baseline_check_box_24_green);
                                            icCheckExec.setImageResource(R.drawable.ic_baseline_check_box_24_green);
                                            icCheckDir.setImageResource(R.drawable.ic_baseline_check_box_24_green);
                                            icCheckHRD.setImageResource(R.drawable.ic_baseline_check_box_24_blue);

                                            tvCheckHead.setText("Disetujui " + "Head/SPV");
                                            tvCheckExec.setText("Disetujui " + "Eksekutif");
                                            tvCheckDir.setText("Disetujui " + "Direktur");
                                            tvCheckHRD.setText("Disetujui " + "HRD");

                                            card_status_approve.setVisibility(View.VISIBLE);
                                            lin_status_approve.setBackgroundResource(R.color.greennew);
                                            tx_status.setText("Disetujui oleh " + hrd_name);
                                            tx_tgl_status.setText(hrd_approve_date);

                                        }
                                        break;
                                }

                                /*
                                 * GET DATA TO STRING END
                                 */

                                /*
                                 * SET DATA TO TEXTVIEW START
                                 */

                                dateSource = dateFormatSources.parse(created_at);
                                dateSource = dateFormatSources.parse(mulai_sakit_tanggal);
                                dateSource1 = dateFormatSources.parse(selesai_sakit_tanggal);

                                tx_nama.setText(name);
                                tx_indikasi_sakit.setText(indikasi_sakit);
                                tx_catatan.setText(catatan);
                                tx_tgl_pengajuan.setText(created_at);
                                tx_alasan.setText(comment);

                                mulai_sakit_tanggal = dateFormat_standart.format(dateSource);
                                selesai_sakit_tanggal = dateFormat_standart.format(dateSource1);
                                tx_waktu_sakit.setText(mulai_sakit_tanggal + "   " + selesai_sakit_tanggal);

                                /*
                                 * SET DATA TO TEXTVIEW END
                                 */

                                /*if (approve_head.equals("1") || approve_exec.equals("0") && approve_hrd.equals("0")) {
                                    tx_status.setText("Ditolak oleh ");
                                    tx_tgl_status.setText(hrd_approve_date);
                                    lin_approve_btn.setVisibility(View.GONE);
                                    card_status_approve.setVisibility(View.VISIBLE);
                                    lin_status_approve.setBackgroundResource(R.color.transparentOranye);

                                } else */
                                /*if (approve_head.equals("0")) {
                                    tx_status.setText("Ditolak oleh ");
                                    tx_tgl_status.setText(head_approve_date);
                                    lin_approve_btn.setVisibility(View.GONE);
                                    card_status_approve.setVisibility(View.VISIBLE);
                                    lin_status_approve.setBackgroundResource(R.color.transparentRed);

                                } else if (approve_head.equals("1")) {
                                    tx_status.setText("Disetujui oleh ");
                                    tx_tgl_status.setText(head_approve_date);
                                    lin_approve_btn.setVisibility(View.GONE);
                                    card_status_approve.setVisibility(View.VISIBLE);
                                    lin_status_approve.setBackgroundResource(R.color.transparentGreen);

                                } else {

                                    card_status_approve.setVisibility(View.GONE);
                                    lin_approve_btn.setVisibility(View.VISIBLE);
                                }*/
                                tx_link_lihat_dokumen.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialogFoto();
                                    }
                                });
                                btn_setuju.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        notifDialog("Apakah Anda yakin menyetujui izin dari ", name, "1");

                                    }
                                });

                                btn_tolak.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        notifDialog("Apakah Anda yakin menolak izin dari ", name, "0");
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

    private void notifDialog(String pesan, String nama, String _value) {
        dialogApprove = new Dialog(DetailIzinSakitApprove.this);
        dialogApprove.setContentView(R.layout.dialog_persetujuan_izin);

        tx_nama_dialog = dialogApprove.findViewById(R.id.tx_nama_dialog);
        cvSubmitDialog = dialogApprove.findViewById(R.id.cvSubmitDialog);
        cvCancelDialog = dialogApprove.findViewById(R.id.cvCancelDialog);
        tx_info_dialog = dialogApprove.findViewById(R.id.tx_info);
        tvSubmitDialog = dialogApprove.findViewById(R.id.tvSubmitDialog);
        tilAlasan = dialogApprove.findViewById(R.id.tilAlasan);
        etAlasan = dialogApprove.findViewById(R.id.etAlasan);
        tx_nama_dialog.setText(nama);

        if (_value.equals("0")) {
            //cvSubmitDialog.setBackgroundColor(getResources().getColor(android.R.color.holo_red_dark));
            cvSubmitDialog.setCardBackgroundColor(getResources().getColor(R.color.quantum_vanillared800));
            //cvSubmitDialog.setBackgroundColor(getResources().getColor(android.R.color.holo_red_dark));
            //cvSubmitDialog.setTint
            tvSubmitDialog.setText("Tolak");
            tilAlasan.setVisibility(View.VISIBLE);

        } else {
            cvSubmitDialog.setCardBackgroundColor(getResources().getColor(R.color.quantum_vanillagreen800
            ));
        }

        dialogApprove.setCancelable(true);
        dialogApprove.setTitle("Konfirmasi");
        tx_info_dialog.setText(pesan);

        dialogApprove.show();

        cvCancelDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogApprove.dismiss();
            }
        });

        cvSubmitDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (_value.equals("0")) {
                    checkInput(_value, etAlasan.getText().toString());
                } else if (_value.equals("1")) {
                    updateApprove(id, _value, access, "");
                }
            }
        });
    }

    private void checkInput(String _value, String _comment) {
        if (etAlasan.getText().toString().isEmpty()) {
            tilAlasan.setError("Alasan menolak masih kosong");
            tilAlasan.requestFocus();
        } else {
            updateApprove(id, _value, access, _comment);
        }
    }

    private void dialogFoto() {
        dialogFoto = new Dialog(DetailIzinSakitApprove.this);
        dialogFoto.setContentView(R.layout.dialog_foto_izin_sakit);
        dialogFoto.setCancelable(true);
        dialogFoto.setCanceledOnTouchOutside(true);
        img_izin_sakit = dialogFoto.findViewById(R.id.img_izin_sakit);
        fabDownloadIzin = dialogFoto.findViewById(R.id.fabDownloadIzin);
        RequestOptions requestOptions = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true);
        Glide.with(DetailIzinSakitApprove.this).load(api.URL_foto_izinsakit + kyano + "/lampiran/surat_sakit/" + lampiran_file).thumbnail(Glide.with(DetailIzinSakitApprove.this).load(R.drawable.loading)).apply(requestOptions).into(img_izin_sakit);
        fabDownloadIzin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadIzin(lampiran_file);
            }
        });
        dialogFoto.show();
    }

    private void updateApprove(String _id, String value, String hak_akses, String comment) {
        AndroidNetworking.put(api.URL_IzinSakit_approve)
                .addHeaders("Authorization", "Bearer " + token)
                .addBodyParameter("id", _id)
                .addBodyParameter("status", value)
                .addBodyParameter("hak_akses", hak_akses)
                .addBodyParameter("comment", comment)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            status = response.getString("status");
                            String responseResult = response.toString();

                            Log.d("TAG_RES_UPDATE", "onResponse: " + responseResult);

                            if (status.equals("200")) {
                                Log.d("APPROVE_SUKSES", "onResponse: " + response.toString());
                                startActivity(getIntent());
                                finish();

                            } else {
                                helper.messageToast(DetailIzinSakitApprove.this, "izin gagal approve..!!");
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

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    private void downloadIzin(String fileName) {
        downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(api.URL_foto_izinsakit + kyano + "/lampiran/surat_sakit/" + fileName);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOCUMENTS, fileName + ".png");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        Long reference = downloadManager.enqueue(request);
        showProgressDownload(reference);
    }

    private void showProgressDownload(Long reference) {
        final ProgressDialog progressBarDialog = new ProgressDialog(this);
        progressBarDialog.setTitle("Mengunduh Memo");
        progressBarDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressBarDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", (dialog, whichButton) -> {
        });
        progressBarDialog.setProgress(0);
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean downloading = true;
                DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                while (downloading) {
                    DownloadManager.Query q = new DownloadManager.Query();
                    q.setFilterById(reference);
                    Cursor cursor = manager.query(q);
                    cursor.moveToFirst();
                    int bytes_downloaded = cursor.getInt(cursor
                            .getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                    int bytes_total = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                    if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {
                        downloading = false;
                    }
                    final int dl_progress = (int) ((bytes_downloaded * 100l) / bytes_total);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBarDialog.setProgress((int) dl_progress);
                        }
                    });
                    cursor.close();
                }

            }
        }).start();
        progressBarDialog.show();
    }

}
