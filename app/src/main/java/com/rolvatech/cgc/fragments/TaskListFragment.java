package com.rolvatech.cgc.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.rolvatech.cgc.APIClient;
import com.rolvatech.cgc.R;
import com.rolvatech.cgc.RecyclerTouchListener;
import com.rolvatech.cgc.adapters.AreaListAdapter;
import com.rolvatech.cgc.adapters.TaskListAdapter;
import com.rolvatech.cgc.dataobjects.AreaDTO;
import com.rolvatech.cgc.dataobjects.Child;
import com.rolvatech.cgc.dataobjects.TaskDTO;
import com.rolvatech.cgc.utils.PrefUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TaskListFragment extends Fragment {
    RecyclerView recyclerView;
    TaskListAdapter areaListAdapter;
    List<TaskDTO> areaDTOList = new ArrayList<>();
    Toolbar toolbar;
    FloatingActionButton addAreabtn;
    Fragment fragment = null;
    FragmentManager fm;
    FragmentTransaction fragmentTransaction;
    AreaDTO areaDTO;
    public TaskListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Bundle bundle = getArguments();
         areaDTO = (AreaDTO) bundle.getSerializable("area");
        View root = inflater.inflate(R.layout.fragment_task_list, container, false);
        recyclerView = root.findViewById(R.id.recycler_view);
        addAreabtn=root.findViewById(R.id.addTaskbtn);
        addAreabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewArea();
            }
        });
        areaListAdapter = new TaskListAdapter(areaDTOList);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(areaListAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                TaskDTO movie = areaDTOList.get(position);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        getAreas();
        return root;
    }

    private void addNewArea() {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(getContext());
        builderSingle.setTitle("Task Name");
        EditText textInput= new EditText(this.getContext());
        textInput.setInputType(InputType.TYPE_CLASS_TEXT);
        builderSingle.setView(textInput);
        builderSingle.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               String areaName= textInput.getText().toString();
               TaskDTO taskDto=new TaskDTO();
               taskDto.setTaskName(areaName);
               taskDto.setArea(areaDTO);
               new APIClient(getContext()).getApi().createTask("Bearer " + PrefUtils.getStringPreference(getActivity(), PrefUtils.TOKEN),taskDto).enqueue(new Callback<TaskDTO>() {
                   @Override
                   public void onResponse(Call<TaskDTO> call, Response<TaskDTO> response) {
                       assert response.body()!=null;
                       Toast.makeText(getContext(),"Area Added Successfully",Toast.LENGTH_LONG);
                   }

                   @Override
                   public void onFailure(Call<TaskDTO> call, Throwable t) {

                   }
               });
            }
        });
        builderSingle.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builderSingle.show();
    }

    public void getAreas() {

        new APIClient(getActivity()).getApi().getTasksforArea("Bearer " + PrefUtils.getStringPreference(getActivity(), PrefUtils.TOKEN),areaDTO.getId()).enqueue(new Callback<List<TaskDTO>>() {
            @Override
            public void onResponse(Call<List<TaskDTO>> call, Response<List<TaskDTO>> response) {
                if (response.code() == 200) {
                    areaDTOList = new ArrayList<>();
                    //  try {
                    assert response.body() != null;
                    areaDTOList = response.body().subList(0, response.body().size());
                    areaListAdapter = new TaskListAdapter(areaDTOList);
                    recyclerView.setHasFixedSize(true);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(areaListAdapter);
                    recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new RecyclerTouchListener.ClickListener() {
                        @Override
                        public void onClick(View view, int position) {
                        }

                        @Override
                        public void onLongClick(View view, int position) {

                        }
                    }));
                    areaListAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<TaskDTO>> call, Throwable throwable) {

            }
        });
    }
}
