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

import androidx.appcompat.widget.Toolbar;
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
import com.rolvatech.cgc.dataobjects.AreaDTO;
import com.rolvatech.cgc.utils.PrefUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AreaListFragment extends Fragment {
    RecyclerView recyclerView;
    AreaListAdapter areaListAdapter;
    List<AreaDTO> areaDTOList = new ArrayList<>();
    Toolbar toolbar;
    FloatingActionButton addAreabtn;
    Fragment fragment = null;
    FragmentManager fm;
    FragmentTransaction fragmentTransaction;
    public AreaListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_area_list, container, false);
        recyclerView = root.findViewById(R.id.recycler_view);
        addAreabtn=root.findViewById(R.id.addAreabtn);
        addAreabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewArea();
            }
        });
        areaListAdapter = new AreaListAdapter(areaDTOList);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(areaListAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                AreaDTO movie = areaDTOList.get(position);
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
        builderSingle.setTitle("Area Name");
        EditText textInput= new EditText(this.getContext());
        textInput.setInputType(InputType.TYPE_CLASS_TEXT);
        builderSingle.setView(textInput);
        builderSingle.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               String areaName= textInput.getText().toString();
               new APIClient(getContext()).getApi().createArea("Bearer " + PrefUtils.getStringPreference(getActivity(), PrefUtils.TOKEN),areaName).enqueue(new Callback<JSONObject>() {
                   @Override
                   public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                       assert response.body()!=null;
                       Toast.makeText(getContext(),"Area Added Successfully",Toast.LENGTH_LONG);
                   }

                   @Override
                   public void onFailure(Call<JSONObject> call, Throwable t) {

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

        new APIClient(getActivity()).getApi().getAreas("Bearer " + PrefUtils.getStringPreference(getActivity(), PrefUtils.TOKEN)).enqueue(new Callback<List<AreaDTO>>() {
            @Override
            public void onResponse(Call<List<AreaDTO>> call, Response<List<AreaDTO>> response) {
                if (response.code() == 200) {
                    areaDTOList = new ArrayList<>();
                    //  try {
                    assert response.body() != null;
                    areaDTOList = response.body().subList(0, response.body().size());
                    areaListAdapter = new AreaListAdapter(areaDTOList);
                    recyclerView.setHasFixedSize(true);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(areaListAdapter);
                    recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new RecyclerTouchListener.ClickListener() {
                        @Override
                        public void onClick(View view, int position) {
                            AreaDTO areaDTO = areaDTOList.get(position);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("area", areaDTO);
                            PrefUtils.setLongPreference(getContext(), PrefUtils.AREA_ID, areaDTO.getId());
                            fragment = new TaskListFragment();
                            fragment.setArguments(bundle);
                            fm = getActivity().getSupportFragmentManager();
// create a FragmentTransaction to begin the transaction and replace the Fragment
                            fragmentTransaction = fm.beginTransaction();
// replace the FrameLayout with new Fragment
                            fragmentTransaction.replace(R.id.content_frame, fragment);

                            fragmentTransaction.commit();
                        }

                        @Override
                        public void onLongClick(View view, int position) {

                        }
                    }));
                    areaListAdapter.notifyDataSetChanged();
//                        JSONArray jsonArray = new JSONArray(response.body().toString());
//                        for (int i = 0; i < jsonArray.length(); i++) {
//                            JSONObject jsonobject = jsonArray.getJSONObject(i);
//                            AreaDTO areaDTO = new AreaDTO();
//                            areaDTO.setId(Long.valueOf(jsonobject.optString("id")));
//                            areaDTO.setAreaName(jsonobject.optString("areaName"));
//                            areaDTO.setAreaNumber(jsonobject.optString("areaNumber"));
//                            areaDTOList.add(areaDTO);
//                            areaListAdapter.notifyDataSetChanged();
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
                }
            }

            @Override
            public void onFailure(Call<List<AreaDTO>> call, Throwable throwable) {

            }
        });
    }
}
