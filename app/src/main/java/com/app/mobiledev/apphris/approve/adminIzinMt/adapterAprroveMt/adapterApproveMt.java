package com.app.mobiledev.apphris.approve.adminIzinMt.adapterAprroveMt;

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
import com.app.mobiledev.apphris.approve.adminIzinMt.detailIzinMtApprove;
import com.app.mobiledev.apphris.approve.adminIzinMt.modelApproveMt.modelApproveMt;

import java.util.List;

public class adapterApproveMt extends RecyclerView.Adapter<adapterApproveMt.ReyclerViewHolder> {
    private Context mCtx;
    private List<modelApproveMt> modelApproveMt;

    public adapterApproveMt(List<modelApproveMt> modelApproveMt , Context ctx){
        this.mCtx=ctx;
        this.modelApproveMt=modelApproveMt;
    }


    @NonNull
    @Override
    public ReyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.list_riwayat_approve_izin_mt, null);
        return new ReyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReyclerViewHolder holder, int position) {
        final modelApproveMt Object = modelApproveMt.get(position);
        holder.tx_waktu.setText(Object.getTgl()+" "+Object.getJam()+"-"+Object.getSampai());
        holder.tx_kepentingan.setText(Object.getKepentingan());
        holder.tx_jenisIzin.setText("Jenis Izin : "+Object.getStatus());

        if(Object.getAprove_by().equals("")||Object.getAprove_by().equals("0")){
            holder.tx_approveHead.setText("Approve : Belum");
        }else{
            holder.tx_approveHead.setText("Approve : Sudah ");
        }

        holder.tx_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mCtx, detailIzinMtApprove.class);
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
                x.putString("aproveBy", Object.getAprove_by());
                x.putString("aproveDate", Object.getAprove_date());
                intent.putExtras(x);
                mCtx.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return modelApproveMt.size();
    }

    public class ReyclerViewHolder extends RecyclerView.ViewHolder  {
        private TextView tx_waktu;
        private TextView tx_jenisIzin;
        private TextView tx_kepentingan;
        private TextView tx_approveHead;
        private TextView tx_detail;
        public ReyclerViewHolder(View itemView) {
            super(itemView);
            tx_waktu=itemView.findViewById(R.id.tx_waktu);
            tx_jenisIzin=itemView.findViewById(R.id.tx_jenisIzin);
            tx_kepentingan=itemView.findViewById(R.id.tx_kepentingan);
            tx_approveHead=itemView.findViewById(R.id.tx_approveHead);
            tx_detail=itemView.findViewById(R.id.tx_detail);

        }


    }

}
