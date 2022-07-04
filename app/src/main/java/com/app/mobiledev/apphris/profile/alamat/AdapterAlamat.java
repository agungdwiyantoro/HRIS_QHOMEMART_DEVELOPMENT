package com.app.mobiledev.apphris.profile.alamat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.mobiledev.apphris.R;
import com.app.mobiledev.apphris.sesion.SessionManager;

import java.util.List;

public class AdapterAlamat  {

    //START ADAPTER PROVINSI
    public static class AdapterAlamatProvinsi extends RecyclerView.Adapter<AdapterAlamatProvinsi.ReyclerViewHolder> {

        private Context mCtx;
        private List<ModelAlamat> modelAlamatsProvinsi;
        private String provinsi;

        public AdapterAlamatProvinsi(Context mCtx, List<ModelAlamat> modelAlamatsProvinsi) {
            this.mCtx = mCtx;
            this.modelAlamatsProvinsi = modelAlamatsProvinsi;
        }

        @NonNull
        @Override
        public AdapterAlamatProvinsi.ReyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater inflater = LayoutInflater.from(mCtx);
            View view = inflater.inflate(R.layout.list_alamat, null);
            return new AdapterAlamatProvinsi.ReyclerViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final AdapterAlamatProvinsi.ReyclerViewHolder holder, int position) {
            final ModelAlamat Object = modelAlamatsProvinsi.get(position);

            holder.idAlamat = Object.getId();
            holder.txtNamaAlamat.setText(Object.getNama());

            final int value = holder.idAlamat;

            holder.llAlamat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mCtx, KotaKab.class);
                    Bundle x = new Bundle();
                    x.putString("idAlamat", String.valueOf(value));
                    x.putString("namaAlamat", Object.getNama());

                    intent.putExtras(x);
                    mCtx.startActivity(intent);

                    Toast.makeText(mCtx, ""+value+", "+Object.getNama(), Toast.LENGTH_SHORT).show();

