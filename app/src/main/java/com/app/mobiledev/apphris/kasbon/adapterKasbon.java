package com.app.mobiledev.apphris.kasbon;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.mobiledev.apphris.R;

import java.util.List;

public class adapterKasbon  extends RecyclerView.Adapter<adapterKasbon.ReyclerViewHolder> {
    private Context mCtx;
    private List<modelKasbon> modelKasbons;

    public adapterKasbon(List<modelKasbon> modelKasbon , Context ctx){
        this.mCtx=ctx;
        this.modelKasbons=modelKasbon;
    }



    @NonNull
    @Override
    public ReyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.list_kasbon, null);
        return new ReyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReyclerViewHolder holder, int position) {
        final modelKasbon Object = modelKasbons.get(position);
        //set data
        holder.nominal.setText(Object.getByrrp());
        holder.tgl.setText(Object.getByrtgl());
        Log.d("NOMINAL", "onBindViewHolder: "+Object.getByrrp());
    }

    @Override
    public int getItemCount() {
        return modelKasbons.size();
    }

    public class ReyclerViewHolder extends RecyclerView.ViewHolder  {
        TextView noBayar,nominal,tgl;



        public ReyclerViewHolder(View itemView) {
            super(itemView);
            nominal=itemView.findViewById(R.id.tvNominal);
            tgl=itemView.findViewById(R.id.tvTgl);






        }


    }
}
