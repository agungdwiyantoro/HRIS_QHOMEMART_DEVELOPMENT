package com.app.mobiledev.apphris.memo.peserta;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.mobiledev.apphris.R;

import java.util.List;

public class adapterPesertaMemo extends RecyclerView.Adapter<adapterPesertaMemo.HolderPeserta> {
    List<modelPesertaMemo> modelPsertas;
    Context context;
    public adapterPesertaMemo(List<modelPesertaMemo> modelPeserta, Context context) {
        this.modelPsertas = modelPeserta;
        this.context = context;
    }

    @NonNull
    @Override
    public adapterPesertaMemo.HolderPeserta onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View row;
        row = inflater.inflate(R.layout.list_karyawan, viewGroup, false);
        return new HolderPeserta(row);
    }

    @Override
    public void onBindViewHolder(@NonNull adapterPesertaMemo.HolderPeserta holder, int position) {
        modelPesertaMemo memo = modelPsertas.get(position);
        holder.tvKyano.setText(""+memo.getKyano());
        holder.tvNama.setText(""+memo.getPeserta());

    }

    @Override
    public int getItemCount() {return modelPsertas.size();
    }

    public class HolderPeserta extends RecyclerView.ViewHolder {

        TextView tvKyano, tvNama;
        String no_key_memo,jns,no_memo,tgl,hal,kpd,dari,isi,wkt,tempat;


        public HolderPeserta(@NonNull View itemView) {
            super(itemView);
            tvKyano = itemView.findViewById(R.id.tvKyano);
            tvNama   = itemView.findViewById(R.id.tvNama);
        }
    }
}
