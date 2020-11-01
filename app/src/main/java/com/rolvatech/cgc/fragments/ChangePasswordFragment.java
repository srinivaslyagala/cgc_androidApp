package com.rolvatech.cgc.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.rolvatech.cgc.APIClient;
import com.rolvatech.cgc.R;
import com.rolvatech.cgc.dataobjects.JwtRequest;
import com.rolvatech.cgc.utils.AlertDialogManager;
import com.rolvatech.cgc.utils.PrefUtils;

import org.json.JSONException;
import org.json.JSONObject;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ChangePasswordFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    TextInputEditText edtOldPassword, edtNewPassword, edtCnfPassword;
    Button  btnSubmit;
    AlertDialogManager alertDialogManager = new AlertDialogManager();
    Toolbar toolbar;
    public ChangePasswordFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_change_password, container, false);
        edtOldPassword = root.findViewById(R.id.edtOldPassword);
        edtNewPassword = root.findViewById(R.id.edtNewPassword);
        edtCnfPassword = root.findViewById(R.id.edtCnfPassword);
        btnSubmit=root.findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
            }
        });
        return root;
    }

    public void changePassword() {
        if (edtOldPassword.getText().toString().equals(edtNewPassword.getText().toString())) {
            alertDialogManager.showToast(getActivity(), "New Password should not be same as Old Password");
        }
        if (!edtNewPassword.getText().toString().equals(edtCnfPassword.getText().toString())) {
            alertDialogManager.showToast(getActivity(), "new and confirm password not matched");
        } else {
            JwtRequest request=new JwtRequest();
            request.setPassword(edtNewPassword.getText().toString());
            request.setUsername(PrefUtils.getStringPreference(getActivity(), PrefUtils.KEY_USER));
            showDialog();
            new APIClient(getActivity()).getApi().updatePassword("Bearer " + PrefUtils.getStringPreference(getActivity(), PrefUtils.TOKEN), request).enqueue(new Callback<JSONObject>() {
                @Override
                public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                    hideDialog();
                    if (response.isSuccessful() || response.code() == 200) {
                        alertDialogManager.showAlertDialog(getActivity(), "Success", "Password changed successfully", true);
                    } else {
                        alertDialogManager.showAlertDialog(getActivity(), "Failure", "Password changing failure", false);
                    }
                }

                @Override
                public void onFailure(Call<JSONObject> call, Throwable t) {
                    hideDialog();
                }
            });

    }
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
