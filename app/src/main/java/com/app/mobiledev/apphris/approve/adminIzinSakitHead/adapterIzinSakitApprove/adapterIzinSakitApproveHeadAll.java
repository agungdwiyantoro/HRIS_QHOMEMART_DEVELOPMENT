package com.app.mobiledev.apphris.approve.adminIzinSakitHead.adapterIzinSakitApprove;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.app.mobiledev.apphris.R;
import com.app.mobiledev.apphris.approve.adminIzinSakitHead.detailIzinSakitApproveHead;
import com.app.mobiledev.apphris.helperPackage.BaseViewHolder;
import com.app.mobiledev.apphris.izin.izinSakit.modelIzinSakit;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;



public class adapterIzinSakitApproveHeadAll extends RecyclerView.Adapter<BaseViewHolder> {
    private static final int VIEW_TYPE_LOADING = 0;
    private static final int VIEW_TYPE_NORMAL = 1;
    private Context mCtx;
    private boolean isLoaderVisible = false;

    private List<modelIzinSakit> modelIzinSakits;

    public adapterIzinSakitApproveHeadAll(List<modelIzinSakit> modelIzinSakit, Context ctx) {
        this.mCtx=ctx;
        this.modelIzinSakits = modelIzinSakit;
    }

    @NonNull @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                return new ViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.list_riwayat_izin_sakit_approve_head, parent, false));
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
            return position == modelIzinSakits.size() - 1 ? VIEW_TYPE_LOADING : VIEW_TYPE_NORMAL;
        } else {
            return VIEW_TYPE_NORMAL;
        }
    }

    @Override
    public int getItemCount() {
        return modelIzinSakits == null ? 0 : modelIzinSakits.size();
    }

    public void addItems(List<modelIzinSakit> modelIzinsakit) {
        modelIzinSakits.addAll(modelIzinsakit);
        notifyDataSetChanged();
    }

    public void addLoading() {
        isLoaderVisible = true;
        modelIzinSakits.add(new modelIzinSakit());
        notifyItemInserted(modelIzinSakits.size() - 1);
    }

    public void removeLoading() {
        try{
            isLoaderVisible = false;
            int position = modelIzinSakits.size() - 1;
            Log.d("ADAPTER_POSITION_IZIN", "removeLoading: "+position);
            modelIzinSakit item = getItem(position);
            if (item != null) {
                modelIzinSakits.remove(position);
                notifyItemRemoved(position);
            }

        }catch (IndexOutOfBoundsException e){
            Log.d("adapter", "removeLoading: "+e);
        }

    }

    public void clear() {
        modelIzinSakits.clear();
        notifyDataSetChanged();
    }

    modelIzinSakit getItem(int position) {
        return modelIzinSakits.get(position);
    }

    public class ViewHolder extends BaseViewHolder {
        private TextView tvNama;
        private TextView tvAlasan;
        private LinearLayout line1;
        private TextView tx_tanggal;
        private TextView tx_bulan_tahun;
        private SimpleDateFormat dateFormatSources;
        private SimpleDateFormat dateFormat_day;
        private SimpleDateFormat dateFormat_month_year;
        private Date dateSource;
        private ImageView imStatus;
        private CardView card_list_riwayat_izin;


        ViewHolder(View itemView) {
            super(itemView);
            tvAlasan = itemView.findViewById(R.id.tvAlasan);
            line1 = itemView.findViewById(R.id.line1);
            tx_tanggal = itemView.findViewById(R.id.tx_tanggal);
            tx_bulan_tahun = itemView.findViewById(R.id.tx_bulan_tahun);
            card_list_riwayat_izin = itemView.findViewById(R.id.card_list_riwayat_izin);
            imStatus = itemView.findViewById(R.id.imStatus);
            tvNama=itemView.findViewById(R.id.tvNama);
            dateFormatSources = new SimpleDateFormat("yyyy-MM-dd");
            dateFormat_day = new SimpleDateFormat("dd");
            dateFormat_month_year = new SimpleDateFormat("MMM-yyyy");
        }

        protected void clear() {

        }

        public void onBind(int position) {
            super.onBind(position);
            modelIzinSakit Object = modelIzinSakits.get(position);


            tvAlasan.setText("" + Object.getIndikasi_sakit());
            tvNama.setText(""+Object.getName());
            Log.d("CEK_ADAPTER", "onBind: "+Object.getName());


            try {
                dateSource = dateFormatSources.parse(Object.getCreated_at());
                tx_tanggal.setText(dateFormat_day.format(dateSource));
                tx_bulan_tahun.setText(dateFormat_month_year.format(dateSource));


            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (Object.getApprove_head().equals("null")) {
                imStatus.setImageResource(R.drawable.ic_dot_point_abu_abu);
            } else if (Object.getApprove_head().equals("0")) {
                imStatus.setImageResource(R.drawable.ic_dot_red);
            }
            else if ((Object.getApprove_head().equals("1")&&Object.getApprove_hrd().equals("null"))||(Object.getApprove_head().equals("1")&&Object.getApprove_hrd().equals("1"))) {
                imStatus.setImageResource(R.drawable.ic_dot_sukses);
            } else if (Object.getApprove_hrd().equals("0")) {
                imStatus.setImageResource(R.drawable.ic_dot_oranye);
            }

            line1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(mCtx, detailIzinSakitApproveHead.class);
                    Bundle x = new Bundle();
                    x.putString("id", Object.getId());
                    i.putExtras(x);
                    mCtx.startActivity(i);

                }
            });

        }
    }

    public class ProgressHolder extends BaseViewHolder {
        ProgressHolder(View itemView) {
            super(itemView);

        }

        @Override
        protected void clear() {
        }
    }
}
