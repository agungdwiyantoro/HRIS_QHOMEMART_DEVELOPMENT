package com.app.mobiledev.apphris.approve.adminIzinSakit.adapterIzinSakitApprove;

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
import com.app.mobiledev.apphris.approve.adminIzinSakit.detailIzinSakitApprove;
import com.app.mobiledev.apphris.izin.izinSakit.modelIzinSakit;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class adapterIzinSakitApprove extends RecyclerView.Adapter<adapterIzinSakitApprove.RecylerViewHolder> {
    private Context mCtx;
    private List<modelIzinSakit> modelIzinSakit;

    public adapterIzinSakitApprove(List<modelIzinSakit> modelIzinSakit, Context ctx) {
        this.mCtx = ctx;
        this.modelIzinSakit = modelIzinSakit;
    }


    @NonNull
    @Override
    public RecylerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.list_riwayat_izin_sakit_approve, null);
        return new RecylerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecylerViewHolder holder, int position) {
        final modelIzinSakit Object = modelIzinSakit.get(position);

        holder.tvAlasan.setText("" + Object.getIndikasi_sakit());
        holder.tvNama.setText(""+Object.getName());


        try {
            holder.dateSource = holder.dateFormatSources.parse(Object.getCreated_at());
            holder.tx_tanggal.setText(holder.dateFormat_day.format(holder.dateSource));
            holder.tx_bulan_tahun.setText(holder.dateFormat_month_year.format(holder.dateSource));


        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.d("CEK_APPROVE_HEAD_APPRO", "onBindViewHolder: " + Object.getApprove_head());

        if (Object.getApprove_head().equals("null")) {
            holder.imStatus.setImageResource(R.drawable.ic_dot_point_abu_abu);
        } else if (Object.getApprove_head().equals("0")) {
            holder.imStatus.setImageResource(R.drawable.ic_dot_red);
        }
        else if ((Object.getApprove_head().equals("1")&&Object.getApprove_hrd().equals("null"))||(Object.getApprove_head().equals("1")&&Object.getApprove_hrd().equals("1"))) {
            holder.imStatus.setImageResource(R.drawable.ic_dot_sukses);
        } else if (Object.getApprove_hrd().equals("0")) {
            holder.imStatus.setImageResource(R.drawable.ic_dot_oranye);
        }

        holder.line1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(mCtx, detailIzinSakitApprove.class);
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

    public class RecylerViewHolder extends RecyclerView.ViewHolder {
        private TextView tvNama;
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
        private String kodeStatus = "";
        private String statusApprove = "";


        public RecylerViewHolder(View itemView) {
            super(itemView);
            tvAlasan = itemView.findViewById(R.id.tvAlasan);
            line1 = itemView.findViewById(R.id.line1);
            tx_tanggal = itemView.findViewById(R.id.tx_tanggal);
            tx_bulan_tahun = itemView.findViewById(R.id.tx_bulan_tahun);
            card_list_riwayat_izin = itemView.findViewById(R.id.card_list_riwayat_izin);
            imStatus = itemView.findViewById(R.id.imStatus);
            tvNama=itemView.findViewById(R.id.tvNama);
            dateFormatSources = new SimpleDateFormat("yyyy-MM-dd");
            dateFormat_day = new SimpleDateFormat("dd");
            dateFormat_month_year = new SimpleDateFormat("MMM-yyyy");


        }


    }
}
