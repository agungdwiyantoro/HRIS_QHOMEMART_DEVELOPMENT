package com.app.mobiledev.apphris.approve.adminIzinSakit;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.app.mobiledev.apphris.R;
import com.app.mobiledev.apphris.approve.adminIzinSakit.adapterIzinSakitApprove.adapterIzinSakitApprove;
import com.app.mobiledev.apphris.newIzin.izinSakit.modelIzinSakit;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class detailIzinSakitApprove extends AppCompatActivity {

    private String id, nama, indikasi, catatan, tanggalPengajuan, tanggalKeputusan, imageName, approveHead;

    TextView txNama, txIndikasi, txCatatan, txTanggalPengajuan, txTanggalKeputusan, txKeputusan;
    ImageView ivImageName;
    LinearLayout linearOption, linearKeputusan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_izin_sakit_approve);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");

        getDetailSakitApprove(id);

        txNama = findViewById(R.id.txNamaDetAprve);
        txIndikasi = findViewById(R.id.txIndikasiDetAprve);
        txCatatan = findViewById(R.id.txCatatanDetAprve);
        txTanggalPengajuan = findViewById(R.id.txTglPengajuanDetAprve);
        txTanggalKeputusan = findViewById(R.id.txTglKeputusanDetAprve);
        txKeputusan = findViewById(R.id.txKeputusan);

        ivImageName = findViewById(R.id.ivDokumen);

        linearOption = findViewById(R.id.lLDetOption);
        linearKeputusan = findViewById(R.id.lLDetKeputusanAprove);

    }

    private void getDetailSakitApprove(String _id) {
        //192.168.50.24/all/hris_ci_3/api/approvehead?id=68
        //AndroidNetworking.get(api.URL_IzinSakit)
        AndroidNetworking.get("http://192.168.50.24/all/hris_ci_3/api/approvehead?id="+id)
                .addHeaders("Authorization", "Bearer " + "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJreWFubyI6IjAwMjAwODAxMDMwNTAzODEiLCJreXBhc3N3b3JkIjoiMTIzNDU2NyIsImlhdCI6MTYzODk0ODM3MSwiZXhwIjoxNjM4OTY2MzcxfQ.hrQ5qcryIQbnZG0FY_FvW32SGMD8wN0jRB8ZWAVCJsI")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            Log.d("RESULT_DET_PENG_SAKIT", "onResponse: " + response.toString());
                            JSONArray dataArray = response.getJSONArray("message");
                            JSONObject dataObject = dataArray.getJSONObject(0);

                            Log.d("RESULT_DET_PENG_SAKIT0", "onResponse: " + dataArray.getJSONObject(0).getString("name"));

                            int status = response.getInt("status");

                            if (status == 200){
                                nama = dataObject.getString("name");
                                indikasi = dataObject.getString("indikasi_sakit");
                                tanggalPengajuan = dataObject.getString("created_at");
                                catatan = dataObject.getString("catatan");
                                
                                imageName = dataObject.getString("lampiran_file");
                                approveHead = dataObject.getString("approve_head");
                                
                                if (dataObject.getString("head_approve_date").equals("null")){
                                    tanggalKeputusan = "";
                                    linearOption.setVisibility(View.VISIBLE);
                                } else {
                                    tanggalKeputusan = dataObject.getString("head_approve_date");
                                    linearKeputusan.setVisibility(View.VISIBLE);
                                    if (approveHead.equals("1")){
                                        linearKeputusan.setBackgroundColor(Color.GREEN);
                                        txKeputusan.setText("Disetujui");
                                    } else {
                                        linearKeputusan.setBackgroundColor(Color.RED);
                                        txKeputusan.setText("Ditolak");
                                    }
                                }

                                txNama.setText(nama);
                                txIndikasi.setText(indikasi);
                                txCatatan.setText(catatan);
                                txTanggalPengajuan.setText(tanggalPengajuan);
                                txTanggalKeputusan.setText(tanggalKeputusan);
                                Glide.with(detailIzinSakitApprove.this).load("http://192.168.50.24/all/hris_ci_3/upload/tmp_surat/"+imageName).into(ivImageName);
                                Log.d("RESULT_DET_PENG_SAKIT1", "onResponse: " + nama + indikasi + tanggalPengajuan + catatan + imageName + tanggalKeputusan + approveHead);
                                
                            } else {
                                Toast.makeText(detailIzinSakitApprove.this, dataObject.toString(), Toast.LENGTH_SHORT).show();
                            }

                            /*if (status.equals("200")) {

                                nama = response.getString("name");

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

                                    model.setHead_kyano(data.getString("head_kyano"));
                                    model.setHrd_kyano(data.getString("hrd_kyano"));
                                    model.setHead_approve_date(data.getString("head_approve_date"));
                                    model.setHrd_approve_date(data.getString("hrd_approve_date"));
                                    model.setHead_name(data.getString("head_name"));

                                    //modelIzinSakits.add(model);
                                }

                            } else {
                                JSONObject object = response.getJSONObject("message");
                                String pesan = object.getString("lampiran_file");

                                //  Toast.makeText(formIzinSakit.this,""+pesan,toast.LENGTH_SHORT).show();
                            }*/


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