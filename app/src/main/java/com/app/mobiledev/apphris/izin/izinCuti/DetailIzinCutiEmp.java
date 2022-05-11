package com.app.mobiledev.apphris.izin.izinCuti;

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
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.app.mobiledev.apphris.Model.modelIzinCutiNew;
import com.app.mobiledev.apphris.R;
import com.app.mobiledev.apphris.api.api;
import com.app.mobiledev.apphris.sesion.SessionManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.github.chrisbanes.photoview.PhotoView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailIzinCutiEmp extends AppCompatActivity {

    private List<modelIzinCutiNew> modelIzinCutiNews;
    private TextView text_keterangan, text_status, text_alasan, text_status_hrd,
            text_status_head, text_status_exec, text_status_dir, tx_nama, tx_jenis_cuti,
            tx_keterangan, tx_selengkapnya, text_head, text_exec, text_dir, text_hrd;
    private ImageView dot_head, dot_exec, dot_dir, dot_hrd, dot_result, img_status;
    private Bundle bundle;
    private String kyano="",idDetailIzin = "", kodeStatus = "", status_approve = "",
            token, nama = "", jabatan, hak_akses;
    private SessionManager msession;
    private LinearLayout lin_head, lin_exec, lin_dir, lin_hrd, lin_result;
    private RelativeLayout rlExecCuti, rlDirCuti, rlHrdCuti;
    private View view_head, view_exec, view_dir, view_hrd, view_result, inc_backPage;

    //dialog
    private Dialog dialogFoto;
    private PhotoView img_izin_cuti;
    private FloatingActionButton fabDownloadIzin;
    private DownloadManager downloadManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_izin_cuti_emp);
        modelIzinCutiNews = new ArrayList<>();

        text_keterangan = findViewById(R.id.text_keterangan);
        text_status = findViewById(R.id.text_status);
        text_alasan = findViewById(R.id.text_alasan);

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

        rlHrdCuti = findViewById(R.id.rlHrdCuti);
        rlExecCuti = findViewById(R.id.rlExecCuti);
        rlDirCuti = findViewById(R.id.rlDirCuti);

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
        tx_jenis_cuti = findViewById(R.id.tx_jenis_cuti);
        tx_keterangan = findViewById(R.id.tx_keterangan);
        tx_selengkapnya = findViewById(R.id.tx_selengkapnya);
        bundle = getIntent().getExtras();
        idDetailIzin = bundle.getString("ctano");

        getRiwayatStatusApprove(idDetailIzin);
        checkJabatan();

        downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);

        inc_backPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void checkJabatan() {

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET,
                //"http://192.168.50.24/all/hris_ci_3/api/akses", null,
                api.URL_Akses, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("TAG_RESPONSE_MENU", "checkJabatan: onMethod"+response.toString());

                            int status = response.getInt("status");

                            JSONObject message = response.getJSONObject("message");
                            String hak = message.getString("hak_akses");

                            Log.d("TAG_TAG_MSG_HAK", "run: " + status + message.toString()+ hak);

                            if (status == 200) {

                                hak_akses = hak;

                                if (hak_akses.equals("HEAD") || hak_akses.equals("EXECUTIV")) {
                                    rlDirCuti.setVisibility(View.VISIBLE);
                                }

                            }

                        } catch (JSONException e) {

                            e.printStackTrace();
                            Log.d("JSON_RIWYAT_IZIN_CUTI", "onResponse: " + e);
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
                headers.put("Authorization", "Bearer "+token);
                return headers;
            }
        };

        Volley.newRequestQueue(DetailIzinCutiEmp.this).add(req);
    }

    private void getRiwayatStatusApprove(String ctano) {
        AndroidNetworking.get(api.URL_IzinCuti + "?id=" + ctano)
                .addHeaders("Authorization", "Bearer "+token)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            Log.d("HASIL_ID", "onResponse: " + ctano + kyano);
                            String status = response.getString("status");

                            JSONObject data = response.getJSONObject("message");
                            Log.d("HASIL_RESPONSE", "onResponse: " + "Status" + status+ " | "+ data.toString());

                            tx_nama.setText(data.getString("kynm"));
                            tx_jenis_cuti.setText(data.getString("nmcuti"));
                            tx_keterangan.setText(data.getString("ctalasan"));

                            status_approve = data.getString("status_approve");

                            Log.d("TAG_IMAGE", "loadDataFormAPI: "+data.getString("nmcuti"));

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
                                    text_alasan.setText("" + status_approve);
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
                                rlExecCuti.setVisibility(View.GONE);
                                rlDirCuti.setVisibility(View.GONE);
                                rlHrdCuti.setVisibility(View.GONE);
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
                        /*lin_exec.setBackgroundResource(R.drawable.ic_boxtext_green);
                        dot_exec.setBackgroundResource(R.drawable.ic_dot_sukses);
                        view_exec.setBackgroundResource(R.color.greennew);
                        //text_status_exec.setText(data.getString("executiv"));
                        text_status_exec.setVisibility(View.GONE);*/
                                    rlExecCuti.setVisibility(View.GONE);
                                } else if(data.getString("approve_head").equals("null") && data.getString("executiv_kyano").equals("null")) {
                                    rlExecCuti.setVisibility(View.GONE);
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
                                rlDirCuti.setVisibility(View.GONE);
                                rlHrdCuti.setVisibility(View.GONE);
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
                                rlHrdCuti.setVisibility(View.GONE);
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
                            Log.d("JSON_RIWYAT_IZIN_CUTI", "onResponse: " + e);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("EROOR_RIWYAT_IZIN_CUTI", "onError: " + anError.getErrorDetail());
                    }
                });
    }

    private void loadDataFormAPI(String status, JSONObject response) throws JSONException {
        modelIzinCutiNews.clear();
        if (status.equals("200")) {

        }
    }

}
