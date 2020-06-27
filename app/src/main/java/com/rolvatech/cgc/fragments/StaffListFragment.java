package com.rolvatech.cgc.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
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
import com.rolvatech.cgc.adapters.StaffListAdapter;
import com.rolvatech.cgc.dataobjects.StaffDTO;
import com.rolvatech.cgc.dataobjects.UserDTO;
import com.rolvatech.cgc.utils.PrefUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StaffListFragment extends Fragment {

    RecyclerView recyclerView;
    StaffListAdapter staffListAdapter;
    List<UserDTO> staffDTOList = new ArrayList<>();
    FloatingActionButton addStaff;
    Fragment fragment = null;
    FragmentManager fm;
    FragmentTransaction fragmentTransaction;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_staff_list, container, false);
        recyclerView = root.findViewById(R.id.staff_list_view);
        addStaff = root.findViewById(R.id.addStaff);
        addStaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = new CreateStaffFragment();
                fm = getActivity().getSupportFragmentManager();
// create a FragmentTransaction to begin the transaction and replace the Fragment
                fragmentTransaction = fm.beginTransaction();
// replace the FrameLayout with new Fragment
                fragmentTransaction.replace(R.id.content_frame, fragment);

                fragmentTransaction.commit();
            }
        });
        staffListAdapter = new StaffListAdapter(staffDTOList);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        getStaff();
        recyclerView.setAdapter(staffListAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                UserDTO staffDTO = staffDTOList.get(position);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        return root;

    }

    public void getStaff() {

        new APIClient(getActivity()).getApi().getStaff("Bearer " + PrefUtils.getStringPreference(getActivity(), PrefUtils.TOKEN)).enqueue(new Callback<List<UserDTO>>() {
            @Override
            public void onResponse(Call<List<UserDTO>> call, Response<List<UserDTO>> response) {
                if (response.code() == 200) {
                    staffDTOList = new ArrayList<>();
                    //  try {
                    assert response.body() != null;
                    //StaffDTO[] enums = gson.fromJson(yourJson, ChannelSearchEnum[].class);
                    staffDTOList = response.body().subList(0, response.body().size());
                    staffListAdapter = new StaffListAdapter(staffDTOList);
                    recyclerView.setHasFixedSize(true);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(staffListAdapter);
                    recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new RecyclerTouchListener.ClickListener() {
                        @Override
                        public void onClick(View view, int position) {
                            UserDTO staffDTO = staffDTOList.get(position);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("staff", staffDTO);
                            fragment = new StaffDetailsFragment();
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
//                        JSONArray jsonArray = new JSONArray(response.body().toString());
//                        Log.e("splash", jsonArray.toString());
//                        for (int i = 0; i < jsonArray.length(); i++) {
//                            JSONObject jsonobject = jsonArray.getJSONObject(i);
//
//                            StaffDTO staffDTO = new StaffDTO();
//                            staffDTO.setId(Long.valueOf(jsonobject.optString("id")));
//                            staffDTO.setUserName(jsonobject.optString("userName"));
//                            staffDTO.setContact(jsonobject.optString("contact"));
//                            staffDTO.setEmail(jsonobject.optString("email"));
//                            staffDTO.setFirstName(jsonobject.optString("firstName"));
//                            staffDTO.setLastName(jsonobject.optString("secondName"));
//                            staffDTO.setPassword(jsonobject.optString("password"));
//                            staffDTO.setProfileImage(jsonobject.optString("profileImage"));
//                            staffDTO.setAssignedChildCount(jsonobject.optString("assignedChildCount"));
//                            staffDTOList.add(staffDTO);
                    staffListAdapter.notifyDataSetChanged();
                    // }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                        Log.e("error", e.getMessage());
//                    }
                }
            }

            @Override
            public void onFailure(Call<List<UserDTO>> call, Throwable t) {
                Log.e("error", t.getMessage());
            }
        });
    }
}
