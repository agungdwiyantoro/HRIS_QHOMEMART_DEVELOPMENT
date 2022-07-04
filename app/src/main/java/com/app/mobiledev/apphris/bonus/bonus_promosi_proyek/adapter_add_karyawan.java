package com.app.mobiledev.apphris.bonus.bonus_promosi_proyek;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.app.mobiledev.apphris.R;
import com.app.mobiledev.apphris.helperPackage.DataSoalSQLite;

import java.util.List;

public class adapter_add_karyawan extends RecyclerView.Adapter<adapter_add_karyawan.ReyclerViewHolder> {
    private Context mCtx;
    private List<model_add_karywan> modelKaryawans;
    private String cek_datas;
    private DialogFragment add_karyawans;

    public adapter_add_karyawan(List<model_add_karywan> modelkaryawan , Context ctx,String cek_data){
        this.mCtx=ctx;
        this.modelKaryawans=modelkaryawan;
        this.cek_datas=cek_data;

    }




    @NonNull
    @Override
    public adapter_add_karyawan.ReyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.list_add_karyawan, null);
        return new ReyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ReyclerViewHolder holder, final int position) {
        final model_add_karywan Object = modelKaryawans.get(position);
        holder.tvNama.setText(""+Object.getKynm());
        holder.tvKyano.setText(""+Object.getKyano());
        holder.tvDivisi.setText(""+Object.getDvnama());
        holder.kyano=Object.getKyano();
        holder.kynm=Object.getKynm();
        holder.divisi=Object.getDvnama();

        if(cek_datas.equals("form_bonus_proyek")){
            holder.btnHapus.setVisibility(View.VISIBLE);
            holder.btnAdd.setVisibility(View.GONE);
        }else if(cek_datas.equals("view")){
            holder.btnHapus.setVisibility(View.GONE);
            holder.btnAdd.setVisibility(View.GONE);

        }else{
            holder.btnHapus.setVisibility(View.GONE);
            holder.btnAdd.setVisibility(View.VISIBLE);
        }

        holder.btnHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.removeItem(holder.getAdapterPosition());
                deleteKaryawan(holder.kyano);
            }
        });
        holder.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpan(holder.kyano,holder.kynm,holder.divisi,position);
                holder.btnAdd.setText("dipilih");
            }
        });
    }
    @Override
    public int getItemCount() {
            return modelKaryawans.size();
    }


    public class ReyclerViewHolder extends RecyclerView.ViewHolder {
        private TextView tvKyano;
        private TextView tvNama;
        private TextView tvDivisi;
        private Button btnAdd,btnHapus;
        private String kyano,kynm,divisi;


        public ReyclerViewHolder(View view) {
            super(view);
            tvKyano=view.findViewById(R.id.tvKyano);
            tvNama=view.findViewById(R.id.tvNama);
            tvDivisi=view.findViewById(R.id.txDivisi);
            btnAdd=view.findViewById(R.id.btnAdd);
            btnHapus=view.findViewById(R.id.btnHapus);
        }

        public void removeItem(int position) {
            try {
                modelKaryawans.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, modelKaryawans.size());

            }catch (IndexOutOfBoundsException e){
                Log.d("index_out_of_bound", "removeItem: "+e);
            }

        }
    }


    public void simpan(final String kyano,final String kynm,final String divisi,int index) {
        DataSoalSQLite mydatabase = new DataSoalSQLite(mCtx);
        SQLiteDatabase db = mydatabase.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("kyano", kyano);
        contentValues.put("nama", kynm);
        contentValues.put("divisi", divisi);

        long result = db.insert("tblkaryawan", null, contentValues);
        if (result != -1) {
            Log.d("simpan_sqlite", "simpan: sukses");
            add_karyawan.pesanBerhasil();
        } else {
            Log.d("simpan_sqlite", "simpan: gagal");
        }
    }

    private void deleteKaryawan(String kyano){
        DataSoalSQLite getDatabase = new DataSoalSQLite(mCtx);
        String deleteQuery = "DELETE FROM tblkaryawan where kyano='" + kyano + "'";
        SQLiteDatabase DeleteData = getDatabase.getWritableDatabase();
        DeleteData.execSQL(deleteQuery);
        DeleteData.close();
    }

}
