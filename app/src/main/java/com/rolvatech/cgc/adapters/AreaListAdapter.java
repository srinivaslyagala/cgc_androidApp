package com.rolvatech.cgc.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rolvatech.cgc.R;
import com.rolvatech.cgc.dataobjects.AreaDTO;

import java.util.ArrayList;
import java.util.List;

public class AreaListAdapter extends RecyclerView.Adapter<AreaListAdapter.ViewHolder> {

    List<AreaDTO> areaDTOS = new ArrayList<>();

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView id, name;

        public ViewHolder(View view) {
            super(view);
            id = view.findViewById(R.id.id);
            name = view.findViewById(R.id.name);
        }
    }

    public AreaListAdapter(List<AreaDTO> areaDTOS) {
        this.areaDTOS = areaDTOS;
    }

    @NonNull
    @Override
    public AreaListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.area_list_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AreaListAdapter.ViewHolder holder, int position) {
        AreaDTO areaDTO = areaDTOS.get(position);
        holder.id.setText(areaDTO.getId() + " ");
        holder.name.setText(areaDTO.getAreaName() + "");
    }

    @Override
    public int getItemCount() {
        return areaDTOS.size();
    }
}
