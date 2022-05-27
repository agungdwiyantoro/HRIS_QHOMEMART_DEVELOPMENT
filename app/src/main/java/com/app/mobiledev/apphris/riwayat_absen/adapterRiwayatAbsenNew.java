package com.app.mobiledev.apphris.riwayat_absen;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.mobiledev.apphris.R;
import com.app.mobiledev.apphris.helperPackage.helper;

import java.util.List;

public class adapterRiwayatAbsenNew extends RecyclerView.Adapter<adapterRiwayatAbsenNew.ReyclerViewHolder> {
    private Context mCtx;
    private List<modelRiwayatAbsen> modelRiwayatAbsens;


    public adapterRiwayatAbsenNew(List<modelRiwayatAbsen> modelRiwayatAbsen , Context ctx){
        this.mCtx=ctx;
        this.modelRiwayatAbsens=modelRiwayatAbsen;
    }


    @NonNull
    @Override
    public ReyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_riwayat, null);
        return new ReyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReyclerViewHolder holder, int position) {
        final modelRiwayatAbsen Object = modelRiwayatAbsens.get(position);
        holder.tgl.setText(helper.format_tgl(Object.getTgl(),""));
        holder.absmasuk=Object.getMasuk();
        holder.absPulang=Object.getPulang();
        holder.absIstirhat=Object.getMulai_istirahat();
        holder.absIstSelesai=Object.getSelesai_istirahat();
        holder.absLembur=Object.getMasuk_lembur();
        holder.tx_presensi.setText("Presensi "+Object.getFINGER());

        if (Object.getStatus().equals("DOFF_MT")) { //tidak berangkat tanpa keterangan
            holder.tvStatusPresensi.setText("DAY OFF");
        } else if (Object.getStatus().equals("DOFF_SAKIT")) { //sakit tanpa SKD
            holder.tvStatusPresensi.setText("DAY OFF");
        } else if (Object.getStatus().equals("SKD")) { //sakit dengan SKD
            holder.tvStatusPresensi.setText("SAKIT");
        } else if (Object.getStatus().equals("CUTI")) {
            holder.tvStatusPresensi.setText("CUTI");
        }

        holder.status = Object.getStatus();

        holder.absSelesaiLembur=Object.getKeluar_lembur();

                if(holder.absmasuk.equals("null")){
                    holder.abs_masuk.setText("-----");
                }else{
                    holder.abs_masuk.setText(""+holder.absmasuk);
                }

                if(holder.absPulang.equals("null")){
                    holder.abs_pulang.setText("-----");
                }else{
                    holder.abs_pulang.setText(""+holder.absPulang);
                }


        if(holder.absIstirhat.equals("null")){
            holder.abs_istirahat.setText("------");

        }else{
            holder.abs_istirahat.setText(""+holder.absIstirhat);
        }


        if(holder.absIstSelesai.equals("null")){
            holder.abs_selesai_istirahat.setText("-----");
        }else{
            holder.abs_selesai_istirahat.setText(""+holder.absIstSelesai);
        }

        if(holder.absSelesaiLembur.equals("null")){
            holder.abs_selesai_lembur.setText("-----");

        }else{
            holder.abs_selesai_lembur.setText(""+holder.absSelesaiLembur);
        }

        if(holder.absLembur.equals("null")){
            holder.abs_lembur.setText("-----");
        }else{
            holder.abs_lembur.setText(""+holder.absLembur);
        }

        if (holder.status.equals("status")) {
            holder.llRiwayatPresensi.setVisibility(View.VISIBLE);
            holder.llRiwayatPresensiStatus.setVisibility(View.GONE);
        } else {
            holder.llRiwayatPresensi.setVisibility(View.GONE);
            holder.llRiwayatPresensiStatus.setVisibility(View.VISIBLE);
        }

        Log.d("MULAI_ISTIRAHAT", "onResponse: "+Object.getMulai_istirahat());
        Log.d("STATUS_PRESENSI", "onResponse: "+Object.getFINGER()+Object.getMasuk()+Object.getPulang());

    }
    @Override
    public int getItemCount() {
        return modelRiwayatAbsens.size();
    }

    public class ReyclerViewHolder extends RecyclerView.ViewHolder  {
        private TextView abs_masuk,abs_pulang,abs_istirahat,abs_selesai_istirahat,abs_lembur,abs_selesai_lembur,tgl,tx_presensi, tvStatusPresensi;
        private String setJam,absmasuk,absPulang,absIstirhat,absIstSelesai,absLembur,absSelesaiLembur,mTgl, status;
        private LinearLayout llRiwayatPresensi, llRiwayatPresensiStatus;

        public ReyclerViewHolder(View itemView) {
            super(itemView);
            abs_masuk=itemView.findViewById(R.id.abs_masuk);
            abs_pulang=itemView.findViewById(R.id.abs_pulang);
            abs_istirahat=itemView.findViewById(R.id.abs_istirahat);
            abs_lembur=itemView.findViewById(R.id.abs_lembur);
            abs_selesai_lembur=itemView.findViewById(R.id.abs_selesai_lembur);
            abs_selesai_istirahat=itemView.findViewById(R.id.abs_selesai_istirahat);
            tx_presensi=itemView.findViewById(R.id.tx_presensi);
            tgl=itemView.findViewById(R.id.tgl);

            tvStatusPresensi = itemView.findViewById(R.id.tvStatusPresensi);
            llRiwayatPresensi = itemView.findViewById(R.id.llRiwayatPresensi);
            llRiwayatPresensiStatus = itemView.findViewById(R.id.llRiwayatPresensiStatus);

        }


    }
}
