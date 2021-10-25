package com.app.mobiledev.apphris.slipGaji;

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
import android.widget.TextView;

import com.app.mobiledev.apphris.R;
import com.app.mobiledev.apphris.memo.DetailMemo;
import com.app.mobiledev.apphris.riwayat_cuti.adapterCuti;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class adapterSlipGaji extends RecyclerView.Adapter<adapterSlipGaji.ReyclerViewHolder>{
    private Context mCtx;
    private List<modelSlipGaji> modelSlipGajis;


    public adapterSlipGaji(List<modelSlipGaji> modelslip,Context ctx){
        this.mCtx=ctx;
        this.modelSlipGajis=modelslip;
    }


    @NonNull
    @Override
    public ReyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.list_slip_gaji, null);
        return new ReyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReyclerViewHolder holder, int position) {
        final modelSlipGaji object=modelSlipGajis.get(position);
        holder.tvJudul.setText(object.getBulan()+"-"+object.getTahun());
        Calendar cal = Calendar.getInstance();
        String bulan=new SimpleDateFormat("MMMM").format(cal.getTime());
        Log.d("LIST_BULAN_CONVERT", "onBindViewHolder: "+bulan);
        Log.d("LIST_BULAN_DATABASE", "onBindViewHolder: "+object.getBulan());



        if(object.getBulan().toLowerCase(Locale.ROOT).equals(bulan.toLowerCase(Locale.ROOT))){
            holder.txNew.setVisibility(View.VISIBLE);
        }
        holder.cvSlipGaji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), detailSlipGaji.class);
                Bundle x = new Bundle();
                x.putString("bulan", object.getBulan());
                x.putString("tahun", object.getTahun());
                intent.putExtras(x);
                view.getContext().startActivity(intent);
            }
        });

//        if(object.getStatus_sent().equals("0")){
//            holder.cvSlipGaji.setVisibility(View.GONE);
//        }

    }

    @Override
    public int getItemCount() {
        return modelSlipGajis.size();
    }

    public class ReyclerViewHolder extends RecyclerView.ViewHolder {
         TextView tvJudul,txNew;
         CardView cvSlipGaji;

        public ReyclerViewHolder(View itemView) {
            super(itemView);
            tvJudul=itemView.findViewById(R.id.tvJudul);
            txNew=itemView.findViewById(R.id.txNew);
            cvSlipGaji=itemView.findViewById(R.id.cvSlipGaji);
        }

    }

}
