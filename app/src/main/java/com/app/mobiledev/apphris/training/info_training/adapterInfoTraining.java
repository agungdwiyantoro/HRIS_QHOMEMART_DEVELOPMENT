package com.app.mobiledev.apphris.training.info_training;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.mobiledev.apphris.R;

import java.util.List;

public class adapterInfoTraining  extends RecyclerView.Adapter<adapterInfoTraining.HolderInfoTraining> {
    private Context mCtx;
    private List<model_training> modelTrains;


    public adapterInfoTraining(List<model_training> modelcuti , Context ctx){
        this.mCtx=ctx;
        this.modelTrains=modelcuti;
    }

    @NonNull
    @Override
    public HolderInfoTraining onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View row;
        row = inflater.inflate(R.layout.list_info_training, viewGroup, false);
        return new HolderInfoTraining(row);
    }

    @Override
    public void onBindViewHolder(@NonNull adapterInfoTraining.HolderInfoTraining holder, int position) {
        final model_training Object = modelTrains.get(position);
        holder.tvMateri.setText(Object.getMateri());
        holder.tvTgl.setText(Object.getTntgl());
        Log.d("TEST_MATERI", "onBindViewHolder: "+Object.getPeserta());
    }

    @Override
    public int getItemCount() {
        return modelTrains.size();
    }
    public class HolderInfoTraining extends RecyclerView.ViewHolder {
        TextView tvMateri, tvTgl;
        public HolderInfoTraining(@NonNull View itemView) {
            super(itemView);
            tvMateri = itemView.findViewById(R.id.tvMateri);
            tvTgl   = itemView.findViewById(R.id.tvTgl);

        }
    }
}
