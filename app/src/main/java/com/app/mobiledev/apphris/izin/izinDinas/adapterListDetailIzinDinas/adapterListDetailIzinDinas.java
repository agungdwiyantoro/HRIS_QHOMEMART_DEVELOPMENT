package com.app.mobiledev.apphris.izin.izinDinas.adapterListDetailIzinDinas;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.mobiledev.apphris.R;
import com.app.mobiledev.apphris.helperPackage.helper;
import com.app.mobiledev.apphris.izin.izinDinas.formDetailIzinDinas;
import com.app.mobiledev.apphris.izin.izinDinas.modelListDetailIzinDinas.modelListDetailIzinDinas;
import java.util.List;


public class adapterListDetailIzinDinas extends RecyclerView.Adapter<adapterListDetailIzinDinas.ReyclerViewHolder>  {
    private Context mCtx;
    private List<modelListDetailIzinDinas> modelListDetailIzinDinass;

    public adapterListDetailIzinDinas(List<modelListDetailIzinDinas> modelListDetailIzinDinas , Context ctx){
        this.mCtx=ctx;
        this.modelListDetailIzinDinass=modelListDetailIzinDinas;
    }

    @NonNull
    @Override
    public ReyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.list_detail_izin_dinas, null);
        return new ReyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ReyclerViewHolder holder, int position) {
        final modelListDetailIzinDinas Object = modelListDetailIzinDinass.get(position);
        holder.tx_tgl.setText(Object.getTgl());
        holder.tx_alamat.setText(helper.getLokasi(Double.parseDouble(Object.getLat()),Double.parseDouble(Object.getLang()),mCtx));
        holder.tx_nama.setText("Nama :"+Object.getKynm());
        holder.image=Object.getImage();
        holder.lang=Object.getLang();
        holder.lat=Object.getLat();
        holder.tgl=Object.getTgl();

        holder.tx_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mCtx, formDetailIzinDinas.class);
                Bundle x = new Bundle();
                x.putString("image",holder.image);
                x.putString("lang",holder.lang);
                x.putString("lat",holder.lat);
                x.putString("tgl",holder.tgl);
                intent.putExtras(x);
                mCtx.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return modelListDetailIzinDinass.size();
    }

    public class ReyclerViewHolder extends RecyclerView.ViewHolder  {
        private TextView tx_tgl;
        private TextView tx_nama;
        private TextView tx_alamat;
        private TextView tx_detail;
        private String image,lang,lat,status,tgl;

        public ReyclerViewHolder(View itemView) {
            super(itemView);
            tx_tgl=itemView.findViewById(R.id.tx_tgl);
            tx_nama=itemView.findViewById(R.id.tx_nama);
            tx_alamat=itemView.findViewById(R.id.tx_alamat);
            tx_detail=itemView.findViewById(R.id.tx_detail);
        }


    }


}
