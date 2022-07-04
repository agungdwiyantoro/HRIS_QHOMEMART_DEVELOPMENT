package com.app.mobiledev.apphris.profile.dataKeluarga;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.app.mobiledev.apphris.R;
import com.app.mobiledev.apphris.api.api;
import com.app.mobiledev.apphris.helperPackage.helper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class AdapterDataKeluarga extends RecyclerView.Adapter<AdapterDataKeluarga.ReyclerViewHolder> {

    private Context mCtx;
    private List<ModelDataKeluarga> modelDataKeluargas;
    AlertDialog.Builder dialog;
    LayoutInflater inflater;
    View dialogView;

    DataKeluarga dk = new DataKeluarga();

    public AdapterDataKeluarga(Context mCtx, List<ModelDataKeluarga> modelDataKeluargas) {
        this.mCtx = mCtx;
        this.modelDataKeluargas = modelDataKeluargas;
    }

    public void removeItem(int position) {
        modelDataKeluargas.remove(position);
        notifyItemRemoved(position);
    }

    @NonNull
    @Override
    public AdapterDataKeluarga.ReyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.list_data_keluarga, null);
        return new AdapterDataKeluarga.ReyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AdapterDataKeluarga.ReyclerViewHolder holder, int position) {
        final ModelDataKeluarga Object = modelDataKeluargas.get(position);

        final String id = Object.getId();
        holder.txtNamaKeluarga.setText(Object.getNama());
        final String nama = Object.getNama();
        holder.txtHubungan.setText(Object.getHubungan());
        holder.txtNoHp.setText(Object.getNoHp());

        Log.d("DATA_KELUARGA", "onResponse: "+ id +"_"+ Object.getNama() + Object.getHubungan() + Object.getNoHp());

        holder.imgDelKeluarga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //delKeluarga(id);
                DialogForm(nama);
            }

            private void DialogForm(String nama) {
                dialog = new AlertDialog.Builder(mCtx);
                dialog.setView(dialogView);
                dialog.setCancelable(true);
                dialog.setIcon(R.drawable.ic_baseline_supervised_user_circle_24);
                dialog.setTitle("Konfirmasi");
                dialog.setMessage("Apakah anda yakin ingin menghapus "+nama+" dari data keluarga ?");
                dialog.setPositiveButton("Ya", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        delKeluarga(id, dialog);

                    }
                });

                dialog.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                });

                dialog.show();
            }

            private void delKeluarga(String id, final DialogInterface dialog) {
                OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                        .connectTimeout(5, TimeUnit.MINUTES)
                        .readTimeout(5, TimeUnit.MINUTES)
                        .writeTimeout(5, TimeUnit.MINUTES)
                        .build();
                AndroidNetworking.initialize(mCtx, okHttpClient);
                AndroidNetworking.post(api.URL_deleteHubungan_keluarga)
                        .addBodyParameter("key", api.key)
                        .addBodyParameter("id", id)
                        .setPriority(Priority.HIGH)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {

                                try {
                                    boolean status = response.getBoolean("success");
                                    String keterangan = response.getString("data");

                                    if (status) {
                                        removeItem(holder.getAdapterPosition());
                                        Toast.makeText(mCtx,keterangan,Toast.LENGTH_LONG).show();
                                        dialog.dismiss();
                                    } else {
                                        Toast.makeText(mCtx,keterangan,Toast.LENGTH_LONG).show();
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Log.d("JSON_Exception", "onResponse: " + e);
                                    helper.showMsg(mCtx, "Peringatan", "" + helper.PESAN_SERVER, helper.ERROR_TYPE);
                                    //progressDialog.dismiss();
                                } catch (NullPointerException e) {
                                    Log.d("JSON_NullPointer", "onResponse: " + e);
                                } catch (NumberFormatException e) {
                                    Log.d("JSONFormatException", "onResponse: " + e);
                                }

                                //progressDialog.dismiss();
                            }

                            @Override
                            public void onError(ANError anError) {
                                helper.showMsg(mCtx, "Peringatan", "" + helper.PESAN_KONEKSI, helper.ERROR_TYPE);
                                Log.d("ANERROR_EXCEPTION", "onError: " + anError);
                                //progressDialog.dismiss();
                            }
                        });
            }
        });



    }

    @Override
    public int getItemCount() {
        return modelDataKeluargas.size();
    }

    public class ReyclerViewHolder extends RecyclerView.ViewHolder {

        private TextView txtNamaKeluarga, txtHubungan, txtNoHp, txtDelKeluarga;
        private ImageView imgDelKeluarga;

        public ReyclerViewHolder(View itemView) {
            super(itemView);
            txtNamaKeluarga = itemView.findViewById(R.id.txtNamaKeluarga);
            txtHubungan = itemView.findViewById(R.id.txtHubungan);
            txtNoHp = itemView.findViewById(R.id.txtNoHp);
            //txtDelKeluarga = itemView.findViewById(R.id.txtDelKeluarga);

            imgDelKeluarga = itemView.findViewById(R.id.imgDelKeluarga);
        }
    }



}