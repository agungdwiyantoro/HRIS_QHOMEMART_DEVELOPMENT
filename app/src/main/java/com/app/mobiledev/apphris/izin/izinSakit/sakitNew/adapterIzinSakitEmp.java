package com.app.mobiledev.apphris.izin.izinSakit.sakitNew;

import android.annotation.SuppressLint;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.mobiledev.apphris.R;
import com.app.mobiledev.apphris.helperPackage.BaseViewHolder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class adapterIzinSakitEmp extends RecyclerView.Adapter<BaseViewHolder> {
    private static final int VIEW_TYPE_LOADING = 0;
    private static final int VIEW_TYPE_NORMAL = 1;
    private Context mCtx;
    private boolean isLoaderVisible = false;

    private final List<modelIzinSakitNew> modelIzinSakitNews;

    public adapterIzinSakitEmp(List<modelIzinSakitNew> modelIzinSakitNew, Context ctx) {
        this.mCtx=ctx;
        this.modelIzinSakitNews = modelIzinSakitNew;
    }

    @NonNull @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                return new ViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.list_riwayat_izin_sakit, parent, false));
            case VIEW_TYPE_LOADING:
                return new ProgressHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading_progress, parent, false));

            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.onBind(position);
        Log.d("cek_position", "onBind: "+position);
    }

    @Override
    public int getItemViewType(int position) {
        if (isLoaderVisible) {
            return position == modelIzinSakitNews.size() - 1 ? VIEW_TYPE_LOADING : VIEW_TYPE_NORMAL;
        } else {
            return VIEW_TYPE_NORMAL;
        }
    }

    @Override
    public int getItemCount() {
        return modelIzinSakitNews == null ? 0 : modelIzinSakitNews.size();
    }

    public void addItems(List<modelIzinSakitNew> modelIzinSakitNews) {
        modelIzinSakitNews.addAll(modelIzinSakitNews);
        Log.d("add_items_all", "addItems: "+modelIzinSakitNews.size());
        notifyDataSetChanged();
    }

    public void addLoading() {
        isLoaderVisible = true;
        modelIzinSakitNews.add(new modelIzinSakitNew());
        notifyItemInserted(modelIzinSakitNews.size() - 1);
    }

    public void removeLoading() {
        try{
            isLoaderVisible = false;
            int position = modelIzinSakitNews.size() - 1;
            Log.d("ADAPTER_POSITION_IZIN", "removeLoading: "+position);
            modelIzinSakitNew item = getItem(position);
            if (item != null) {
                modelIzinSakitNews.remove(position);
                notifyItemRemoved(position);
            }

        }catch (IndexOutOfBoundsException e){
            Log.d("adapter", "removeLoading: "+e);
        }

    }

    public void clear() {
        modelIzinSakitNews.clear();
        notifyDataSetChanged();
    }

    modelIzinSakitNew getItem(int position) {
        return modelIzinSakitNews.get(position);
    }

    public class ViewHolder extends BaseViewHolder {
        TextView tvNama;
        TextView tvAlasan;
        LinearLayout line1;
        TextView tx_tanggal;
        TextView tx_bulan_tahun;
        SimpleDateFormat dateFormatSources;
        SimpleDateFormat dateFormat_day;
        SimpleDateFormat dateFormat_month_year;
        Date dateSource;
        ImageView imStatus;
        CardView card_list_riwayat_izin;


        @SuppressLint("SimpleDateFormat")
        ViewHolder(View itemView) {
            super(itemView);
            tvAlasan = itemView.findViewById(R.id.tvAlasan);
            line1 = itemView.findViewById(R.id.line1);
            tx_tanggal = itemView.findViewById(R.id.tx_tanggal);
            tx_bulan_tahun = itemView.findViewById(R.id.tx_bulan_tahun);
            card_list_riwayat_izin = itemView.findViewById(R.id.card_list_riwayat_izin);
            imStatus = itemView.findViewById(R.id.imStatus);
            tvNama=itemView.findViewById(R.id.tvKet);
            dateFormatSources = new SimpleDateFormat("yyyy-MM-dd");
            dateFormat_day = new SimpleDateFormat("dd");
            dateFormat_month_year = new SimpleDateFormat("MMM-yyyy");
        }

        protected void clear() {

        }

        @SuppressLint("SetTextI18n")
        public void onBind(int position) {
            super.onBind(position);
            modelIzinSakitNew Object = modelIzinSakitNews.get(position);


            tvAlasan.setText("" + Object.getCatatan());
            tvNama.setText(""+Object.getName());
            Log.d("CEK_ADAPTER", "onBind: "+Object.getName());


            line1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(mCtx, DetailSakitEmp.class);
                    Bundle x = new Bundle();
                    x.putString("id", Object.getId());
                    i.putExtras(x);
                    mCtx.startActivity(i);

                }
            });

        }
    }

    public static class ProgressHolder extends BaseViewHolder {
        ProgressHolder(View itemView) {
            super(itemView);

        }

        @Override
        protected void clear() {
        }
    }
}
