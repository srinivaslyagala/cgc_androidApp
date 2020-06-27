package com.rolvatech.cgc.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rolvatech.cgc.R;
import com.rolvatech.cgc.dataobjects.AreaDTO;
import com.rolvatech.cgc.dataobjects.TaskDTO;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.ViewHolder> {

    List<TaskDTO> areaDTOS = new ArrayList<>();

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView id, name;

        public ViewHolder(View view) {
            super(view);
            id = view.findViewById(R.id.id);
            name = view.findViewById(R.id.name);
        }
    }

    public TaskListAdapter(List<TaskDTO> areaDTOS) {
        this.areaDTOS = areaDTOS;
    }

    @NonNull
    @Override
    public TaskListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.area_list_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskListAdapter.ViewHolder holder, int position) {
        TaskDTO areaDTO = areaDTOS.get(position);
        holder.id.setText(areaDTO.getId() + " ");
        holder.name.setText(areaDTO.getTaskName() + "");
    }

    @Override
    public int getItemCount() {
        return areaDTOS.size();
    }
}
