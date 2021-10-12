package com.app.mobiledev.apphris;

import android.content.Intent;
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

import com.app.mobiledev.apphris.approve.menu_approve;
import com.app.mobiledev.apphris.bonus.menu_bonus;
import com.app.mobiledev.apphris.helperPackage.helper;
import com.app.mobiledev.apphris.izin.menu_izin;
import com.app.mobiledev.apphris.sesion.SessionManager;
import com.app.mobiledev.apphris.training.soal_training.ModelLatihan;
import com.app.mobiledev.apphris.helperPackage.DataSoalSQLite;
import com.app.mobiledev.apphris.kasbon.kasbon_karyawan;
import com.app.mobiledev.apphris.pinjaman.pinjamanUang;

import java.util.ArrayList;
import java.util.List;

public class fragment_info extends Fragment {
    public fragment_info(){}
    private View rootView;
    private CardView pinjaman,kasbon,kredit,izin,bonus, latihan,projectManage,jadwalSales, visitor,cvApprove;
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
        rootView= inflater.inflate(R.layout.activity_fragment_pinjaman, container, false);
        pinjaman=rootView.findViewById(R.id.pinjaman);
        kasbon=rootView.findViewById(R.id.kasbon);
        kredit=rootView.findViewById(R.id.kredit);
        cvApprove=rootView.findViewById(R.id.cvApprove);
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

        cvApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(helper.regexKata("MANAGER",sessionmanager.getJabatan())){
                    startActivity(new Intent(getActivity(), menu_approve.class));
                }else{
                    helper.snackBar(lmenu,"anda tidak memiliki akses ke menu ini...!!");
                }

            }
        });

        pinjaman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), pinjamanUang.class));
                //helper.showMsg(getActivity(),"Informasi","Menu ini dalam perbaikan");

            }
        });


        kasbon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), kasbon_karyawan.class));
                //helper.showMsg(getActivity(),"Informasi","Menu ini dalam perbaikan");

            }
        });



        kredit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(new Intent(getActivity(), cuti.class));
              //  helper.showMsg(getActivity(),"Informasi","Menu ini dalam perbaikan");
            }
        });

        izin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), menu_izin.class);
                Bundle x = new Bundle();
                x.putString("lokasi_tujuan", "");
                x.putString("lokasi_awal", "");
                intent.putExtras(x);
                getActivity().startActivity(intent);
                //helper.showMsg(getActivity(),"Informasi","Menu ini dalam perbaikan");
            }
        });

        bonus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), menu_bonus.class));

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
              //  startActivity(new Intent(getActivity(), menu_training.class));
                helper.snackBar(lmenu,"menu ini belum tersedia....!!!");

            }
        });
        projectManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helper.cekAkses(getActivity(),lmenu);
               // helper.cekAkses(getActivity(),lmenu,"projek_manage");
            }
        });

        visitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //helper.cekAkses(getActivity(),lmenu,"visitor");
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



}
