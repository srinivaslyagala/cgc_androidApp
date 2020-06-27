package com.rolvatech.cgc.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.rolvatech.cgc.APIClient;
import com.rolvatech.cgc.R;
import com.rolvatech.cgc.RecyclerTouchListener;
import com.rolvatech.cgc.adapters.AreaListAdapter;
import com.rolvatech.cgc.adapters.CustomExpandableListAdapter;
import com.rolvatech.cgc.dataobjects.AreaDTO;
import com.rolvatech.cgc.dataobjects.AreaTaskDTO;
import com.rolvatech.cgc.dataobjects.Child;
import com.rolvatech.cgc.dataobjects.TaskDTO;
import com.rolvatech.cgc.dataobjects.UserDTO;
import com.rolvatech.cgc.utils.PrefUtils;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChildTab2 extends Fragment {
    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<String> expandableListTitle;
    HashMap<String, List<TaskDTO>> expandableListDetail;
    UserDTO userDTO;
    Fragment fragment = null;
    FragmentManager fm;
    FragmentTransaction fragmentTransaction;
    FloatingActionButton btnAssignTask;
    List<AreaDTO> areaDtoList;
    List<TaskDTO> taskListDto;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Returning the layout file after inflating
        //Change R.layout.tab1 in you classes
        View rootView = inflater.inflate(R.layout.child_tab2, container, false);
        Bundle bundle = getArguments();
        Long childId = PrefUtils.getLongPreference(getContext(), PrefUtils.CHILD_ID, 0L);
        getChildDetails(childId);
        expandableListView = rootView.findViewById(R.id.expandableListView);
//        btnAssignTask=rootView.findViewById(R.id.btnAssignTask);
//        btnAssignTask.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                assignNewTasks();
//            }
//        });
        getAssignedTasks(childId);
        return rootView;
    }

    private void getAssignedTasks(Long childId) {
        new APIClient(getActivity()).getApi().getChildTasksByArea("Bearer " + PrefUtils.getStringPreference(getActivity(), PrefUtils.TOKEN), (childId)).enqueue(new Callback<List<AreaTaskDTO>>() {
            @Override
            public void onResponse(Call<List<AreaTaskDTO>> call, Response<List<AreaTaskDTO>> response) {
                if (response.code() == 200) {
                    List<AreaTaskDTO> areaDTOS = response.body().subList(0, response.body().size());
                    expandableListDetail = new HashMap<>();
                    for (int i = 0; i < areaDTOS.size(); i++) {
                        expandableListDetail.put(areaDTOS.get(i).getName(), areaDTOS.get(i).getTasks());
                    }
                    expandableListTitle = new ArrayList<>(expandableListDetail.keySet());
                    expandableListAdapter = new CustomExpandableListAdapter(getContext(), expandableListTitle, expandableListDetail);
                    expandableListView.setAdapter(expandableListAdapter);
                    expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

                        @Override
                        public void onGroupExpand(int groupPosition) {
//                            Toast.makeText(getActivity(),
//                                    expandableListTitle.get(groupPosition) + " List Expanded.",
//                                    Toast.LENGTH_SHORT).show();
                        }
                    });

                    expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

                        @Override
                        public void onGroupCollapse(int groupPosition) {
//                            Toast.makeText(getContext(),
//                                    expandableListTitle.get(groupPosition) + " List Collapsed.",
//                                    Toast.LENGTH_SHORT).show();

                        }
                    });

                    expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                        @Override
                        public boolean onChildClick(ExpandableListView parent, View v,
                                                    int groupPosition, int childPosition, long id) {

                            Log.i("SubTask Selected", "onChildClick: "+expandableListTitle.get(groupPosition)
                                    + " -> "
                                    + expandableListDetail.get(
                                    expandableListTitle.get(groupPosition)).get(
                                    childPosition).getUserTaskId());
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("user", userDTO);
                            bundle.putSerializable("userTaskId",expandableListDetail.get(
                                    expandableListTitle.get(groupPosition)).get(
                                    childPosition).getUserTaskId());
                            //   PrefUtils.setLongPreference(getContext(), PrefUtils.CHILD_ID, child.getId());
                            fragment = new SubTaskFragment();
                            fragment.setArguments(bundle);
                            fm = getActivity().getSupportFragmentManager();
// create a FragmentTransaction to begin the transaction and replace the Fragment
                            fragmentTransaction = fm.beginTransaction();
// replace the FrameLayout with new Fragment
                            fragmentTransaction.replace(R.id.content_frame, fragment);

                            fragmentTransaction.commit();
                            return false;
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<AreaTaskDTO>> call, Throwable throwable) {

            }
        });
    }

    private void getChildDetails(Long childId) {
        new APIClient(getActivity()).getApi().getUserDetailsById("Bearer " + PrefUtils.getStringPreference(getActivity(), PrefUtils.TOKEN), (childId)).enqueue(new Callback<UserDTO>() {
            @Override
            public void onResponse(Call<UserDTO> call, Response<UserDTO> response) {
                if (response.code() == 200) {
                    userDTO = response.body();

                } else {

                }
            }

            @Override
            public void onFailure(Call<UserDTO> call, Throwable t) {

            }
        });
    }

    public void getAreas() {
        new APIClient(getActivity()).getApi().getAllTasksByAreaOrder("Bearer " + PrefUtils.getStringPreference(getActivity(), PrefUtils.TOKEN)).enqueue(new Callback<List<AreaTaskDTO>>() {
            @Override
            public void onResponse(Call<List<AreaTaskDTO>> call, Response<List<AreaTaskDTO>> response) {
                if (response.code() == 200) {
                    List<AreaTaskDTO> areaDTOS = response.body().subList(0, response.body().size());
                    expandableListDetail = new HashMap<>();
                    for (int i = 0; i < areaDTOS.size(); i++) {
                        expandableListDetail.put(areaDTOS.get(i).getName(), areaDTOS.get(i).getTasks());
                    }
                    expandableListTitle = new ArrayList<>(expandableListDetail.keySet());
                    expandableListAdapter = new CustomExpandableListAdapter(getContext(), expandableListTitle, expandableListDetail);
                    expandableListView.setAdapter(expandableListAdapter);
                    expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

                        @Override
                        public void onGroupExpand(int groupPosition) {
                        }
                    });

                    expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

                        @Override
                        public void onGroupCollapse(int groupPosition) {
                        }
                    });

                    expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                        @Override
                        public boolean onChildClick(ExpandableListView parent, View v,
                                                    int groupPosition, int childPosition, long id) {
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("user", userDTO);
                            bundle.putString("taskName", expandableListDetail.get(expandableListTitle.get(groupPosition)).get(childPosition).getTaskName());
                            bundle.putLong("userTaskId", expandableListDetail.get(expandableListTitle.get(groupPosition)).get(childPosition).getId());
                            bundle.putString("areaName", areaDTOS.get(groupPosition).getName());
                            //   PrefUtils.setLongPreference(getContext(), PrefUtils.CHILD_ID, child.getId());
                            fragment = new SubTaskFragment();
                            fragment.setArguments(bundle);
                            fm = getActivity().getSupportFragmentManager();
// create a FragmentTransaction to begin the transaction and replace the Fragment
                            fragmentTransaction = fm.beginTransaction();
// replace the FrameLayout with new Fragment
                            fragmentTransaction.replace(R.id.content_frame, fragment);

                            fragmentTransaction.commit();
                            return false;
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<AreaTaskDTO>> call, Throwable throwable) {

            }
        });
    }

    private void assignNewTasks(){
        ConcurrentMap<Long,TaskDTO> selectedTasks=new ConcurrentHashMap<>();
        ConcurrentMap<Long,TaskDTO> removedTasks=new ConcurrentHashMap<>();
        AlertDialog.Builder areaListDialog = new AlertDialog.Builder(getContext());
        areaListDialog.setTitle("Select Area");
        final ArrayAdapter<String> areaListAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.select_dialog_singlechoice);
        new APIClient(getActivity()).getApi().getAreas("Bearer " + PrefUtils.getStringPreference(getActivity(), PrefUtils.TOKEN)).enqueue(new Callback<List<AreaDTO>>() {
            @Override
            public void onResponse(Call<List<AreaDTO>> call, Response<List<AreaDTO>> response) {
                if(response.isSuccessful()){
                    assert response.body()!=null;
                    areaDtoList = response.body().subList(0,response.body().size());
                    for(AreaDTO area:areaDtoList){
                        areaListAdapter.add(area.getId()+" "+area.getAreaName());
                    }
                }else{ }
            }
            @Override
            public void onFailure(Call<List<AreaDTO>> call, Throwable t) { }
        });
        areaListDialog.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        int checkedItem=0;
        areaListDialog.setSingleChoiceItems(areaListAdapter,checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String selectedArea=areaListAdapter.getItem(i);
                Long areaId=Long.parseLong(selectedArea.substring(0,selectedArea.indexOf(' ')));
                AlertDialog.Builder taskListDialog = new AlertDialog.Builder(getContext());
                taskListDialog.setTitle("Select Task");
                ArrayList<String> taskList=new ArrayList<>();
                ArrayList<Boolean> checkedTasks=new ArrayList<Boolean>();
                new APIClient(getActivity()).getApi().getTasksforArea("Bearer " + PrefUtils.getStringPreference(getActivity(), PrefUtils.TOKEN),areaId).enqueue(new Callback<List<TaskDTO>>() {
                    @Override
                    public void onResponse(Call<List<TaskDTO>> call, Response<List<TaskDTO>> response) {
                        if(response.isSuccessful()){
                            assert response.body()!=null;
                            taskListDto = response.body().subList(0, response.body().size());
                            for(TaskDTO taskDto:taskListDto){
                                taskList.add(taskDto.getId()+" "+taskDto.getTaskName());
                                List<TaskDTO>assignedTasks=expandableListDetail.get(taskDto.getArea().getAreaName());
                                for(TaskDTO assignedTask:assignedTasks){
                                    if(assignedTask.getId() == taskDto.getId()){
                                        checkedTasks.add(true);
                                        selectedTasks.put(assignedTask.getId(),taskDto);
                                    }else{
                                        checkedTasks.add(false);
                                    }
                                }
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<List<TaskDTO>> call, Throwable t) {

                    }
                });
                taskListDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                CharSequence[] taskListArray=new CharSequence[taskList.size()];
                taskList.toArray(taskListArray);
                boolean[] checkedTaskArray=new boolean[taskList.size()];
                for(int j=0;j<checkedTasks.size();j++){
                    checkedTaskArray[j]=checkedTasks.get(j);
                }
                taskListDialog.setMultiChoiceItems(taskListArray,checkedTaskArray, new DialogInterface.OnMultiChoiceClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                        String task=new String(taskListArray[i].toString());
                        Long taskId=new Long(task.substring(0,task.indexOf(' ')));
                        if(b){
                            TaskDTO addedTask=new TaskDTO();
                            addedTask.setId(taskId);
                            selectedTasks.put(taskId,addedTask);
                        }else{
                            TaskDTO removedTask=new TaskDTO();
                            removedTask.setId(taskId);
                            removedTasks.put(taskId,removedTask);
                        }
                    }
                });

                taskListDialog.setPositiveButton("OK", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getContext(), "Selected Tasks List"+selectedTasks.toString(), Toast.LENGTH_LONG).show();
                    }
                });
            }

        });
    }
}
