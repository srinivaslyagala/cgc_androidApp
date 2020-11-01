package com.rolvatech.cgc.fragments;

import android.app.AlertDialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
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
import com.rolvatech.cgc.adapters.ChildListAdapter;
import com.rolvatech.cgc.dataobjects.Child;
import com.rolvatech.cgc.utils.PrefUtils;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChildListFragment extends Fragment {
    RecyclerView recyclerView;
    ChildListAdapter childListAdapter;
    List<Child> childList = new ArrayList<>();
    Fragment fragment = null;
    FragmentManager fm;
    FragmentTransaction fragmentTransaction;
    FloatingActionButton addChild;
    Toolbar toolbar;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_child_list, container, false);
        recyclerView = root.findViewById(R.id.recycler_view);
        addChild = root.findViewById(R.id.fab);
        addChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = new CreateChildFragment();
                fm = getActivity().getSupportFragmentManager();
// create a FragmentTransaction to begin the transaction and replace the Fragment
                fragmentTransaction = fm.beginTransaction();
// replace the FrameLayout with new Fragment
                fragmentTransaction.replace(R.id.content_frame, fragment);

                fragmentTransaction.commit();
            }
        });
        getChildren();
        childListAdapter = new ChildListAdapter(childList);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(childListAdapter);

        getChildren();
        return root;
    }

    public void getChildren() {
        showDialog("Loading....");
        new APIClient(getActivity()).getApi().getChild("Bearer " + PrefUtils.getStringPreference(getActivity(), PrefUtils.TOKEN)).enqueue(new Callback<List<Child>>() {
            @Override
            public void onResponse(Call<List<Child>> call, Response<List<Child>> response) {
                hideDialog();
                if (response.code() == 200) {
                    childList = new ArrayList<>();
                    //  try {
                    assert response.body() != null;
                    childList = response.body().subList(0, response.body().size());
                    childListAdapter = new ChildListAdapter(childList);
                    recyclerView.setHasFixedSize(true);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(childListAdapter);
                    recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new RecyclerTouchListener.ClickListener() {
                        @Override
                        public void onClick(View view, int position) {
                            Child child = childList.get(position);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("child", child);
                            PrefUtils.setLongPreference(getContext(), PrefUtils.CHILD_ID, child.getId());
                            fragment = new ChildDetailsFragment();
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
//                    for (int i = 0; i < response.body().size(); i++) {
//
//                    }

                    childListAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Child>> call, Throwable t) {
                hideDialog();
                Log.e("error", t.getMessage());
            }
        });
    }
    AlertDialog spotsDialog;

    private void showDialog(String message) {

        if (spotsDialog == null) {
            spotsDialog = new SpotsDialog.Builder()
                    .setContext(getActivity())
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
