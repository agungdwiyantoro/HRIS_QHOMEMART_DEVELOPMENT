package com.app.mobiledev.apphris.approve.approveSakitNew;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.mobiledev.apphris.Model.modelIzinSakitNew;
import com.app.mobiledev.apphris.R;
import com.app.mobiledev.apphris.helperPackage.BaseViewHolder;
import com.app.mobiledev.apphris.sesion.SessionManager;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

public class adapterIzinSakitApprove extends RecyclerView.Adapter<BaseViewHolder> {
    private static final int VIEW_TYPE_LOADING = 0;
    private static final int VIEW_TYPE_NORMAL = 1;

    private boolean isLoaderVisible = false;

    private Context mCtx;
    private final List<modelIzinSakitNew> modelIzinSakitNews;
    private String access, status;

    public adapterIzinSakitApprove(Context mCtx, List<modelIzinSakitNew> modelIzinSakitNews, String access, String status) {
        this.mCtx = mCtx;
        this.modelIzinSakitNews = modelIzinSakitNews;
        this.access = access;
        this.status = status;
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @NonNull
    @Override
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
        Log.d("cek_position", "onBind: " + position);
    }

    @Override
    public int getItemViewType(int position) {
        if (isLoaderVisible) {
            return position == modelIzinSakitNews.size() ? VIEW_TYPE_LOADING : VIEW_TYPE_NORMAL;
        } else {
            return VIEW_TYPE_NORMAL;
        }
    }

    @Override
    public int getItemCount() {
        return modelIzinSakitNews == null ? 0 : modelIzinSakitNews.size();
    }

    public void addItems(List<modelIzinSakitNew> modelIzinsakit) {
        modelIzinSakitNews.addAll(modelIzinsakit);
        Log.d("add_items_all", "addItems: " + modelIzinSakitNews.size());
        notifyDataSetChanged();
    }

    /*public void addLoading() {
        isLoaderVisible = true;
        modelIzinSakitNews.add(new modelIzinSakitNew());
        notifyItemInserted(modelIzinSakitNews.size() - 1);
    }*/

