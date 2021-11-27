package com.app.mobiledev.apphris.newIzin.izinSakit;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.app.mobiledev.apphris.R;


import java.util.ArrayList;
import java.util.List;

public class listIzinSakit extends AppCompatActivity {


    RecyclerView recyler_izin_sakit;
    private List<modelIzinSakit> modelIzinSakits;
    String dataDummy[]={"Demam","Demam","Demam","Demam","Demam","Demam","Demam","Demam","Demam","Demam"};
    String dataDummy2[]={"Izin Dokter Istirahat du...","Izin Dokter Istirahat du...","Izin Dokter Istirahat du...","Izin Dokter Istirahat du...","Izin Dokter Istirahat du...","Izin Dokter Istirahat du...","Izin Dokter Istirahat du...","Izin Dokter Istirahat du...","Izin Dokter Istirahat du...","Izin Dokter Istirahat du..."};
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_izin_sakit);
        recyler_izin_sakit=findViewById(R.id.recyler_izin_sakit);
        modelIzinSakits = new ArrayList<>();
        getDatdummy();



    }


    public void getDatdummy(){
        for(int i=0;i<dataDummy.length;i++){
            modelIzinSakit model = new modelIzinSakit();
            model.setKeterangan(dataDummy[i]);
            model.setalasan(dataDummy2[i]);
            modelIzinSakits.add(model);


        }

        adapterIzinSakit mAdapter;
        mAdapter = new adapterIzinSakit(modelIzinSakits, listIzinSakit.this);
        mAdapter.notifyDataSetChanged();
        recyler_izin_sakit.setLayoutManager(new LinearLayoutManager(listIzinSakit.this));
        recyler_izin_sakit.setItemAnimator(new DefaultItemAnimator());
        recyler_izin_sakit.setAdapter(mAdapter);
    }
}