                    ((Activity)mCtx).finish();
                }
            });

            Log.d("ID_NAMA_ALAMAT", "onResponse: " + Object.getId() + Object.getNama());
        }

        @Override
        public int getItemCount() {
            return modelAlamatsProvinsi.size();
        }

        public class ReyclerViewHolder extends RecyclerView.ViewHolder {
            private TextView txtNamaAlamat;
            private int idAlamat;
            private LinearLayout llAlamat;

            public ReyclerViewHolder(View itemView) {
                super(itemView);
                txtNamaAlamat = itemView.findViewById(R.id.txtNamaAlamat);
                idAlamat = 0;

                llAlamat = itemView.findViewById(R.id.lLAlamat);
            }
        }

    }
    //END ADAPTER PROVINSI

    //START ADAPTER KABUPATEN
    public static class AdapterAlamatKabupaten extends RecyclerView.Adapter<AdapterAlamatKabupaten.ReyclerViewHolder> {

        private Context mCtx;
        private List<ModelAlamat> modelAlamatsKabupaten;
        private String alamatLengkap;

        public AdapterAlamatKabupaten(Context mCtx, List<ModelAlamat> modelAlamatsKabupaten, String alamatLengkap) {
            this.mCtx = mCtx;
            this.modelAlamatsKabupaten = modelAlamatsKabupaten;
            this.alamatLengkap = alamatLengkap;
        }

        @NonNull
        @Override
        public AdapterAlamatKabupaten.ReyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater inflater = LayoutInflater.from(mCtx);
            View view = inflater.inflate(R.layout.list_alamat, null);
            return new AdapterAlamatKabupaten.ReyclerViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final AdapterAlamatKabupaten.ReyclerViewHolder holder, int position) {
            final ModelAlamat Object = modelAlamatsKabupaten.get(position);

            holder.idAlamat = Object.getId();
            holder.txtNamaAlamat.setText(Object.getNama());
            final String al = this.alamatLengkap;

            final int value = holder.idAlamat;


            holder.llAlamat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mCtx, Kecamatan.class);
                    Bundle x = new Bundle();
                    x.putString("idAlamat", String.valueOf(value));
                    x.putString("namaAlamat", Object.getNama());
                    x.putString("aL", al);

                    intent.putExtras(x);
                    mCtx.startActivity(intent);

                    Toast.makeText(mCtx, ""+value+", "+Object.getNama(), Toast.LENGTH_SHORT).show();

                    ((Activity)mCtx).finish();
                }
            });

            Log.d("ID_NAMA_ALAMAT", "onResponse: " + Object.getId() + Object.getNama());
        }

        @Override
        public int getItemCount() {
            return modelAlamatsKabupaten.size();
        }

        public class ReyclerViewHolder extends RecyclerView.ViewHolder {
            private TextView txtNamaAlamat;
            private int idAlamat;
            private LinearLayout llAlamat;

            public ReyclerViewHolder(View itemView) {
                super(itemView);
                txtNamaAlamat = itemView.findViewById(R.id.txtNamaAlamat);
                idAlamat = 0;

                llAlamat = itemView.findViewById(R.id.lLAlamat);
            }
        }

    }
    //END ADAPTER KABUPATEN

    //START ADAPTER KECAMATAN
    public static class AdapterAlamatKecamatan extends RecyclerView.Adapter<AdapterAlamatKecamatan.ReyclerViewHolder> {

        private Context mCtx;
        private List<ModelAlamat> modelAlamatsKecamatan;
        private String alamatLengkap;

        public AdapterAlamatKecamatan(Context mCtx, List<ModelAlamat> modelAlamatsKecamatan, String alamatLengkap) {
            this.mCtx = mCtx;
            this.modelAlamatsKecamatan = modelAlamatsKecamatan;
            this.alamatLengkap = alamatLengkap;
        }

        @NonNull
        @Override
        public AdapterAlamatKecamatan.ReyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater inflater = LayoutInflater.from(mCtx);
            View view = inflater.inflate(R.layout.list_alamat, null);
            return new AdapterAlamatKecamatan.ReyclerViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final AdapterAlamatKecamatan.ReyclerViewHolder holder, int position) {
            final ModelAlamat Object = modelAlamatsKecamatan.get(position);

            holder.idAlamat = Object.getId();
            holder.txtNamaAlamat.setText(Object.getNama());

            final String al = this.alamatLengkap;

            final int value = holder.idAlamat;


            holder.llAlamat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mCtx, Kelurahan.class);
                    Bundle x = new Bundle();
                    x.putString("idAlamat", String.valueOf(value));
                    x.putString("namaAlamat", Object.getNama());
                    x.putString("aL", al);

                    intent.putExtras(x);
                    mCtx.startActivity(intent);

                    Toast.makeText(mCtx, ""+value+", "+Object.getNama(), Toast.LENGTH_SHORT).show();

                    ((Activity)mCtx).finish();
                }
            });

            Log.d("ID_NAMA_ALAMAT", "onResponse: " + Object.getId() + Object.getNama());
        }

        @Override
        public int getItemCount() {
            return modelAlamatsKecamatan.size();
        }

        public class ReyclerViewHolder extends RecyclerView.ViewHolder {
            private TextView txtNamaAlamat;
            private int idAlamat;
            private LinearLayout llAlamat;

            public ReyclerViewHolder(View itemView) {
                super(itemView);
                txtNamaAlamat = itemView.findViewById(R.id.txtNamaAlamat);
                idAlamat = 0;

                llAlamat = itemView.findViewById(R.id.lLAlamat);
            }
        }

    }
    //END ADAPTER KECAMATAN

    //START ADAPTER KELURAHAN
    public static class AdapterAlamatKelurahan extends RecyclerView.Adapter<AdapterAlamatKelurahan.ReyclerViewHolder> {

        private Context mCtx;
        private List<ModelAlamat> modelAlamatsKelurahan;
        private String alamatLengkap;

        SessionManager sessionManager;

        public AdapterAlamatKelurahan(Context mCtx, List<ModelAlamat> modelAlamatsKelurahan, String alamatLengkap) {
            this.mCtx = mCtx;
            this.modelAlamatsKelurahan = modelAlamatsKelurahan;
            this.alamatLengkap = alamatLengkap;
        }

        @NonNull
        @Override
        public AdapterAlamatKelurahan.ReyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater inflater = LayoutInflater.from(mCtx);
            View view = inflater.inflate(R.layout.list_alamat, null);
            return new AdapterAlamatKelurahan.ReyclerViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final AdapterAlamatKelurahan.ReyclerViewHolder holder, int position) {
            final ModelAlamat Object = modelAlamatsKelurahan.get(position);

            holder.idAlamat = Object.getId();
            holder.txtNamaAlamat.setText(Object.getNama());

            final String al = this.alamatLengkap;

            final int value = holder.idAlamat;

            sessionManager = new SessionManager(mCtx);

            holder.llAlamat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (sessionManager.getStatusAlamat().equals("now")){

                        /*Intent intent = new Intent(mCtx, UpdateDataDiri.class);
                        Bundle x = new Bundle();
                        x.putString("idAlamat", String.valueOf(value));
                        x.putString("namaAlamat", Object.getNama());
                        x.putString("aL", Object.getNama()+", "+al);*/

                        sessionManager.putAlamatNow(Object.getNama()+", "+al);

                        /*intent.putExtras(x);
                        mCtx.startActivity(intent);*/

                        Toast.makeText(mCtx, ""+value+", "+Object.getNama()+al, Toast.LENGTH_SHORT).show();

                        ((Activity)mCtx).finish();

                    } else if (sessionManager.getStatusAlamat().equals("ktp")) {

                        /*Intent intent = new Intent(mCtx, UpdateDataDiri.class);
                        Bundle x = new Bundle();
                        x.putString("idAlamat", String.valueOf(value));
                        x.putString("namaAlamat", Object.getNama());
                        x.putString("aL", Object.getNama()+", "+al);*/

                        sessionManager.putAlamatKtp(Object.getNama()+", "+al);


                        /*intent.putExtras(x);
                        mCtx.startActivity(intent);*/

                        Toast.makeText(mCtx, ""+value+", "+Object.getNama(), Toast.LENGTH_SHORT).show();

                        ((Activity)mCtx).finish();

                    }

                }
            });

            Log.d("ID_NAMA_ALAMAT", "onResponse: " + Object.getId() + Object.getNama());
        }

        @Override
        public int getItemCount() {
            return modelAlamatsKelurahan.size();
        }

        public class ReyclerViewHolder extends RecyclerView.ViewHolder {
            private TextView txtNamaAlamat;
            private int idAlamat;
            private LinearLayout llAlamat;

            public ReyclerViewHolder(View itemView) {
                super(itemView);
                txtNamaAlamat = itemView.findViewById(R.id.txtNamaAlamat);
                idAlamat = 0;

                llAlamat = itemView.findViewById(R.id.lLAlamat);
            }
        }

    }
    //END ADAPTER KELURAHAN

}
