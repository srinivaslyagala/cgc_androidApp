package com.rolvatech.cgc.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rolvatech.cgc.APIClient;
import com.rolvatech.cgc.R;
import com.rolvatech.cgc.application.CGCApplication;
import com.rolvatech.cgc.dataobjects.AreaTaskDTO;
import com.rolvatech.cgc.dataobjects.TaskDTO;
import com.rolvatech.cgc.utils.PrefUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.TaskViewHolder> {
    private List<TaskDTO> mDataset;
    RecyclerViewItemClickListener recyclerViewItemClickListener;

    public DataAdapter(List<TaskDTO> myDataset, RecyclerViewItemClickListener listener) {
        mDataset = myDataset;
        this.recyclerViewItemClickListener = listener;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent, false);

        return new TaskViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder fruitViewHolder, int i) {
        fruitViewHolder.mTextView.setText(mDataset.get(i).getTaskName());
        fruitViewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }


    public class TaskViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView mTextView;
        CheckBox checkBox;

        public TaskViewHolder(View v) {
            super(v);
            mTextView = v.findViewById(R.id.textView);
            checkBox = v.findViewById(R.id.checkbox);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
//            List<TaskDTO> taskDTOS = new ArrayList<>();
//            for (int i = 0; i < mDataset.size(); i++) {
//                if (checkBox.isChecked()) {
//                    taskDTOS.add(mDataset.get(i));
//                }
//            }
//            recyclerViewItemClickListener.clickOnItem(taskDTOS);
        }
    }


    public interface RecyclerViewItemClickListener {
        void clickOnItem(List<TaskDTO> data);
    }
}
