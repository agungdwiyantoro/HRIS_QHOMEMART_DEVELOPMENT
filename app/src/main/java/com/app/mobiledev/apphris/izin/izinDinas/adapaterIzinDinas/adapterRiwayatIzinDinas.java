package com.app.mobiledev.apphris.izin.izinDinas.adapaterIzinDinas;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.app.mobiledev.apphris.R;
import com.app.mobiledev.apphris.helperPackage.helper;
import com.app.mobiledev.apphris.izin.izinDinas.form_izin_dinas;
import com.app.mobiledev.apphris.izin.izinDinas.modelRiwayatIzinDinas.modelRiwayatIzinDinas;


import java.util.List;

public class adapterRiwayatIzinDinas  extends RecyclerView.Adapter<adapterRiwayatIzinDinas.ReyclerViewHolder> {
    private Context mCtx;
    private List<modelRiwayatIzinDinas> modelRiwayatIzinDinass;



    public adapterRiwayatIzinDinas(List<modelRiwayatIzinDinas> modelRiwayatIzinDinas , Context ctx){
        this.mCtx=ctx;
        this.modelRiwayatIzinDinass=modelRiwayatIzinDinas;
    }


    @NonNull
    @Override
    public ReyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.list_riwayat_izin_dinas, null);
        return new ReyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ReyclerViewHolder holder, int position) {
        final modelRiwayatIzinDinas Object = modelRiwayatIzinDinass.get(position);
        holder.tx_waktu.setText(Object.getDari()+" -- "+Object.getSampai());
        holder.tx_keperluan.setText(Object.getKeperluan());
        holder.tdano=Object.getTdano();
        if(Object.getAprove_by().equals("belum")){
            holder.tx_status_head.setText("Approve Head: "+Object.getAprove_by());
        }else{
            holder.tx_status_head.setText("Approve Head: Sudah");
        }

        holder.tx_status_hrd.setText("Approve HRD: "+Object.getAprove_hrd());

        holder.tx_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mCtx, form_izin_dinas.class);
                Bundle x = new Bundle();
                x.putString("tdano", holder.tdano);

                if(Object.getDari().equals("")||Object.getDari().equals(null)){
                    x.putString("tgl_mulai", "");
                    x.putString("jam_mulai", "");
                }else{
                    x.putString("tgl_mulai", helper.split_date_time("date",Object.getDari()));
                    x.putString("jam_mulai", helper.split_date_time("time",Object.getDari()));
                }

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
        return modelRiwayatIzinDinass.size();
    }

    public class ReyclerViewHolder extends RecyclerView.ViewHolder  {
        private TextView tx_waktu;
        private TextView tx_transportasi;
        private TextView tx_keperluan;
        private TextView tx_detail;
        private TextView tx_status_head,tx_status_hrd;
        private String tdano;
        private CardView cv_list_riwayat_izin_dinas;
        public ReyclerViewHolder(View itemView) {
            super(itemView);
            tx_waktu=itemView.findViewById(R.id.tx_waktu);
            tx_transportasi=itemView.findViewById(R.id.tx_transportasi);
            tx_keperluan=itemView.findViewById(R.id.tx_keperluan);
            tx_detail=itemView.findViewById(R.id.tx_detail);
            cv_list_riwayat_izin_dinas=itemView.findViewById(R.id.cv_list_riwayat_izin_dinas);
            tx_status_head=itemView.findViewById(R.id.tx_status_head);
            tx_status_hrd=itemView.findViewById(R.id.tx_status_hrd);
        }


    }

}
