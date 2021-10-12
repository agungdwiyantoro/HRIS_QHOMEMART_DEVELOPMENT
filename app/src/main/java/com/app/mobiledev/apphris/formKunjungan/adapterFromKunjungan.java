package com.app.mobiledev.apphris.formKunjungan;

import android.app.AlertDialog;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.app.mobiledev.apphris.R;
import com.app.mobiledev.apphris.api.api;
import com.app.mobiledev.apphris.helperPackage.helper;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class adapterFromKunjungan extends RecyclerView.Adapter<adapterFromKunjungan.ReyclerViewHolder> {

    private Context mCtx;
    private List<modelFromKunjungan> modelFromKunjungans;
    private AlertDialog.Builder dialog;
    private  LayoutInflater inflater;
    private View dialogView;
    private String TAG="adapter_promanage";


    public adapterFromKunjungan(List<modelFromKunjungan> modelFromKunjungan, Context ctx){
        this.mCtx=ctx;
        this.modelFromKunjungans = modelFromKunjungan;
    }

    public void removeItem(int position) {
        modelFromKunjungans.remove(position);
        notifyItemRemoved(position);
    }

    @NonNull
    @Override
    public adapterFromKunjungan.ReyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.list_form_kunjungan, null);
        return new ReyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final adapterFromKunjungan.ReyclerViewHolder holder, int position) {
        final modelFromKunjungan Object = modelFromKunjungans.get(position);
        holder.tvNamaProyek.setText(""+Object.getNmProyek());
        holder.tvOwnerProyek.setText(""+Object.getOwnerProyek());
        holder.status=Object.getStatus();

        if(holder.status.equals("0")){
            holder.btnStatus.setText("BLM ACC");
            holder.btnStatus.setBackgroundResource(R.color.btnDanger);

        }else{
            holder.btnStatus.setText("ACC");
            holder.btnStatus.setBackgroundResource(R.color.greennew);

        }

        holder.btnhapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(holder.status.equals("0")){
                        holder.deleteFormKunjungan(Object.getIdForm());
                        removeItem(holder.getAdapterPosition());
                    }else{

                    }

                }catch (Exception e){
                    Log.d("btnHAPUS", "onClick: "+e);
                }
            }
        });


        holder.cardListProyek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    inflater = (LayoutInflater) mCtx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);;
                    dialogView = inflater.inflate(R.layout.dialog_detail_form_kunjungan, null);
                    ImageView image =  dialogView.findViewById(R.id.image);
                    TextView txTgl =  dialogView.findViewById(R.id.txTgl);
                    ProgressBar progressWheel=dialogView.findViewById(R.id.progressWheel);
                    TextView txInformasi =  dialogView.findViewById(R.id.txInformasi);
                    Button btn=  dialogView.findViewById(R.id.btn);
                    Log.d("url_photo", "DialogForm: "+ api.URL_foto_form_kunjungan+""+Object.getImage());
                    txTgl.setText(Object.getTgl());
                    txInformasi.setText(Object.getNmProyek());
                    RequestOptions requestOptions = new RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true);
                    Glide.with(mCtx).load(api.URL_foto_form_kunjungan+""+Object.getImage()).thumbnail(Glide.with(mCtx).load(R.drawable.loading)).apply(requestOptions).into(image);
                    dialog.setView(dialogView);
                    dialog.setCancelable(true);
                    dialog.show();
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if(holder.status.equals("0")){
                                Intent intent = new Intent(mCtx, form_kunjungan.class);
                                intent.putExtra("idform",Object.getIdForm());
                                intent.putExtra("nmproyek",Object.getNmProyek());
                                Log.d("CEK_NAMA_PROYEK", "onClick: "+Object.getNmProyek());
                                intent.putExtra("alamtproyek",Object.getAlamtProyek());
                                intent.putExtra("ownerproyek",Object.getOwnerProyek());
                                intent.putExtra("penanggungjwb",Object.getpJawab());
                                intent.putExtra("notelp",Object.getNoTelp());
                                intent.putExtra("thpproyek",Object.getThpProyek());
                                intent.putExtra("jam",Object.getJam());
                                intent.putExtra("tgl",Object.getTgl());
                                intent.putExtra("lokasi",Object.getLokasi());
                                intent.putExtra("jenis",Object.getJenis());
                                intent.putExtra("kyano",Object.getKyano());
                                intent.putExtra("image",Object.getImage());
                                intent.putExtra("status",Object.getStatus());
                                v.getContext().startActivity(intent);
                                ((AppCompatActivity)mCtx).finish();

                            }else{
                                helper.showMsg(mCtx,"informasi","status acc tidak dapat diperbarui");
                            }


                        }
                    });

                }catch (NullPointerException e){
                    Log.d("NULLPointer", "DialogForm: "+e);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return modelFromKunjungans.size();
    }

    public class ReyclerViewHolder extends RecyclerView.ViewHolder  {
        private TextView tvOwnerProyek;
        private TextView tvNamaProyek;
        private Button btnStatus;
        private String status="";
        private CardView cardListProyek;
        private Button btnhapus;




        public ReyclerViewHolder(View itemView) {
            super(itemView);
            tvOwnerProyek=itemView.findViewById(R.id.tvOwnerProyek);
            tvNamaProyek=itemView.findViewById(R.id.tvNamaProyek);
            btnStatus=itemView.findViewById(R.id.btnStatus);
            cardListProyek=itemView.findViewById(R.id.cardListProyek);
            btnhapus=itemView.findViewById(R.id.btnhapus);
            dialog = new AlertDialog.Builder(mCtx);
        }

        private void deleteFormKunjungan(final String idform){
            AndroidNetworking.post(api.URL_deleteFormKunjungan)
                    .addBodyParameter("key", api.key)
                    .addBodyParameter("idform", idform)
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Boolean success = response.getBoolean("success");
                                String data = response.getString("data");
                                if (success) {
                                    Log.d(TAG, "onResponse: "+data);
                                }else{
                                    Log.d("DATA_BOOLEAN_pro", "onResponse: "+success);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.d(TAG+"_JASON", "onResponse: "+e);
                            }catch (NullPointerException e){
                                Log.d(TAG+"_NULL", "onResponse: "+e);
                            }catch (NumberFormatException e){
                                Log.d(TAG+"_EXCEPTION", "onResponse: "+e);
                            }


                        }

                        @Override
                        public void onError(ANError anError) {
                            Log.d("EROOR_EXCEPTION", "onError: "+anError);
                        }
                    });
        }



    }
}
