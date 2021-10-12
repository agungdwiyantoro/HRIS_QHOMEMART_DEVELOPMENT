package com.app.mobiledev.apphris.bonus.bonus_promosi_proyek;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.app.mobiledev.apphris.R;
import com.app.mobiledev.apphris.api.api;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class adapter_list_bonus_proyek extends RecyclerView.Adapter<adapter_list_bonus_proyek.ReyclerViewHolder> {
    private Context mCtx;
    private List<model_bonus_proyek> model_bonus_proyeks;


    public adapter_list_bonus_proyek(List<model_bonus_proyek> model_bonus_proyek , Context ctx){
        this.mCtx=ctx;
        this.model_bonus_proyeks=model_bonus_proyek;
    }

    public void removeItem(int position) {
        try {
            model_bonus_proyeks.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position,model_bonus_proyeks.size());

        }catch (IndexOutOfBoundsException e){
            Log.d("index_out_of_bound", "removeItem: "+e);
        }

    }



    @NonNull
    @Override
    public ReyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.list_bonus_proyek, null);
        return new ReyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ReyclerViewHolder holder, final int position) {
        final model_bonus_proyek Object = model_bonus_proyeks.get(position);
        holder.tvWaktu.setText(""+Object.getTgl());
        holder.tvCust.setText(""+Object.getCust());
        holder.tvNamaProyek.setText(""+Object.getNama_proyek());
        holder.kjnkd=Object.getKode_kunjungan();
        holder.jenis_cust=Object.getJenis();
        holder.status=Object.getStatus();
        holder.tvStatusProyek.setText("status proyek: "+Object.getStatus_proyek());

        Log.d("KODE_KUNJUNGAN", "onBindViewHolder: "+holder.kjnkd);
        holder.txhapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeItem(holder.getAdapterPosition());
                delete_trkunjungan(holder.kjnkd);

            }
        });

        holder.txedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mCtx, edit_form_bonus_proyek.class);
                intent.putExtra("kjnkd",Object.getKode_kunjungan());
                intent.putExtra("jenis_proyek",Object.getJenis());
                intent.putExtra("status",Object.getStatus());
                intent.putExtra("status_proyek",Object.getStatus_proyek());
                v.getContext().startActivity(intent);
                ((AppCompatActivity)mCtx).finish();
            }
        });

        holder.btn_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mCtx, form_bonus_proyek.class);
                intent.putExtra("kjnkd",Object.getKode_kunjungan());
                intent.putExtra("jenis_proyek",Object.getJenis());
                intent.putExtra("status",Object.getStatus());
                intent.putExtra("status_proyek",Object.getStatus_proyek());
                v.getContext().startActivity(intent);
                ((AppCompatActivity)mCtx).finish();
            }
        });

    }

    @Override
    public int getItemCount() {
        return model_bonus_proyeks.size();
    }

    public class ReyclerViewHolder extends RecyclerView.ViewHolder {
        private TextView tvCust,tvNamaProyek,tvWaktu,tvStatusProyek;
        private String kjnkd="",jenis_cust="",status="",status_bayar="",status_proyek="";
        private TextView txhapus,txedit,btn_view;
        private CardView cardListProyek;


        public ReyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCust=itemView.findViewById(R.id.tvCust);
            tvNamaProyek=itemView.findViewById(R.id.tvNamaProyek);
            tvWaktu=itemView.findViewById(R.id.tvWaktu);
            txhapus=itemView.findViewById(R.id.btnhapus);
            txedit=itemView.findViewById(R.id.btn_update);
            cardListProyek=itemView.findViewById(R.id.cardListProyek);
            tvStatusProyek=itemView.findViewById(R.id.tvStatusProyek);
            btn_view=itemView.findViewById(R.id.btn_view);
            txedit.setVisibility(View.VISIBLE);
        }
    }
    private void delete_trkunjungan(final String kjnkd){
        AndroidNetworking.post(api.URL_delete_trkunjungan)
                .addBodyParameter("key", api.key)
                .addBodyParameter("kjnkd", kjnkd)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        model_bonus_proyeks.clear();
                        try {
                            Boolean success = response.getBoolean("success");
                            String ket=response.getString("ket");
                            if (success) {
                                Log.d("respond_cek", ""+ket);
                            } else{
                                Log.d("respond_cek", ""+ket);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("JSONEROR_HAPUS", "onResponse: "+e);
                        }catch (NullPointerException e){
                            Log.d("JSONEROR_HAPUS", "onResponse: "+e);
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        Log.d("EROOR_EXCEPTION_hapus", "onError: "+anError);
                    }
                });

    }

}
