package com.rolvatech.cgc.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.rolvatech.cgc.APIClient;
import com.rolvatech.cgc.R;
import com.rolvatech.cgc.application.CGCApplication;
import com.rolvatech.cgc.dataobjects.ChildTaskDTO;
import com.rolvatech.cgc.dataobjects.SubTaskDTO;
import com.rolvatech.cgc.dataobjects.TaskDTO;
import com.rolvatech.cgc.dataobjects.TaskDTO;
import com.rolvatech.cgc.utils.AlertDialogManager;
import com.rolvatech.cgc.utils.PrefUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubTaskListAdapter extends RecyclerView.Adapter<SubTaskListAdapter.ViewHolder> {

    List<SubTaskDTO> taskDTOS = new ArrayList<>();
    public Context context;
    TaskDTO taskDTO;
    AlertDialogManager alertDialogManager = new AlertDialogManager();

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtSubTaskName;
        public Switch aSwitch;
        TextInputEditText edtSubTaskName;
        public ImageView imgCancel, imgOK, imgEdit;

        public ViewHolder(View view) {
            super(view);
            imgOK = view.findViewById(R.id.imgOK);
            imgCancel = view.findViewById(R.id.imgCancel);
            edtSubTaskName = view.findViewById(R.id.edtSubTaskName);
            imgEdit = view.findViewById(R.id.imgEdit);
            aSwitch = view.findViewById(R.id.updateStatus);
            txtSubTaskName = view.findViewById(R.id.txtSubTaskName);
        }
    }

    public SubTaskListAdapter(Context context, List<SubTaskDTO> taskDTOS, TaskDTO taskDTO) {
        this.taskDTOS = taskDTOS;
        this.context = context;
        this.taskDTO = taskDTO;
    }

    @NonNull
    @Override
    public SubTaskListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sub_list_item, parent, false);
        return new SubTaskListAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SubTaskListAdapter.ViewHolder holder, int position) {
        SubTaskDTO subTaskDTO = taskDTOS.get(position);
        holder.txtSubTaskName.setText(subTaskDTO.getName());
        holder.edtSubTaskName.setText(subTaskDTO.getName());
        holder.aSwitch.setChecked("COMPLETE".equalsIgnoreCase(subTaskDTO.getStatus()));
        holder.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.edtSubTaskName.setVisibility(View.VISIBLE);
                holder.txtSubTaskName.setVisibility(View.GONE);
                holder.imgEdit.setVisibility(View.GONE);
                holder.imgCancel.setVisibility(View.VISIBLE);
                holder.imgOK.setVisibility(View.VISIBLE);

            }
        });
        holder.imgOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.edtSubTaskName.setVisibility(View.GONE);
                holder.txtSubTaskName.setVisibility(View.VISIBLE);
                holder.imgEdit.setVisibility(View.VISIBLE);
                holder.imgCancel.setVisibility(View.GONE);
                holder.imgOK.setVisibility(View.GONE);
                if (subTaskDTO.getId() == 0)
                    addSubTask(taskDTO.getId(), holder.edtSubTaskName.getText().toString(), subTaskDTO.getDescription());
                else
                    updateSubTask(subTaskDTO.getId(), holder.edtSubTaskName.getText().toString(), subTaskDTO.getDescription());
            }
        });
        holder.imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.edtSubTaskName.setVisibility(View.GONE);
                holder.txtSubTaskName.setVisibility(View.VISIBLE);
                holder.imgEdit.setVisibility(View.VISIBLE);
                holder.imgCancel.setVisibility(View.GONE);
                holder.imgOK.setVisibility(View.GONE);
            }
        });
        holder.aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    updateStatus(subTaskDTO.getId(), "COMPLETE");
                } else
                    updateStatus(subTaskDTO.getId(), "IN PROCESS");
            }
        });
    }

    @Override
    public int getItemCount() {
        return null!=taskDTOS?taskDTOS.size():0;
    }

    public void updateStatus(long id, String status) {
        showDialog();
            SubTaskDTO subTaskDTO=new SubTaskDTO();
            TaskDTO taskDto=new TaskDTO();
            taskDto.setId(taskDTO.getId());
            subTaskDTO.setTask(taskDto);
            subTaskDTO.setStatus(status);
            subTaskDTO.setId(id);
            new APIClient(CGCApplication.app()).getApi().updateSubTask("Bearer " + PrefUtils.getStringPreference(CGCApplication.app(), PrefUtils.TOKEN), subTaskDTO).enqueue(new Callback<SubTaskDTO>() {
                @Override
                public void onResponse(Call<SubTaskDTO> call, Response<SubTaskDTO> response) {
                    hideDialog();
                    if (response.isSuccessful() || response.code() == 200) {
                        alertDialogManager.showAlertDialog(context, "Success", "Status updated successfully", true);
                    } else {
                        alertDialogManager.showAlertDialog(context, "Failure", "Status updation failed", false);
                    }
                }

                @Override
                public void onFailure(Call<SubTaskDTO> call, Throwable t) {
                    hideDialog();
                }
            });
    }

    public void updateSubTask(long id, String name, String description) {
        showDialog();
            SubTaskDTO subTaskDTO=new SubTaskDTO();
            TaskDTO taskDto=new TaskDTO();
            taskDto.setId(taskDTO.getId());
            subTaskDTO.setTask(taskDto);
            subTaskDTO.setName(name);
            subTaskDTO.setDescription(description);
            subTaskDTO.setStatus("UPDATE");
            subTaskDTO.setId(id);
            new APIClient(CGCApplication.app()).getApi().updateSubTask("Bearer " + PrefUtils.getStringPreference(CGCApplication.app(), PrefUtils.TOKEN), subTaskDTO).enqueue(new Callback<SubTaskDTO>() {
                @Override
                public void onResponse(Call<SubTaskDTO> call, Response<SubTaskDTO> response) {
                    hideDialog();
                    if (response.isSuccessful() || response.code() == 200) {
                        alertDialogManager.showAlertDialog(context, "Success", "subtask updated successfully", true);
                    } else {
                        alertDialogManager.showAlertDialog(context, "Failure", "subtask updation failed", false);
                    }
                }

                @Override
                public void onFailure(Call<SubTaskDTO> call, Throwable t) {
hideDialog();
                }
            });
    }

    public void addSubTask(long id, String name, String description) {
        showDialog();
        SubTaskDTO subTaskDTO=new SubTaskDTO();
        TaskDTO taskDto=new TaskDTO();
        Log.i("Add SubTasks", "addSubTask: UserTaskID:"+taskDTO.getUserTaskId());
        Log.i("Add SubTasks", "addSubTask: TaskID:"+taskDTO.getId());
        taskDto.setId(taskDTO.getId());
        subTaskDTO.setTask(taskDto);
        subTaskDTO.setName(name);
        subTaskDTO.setDescription(description);
        subTaskDTO.setStatus("NEW");
        subTaskDTO.setId(id);
        new APIClient(CGCApplication.app()).getApi().addSubTasktoChild("Bearer " + PrefUtils.getStringPreference(CGCApplication.app(), PrefUtils.TOKEN), subTaskDTO).enqueue(new Callback<SubTaskDTO>() {
            @Override
            public void onResponse(Call<SubTaskDTO> call, Response<SubTaskDTO> response) {
                hideDialog();
                if (response.isSuccessful() || response.code() == 200) {
                    alertDialogManager.showAlertDialog(context, "Success", "subtask added successfully", true);
                    notifyItemRemoved(0);
                } else {
                    alertDialogManager.showAlertDialog(context, "Failure", "subtask adding failed", false);
                }
            }

            @Override
            public void onFailure(Call<SubTaskDTO> call, Throwable t) {
                hideDialog();
            }
        });
    }
    AlertDialog spotsDialog;

    private void showDialog() {

        if (spotsDialog == null) {
            spotsDialog = new SpotsDialog.Builder()
                    .setContext(context)
                    .setMessage("Loading...")
                    .build();
        }
        spotsDialog.show();
    }

    private void hideDialog() {
        if (spotsDialog != null) {
            spotsDialog.dismiss();
        }
    }
}