    public void removeLoading() {
        try {
            isLoaderVisible = false;
            int position = modelIzinSakitNews.size();
            Log.d("ADAPTER_POSITION_IZIN", "removeLoading: " + position);
            modelIzinSakitNew item = getItem(position);
            if (item != null) {
                modelIzinSakitNews.remove(position);
                notifyItemRemoved(position);
            }

        } catch (IndexOutOfBoundsException e) {
            Log.d("adapter", "removeLoading: " + e);
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
        private TextView tvKetEmp, tvNamaEmp, tvDivisiEmp;
        private LinearLayout line1;
        private TextView tx_tanggal, tx_bulan_tahun, tvStatusIzin;
        private SimpleDateFormat dateFormatSources, dateFormat_day, dateFormat_month_year;
        private Date dateSource;
        private ImageView ivStatus;
        private CardView card_list_riwayat_izin;
        private String approve_head, approve_exec, approve_dir, approve_hrd, divisi;


        @RequiresApi(api = Build.VERSION_CODES.N)
        ViewHolder(View itemView) {
            super(itemView);

            SessionManager sessionManager = new SessionManager(mCtx);

            divisi = sessionManager.getDivisi();

            tvNamaEmp = itemView.findViewById(R.id.tvNamaEmp);
            tvDivisiEmp = itemView.findViewById(R.id.tvDivisiEmp);
            tvKetEmp = itemView.findViewById(R.id.tvKetEmp);
            line1 = itemView.findViewById(R.id.line1Emp);
            tx_tanggal = itemView.findViewById(R.id.tx_tanggalEmp);
            tx_bulan_tahun = itemView.findViewById(R.id.tx_bulan_tahunEmp);
            card_list_riwayat_izin = itemView.findViewById(R.id.card_list_riwayat_izinEmp);
            ivStatus = itemView.findViewById(R.id.ivStatusIzin);
            tvStatusIzin = itemView.findViewById(R.id.tvStatusIzin);

            dateFormatSources = new SimpleDateFormat("yyyy-MM-dd");
            dateFormat_day = new SimpleDateFormat("dd");
            dateFormat_month_year = new SimpleDateFormat("MMM yyyy");
        }

        protected void clear() {

        }

        @SuppressLint("SetTextI18n")
        @RequiresApi(api = Build.VERSION_CODES.N)
        public void onBind(int position) {
            super.onBind(position);
            modelIzinSakitNew Object = modelIzinSakitNews.get(position);

            tvNamaEmp.setText("" + Object.getName());
            tvDivisiEmp.setText(""+Object.getDvnama());
            tvKetEmp.setText("" + Object.getIndikasiSakit());

            approve_head = Object.getApproveHead();
            approve_exec = Object.getApproveExecutiv();
            approve_dir = Object.getApproveDirectur();
            approve_hrd = Object.getApproveHrd();

            Log.d("TAG_STATUS_", "onBind: "+ Object.getStatus() +" | "+status);

            /*if (access.equals("EXECUTIVE") || access.equals("DIRECTUR") || access.equals("HRD")) {
                tvDivisiEmp.setVisibility(View.VISIBLE);
                tvDivisiEmp.setText("" + divisi);
            }*/

            if (status.equals("2") && Object.getStatus().equals("ON PROGRESS")) {
                tvStatusIzin.setTextColor(ContextCompat.getColor(mCtx, R.color.main_blue_color));
                ivStatus.setImageResource(R.drawable.ic_circle_blue_48);
                //tvStatusIzin.setText("Proses");

                switch (access) {
                    case "HEAD" :
                        Log.d("TAG_HEAD", "onResponse: "+access);
                        if (approve_head.equals("1") && approve_exec.equals("null")) {
                            tvStatusIzin.setText("Menunggu ACC Eksekutif");
                        } else if (approve_exec.equals("1") && approve_dir.equals("null")) {
                            tvStatusIzin.setText("Menunggu ACC Direktur");
                        }else if (approve_exec.equals("1") && approve_dir.equals("1")) {
                            tvStatusIzin.setText("Menunggu ACC HRD");
                        } else if (approve_dir.equals("1") && approve_hrd.equals("null")) {
                            tvStatusIzin.setText("Menunggu ACC HRD");
                        }
                        break;
                    case "EXECUTIV" :
                        Log.d("TAG_EXEC", "onResponse: "+access);
                        if (approve_head.equals("1") && approve_exec.equals("null")) {
                            tvStatusIzin.setText("Menunggu ACC Eksekutif");
                        } else if (approve_exec.equals("1") && approve_dir.equals("null")) {
                            tvStatusIzin.setText("Menunggu ACC Direktur");
                        }else if (approve_exec.equals("1") && approve_dir.equals("1")) {
                            tvStatusIzin.setText("Menunggu ACC HRD");
                        } else if (approve_dir.equals("1") && approve_hrd.equals("null")) {
                            tvStatusIzin.setText("Menunggu ACC HRD");
                        }
                        break;
                    case "DIRECTUR" :
                        Log.d("TAG_DIR", "onResponse: "+access);
                        if (approve_head.equals("1") && approve_exec.equals("null")) {
                            tvStatusIzin.setText("Menunggu ACC Eksekutif");
                        } else if (approve_exec.equals("1") && approve_dir.equals("null")) {
                            tvStatusIzin.setText("Menunggu ACC Direktur");
                        }else if (approve_exec.equals("1") && approve_dir.equals("1")) {
                            tvStatusIzin.setText("Menunggu ACC HRD");
                        } else if (approve_dir.equals("1") && approve_hrd.equals("null")) {
                            tvStatusIzin.setText("Menunggu ACC HRD");
                        }
                        break;
                    case "HRD" :
                        Log.d("TAG_HRD", "onResponse: "+access);
                        if (approve_hrd.equals("null")) {
                            tvStatusIzin.setText("Menunggu ACC HRD");
                        }
                        break;
                    default:
                }

                /*if (approve_head.equals("1") && approve_exec.equals("")) {
                    tvStatusIzin.setText("Menunggu ACC Executive");
                } */

            } else if (Object.getStatus().equals("ON PROGRESS")) {
                tvStatusIzin.setTextColor(ContextCompat.getColor(mCtx, R.color.second_color_black));
                ivStatus.setImageResource(R.drawable.ic_circle_grey_48);
                //tvStatusIzin.setText("Menunggu");
            } else if (Object.getStatus().equals("SELESAI")) {
                tvStatusIzin.setTextColor(ContextCompat.getColor(mCtx, R.color.greennew));
                ivStatus.setImageResource(R.drawable.ic_circle_green_48);
                tvStatusIzin.setText("Diterima");
            } else {
                tvStatusIzin.setTextColor(ContextCompat.getColor(mCtx, R.color.red_btn_bg_pressed_color));
                ivStatus.setImageResource(R.drawable.ic_circle_red_48);
                tvStatusIzin.setText("Ditolak");
            }

            try {
                dateSource = dateFormatSources.parse(Object.getMulaiSakitTanggal());
                tx_tanggal.setText(dateFormat_day.format(dateSource));
                tx_bulan_tahun.setText(dateFormat_month_year.format(dateSource));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Log.d("CEK_ADAPTER", "onBind: " + Object.getName());

            card_list_riwayat_izin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(mCtx, DetailIzinSakitApprove.class);
                    Bundle x = new Bundle();
                    x.putString("id", Object.getId());
                    x.putString("access", access);
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
