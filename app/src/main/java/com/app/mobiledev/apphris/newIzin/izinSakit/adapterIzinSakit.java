package com.app.mobiledev.apphris.newIzin.izinSakit;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.mobiledev.apphris.R;

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
        holder.tvKet.setText(""+Object.getKeterangan());
        holder.tvAlasan.setText(""+Object.getalasan());
    }



    @Override
    public int getItemCount() {
        return modelIzinSakit.size();
    }

    public class ReyclerViewHolder extends RecyclerView.ViewHolder  {
        private TextView tvKet;
        private TextView tvAlasan;

        public ReyclerViewHolder(View itemView) {
            super(itemView);
            tvKet=itemView.findViewById(R.id.tvKet);
            tvAlasan=itemView.findViewById(R.id.tvAlasan);

        }


    }
}
