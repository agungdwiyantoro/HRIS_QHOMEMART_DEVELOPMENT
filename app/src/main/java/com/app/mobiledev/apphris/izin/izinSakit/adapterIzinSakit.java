package com.app.mobiledev.apphris.izin.izinSakit;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.mobiledev.apphris.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class adapterIzinSakit extends RecyclerView.Adapter<adapterIzinSakit.ReyclerViewHolder> {
    private Context mCtx;
    private List<modelIzinSakit> modelIzinSakit;

    public adapterIzinSakit(List<modelIzinSakit> modelIzinSakit , Context ctx){
        this.mCtx=ctx;
        this.modelIzinSakit=modelIzinSakit;
    }



    @NonNull
    @Override
    public ReyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.list_riwayat_izin_sakit, null);
        return new ReyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReyclerViewHolder holder, int position) {
        final modelIzinSakit Object = modelIzinSakit.get(position);
        holder.tvKet.setText(""+Object.getIndikasi_sakit());
        holder.tvAlasan.setText(""+Object.getCatatan());




        try {
            holder.dateSource = holder.dateFormatSources.parse(Object.getCreated_at());
            holder.tx_tanggal.setText(holder.dateFormat_day.format(holder.dateSource));
            holder.tx_bulan_tahun.setText(holder.dateFormat_month_year.format(holder.dateSource));


        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.d("CEK_APPROVE_HEAD", "onBindViewHolder: "+Object.getApprove_head());

        if(Object.getApprove_head().equals("null")){
            holder.imStatus.setImageResource(R.drawable.ic_dot_point_abu_abu);
            holder.kodeStatus="";
            holder.statusApprove="Dalam Proses";

        }

        else if(Object.getApprove_head().equals("0")){

            holder.imStatus.setImageResource(R.drawable.ic_dot_red);
            holder.kodeStatus="0";
            holder.statusApprove="Ditolak oleh Head";

        }
        else if(Object.getApprove_head().equals("1")){

            if(Object.getApprove_hrd().equals("0")){
                holder.imStatus.setImageResource(R.drawable.ic_dot_red);
                holder.kodeStatus="0";
                holder.statusApprove="Ditolak oleh Human Resources Development";

            }
            else if(Object.getApprove_hrd().equals("1")){
                holder.imStatus.setImageResource(R.drawable.ic_dot_sukses);
                holder.kodeStatus="1";
                holder.statusApprove="Diterima oleh HRD dan Head/Supervisor";
            }
            else{
                holder.imStatus.setImageResource(R.drawable.ic_dot_point_abu_abu);
                holder.kodeStatus="";
                holder.statusApprove="Dalam Proses";
            }
        }

        holder.line1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(mCtx, statusApproveIzinSakit.class);
                Bundle x = new Bundle();
                x.putString("id", Object.getId());
                x.putString("kode_status", holder.kodeStatus);
                x.putString("status_approve", holder.statusApprove);
                i.putExtras(x);
                mCtx.startActivity(i);

            }
        });



    }



    @Override
    public int getItemCount() {
        return modelIzinSakit.size();
    }

    public class ReyclerViewHolder extends RecyclerView.ViewHolder  {
        private TextView tvKet;
        private TextView tvAlasan;
        private LinearLayout line1;
        private TextView tx_tanggal;
        private TextView tx_bulan_tahun;
        private SimpleDateFormat dateFormatSources;
        private SimpleDateFormat dateFormat_day;
        private SimpleDateFormat dateFormat_month_year;
        private Date dateSource;
        private ImageView imStatus;
        private CardView card_list_riwayat_izin;
        private String kodeStatus="";
        private String statusApprove="";




        public ReyclerViewHolder(View itemView) {
            super(itemView);
            tvKet=itemView.findViewById(R.id.tvKet);
            tvAlasan=itemView.findViewById(R.id.tvAlasan);
            line1=itemView.findViewById(R.id.line1);
            tx_tanggal=itemView.findViewById(R.id.tx_tanggal);
            tx_bulan_tahun=itemView.findViewById(R.id.tx_bulan_tahun);
            card_list_riwayat_izin=itemView.findViewById(R.id.card_list_riwayat_izin);
            imStatus=itemView.findViewById(R.id.imStatus);
            dateFormatSources = new SimpleDateFormat("yyyy-MM-dd");
            dateFormat_day = new SimpleDateFormat("dd");
            dateFormat_month_year = new SimpleDateFormat("MMM-yyyy");



        }


    }
}
