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

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.app.mobiledev.apphris.api.api;
import com.app.mobiledev.apphris.approve.menu_approve;
import com.app.mobiledev.apphris.bonus.menu_bonus;
import com.app.mobiledev.apphris.formKunjungan.list_formKunjungan;
import com.app.mobiledev.apphris.helperPackage.helper;
import com.app.mobiledev.apphris.izin.dashboardIzin;
import com.app.mobiledev.apphris.sesion.SessionManager;
import com.app.mobiledev.apphris.slipGaji.riwayatSlipGaji;
import com.app.mobiledev.apphris.training.menu_training;
import com.app.mobiledev.apphris.training.soal_training.ModelLatihan;
import com.app.mobiledev.apphris.helperPackage.DataSoalSQLite;
import com.app.mobiledev.apphris.kasbon.kasbon_karyawan;
import com.app.mobiledev.apphris.pinjaman.pinjamanUang;
import com.app.mobiledev.apphris.visitor.Visitor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class fragment_menu extends Fragment {
    public fragment_menu(){}
    private View rootView;
    private CardView pinjaman,kasbon,cuti,izin,bonus, latihan,projectManage,jadwalSales, visitor,cvApprove,slipGaji;
    private DataSoalSQLite db;
    private List<ModelLatihan> modelLatihan;
    private  double lat=0,lon=0;
    private TooltipCompat tooltipCompat;
    private SessionManager sessionmanager;
    private String jabatan,kyano;
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
        kyano=sessionmanager.getIdUser();
        jabatan=sessionmanager.getCekStaff();
        idtraining=sessionmanager.getIdtraning();
        lmenu=rootView.findViewById(R.id.lmenu);
        txnotif_latihan.setVisibility(View.GONE);

        checkApproveAvailable();

        cvApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // startActivity(new Intent(getActivity(), ListIzinSakitApproveHead.class));
//                if(helper.regexKata("MANAGER",sessionmanager.getJabatan())){
//                    startActivity(new Intent(getActivity(), menu_approve.class));
//                }else{
//                    helper.snackBar(lmenu,"anda tidak memiliki akses ke menu ini...!!");
//                }

                helper.cekAkses(getActivity(),lmenu,"approve", menu_approve.class);

            }
        });

        pinjaman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helper.cekAkses(getActivity(),lmenu,"pinjaman", pinjamanUang.class);
            }
        });

        kasbon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helper.cekAkses(getActivity(),lmenu,"kasbon", kasbon_karyawan.class);

            }
        });

        cuti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               helper.cekAkses(getActivity(),lmenu,"cuti", cuti.class);
            }
        });

        izin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i = new Intent(getActivity(), dashboardIzin.class);
//                startActivity(i);
                  helper.cekAkses(getActivity(),lmenu,"izin", dashboardIzin.class);
            }
        });

        bonus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helper.cekAkses(getActivity(),lmenu,"bonus", menu_bonus.class);

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
                helper.cekAkses(getActivity(),lmenu,"latihan", menu_training.class);

            }
        });

        projectManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helper.cekAkses(getActivity(),lmenu,"projek_manage", list_formKunjungan.class);
            }
        });

        visitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helper.cekAkses(getActivity(),lmenu,"pengunjung", Visitor.class);

            }
        });

        slipGaji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                helper.cekAkses(getActivity(),lmenu,"slip gaji", riwayatSlipGaji.class);

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

    private void checkApproveAvailable() {
        AndroidNetworking.post(api.URL_getAksesMobile)
                .addBodyParameter("key", api.key)
                .addBodyParameter("menu_mobile", "approve")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Boolean success = response.getBoolean("success");
                            if (success) {
                                JSONArray jsonArray = response.getJSONArray("data");

                                Log.d("TAG_PREF_JABATAN", "onResponse: "+sessionmanager.getNoJabatan());

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject data = jsonArray.getJSONObject(i);

                                    if (data.getString("jabatan").contains(sessionmanager.getNoJabatan())){
                                        cvApprove.setVisibility(View.VISIBLE);
                                    } else {
                                        cvApprove.setVisibility(View.GONE);
                                    }

                                }

                            }
                            Log.d("CEK_UPDATE", "onResponse: " + success);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            helper.showMsg(getActivity(), "informasi", "" + helper.PESAN_SERVER);
                            Log.d("JSONUPDATE", "onResponse: " + e);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("EROOR_UPDATE", "onError: " + anError);
                        helper.showMsg(getActivity(), "informasi", "" + helper.PESAN_KONEKSI);

                    }
                });
    }

}
