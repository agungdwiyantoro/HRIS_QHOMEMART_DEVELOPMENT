package com.app.mobiledev.apphris.kasbon;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.mobiledev.apphris.R;

import java.util.List;

public class adapterKasbonMingguan  extends RecyclerView.Adapter<adapterKasbonMingguan.ReyclerViewHolder>  {

    private Context mCtx;
    private List<model_kasbonMingguan> modelKasbons;


    public adapterKasbonMingguan(List<model_kasbonMingguan> modelKasbon , Context ctx){
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
        final model_kasbonMingguan Object = modelKasbons.get(position);
        //set data
        holder.tvNominal.setText(Object.getKbmNominal());
        holder.tvAlasan.setText("Alasan :  "+Object.getKbmAlasan());
        holder.tvTgl.setText(Object.getKbmPeriode());
        holder.lin1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        if(Object.getKbmStatus().equals("O")||Object.getKbmStatus().equals("N")){
            holder.imStatus.setImageResource(R.drawable.ic_dot_status);

        }else{

            holder.imStatus.setImageResource(R.drawable.ic_dot_sukses);
        }

    }

    @Override
    public int getItemCount() {
        return modelKasbons.size();
    }

    public class ReyclerViewHolder extends RecyclerView.ViewHolder  {
        TextView tvNominal,tvTgl,tvAlasan;
        LinearLayout lin1;
        ImageButton imStatus;
        public ReyclerViewHolder(View itemView) {
            super(itemView);

            tvNominal=itemView.findViewById(R.id.tvNominal);
            tvTgl=itemView.findViewById(R.id.tvTgl);
            tvAlasan=itemView.findViewById(R.id.tvAlasan);
            lin1=itemView.findViewById(R.id.line1);
            imStatus=itemView.findViewById(R.id.imStatus);


        }


    }



}
