package com.app.mobiledev.apphris.visitor.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.mobiledev.apphris.R;
import com.app.mobiledev.apphris.visitor.model.modelVisit;

import java.util.List;


public class adapterVisit extends RecyclerView.Adapter<adapterVisit.VisitHolder> {
    //Declare List of Recyclerview Items
    List<modelVisit> modelVisits;
    private static final int FOOD_ITEM = 2;
    Context mContext;

    public adapterVisit(List<modelVisit> modelVisits, Context mContext) {
        this.modelVisits = modelVisits;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public adapterVisit.VisitHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View row;
        row = inflater.inflate(R.layout.list_mobil, parent, false);
        return new VisitHolder(row);
    }


    @Override
    public void onBindViewHolder(@NonNull final adapterVisit.VisitHolder holder, int position) {
        modelVisit Object = modelVisits.get(position);
        //holder.textTitle.setText(Object.getPromo());
        holder.textTanggal.setText(Object.getTanggal());
        holder.textMobil.setText(Object.getMobil());
        holder.textMotor.setText(Object.getMotor());
        holder.textCustomer.setText(Object.getCustomer());
        //holder.textKet.setText(Object.getKet());
    }


    @Override
    public int getItemCount() {
        return modelVisits.size();
    }
    //Food item holder


    public class VisitHolder extends RecyclerView.ViewHolder {
        TextView textKynm, textTanggal, textWaktu, textMobil, textMotor, textCustomer;
        ImageView imageView;
        String url;

        public VisitHolder(View itemView) {
            super(itemView);
            //textHari = itemView.findViewById(R.id.txtHari);
            //imageView = itemView.findViewById(R.id.imageViewFood);

            textTanggal = itemView.findViewById(R.id.txtTanggal);
            textMobil = itemView.findViewById(R.id.txtMobil);
            textMotor = itemView.findViewById(R.id.txtMotor);
            textCustomer = itemView.findViewById(R.id.txtCustomer);
        }
    }
    //header holder
}