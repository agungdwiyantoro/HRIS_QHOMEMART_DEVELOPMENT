package com.app.mobiledev.apphris.riwayat_cuti;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.app.mobiledev.apphris.R;

import java.util.List;

public class adapterCuti extends RecyclerView.Adapter<adapterCuti.ReyclerViewHolder> {
    private Context mCtx;
    private List<modelCuti> modelCutis;


    public adapterCuti(List<modelCuti> modelcuti , Context ctx){
        this.mCtx=ctx;
        this.modelCutis=modelcuti;
    }


    @NonNull
    @Override
    public ReyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.list_riwayat_cuti, null);
        return new ReyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReyclerViewHolder holder, int position) {
        final modelCuti Object = modelCutis.get(position);
        holder.tvAlasan.setText(Object.getCtalasan());
        if(Object.getCtmulai().equals(Object.getCtselesai())){
            holder.tvTglCuti.setText(Object.getCtmulai().replace("-","/"));
        }else{
            holder.tvTglCuti.setText(Object.getCtmulai().replace("-","/")+" - "+Object.getCtselesai().replace("-","/"));
        }

        if(Object.getStatus().equals("Y")){
            holder.imStatus.setImageResource(R.drawable.ic_dot_sukses);
        }else{
            holder.imStatus.setImageResource(R.drawable.ic_dot_status);
        }
        holder.tvJenisCuti.setText("Jenis :"+Object.getCtjenis());
        holder.tvLama.setText("Lama : "+Object.getCtlama()+" hari");
        //set data

    }

    @Override
    public int getItemCount() {
        return modelCutis.size();
    }

    public class ReyclerViewHolder extends RecyclerView.ViewHolder  {
        TextView tvTglCuti,tvLama,tvJenisCuti,tvAlasan;
        ImageButton imStatus;


        public ReyclerViewHolder(View itemView) {
            super(itemView);
           tvTglCuti=itemView.findViewById(R.id.tvTglCuti);
           tvLama=itemView.findViewById(R.id.tvLama);
           tvJenisCuti=itemView.findViewById(R.id.tvJenisCuti);
           tvAlasan=itemView.findViewById(R.id.tvAlasan);
           imStatus=itemView.findViewById(R.id.imStatus);


        }


    }
}
