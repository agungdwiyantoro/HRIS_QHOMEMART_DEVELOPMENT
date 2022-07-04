package com.app.mobiledev.apphris.bonus.bonus_promosi_iklan;

import android.app.AlertDialog;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.mobiledev.apphris.R;
import com.app.mobiledev.apphris.api.api;
import com.app.mobiledev.apphris.api.set_ip;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class adapterBonus extends RecyclerView.Adapter<adapterBonus.ReyclerViewHolder> {
    private Context mCtx;
    private List<model_bonus> modelBonus;
    private AlertDialog.Builder dialog;
    private  LayoutInflater inflater;
    private View dialogView;


    public adapterBonus(List<model_bonus> modelbonus , Context ctx){
        this.mCtx=ctx;
        this.modelBonus=modelbonus;
    }
    @NonNull
    @Override
    public adapterBonus.ReyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.list_bonusan, null);
        return new ReyclerViewHolder(view);
    }
    public void removeItem(int position) {
        modelBonus.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void onBindViewHolder(@NonNull final adapterBonus.ReyclerViewHolder holder, int position) {
        final model_bonus Object = modelBonus.get(position);
        //set data
        holder.tvKet.setText(Object.getKet());
        holder.tvTgl.setText(Object.getTgl().replace("-","/"));
        holder.url_image=Object.getImage();
        holder.status=Object.getStatus();
        holder.id_hastag=Object.getId_hastag();
        if(holder.status.equals("Y")){
            holder.imStatus.setImageResource(R.drawable.ic_dot_sukses);

        }else{
            holder.imStatus.setImageResource(R.drawable.ic_dot_red);
        }
        holder.cvList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.DialogForm(holder.url_image,Object.getTgl().replace("-","/"),Object.getKet());
            }
        });
    }

    @Override
    public int getItemCount() {
        return modelBonus.size();
    }


    public class ReyclerViewHolder extends RecyclerView.ViewHolder  {
        TextView tvKet,tvTgl;
        String url_image,id_hastag,status;
        ImageView imStatus;
        CardView cvList;

        set_ip ip = new set_ip();



        public ReyclerViewHolder(View itemView) {
            super(itemView);
            tvKet=itemView.findViewById(R.id.tvKet);
            tvTgl=itemView.findViewById(R.id.tvTgl);
            imStatus=itemView.findViewById(R.id.imStatus);
            dialog = new AlertDialog.Builder(mCtx);
            cvList = itemView.findViewById(R.id.cvList);




        }

        public void DialogForm(String url,String tgl, String informasi) {
            try {
                inflater = (LayoutInflater) mCtx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);;
                dialogView = inflater.inflate(R.layout.dialog_detail_bonus, null);
                ImageView image =  dialogView.findViewById(R.id.image);
                TextView txTgl =  dialogView.findViewById(R.id.txTgl);
                ProgressBar progressWheel=dialogView.findViewById(R.id.progressWheel);
                TextView txInformasi =  dialogView.findViewById(R.id.txInformasi);
                Button btn=  dialogView.findViewById(R.id.btn);
                Log.d("url_photo", "DialogForm: "+api.URL_foto+""+url);
                txTgl.setText(tgl);
                txInformasi.setText(informasi);
                RequestOptions requestOptions = new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true);
                Glide.with(mCtx).load(api.URL_foto+""+url).thumbnail(Glide.with(mCtx).load(R.drawable.loading)).apply(requestOptions).into(image);
                dialog.setView(dialogView);
                dialog.setCancelable(true);
                dialog.show();
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });

            }catch (NullPointerException e){
                Log.d("NULLPointer", "DialogForm: "+e);
            }

        }


    }





}
