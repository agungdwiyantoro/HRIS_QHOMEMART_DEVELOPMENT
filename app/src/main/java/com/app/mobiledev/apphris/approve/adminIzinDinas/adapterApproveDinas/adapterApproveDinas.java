package com.app.mobiledev.apphris.approve.adminIzinDinas.adapterApproveDinas;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.app.mobiledev.apphris.R;
import com.app.mobiledev.apphris.approve.adminIzinDinas.detailListDinasApprove;
import com.app.mobiledev.apphris.approve.adminIzinDinas.modelApproveDinas.modelApproveDinas;
import com.app.mobiledev.apphris.helperPackage.helper;


import java.util.List;

public class adapterApproveDinas  extends RecyclerView.Adapter<adapterApproveDinas.ReyclerViewHolder> {
    private Context mCtx;
    private List<modelApproveDinas> modelApproveDinass;



    public adapterApproveDinas(List<modelApproveDinas> modelApproveDinas , Context ctx){
        this.mCtx=ctx;
        this.modelApproveDinass=modelApproveDinas;
    }


    @NonNull
    @Override
    public ReyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.list_riwayat_approve_izin_dinas, null);
        return new ReyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ReyclerViewHolder holder, int position) {
        final modelApproveDinas Object = modelApproveDinass.get(position);
        holder.tx_waktu.setText(Object.getDari()+" -- "+Object.getSampai());
        holder.tx_keperluan.setText(Object.getKeperluan());
        holder.tdano=Object.getTdano();
        holder.kyano_izin=Object.getKyano();
        holder.tx_nama.setText(Object.getKynm());

        if(Object.getApprove_head().equals("0")){
            holder.tx_approveHead.setText("Approve Head: Belum");
        }else{
            holder.tx_approveHead.setText("Approve Head: Sudah");
        }

        holder.tx_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mCtx, detailListDinasApprove.class);
                Bundle x = new Bundle();
                x.putString("tdano", holder.tdano);
                x.putString("kyano_izin", holder.kyano_izin);

                if(Object.getDari().equals("")||Object.getDari().equals(null)){
                    x.putString("tgl_mulai", "");
                    x.putString("jam_mulai", "");
                }else{
                    x.putString("tgl_mulai", helper.split_date_time("date",Object.getDari()));
                    x.putString("jam_mulai", helper.split_date_time("time",Object.getDari()));
                }
                Log.d("CEK_SPLIT", "onClick: "+helper.split_date_time("time",Object.getDari()));

                if(Object.getSampai().equals("")||Object.getSampai().equals(null)){
                    x.putString("tgl_selesai", "");
                    x.putString("jam_selesai", "");
                }else{
                    x.putString("tgl_selesai", helper.split_date_time("date",Object.getSampai()));
                    x.putString("jam_selesai", helper.split_date_time("time",Object.getSampai()));
                }

                x.putString("keperluan",Object.getKeperluan());
                x.putString("trans",Object.getTrans());
                x.putString("akomodasi",Object.getAkomodasi());
                x.putString("daerah",Object.getDaerah());
                x.putString("ket",Object.getKet());
                intent.putExtras(x);
                mCtx.startActivity(intent);

            }
        });
        if(Object.getTrans().equals("")){
            holder.tx_transportasi.setText("Transportasi : ______");
        }else{
            holder.tx_transportasi.setText("Transportasi :"+Object.getTrans());
        }

    }

    @Override
    public int getItemCount() {
        return modelApproveDinass.size();
    }

    public class ReyclerViewHolder extends RecyclerView.ViewHolder  {
        private TextView tx_waktu;
        private TextView tx_transportasi;
        private TextView tx_keperluan;
        private TextView tx_detail;
        private String tdano,kyano_izin;
        private TextView tx_nama;
        private TextView tx_approveHead;
        private CardView cv_list_riwayat_izin_dinas;
        public ReyclerViewHolder(View itemView) {
            super(itemView);
            tx_waktu=itemView.findViewById(R.id.tx_waktu);
            tx_transportasi=itemView.findViewById(R.id.tx_transportasi);
            tx_keperluan=itemView.findViewById(R.id.tx_keperluan);
            tx_detail=itemView.findViewById(R.id.tx_detail);
            tx_nama=itemView.findViewById(R.id.tx_nama);
            tx_approveHead=itemView.findViewById(R.id.tx_approveHead);
            cv_list_riwayat_izin_dinas=itemView.findViewById(R.id.cv_list_riwayat_izin_dinas);
        }


    }

}
