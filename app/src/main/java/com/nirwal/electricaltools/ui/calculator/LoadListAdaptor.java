package com.nirwal.electricaltools.ui.calculator;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.nirwal.electricaltools.R;
import com.nirwal.electricaltools.ui.model.Load;

import java.util.ArrayList;
import java.util.List;

class LoadListAdaptor extends RecyclerView.Adapter<LoadListAdaptor.LoadHolder> {
    private static final String TAG = "LoadListAdaptor";
    List<Load> loads;
    public LoadListAdaptor(List<Load> loads) {
        this.loads = loads;
    }

    @NonNull
    @Override
    public LoadHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: ");
        return new LoadHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclery_load_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull LoadHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: ");
        Load load = loads.get(position);
        holder.loadTitle.setText(load.name);
        holder.loadQty.setText(String.valueOf(load.quantity));
        holder.loadCapacityEach.setText(String.valueOf(load.powerConsumption));
        holder.unit.setText(load.unit + " each");

        if(load.loadType.getLogoRes()!=0){
            holder.logo.setImageResource(load.loadType.getLogoRes());
        }
        else if(load.loadType.getLogoUrl()!=null && !load.loadType.getLogoUrl().isEmpty()){
            Glide.with(holder.itemView)
                    .load(load.loadType.getLogoUrl())
                    .fitCenter()
                    .placeholder(R.drawable.ic_baseline_electrical_services_24)
                    .into(holder.logo);
        }


        holder.closeBtn.setOnClickListener(v -> {
        removeItem(position);
        });
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: "+ String.valueOf(loads==null? 0:loads.size()));
        return loads==null? 0:loads.size();
    }

    public void removeItem(int position){
        notifyItemRemoved(position);
        this.loads.remove(position);
    }

    public void updateData(ArrayList<Load> data){
        Log.d(TAG, "updateData: ");
        this.loads = data;
        notifyDataSetChanged();
        notifyItemInserted(data.size()-1);
    }
    static class LoadHolder extends RecyclerView.ViewHolder{

        TextView loadTitle, loadQty, loadCapacityEach,unit;
        ImageView logo;
        ImageButton closeBtn;
        public LoadHolder(View itemView) {
            super(itemView);
            this.loadTitle = itemView.findViewById(R.id.load_title);
            this.loadQty = itemView.findViewById(R.id.load_qty);
            this.loadCapacityEach = itemView.findViewById(R.id.load_capacity_each);
            this.logo =itemView.findViewById(R.id.load_logos);
            this.unit = itemView.findViewById(R.id.load_unit);
            this.closeBtn = itemView.findViewById(R.id.remove_load);

        }
    }
}

