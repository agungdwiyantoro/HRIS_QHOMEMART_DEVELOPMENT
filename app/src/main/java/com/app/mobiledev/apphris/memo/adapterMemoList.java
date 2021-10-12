package com.app.mobiledev.apphris.memo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.mobiledev.apphris.R;

import java.util.List;

public class adapterMemoList extends RecyclerView.Adapter<adapterMemoList.HolderMemo> {

    List<ModelMemo> modelMemos;
    Context context;

    public adapterMemoList(List<ModelMemo> modelMemos, Context context) {
        this.modelMemos = modelMemos;
        this.context = context;
    }

    @NonNull
    @Override
    public HolderMemo onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View row;
        row = inflater.inflate(R.layout.list_memo_detail, viewGroup, false);
        return new HolderMemo(row);
    }

    @Override
    public void onBindViewHolder(@NonNull final HolderMemo holderMemo, int position) {
        try {
            ModelMemo memo = modelMemos.get(position);
            holderMemo.no_key_memo=memo.getMmANo();
            holderMemo.no_memo=memo.getMo();
            holderMemo.dari=memo.getDari();
            holderMemo.hal=memo.getHal();
            holderMemo.isi=memo.getIsi();
            holderMemo.jns=memo.getJns();
            holderMemo.kpd=memo.getKpd();
            holderMemo.tgl=memo.getTgl();
            holderMemo.pdf=memo.getPdf();
            holderMemo.video=memo.getVideo();
            if(holderMemo.jns.equals("Notulen Meeting")){
                holderMemo.wkt=memo.getWkt();
                holderMemo.tempat=memo.getTempat();
            }else{
                holderMemo.wkt="__";
                holderMemo.tempat="__";
            }
            if(memo.getHal().length()>=25){
                holderMemo.tvJudul.setText(memo.getHal().substring(0,25)+"...");
            }else{
                holderMemo.tvJudul.setText(memo.getHal());
            }
            if(memo.getMemo_baru().equals("0")){
                holderMemo.tv_memo_baru.setVisibility(View.GONE);
            }else{
                holderMemo.tv_memo_baru.setVisibility(View.VISIBLE);
            }


            holderMemo.tvJenis.setText(memo.getJns());
            holderMemo.tvTgl.setText(memo.getTgl());
            holderMemo.cvMemo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), DetailMemo.class);
                    Bundle x = new Bundle();
                    x.putString("jenis_memo", holderMemo.jns);
                    x.putString("key_memo", holderMemo.no_key_memo);
                    x.putString("no_memo", holderMemo.no_memo);
                    x.putString("kepada", holderMemo.kpd);
                    x.putString("dari", holderMemo.dari);
                    x.putString("hal", holderMemo.hal);
                    x.putString("tgl", holderMemo.tgl);
                    x.putString("isi", holderMemo.isi);
                    x.putString("waktu", holderMemo.wkt);
                    x.putString("tempat", holderMemo.tempat);
                    x.putString("pdf", holderMemo.pdf);
                    x.putString("video",holderMemo.video);
                    intent.putExtras(x);
                    v.getContext().startActivity(intent);

                    Log.d("GET_NmPd", "onBindViewHolder: "+holderMemo.tvJudul.getText().toString());
                    //  helper2.showMsg(v.getContext(),"");
                }
            });

        } catch (Exception e) {
            Log.e("Exception_cek", "onBindViewHolder: ", e);
        }
    }

    @Override
    public int getItemCount() {
        return modelMemos.size();
    }

    public class HolderMemo extends RecyclerView.ViewHolder {

        TextView tvJudul, tvTgl,tv_memo_baru,tvJenis;
        CardView cvMemo;
        String no_key_memo,jns,no_memo,tgl,hal,kpd,dari,isi,wkt,tempat,pdf,video;


        public HolderMemo(@NonNull View itemView) {
            super(itemView);
            tvJudul = itemView.findViewById(R.id.tvJudul);
            tvTgl   = itemView.findViewById(R.id.tvTgl);
            tv_memo_baru=itemView.findViewById(R.id.tv_memo_baru);
            tvJenis=itemView.findViewById(R.id.tvJenis);

            cvMemo   = itemView.findViewById(R.id.cvMemo);
        }
    }
}
