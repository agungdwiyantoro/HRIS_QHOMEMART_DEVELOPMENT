package com.app.mobiledev.apphris.newIzin.izinSakit;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.mobiledev.apphris.R;

import java.util.List;

public class adapterApproveIzinSakit extends RecyclerView.Adapter<adapterApproveIzinSakit.ReyclerViewHolder> {
    private Context mCtx;
    private List<modelapproveIzinSakit> modelApproveIzinSakits;

    public adapterApproveIzinSakit(List<modelapproveIzinSakit> modelApproveIzinSakit , Context ctx){
        this.mCtx=ctx;
        this.modelApproveIzinSakits=modelApproveIzinSakit;
    }

    @NonNull
    @Override
    public ReyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.list_timeline_izin_status, null);
        return new ReyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReyclerViewHolder holder, int position) {
        final modelapproveIzinSakit Object = modelApproveIzinSakits.get(position);
        holder.text_judul.setText(""+Object.getJudul());
        if(Object.getJudul().equals("Dalam Proses")){}
        holder.text_alasan.setText(""+Object.getKeterangan());

    }

    @Override
    public int getItemCount() {
        return modelApproveIzinSakits.size();
    }


    public class ReyclerViewHolder extends RecyclerView.ViewHolder  {
        private TextView text_judul;
        private TextView text_alasan;

        public ReyclerViewHolder(View itemView) {
            super(itemView);
            text_judul=itemView.findViewById(R.id.text_judul);
            text_alasan=itemView.findViewById(R.id.text_alasan);

        }


    }
}
