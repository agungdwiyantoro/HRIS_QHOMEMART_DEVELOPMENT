package com.app.mobiledev.apphris.izin.izinMT.adapterizinMt;

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
import com.app.mobiledev.apphris.izin.izinMT.form_izin_meninggalkan_tugas;
import com.app.mobiledev.apphris.izin.izinMT.modelizinMt.modelizinMt;

import java.util.List;

public class adapterizinMt extends RecyclerView.Adapter<adapterizinMt.ReyclerViewHolder> {
    private Context mCtx;
    private List<modelizinMt> modelizinMts;

    public adapterizinMt(List<modelizinMt> modelizinMt , Context ctx){
        this.mCtx=ctx;
        this.modelizinMts=modelizinMt;
    }


    @NonNull
    @Override
    public ReyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.list_izin_mt, null);
        return new ReyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ReyclerViewHolder holder, int position) {
        final modelizinMt Object = modelizinMts.get(position);
        holder.tx_waktu.setText(Object.getTgl()+" "+Object.getJam()+"-"+Object.getSampai());
        holder.tx_kepentingan.setText(Object.getKepentingan());
        holder.tx_jenisIzin.setText("Jenis Izin : "+Object.getStatus());
        holder.tx_approveHead.setText("Approve Head: "+Object.getAproveBy());
        if(Object.getAprove().equals("N")){
            holder.tx_approveHrd.setText("Approve HRD: Belum");
        }else{
            holder.tx_approveHrd.setText("Approve HRD: Sudah");
        }

        holder.tx_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mCtx, form_izin_meninggalkan_tugas.class);
                Bundle x = new Bundle();
                x.putString("tjano", Object.getTjano());
                x.putString("kyano", Object.getKyano());
                x.putString("dvano", Object.getDvano());
                x.putString("jbano", Object.getJbano());
                x.putString("tgl", Object.getTgl());
                x.putString("jam", Object.getJam());
                x.putString("sampai", Object.getSampai());
                x.putString("kepentingan", Object.getKepentingan());
                x.putString("status", Object.getStatus());
                x.putString("image", Object.getImage());
                x.putString("aprove", Object.getAprove());
                x.putString("aproveBy", Object.getAproveBy());
                x.putString("aproveDate", Object.getAproveDate());
                intent.putExtras(x);
                mCtx.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return modelizinMts.size();
    }

    public class ReyclerViewHolder extends RecyclerView.ViewHolder  {

        private TextView tx_waktu;
        private TextView tx_jenisIzin;
        private TextView tx_kepentingan;
        private TextView tx_approveHead;
        private TextView tx_approveHrd;
        private TextView tx_detail;


        public ReyclerViewHolder(View itemView) {
            super(itemView);
            tx_waktu=itemView.findViewById(R.id.tx_waktu);
            tx_jenisIzin=itemView.findViewById(R.id.tx_jenisIzin);
            tx_kepentingan=itemView.findViewById(R.id.tx_kepentingan);
            tx_approveHead=itemView.findViewById(R.id.tx_approveHead);
            tx_approveHrd=itemView.findViewById(R.id.tx_approveHrd);
            tx_detail=itemView.findViewById(R.id.tx_detail);
        }


    }
}
