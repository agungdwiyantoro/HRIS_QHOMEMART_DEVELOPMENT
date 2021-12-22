package com.app.mobiledev.apphris.approve.adminIzinSakit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.app.mobiledev.apphris.R;
import com.app.mobiledev.apphris.api.api;
import com.app.mobiledev.apphris.approve.adminIzinSakit.adapterIzinSakitApprove.adapterIzinSakitApprove;
import com.app.mobiledev.apphris.helperPackage.PaginationListener;
import com.app.mobiledev.apphris.izin.izinSakit.modelIzinSakit;
import com.app.mobiledev.apphris.sesion.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ListIzinSakitApprove extends AppCompatActivity {

    RecyclerView recyler_izin_sakit;
    private List<modelIzinSakit> modelIzinSakits;
    private String token;
    private ImageView img_back;
    private SessionManager msession;
    private RadioGroup rbFilter;
    private LinearLayout lin_transparant;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_pengajuan_approve);
        recyler_izin_sakit = findViewById(R.id.recyler_izin_sakit);
        rbFilter=findViewById(R.id.rbFilter);
        img_back = findViewById(R.id.img_back);
        lin_transparant=findViewById(R.id.lin_transparant);
        msession = new SessionManager(ListIzinSakitApprove.this);
        modelIzinSakits = new ArrayList<>();
        token = msession.getToken();
        lin_transparant.setVisibility(View.VISIBLE);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        getRiwayatSakitAll();
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        rbFilter.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                lin_transparant.setVisibility(View.VISIBLE);
                recyler_izin_sakit.setVisibility(View.GONE);
                if (checkedId==R.id.news){
                    getRiwayatSakitNew();

                } else if(checkedId==R.id.all) {
                    getRiwayatSakitAll();

                }
            }
        });

        recyler_izin_sakit.addOnScrollListener(new PaginationListener(layoutManager) {
            @Override
            protected void loadMoreItems() {

            }

            @Override
            public boolean isLastPage() {
                return false;
            }

            @Override
            public boolean isLoading() {
                return false;
            }
        });
    }

    private void getRiwayatSakitNew() {
        AndroidNetworking.get(api.URL_IzinSakit_approve)
                .addHeaders("Authorization", "Bearer "+token)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            recyler_izin_sakit.setVisibility(View.VISIBLE);
                            String status = response.getString("status");
                            Log.d("HASL_RESPONSE_APPROVE", "onResponse: " + status);
                            if (status.equals("200")) {
                                modelIzinSakits.clear();
                                JSONArray jsonArray = response.getJSONArray("message");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject data = jsonArray.getJSONObject(i);
                                    modelIzinSakit model = new modelIzinSakit();

                                    if(data.getString("approve_head").equals("null")){
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
                                        model.setName(data.getString("name"));
                                        modelIzinSakits.add(model);

                                    }


                                }

                                adapterIzinSakitApprove mAdapter;
                                mAdapter = new adapterIzinSakitApprove(modelIzinSakits, ListIzinSakitApprove.this);
                                mAdapter.notifyDataSetChanged();
                                recyler_izin_sakit.setLayoutManager(new LinearLayoutManager(ListIzinSakitApprove.this));
                                recyler_izin_sakit.setItemAnimator(new DefaultItemAnimator());
                                recyler_izin_sakit.setAdapter(mAdapter);

                            } else {
                                JSONObject object = response.getJSONObject("message");
                                String pesan = object.getString("lampiran_file");

                                //  Toast.makeText(formIzinSakit.this,""+pesan,toast.LENGTH_SHORT).show();
                            }

                            lin_transparant.setVisibility(View.GONE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("JSON_RIWYAT_IZIN_SAKIT", "onResponse: " + e);
                            lin_transparant.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("EROOR_RIWYAT_IZIN_SAKIT", "onError: " + anError.getErrorDetail());
                        lin_transparant.setVisibility(View.GONE);
                    }
                });


    }

    private void getRiwayatSakitAll() {
        AndroidNetworking.get(api.URL_IzinSakit_approve)
                .addHeaders("Authorization", "Bearer "+token)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            recyler_izin_sakit.setVisibility(View.VISIBLE);
                            String status = response.getString("status");
                            Log.d("HASL_RESPONSE_APPROVE", "onResponse: " + status);
                            if (status.equals("200")) {
                                modelIzinSakits.clear();
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
                                        model.setName(data.getString("name"));
                                        modelIzinSakits.add(model);




                                }

                                adapterIzinSakitApprove mAdapter;
                                mAdapter = new adapterIzinSakitApprove(modelIzinSakits, ListIzinSakitApprove.this);
                                mAdapter.notifyDataSetChanged();
                                recyler_izin_sakit.setLayoutManager(new LinearLayoutManager(ListIzinSakitApprove.this));
                                recyler_izin_sakit.setItemAnimator(new DefaultItemAnimator());
                                recyler_izin_sakit.setAdapter(mAdapter);

                            } else {
                                JSONObject object = response.getJSONObject("message");
                                String pesan = object.getString("lampiran_file");

                                //  Toast.makeText(formIzinSakit.this,""+pesan,toast.LENGTH_SHORT).show();
                            }
                            lin_transparant.setVisibility(View.GONE);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("JSON_RIWYAT_IZIN_SAKIT", "onResponse: " + e);
                            lin_transparant.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("EROOR_RIWYAT_IZIN_SAKIT", "onError: " + anError.getErrorDetail());
                        lin_transparant.setVisibility(View.GONE);
                    }
                });


    }
}