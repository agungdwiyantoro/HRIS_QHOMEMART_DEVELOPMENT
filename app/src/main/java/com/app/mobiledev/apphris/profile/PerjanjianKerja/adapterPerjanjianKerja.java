package com.app.mobiledev.apphris.profile.PerjanjianKerja;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
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
        holderPK.tvJudul.setText(kerja.getMkano());
        holderPK.tvTglDari.setText(kerja.getMktglDari());

        holderPK.cvListPK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), DetPerjanjianKerja.class);
                Bundle x = new Bundle();
                x.putString("file", kerja.getFile());
                intent.putExtras(x);
                v.getContext().startActivity(intent);

                Log.d("TAG_FILE_KONTRAK", "onClick: "+kerja.getFile());
                //  helper2.showMsg(v.getContext(),"");
            }
        });

    }

    @Override
    public int getItemCount() {
        return modelPerjanjianKerjas.size();
    }

    public static class HolderPK extends RecyclerView.ViewHolder {

        TextView tvJudul, tvTglDari;
        CardView cvListPK;

        public HolderPK(View view) {
            super(view);

            tvJudul = view.findViewById(R.id.tvJudul);
            tvTglDari = view.findViewById(R.id.tvTglDari);
            cvListPK = view.findViewById(R.id.cvListPK);

        }
    }
}
