package com.rolvatech.cgc.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.rolvatech.cgc.APIClient;
import com.rolvatech.cgc.R;
import com.rolvatech.cgc.RecyclerTouchListener;
import com.rolvatech.cgc.adapters.AreaListAdapter;
import com.rolvatech.cgc.adapters.SubTaskListAdapter;
import com.rolvatech.cgc.application.CGCApplication;
import com.rolvatech.cgc.dataobjects.AreaDTO;
import com.rolvatech.cgc.dataobjects.AreaTaskDTO;
import com.rolvatech.cgc.dataobjects.Child;
import com.rolvatech.cgc.dataobjects.SubTaskDTO;
import com.rolvatech.cgc.dataobjects.TaskDTO;
import com.rolvatech.cgc.dataobjects.UserDTO;
import com.rolvatech.cgc.utils.AlertDialogManager;
import com.rolvatech.cgc.utils.PrefUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.internal.concurrent.TaskLoggerKt;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubTaskFragment extends Fragment {

    TextView txtChildName, txtStaffName, txtAreaName, txtTaskName;
    RecyclerView recyclerView;
    AreaTaskDTO areaTaskDTO;
    UserDTO userDTO;
    Long taskId;
    SubTaskListAdapter subTaskListAdapter;
    List<SubTaskDTO> subTaskDTOS;
    TaskDTO taskDTO;
    FloatingActionButton addSubTask;
    AlertDialogManager alertDialogManager = new AlertDialogManager();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.sub_task_list, container, false);
        txtChildName = root.findViewById(R.id.txtChildName);
        txtStaffName = root.findViewById(R.id.txtStaffName);
        txtAreaName = root.findViewById(R.id.txtAreaName);
        txtTaskName = root.findViewById(R.id.txtTaskName);
        Bundle bundle = getArguments();
        userDTO = (UserDTO) bundle.getSerializable("user");
        txtAreaName.setText("Area Name");
        txtTaskName.setText("Task Name");
        taskId = bundle.getLong("userTaskId");
        recyclerView = root.findViewById(R.id.recycler_view);
        getSubTasks();
        subTaskListAdapter = new SubTaskListAdapter(getActivity(), subTaskDTOS, taskDTO);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(subTaskListAdapter);
        addSubTask = root.findViewById(R.id.fab);
        txtChildName.setText("Child Name: "+userDTO.getFirstName()+" "+userDTO.getLastName());
        if(userDTO.getStaff()!=null){
            txtStaffName.setText("Staff Name: "+userDTO.getStaff().getFirstname()+" "+userDTO.getStaff().getLastName());
        }

        return root;
    }


    public void getSubTasks() {
        new APIClient(getActivity()).getApi().getSubTasks("Bearer " + PrefUtils.getStringPreference(getActivity(), PrefUtils.TOKEN), String.valueOf(taskId)).enqueue(new Callback<TaskDTO>() {
            @Override
            public void onResponse(Call<TaskDTO> call, Response<TaskDTO> response) {
                subTaskDTOS = new ArrayList<>();
                if (response.code() == 200) {
                    taskDTO = response.body();
                    if (taskDTO.getSubTasks() != null && taskDTO.getSubTasks().size() > 0) {
                        subTaskListAdapter = new SubTaskListAdapter(getActivity(), taskDTO.getSubTasks(), taskDTO);
                        txtAreaName.setText("Area Name: "+taskDTO.getArea().getAreaName());
                        txtTaskName.setText("Task Name: "+taskDTO.getTaskName());
                        recyclerView.setHasFixedSize(true);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                        recyclerView.setLayoutManager(mLayoutManager);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.setAdapter(subTaskListAdapter);
                        subTaskListAdapter.notifyDataSetChanged();
                        addSubTask.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                SubTaskDTO subTaskDTO = new SubTaskDTO();
                                subTaskDTO.setId(0L);
                                subTaskDTO.setName("Name");
                                subTaskDTO.setDescription("description");
                                subTaskDTOS.addAll(taskDTO.getSubTasks());
                                subTaskDTOS.add(subTaskDTO);
                                subTaskListAdapter = new SubTaskListAdapter(getActivity(), subTaskDTOS, taskDTO);
                                recyclerView.setHasFixedSize(true);
                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                                recyclerView.setLayoutManager(mLayoutManager);
                                recyclerView.setItemAnimator(new DefaultItemAnimator());
                                recyclerView.setAdapter(subTaskListAdapter);
                                subTaskListAdapter.notifyDataSetChanged();
                            }
                        });

                    } else {
                        addSubTask.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                SubTaskDTO subTaskDTO = new SubTaskDTO();
                                subTaskDTO.setId(0L);
                                subTaskDTO.setName("Name");
                                subTaskDTO.setDescription("description");
                                subTaskDTOS.add(subTaskDTO);
                                subTaskListAdapter = new SubTaskListAdapter(getActivity(), subTaskDTOS, taskDTO);
                                recyclerView.setHasFixedSize(true);
                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                                recyclerView.setLayoutManager(mLayoutManager);
                                recyclerView.setItemAnimator(new DefaultItemAnimator());
                                recyclerView.setAdapter(subTaskListAdapter);
                                subTaskListAdapter.notifyDataSetChanged();
                            }
                        });
                    }

                } else {

                }
            }

            @Override
            public void onFailure(Call<TaskDTO> call, Throwable t) {

            }
        });
    }

}
