package com.app.mobiledev.apphris.pinjaman;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.app.mobiledev.apphris.R;


import java.util.List;

public class adapaterPinjaman extends RecyclerView.Adapter<adapaterPinjaman.ReyclerViewHolder> {
    private Context mCtx;
    private List<model_pinjaman> model_pinjamans;


    public adapaterPinjaman(List<model_pinjaman> model_pinjaman , Context ctx){
        this.mCtx=ctx;
        this.model_pinjamans=model_pinjaman;
    }


    @NonNull
    @Override
    public ReyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.list_pinjaman_uang, null);
        return new ReyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReyclerViewHolder holder, int position) {
        final model_pinjaman Object = model_pinjamans.get(position);
        //set data
        if(Object.getMut().equals("null")){
            holder.tvNominal.setText(Object.getKbrpaj());
        }else{
            holder.tvNominal.setText(Object.getMut());
        }

        holder.tvAlasan.setText("Alasan :  "+Object.getKbalasan());
        holder.tvTgl.setText(Object.getTgl());
        holder.cek=Object.getKbrpaj();
        holder.nominal=Object.getMut();
        holder.kbstatus=Object.getKbstatus();

        holder.cekData =  Character.toString(holder.cek.charAt(0));
        Log.d("CEK_DATA", "onBindViewHolder: "+holder.cek);
        if(holder.cekData.equals("+")||holder.cekData.equals("+ ")){
            holder.angsuran.setText("Pinjaman Ke-"+(position+1));
            holder.lin1.setBackgroundResource(R.color.yellow);

        }else{
            holder.angsuran.setText("Angsuran Ke-"+(position-1));
            holder.lin1.setBackgroundResource(R.color.greennew);

        }

        if(holder.kbstatus.equals("Y")){
            holder.imStatus.setImageResource(R.drawable.ic_dot_blue);
        }else if(holder.kbstatus.equals("O")){
            holder.imStatus.setImageResource(R.drawable.ic_dot_oranye);
        }else{
            holder.imStatus.setImageResource(R.drawable.ic_dot_red);
        }
    }

    @Override
    public int getItemCount() {
        return model_pinjamans.size();
    }

    public class ReyclerViewHolder extends RecyclerView.ViewHolder  {
        private TextView tvNominal,tvTgl,tvAlasan,angsuran;
        private LinearLayout lin1;
        private String cek;
        private int btnwarning=0x7f060024;
        private String  cekData;
        private String nominal,kbstatus;
        private ImageButton imStatus;


        public ReyclerViewHolder(View itemView) {
            super(itemView);
            tvNominal=itemView.findViewById(R.id.tvNominal);
            tvTgl=itemView.findViewById(R.id.tvTgl);
            tvAlasan=itemView.findViewById(R.id.tvAlasan);
            angsuran=itemView.findViewById(R.id.angsuran);
            lin1=itemView.findViewById(R.id.line1);
            imStatus=itemView.findViewById(R.id.imStatus);

        }


    }
}
