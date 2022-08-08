package com.app.mobiledev.apphris.izin.izinOffTerlambat.off;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.app.mobiledev.apphris.Model.modelIzinOff;
import com.app.mobiledev.apphris.R;
import com.app.mobiledev.apphris.api.api;
import com.app.mobiledev.apphris.sesion.SessionManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailIzinOffEmp extends AppCompatActivity {

    private List<modelIzinOff> modelIzinOff;
    private TextView tvName, tvTanggalOff, tvLamaOff, tvAlasan, tvLampiran,text_keterangan, text_status, text_alasan, tx_date, text_status_hrd,
            text_status_head, text_status_exec, text_status_dir, tx_nama, tx_kepentingan,
            tx_catatan, tx_lampiran, text_head, text_exec, text_dir, text_hrd, tx_lamaDns, tx_daerah_tujuan, tx_transportasi;
    private ImageView dot_head, dot_exec, dot_dir, dot_hrd, dot_result, img_status;
    private Bundle bundle;
    private String kyano = "", idDetailIzin = "", kodeStatus = "", status_approve = "",
            token, nama = "", lampiran_file="", jabatan, hak_akses;
    private SessionManager msession;
    private LinearLayout lin_head, lin_exec, lin_dir, lin_hrd, lin_result;
    private RelativeLayout rlExecDns, rlDirDns, rlHrdDns;
    private View view_head, view_exec, view_dir, view_hrd, view_result, inc_backPage;

    //dialog
    private Dialog dialogFoto;
    private PhotoView img_izin_dns;
    private FloatingActionButton fabDownloadIzin;
    private DownloadManager downloadManager;

    private String comment;
    private final String TAG = DetailIzinOffEmp.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_izin_off_emp);
        modelIzinOff = new ArrayList<>();

        tvName = findViewById(R.id.tv_nama);
        tvTanggalOff = findViewById(R.id.tv_tanggal_off);
        tvLamaOff = findViewById(R.id.tv_lama_off);
        tvAlasan = findViewById(R.id.tv_alasan);
        tvLampiran = findViewById(R.id.tv_lampiran);

        text_keterangan = findViewById(R.id.text_keterangan);
        text_status = findViewById(R.id.text_status);
        text_alasan = findViewById(R.id.text_alasan);
        tx_date = findViewById(R.id.tx_date);
        tx_lamaDns = findViewById(R.id.tx_lama);
        tx_daerah_tujuan = findViewById(R.id.tx_daerah_tujuan);
        tx_transportasi = findViewById(R.id.tx_transportasi);

        text_status_hrd = findViewById(R.id.text_status_hrd);
        text_status_head = findViewById(R.id.text_status_head);
        text_status_exec = findViewById(R.id.text_status_exec);
        text_status_dir = findViewById(R.id.text_status_dir);

        text_hrd = findViewById(R.id.text_hrd);
        text_dir = findViewById(R.id.text_dir);
        text_exec = findViewById(R.id.text_exec);
        text_head = findViewById(R.id.text_head);

        lin_hrd = findViewById(R.id.lin_hrd);
        lin_dir = findViewById(R.id.lin_dir);
        lin_exec = findViewById(R.id.lin_exec);
        lin_head = findViewById(R.id.lin_head);

        rlHrdDns = findViewById(R.id.rlHrdDns);
        rlExecDns = findViewById(R.id.rlExecDns);
        rlDirDns = findViewById(R.id.rlDirDns);

        dot_hrd = findViewById(R.id.dot_hrd);
        dot_dir = findViewById(R.id.dot_dir);
        dot_exec = findViewById(R.id.dot_exec);
        dot_head = findViewById(R.id.dot_head);

        view_hrd = findViewById(R.id.view_hrd);
        view_dir = findViewById(R.id.view_dir);
        view_exec = findViewById(R.id.view_exec);
        view_head = findViewById(R.id.view_head);

        img_status = findViewById(R.id.img_status);
        inc_backPage = findViewById(R.id.inc_backPage);

        lin_result = findViewById(R.id.lin_result);

        view_result = findViewById(R.id.view_result);

        dot_result = findViewById(R.id.dot_result);

        msession = new SessionManager(this);
        token = msession.getToken();
        jabatan = msession.getJabatan();

        text_keterangan.setText("Dalam Proses");
        tx_nama = findViewById(R.id.tx_nama);
        tx_kepentingan = findViewById(R.id.tx_keperluan);
        tx_catatan = findViewById(R.id.tx_catatan);
        tx_lampiran = findViewById(R.id.tx_lampiran);
        bundle = getIntent().getExtras();
        idDetailIzin = getIntent().getStringExtra("id_off");

        getAlasanTolak(idDetailIzin);
        //getRiwayatStatusApprove(idDetailIzin);
        checkJabatan();

        downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);

        inc_backPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        tvLampiran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lampiran_file.equals("null") || lampiran_file == null) {
                    Toast.makeText(DetailIzinOffEmp.this, "Foto izin tidak ada", Toast.LENGTH_SHORT).show();
                } else {
                    dialogFotoIzinSakit();
                }
            }
        });

    }

    private void dialogFotoIzinSakit() {
        dialogFoto = new Dialog(DetailIzinOffEmp.this);
        dialogFoto.setContentView(R.layout.dialog_foto_izin_sakit);
        dialogFoto.setCancelable(true);
        dialogFoto.setCanceledOnTouchOutside(true);
        img_izin_dns = dialogFoto.findViewById(R.id.img_izin_sakit);
        fabDownloadIzin = dialogFoto.findViewById(R.id.fabDownloadIzin);
        RequestOptions requestOptions = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true);
        Glide.with(DetailIzinOffEmp.this)
                //.load("https://hris.qhomedata.id/upload/karyawan/1234567890123456/lampiran/surat_mt_dinas/20220609140520-surat-1234567890123456.jpg")
                .load(api.URL_foto_izinLampiran+kyano+"/lampiran/OFF/"+lampiran_file)
                .thumbnail(Glide.with(DetailIzinOffEmp.this)
                        .load(R.drawable.loading))
                .apply(requestOptions)
                .into(img_izin_dns);

        fabDownloadIzin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadIzin(lampiran_file);
            }
        });
        dialogFoto.show();
    }

    private void downloadIzin(String fileName) {
        downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        //Uri uri = Uri.parse("https://hris.qhomedata.id/upload/karyawan/1234567890123456/lampiran/surat_mt_dinas/20220609140520-surat-1234567890123456.jpg");
        Uri uri = Uri.parse(api.URL_foto_izinLampiran+kyano+"/lampiran/OFF/"+fileName);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOCUMENTS, fileName + ".png");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        Long reference = downloadManager.enqueue(request);
        showProgressDownload(reference);
    }

    private void showProgressDownload(Long reference) {
        final ProgressDialog progressBarDialog = new ProgressDialog(this);
        progressBarDialog.setTitle("Mengunduh Lampiran");
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

    private void checkJabatan() {

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET,
                //"http://192.168.50.24/all/hris_ci_3/api/akses", null,
                api.URL_Akses, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("TAG_RESPONSE_MENU", "checkJabatan: onMethod" + response.toString());

                            int status = response.getInt("status");

                            JSONObject message = response.getJSONObject("message");
                            String hak = message.getString("hak_akses");

                            Log.d("TAG_TAG_MSG_HAK", "run: " + status + message.toString() + hak);

                            if (status == 200) {

                                hak_akses = hak;

                                if (hak_akses.equals("HEAD") || hak_akses.equals("EXECUTIV")) {
                                    rlDirDns.setVisibility(View.VISIBLE);
                                }

                            }

                        } catch (JSONException e) {

                            e.printStackTrace();
                            Log.d("JSON_RIWYAT_IZIN_DNS", "onResponse: " + e);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error_Volley_MENU_APP: ", error.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };

        Volley.newRequestQueue(DetailIzinOffEmp.this).add(req);
    }

    private void getRiwayatStatusApprove(String id_off) {
        AndroidNetworking.get(api.URL_getIzinOff + "?id=" +id_off +"&kehadiran=OFF")
                .addHeaders("Authorization", "Bearer " + token)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            Log.d("HASIL_ID", "onResponse: " + idDetailIzin);
                            String status = response.getString("status");

                            JSONObject data = response.getJSONObject("message");
                            Log.d("HASIL_RESPONSE", "onResponse: " + "Status" + status + " | " + data.toString());

                            tvName.setText(data.getString("name"));
                            //tx_kepentingan.setText(data.getString("keperluan"));
                            tvAlasan.setText(data.getString("alasan"));
                            //tx_daerah_tujuan.setText(data.getString("daerah"));
                            //tx_transportasi.setText(data.getString("trans"));
                            tvTanggalOff.setText(data.getString("select_off"));
                            tvLamaOff.setText(data.getString("lama_off"));
                            //  tx_lamaDns.setText(data.getString("dari") +" - "+ data.getString("sampai"));
                            lampiran_file = data.getString("lampiran");
                            status_approve = data.getString("status_approve");

                            Log.d("TAG_IMAGE", "loadDataFormAPI: " + data.getString("lampiran"));

                            kyano = data.getString("kyano");

                            switch (status_approve) {
                                case "ON PROGRESS":
                                    text_status.setText("Diproses");
                                    text_alasan.setVisibility(View.GONE);
                                    text_keterangan.setText("Dalam Proses");
                                    img_status.setBackgroundResource(R.drawable.onprogress);
                                    lin_result.setBackgroundResource(R.drawable.ic_boxtext_result_grey);
                                    dot_result.setBackgroundResource(R.drawable.ic_dot_point_abu_abu);
                                    view_result.setBackgroundResource(R.color.abu_abu);
                                    text_status.setTextColor(Color.BLACK);
                                    text_alasan.setTextColor(Color.BLACK);
                                    text_status.setTextColor(Color.WHITE);
                                    break;

                                case "TOLAK":
                                    text_status.setText("Ditolak");
                                    //text_alasan.setText("" + status_approve);
                                    text_alasan.setText(comment);;
                                    text_keterangan.setText("Ditolak");
                                    img_status.setBackgroundResource(R.drawable.rejected);
                                    lin_result.setBackgroundResource(R.drawable.ic_boxtext_result_red);
                                    dot_result.setBackgroundResource(R.drawable.ic_dot_red);
                                    view_result.setBackgroundResource(R.color.red_btn_bg_pressed_color);
                                    break;

                                default:
                                    text_status.setText("Diterima");
                                    text_alasan.setText("" + status_approve);
                                    text_keterangan.setText("Diterima");
                                    img_status.setBackgroundResource(R.drawable.success);
                                    lin_result.setBackgroundResource(R.drawable.ic_boxtext_result_green);
                                    dot_result.setBackgroundResource(R.drawable.ic_dot_sukses);
                                    view_result.setBackgroundResource(R.color.greennew);
                            }

                            /*
                             * ====================== Head Division Condition START ======================
                             */

                            if (data.getString("approve_head").equals("1")) {
                                if (data.getString("head_kyano").equals("null")) {
                                    lin_head.setVisibility(View.GONE);
                                    dot_head.setVisibility(View.GONE);
                                    view_head.setVisibility(View.GONE);
                                    text_status_head.setVisibility(View.GONE);
                                } else {
                                    lin_head.setBackgroundResource(R.drawable.ic_boxtext_green);
                                    dot_head.setBackgroundResource(R.drawable.ic_dot_sukses);
                                    view_head.setBackgroundResource(R.color.greennew);
                                    text_status_head.setText(data.getString("head_name"));
                                }
                            } else if (data.getString("approve_head").equals("0")) {
                                lin_head.setBackgroundResource(R.drawable.ic_boxtext_red);
                                dot_head.setBackgroundResource(R.drawable.ic_dot_red);
                                view_head.setBackgroundResource(R.color.red_btn_bg_pressed_color);
                                text_status_head.setText(data.getString("head_name"));
                                rlExecDns.setVisibility(View.GONE);
                                rlDirDns.setVisibility(View.GONE);
                                rlHrdDns.setVisibility(View.GONE);
                            } else {
                                text_status_head.setText("----");
                                Log.d("TAG_IS", "loadDataFormAPI: " + data.getString("approve_head"));
                                lin_head.setBackgroundResource(R.drawable.ic_boxtext_grey);
                                dot_head.setBackgroundResource(R.drawable.ic_dot_point_abu_abu);
                                view_head.setBackgroundResource(R.color.abu_abu);
                            }

                            /*
                             * ====================== Head Division Condition END ======================
                             */

                            /*
                             * ====================== Executive Division Condition START ======================
                             */

                            if (data.getString("approve_executiv").equals("1")) {

                                if (data.getString("approve_head").equals("1") && data.getString("executiv_kyano").equals("null")) {
                                    lin_exec.setBackgroundResource(R.drawable.ic_boxtext_green);
                                    dot_exec.setBackgroundResource(R.drawable.ic_dot_sukses);
                                    view_exec.setBackgroundResource(R.color.greennew);
                                    //text_status_exec.setText(data.getString("executiv"));
                                    text_status_exec.setVisibility(View.GONE);
                                    rlExecDns.setVisibility(View.GONE);
                                } else if (data.getString("approve_head").equals("null") && data.getString("executiv_kyano").equals("null")) {
                                    rlExecDns.setVisibility(View.GONE);
                                } else {
                                    lin_exec.setBackgroundResource(R.drawable.ic_boxtext_green);
                                    dot_exec.setBackgroundResource(R.drawable.ic_dot_sukses);
                                    view_exec.setBackgroundResource(R.color.greennew);
                                    text_status_exec.setText(data.getString("executiv"));
                                }

                            } else if (data.getString("approve_executiv").equals("0")) {
                                lin_exec.setBackgroundResource(R.drawable.ic_boxtext_red);
                                dot_exec.setBackgroundResource(R.drawable.ic_dot_red);
                                view_exec.setBackgroundResource(R.color.red_btn_bg_pressed_color);
                                text_status_exec.setText(data.getString("executiv"));
                                rlDirDns.setVisibility(View.GONE);
                                rlHrdDns.setVisibility(View.GONE);
                            } else {
                                text_status_exec.setText("----");
                                lin_exec.setBackgroundResource(R.drawable.ic_boxtext_grey);
                                dot_exec.setBackgroundResource(R.drawable.ic_dot_point_abu_abu);
                                view_exec.setBackgroundResource(R.color.abu_abu);
                            }

                            /*
                             * ====================== Executive Division Condition END ======================
                             */

                            /*
                             * ====================== Director Condition START ======================
                             */

                            if (data.getString("approve_directur").equals("1")) {
                                lin_dir.setBackgroundResource(R.drawable.ic_boxtext_green);
                                dot_dir.setBackgroundResource(R.drawable.ic_dot_sukses);
                                view_dir.setBackgroundResource(R.color.greennew);
                                text_status_dir.setText(data.getString("dir"));
                            } else if (data.getString("approve_directur").equals("0")) {
                                lin_dir.setBackgroundResource(R.drawable.ic_boxtext_red);
                                dot_dir.setBackgroundResource(R.drawable.ic_dot_red);
                                view_dir.setBackgroundResource(R.color.red_btn_bg_pressed_color);
                                text_status_dir.setText(data.getString("dir"));
                                rlHrdDns.setVisibility(View.GONE);
                            } else {
                                text_status_dir.setText("----");
                                lin_dir.setBackgroundResource(R.drawable.ic_boxtext_grey);
                                dot_dir.setBackgroundResource(R.drawable.ic_dot_point_abu_abu);
                                view_dir.setBackgroundResource(R.color.abu_abu);
                            }

                            /*
                             * ====================== Director Condition END ======================
                             */

                            /*
                             * ====================== HRD Condition START ======================
                             */

                            if (data.getString("approve_hrd").equals("null")) {
                                text_status_hrd.setText("----");
                                lin_hrd.setBackgroundResource(R.drawable.ic_boxtext_grey);
                                dot_hrd.setBackgroundResource(R.drawable.ic_dot_point_abu_abu);
                                view_hrd.setBackgroundResource(R.color.abu_abu);
                            } else if (data.getString("approve_hrd").equals("0") || data.getString("approve_head").equals("0") || data.getString("approve_head").equals("0")) {
                                lin_hrd.setBackgroundResource(R.drawable.ic_boxtext_red);
                                dot_hrd.setBackgroundResource(R.drawable.ic_dot_red);
                                view_hrd.setBackgroundResource(R.color.red_btn_bg_pressed_color);
                                text_status_hrd.setText(data.getString("hrd_name"));
                                lin_result.setBackgroundResource(R.drawable.ic_boxtext_result_red);
                            } else {
                                lin_hrd.setBackgroundResource(R.drawable.ic_boxtext_green);
                                dot_hrd.setBackgroundResource(R.drawable.ic_dot_sukses);
                                view_hrd.setBackgroundResource(R.color.greennew);
                                text_status_hrd.setText(data.getString("hrd_name"));
                            }

                            /*
                             * ====================== HRD Condition END ======================
                             */

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("JSON_RIWYAT_IZIN_DNS", "onResponse: " + e);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("EROOR_RIWYAT_IZIN_DNS", "onError: " + anError.getErrorDetail());
                    }
                });
    }

    private void getAlasanTolak(String id_off) {
        //"http://192.168.50.24/all/hris_ci_3/api/approve_OT"
        AndroidNetworking.get(api.URL_IzinOffTerlambat_approve+ "?id="+ id_off+"&jenis=OFF" + "&status=0")
                //AndroidNetworking.get("http://192.168.50.24/all/hris_ci_3/api/approve_OT"+ "?id="+_id+"&jenis=" + jenis)

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

                                JSONObject data = response.getJSONObject("message");

                                /*
                                 * GET DATA TO STRING
                                 */

                                if(data.getString("status_approve").equals("TOLAK")){
                                    comment = data.getString("comment");
                                    Log.d(TAG, "xxx" + data.getString("comment"));
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("JSON_RIWYAT_IZIN_MT", "onResponse: " + e);
                        } finally {
                            getRiwayatStatusApprove(id_off);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("ERROR_DET_MT_APRVE", "onError: " + anError.getErrorDetail());
                    }
                });
    }

}
