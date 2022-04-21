package com.app.mobiledev.apphris;

import android.app.Fragment;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.TooltipCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.app.mobiledev.apphris.api.api;
import com.app.mobiledev.apphris.approve.menu_approve;
import com.app.mobiledev.apphris.bonus.menu_bonus;
import com.app.mobiledev.apphris.formKunjungan.list_formKunjungan;
import com.app.mobiledev.apphris.helperPackage.helper;
import com.app.mobiledev.apphris.izin.izinSakit.dashboardIzin;
import com.app.mobiledev.apphris.sesion.SessionManager;
import com.app.mobiledev.apphris.slipGaji.riwayatSlipGaji;
import com.app.mobiledev.apphris.training.menu_training;
import com.app.mobiledev.apphris.training.soal_training.ModelLatihan;
import com.app.mobiledev.apphris.helperPackage.DataSoalSQLite;
import com.app.mobiledev.apphris.kasbon.kasbon_karyawan;
import com.app.mobiledev.apphris.pinjaman.pinjamanUang;
import com.app.mobiledev.apphris.visitor.Visitor;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class fragment_menu extends Fragment {
    public fragment_menu(){}
    private View rootView;
    private CardView pinjaman,kasbon,cuti,izin,bonus, latihan,projectManage,jadwalSales, visitor,cvApprove,slipGaji;
    private DataSoalSQLite db;
    private List<ModelLatihan> modelLatihan;
    private  double lat=0,lon=0;
    private TooltipCompat tooltipCompat;
    private SessionManager sessionmanager;
    private String jabatan,kyano, hak_akses, token;
    private ConstraintLayout lmenu;
    private String idtraining="",index="";
    private TextView txnotif_latihan;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView= inflater.inflate(R.layout.activity_fragment_menu, container, false);
        pinjaman=rootView.findViewById(R.id.pinjaman);
        kasbon=rootView.findViewById(R.id.kasbon);
        cuti=rootView.findViewById(R.id.cuti);
        cvApprove=rootView.findViewById(R.id.cvApprove);
        slipGaji=rootView.findViewById(R.id.slipGaji);
        projectManage=rootView.findViewById(R.id.projectManage);
        izin=rootView.findViewById(R.id.izin);
        bonus=rootView.findViewById(R.id.bonus);
        txnotif_latihan=rootView.findViewById(R.id.txnotif_latihan);
        jadwalSales=rootView.findViewById(R.id.jadwalSales);
        latihan = rootView.findViewById(R.id.latihan);
        visitor = rootView.findViewById(R.id.cvVisitor);
        modelLatihan = new ArrayList<>();
        db = new DataSoalSQLite(getActivity());
        sessionmanager = new SessionManager(getActivity());
        token = sessionmanager.getToken();
        kyano=sessionmanager.getIdUser();
        jabatan=sessionmanager.getCekStaff();
        idtraining=sessionmanager.getIdtraning();
        lmenu=rootView.findViewById(R.id.lmenu);
        txnotif_latihan.setVisibility(View.GONE);

        checkJabatan();

        cvApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // startActivity(new Intent(getActivity(), ListIzinSakitApproveHead.class));
//                if(helper.regexKata("MANAGER",sessionmanager.getJabatan())){
//                    startActivity(new Intent(getActivity(), menu_approve.class));
//                }else{
//                    helper.snackBar(lmenu,"anda tidak memiliki akses ke menu ini...!!");
//                }

                helper.cekAkses(getActivity(),lmenu,"approve", menu_approve.class, hak_akses);

            }
        });

        pinjaman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helper.cekAkses(getActivity(),lmenu,"pinjaman", pinjamanUang.class, "");
            }
        });

        kasbon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helper.cekAkses(getActivity(),lmenu,"kasbon", kasbon_karyawan.class, "");

            }
        });

        cuti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               helper.cekAkses(getActivity(),lmenu,"cuti", cuti.class, "");
            }
        });

        izin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i = new Intent(getActivity(), dashboardIzin.class, ");
//                startActivity(i);
                  helper.cekAkses(getActivity(),lmenu,"izin", dashboardIzin.class, "");
            }
        });

        bonus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helper.cekAkses(getActivity(),lmenu,"bonus", menu_bonus.class, "");

            }
        });

        jadwalSales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helper.snackBar(lmenu,"menu ini belum tersedia....!!!");

            }
        });

        latihan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helper.cekAkses(getActivity(),lmenu,"latihan", menu_training.class, "");

            }
        });

        projectManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helper.cekAkses(getActivity(),lmenu,"projek_manage", list_formKunjungan.class, "");
            }
        });

        visitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helper.cekAkses(getActivity(),lmenu,"pengunjung", Visitor.class, "");

            }
        });

        slipGaji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                helper.cekAkses(getActivity(),lmenu,"slip gaji", riwayatSlipGaji.class, "");

            }
        });

       // deleteTrsoal();
        index = getArguments().getString("index_notif");
        Log.d("CEWK_INDEX", "onCreateView: "+index);
        cekNotigLatihan(index);

        return rootView;
    };

    private void cekNotigLatihan(String index){
        if(index.equals("")||index.equals("0")){
            txnotif_latihan.setVisibility(View.GONE);
        }else{
            txnotif_latihan.setVisibility(View.VISIBLE);
            txnotif_latihan.setText(""+index);

        }
    }

    private void checkJabatan() {
        JsonObjectRequest req = new JsonObjectRequest(
                api.URL_Akses, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String status = response.getString("status");

                            JSONObject message = response.getJSONObject("message");

                            if (status.equals("200")) {

                                hak_akses = message.getString("hak_akses");
                                Log.d("TAG_HAK", "run: " + hak_akses);

                                if (hak_akses.equals("HEAD")) {
                                    cvApprove.setVisibility(View.VISIBLE);
                                    hak_akses = message.getString("hak_akses");
                                } else if(hak_akses.equals("EXECUTIV")){
                                    cvApprove.setVisibility(View.VISIBLE);
                                    hak_akses = message.getString("hak_akses");
                                } else if(hak_akses.equals("DIRECTUR")){
                                    cvApprove.setVisibility(View.VISIBLE);
                                    hak_akses = message.getString("hak_akses");
                                } else if(hak_akses.equals("HRD")){
                                    cvApprove.setVisibility(View.VISIBLE);
                                    hak_akses = message.getString("hak_akses");
                                } else if(hak_akses.equals("HRD,EXECUTIV,DIRECTUR")){
                                    cvApprove.setVisibility(View.VISIBLE);
                                    hak_akses = message.getString("hak_akses");
                                }
                            }

                        } catch (JSONException e) {

                            e.printStackTrace();
                            Log.d("JSON_RIWYAT_IZIN_SAKIT", "onResponse: " + e);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error_Volley: ", error.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer "+token);
                return headers;
            }
        };

        Volley.newRequestQueue(getActivity()).add(req);
    }

}
