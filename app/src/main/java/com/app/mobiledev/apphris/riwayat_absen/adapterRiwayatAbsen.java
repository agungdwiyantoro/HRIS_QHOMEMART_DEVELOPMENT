package com.app.mobiledev.apphris.riwayat_absen;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.mobiledev.apphris.R;
import com.app.mobiledev.apphris.helperPackage.helper;

import java.util.List;

public class adapterRiwayatAbsen extends RecyclerView.Adapter<adapterRiwayatAbsen.ReyclerViewHolder> {
    private Context mCtx;
    private List<modelRiwayatAbsen> modelRiwayatAbsens;


    public adapterRiwayatAbsen(List<modelRiwayatAbsen> modelRiwayatAbsen , Context ctx){
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
        holder.tx_presensi.setText(Object.getFINGER());

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






        Log.d("MULAI_ISTIRAHAT", "onResponse: "+Object.getMulai_istirahat());




    }
    @Override
    public int getItemCount() {
        return modelRiwayatAbsens.size();
    }

    public class ReyclerViewHolder extends RecyclerView.ViewHolder  {
        private TextView abs_masuk,abs_pulang,abs_istirahat,abs_selesai_istirahat,abs_lembur,abs_selesai_lembur,tgl,tx_presensi;
        private String setJam,absmasuk,absPulang,absIstirhat,absIstSelesai,absLembur,absSelesaiLembur,mTgl;



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






        }


    }
}