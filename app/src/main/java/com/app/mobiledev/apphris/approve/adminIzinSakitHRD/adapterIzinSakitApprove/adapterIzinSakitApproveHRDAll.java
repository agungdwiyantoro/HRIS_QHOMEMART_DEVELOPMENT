package com.app.mobiledev.apphris.approve.adminIzinSakitHRD.adapterIzinSakitApprove;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.app.mobiledev.apphris.R;
import com.app.mobiledev.apphris.helperPackage.BaseViewHolder;
import com.app.mobiledev.apphris.izin.izinSakit.modelIzinSakit;

import java.util.List;

public class adapterIzinSakitApproveHRDAll extends RecyclerView.Adapter<BaseViewHolder> {
    private static final int VIEW_TYPE_LOADING = 0;
    private static final int VIEW_TYPE_NORMAL = 1;
    private Context mCtx;
    private boolean isLoaderVisible = false;


    private List<modelIzinSakit> modelIzinSakits;


    public adapterIzinSakitApproveHRDAll(List<modelIzinSakit> modelIzinSakit,Context ctx){
        this.mCtx=ctx;
        this.modelIzinSakits=modelIzinSakit;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch(viewType){
            case VIEW_TYPE_NORMAL:
                return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_riwayat_izin_sakit_approve_hrd,parent,false));
            case VIEW_TYPE_LOADING:
                return  new ProgressHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading_progress,parent,false));
            default:
                return null;


        }
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.onBind(position);

    }

    @Override
    public int getItemViewType(int position){
        if(isLoaderVisible){
            return position== modelIzinSakits.size()-1?VIEW_TYPE_LOADING:VIEW_TYPE_NORMAL;

        }else{
            return  VIEW_TYPE_NORMAL;
        }
    }

    @Override
    public int getItemCount() {
        return modelIzinSakits==null?0:modelIzinSakits.size();
    }


    public void addItems (List<modelIzinSakit> modelIzinSakit){
        modelIzinSakits.addAll(modelIzinSakit);
        notifyDataSetChanged();
    }

    public void addLoading(){
        isLoaderVisible=true;
        modelIzinSakits.add(new modelIzinSakit());
        notifyItemInserted(modelIzinSakits.size()-1);
    }

    public void removeLoading(){
        try{
            isLoaderVisible=false;
            int position=modelIzinSakits.size()-1;
            modelIzinSakit item=getItem(position);
            if(item!=null){
                modelIzinSakits.remove(position);
                notifyItemRemoved(position);

            }
        }catch (IndexOutOfBoundsException e){
            Log.d("adapter", "removeLoading: "+e);
        }
    }

    public void clear(){
        modelIzinSakits.clear();
        notifyDataSetChanged();
    }

    modelIzinSakit getItem(int position){
        return  modelIzinSakits.get(position);
    }



    public class ViewHolder extends BaseViewHolder{
        private TextView txNama;
        private TextView txNamaHead;
        private TextView txAlasan;
        private TextView txTgl;
        private CardView card_list_riwayat_izin;

        ViewHolder(View itemView){
            super(itemView);
            txNama=itemView.findViewById(R.id.txNama);
            txNamaHead=itemView.findViewById(R.id.txNamaHead);
            txAlasan=itemView.findViewById(R.id.txAlasan);
            txTgl=itemView.findViewById(R.id.txTgl);
            card_list_riwayat_izin=itemView.findViewById(R.id.card_list_riwayat_izin);

        }

        @Override
        protected void clear() {

        }


        public void onBind(int position){
            super.onBind(position);
            modelIzinSakit Object =modelIzinSakits.get(position);
            txNama.setText(Object.getName());
            txNamaHead.setText(Object.getHead_name());
            txAlasan.setText(Object.getIndikasi_sakit());
            card_list_riwayat_izin.setCardBackgroundColor(Color.GREEN);

        }
    }


    public class ProgressHolder extends BaseViewHolder{
        ProgressHolder(View itemView){
            super(itemView);

        }

        @Override
        protected void clear() {

        }
    }
}
