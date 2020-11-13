package com.rolvatech.cgc.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.rolvatech.cgc.APIClient;
import com.rolvatech.cgc.R;
import com.rolvatech.cgc.dataobjects.Stastics;
import com.rolvatech.cgc.utils.PrefUtils;
import com.rolvatech.cgc.viewmodels.HomeViewModel;

import java.util.List;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    RelativeLayout childMenu, areaMenu, staffMenu, reportMenu;
    TextView txtStaffCount, txtChildCount, txtAreaCount, txtReportCount;
    Fragment fragment = null;
    FragmentManager fm;
    FragmentTransaction fragmentTransaction;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        childMenu = root.findViewById(R.id.child_menu);
        staffMenu = root.findViewById(R.id.staff_menu);
        areaMenu = root.findViewById(R.id.area_menu);
        reportMenu = root.findViewById(R.id.report_menu);
        txtAreaCount = root.findViewById(R.id.txtAreaCount);
        txtChildCount = root.findViewById(R.id.txtChildCount);
        txtStaffCount = root.findViewById(R.id.txtStaffCount);
        txtReportCount = root.findViewById(R.id.txtReportCount);
        getStastics();
        childMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment = new ChildListFragment();
                loadView(fragment);
            }
        });
        staffMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment = new StaffListFragment();
                loadView(fragment);
            }
        });
        areaMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment = new AreaListFragment();
                loadView(fragment);
            }
        });
        reportMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        return root;
    }

    public void getStastics() {
        showDialog();
        new APIClient(getActivity()).getApi().getStastics("Bearer " + PrefUtils.getStringPreference(getActivity(), PrefUtils.TOKEN)).enqueue(new Callback<Stastics>() {
            @Override
            public void onResponse(Call<Stastics> call, Response<Stastics> response) {
                hideDialog();
                if (response.code() == 200 && response.isSuccessful()) {
                    assert response.body() != null;
                    txtAreaCount.setText(response.body().getAREAS());
                    txtChildCount.setText(response.body().getCHILD());
                    txtStaffCount.setText(response.body().getSTAFF());
                    //txtReportCount.setText(response.body().getREPORT());
                }

            }

            @Override
            public void onFailure(Call<Stastics> call, Throwable t) {
                hideDialog();
            }
        });
    }

    public void loadView(Fragment fragment) {
        fm = getActivity().getSupportFragmentManager();
// create a FragmentTransaction to begin the transaction and replace the Fragment
        fragmentTransaction = fm.beginTransaction();
// replace the FrameLayout with new Fragment
        fragmentTransaction.replace(R.id.content_frame, fragment);
        fragmentTransaction.commit();
    }
    AlertDialog spotsDialog;

    private void showDialog() {

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
