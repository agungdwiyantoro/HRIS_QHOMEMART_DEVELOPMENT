package com.app.mobiledev.apphris.profile.PerjanjianKerja;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.mobiledev.apphris.Model.modelPerjanjianKerja;
import com.app.mobiledev.apphris.R;

import java.util.List;

public class adapterPerjanjianKerja extends RecyclerView.Adapter<adapterPerjanjianKerja.HolderPK> {

    List<modelPerjanjianKerja> modelPerjanjianKerjas;
    Context context;

    public adapterPerjanjianKerja(List<modelPerjanjianKerja> modelPerjanjianKerjas, Context context) {
        this.modelPerjanjianKerjas = modelPerjanjianKerjas;
        this.context = context;
    }

    @NonNull
    @Override
    public adapterPerjanjianKerja.HolderPK onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view;
        view = inflater.inflate(R.layout.list_kontrak, viewGroup, false);
        return new HolderPK(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final HolderPK holderPK, int i) {
        modelPerjanjianKerja kerja = modelPerjanjianKerjas.get(i);
        holderPK.tvEmail.setText("Perjanjian Kerja "+i);
        holderPK.tvPhone.setText(kerja.getPhone());

    }

    @Override
    public int getItemCount() {
        return modelPerjanjianKerjas.size();
    }

    public static class HolderPK extends RecyclerView.ViewHolder {

        TextView tvUsername, tvEmail, tvPhone;

        public HolderPK(View view) {
            super(view);

            tvEmail = view.findViewById(R.id.tvJudul);
            tvPhone = view.findViewById(R.id.tvTgl);

        }
    }
}
