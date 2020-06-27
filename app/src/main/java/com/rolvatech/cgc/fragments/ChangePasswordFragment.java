package com.rolvatech.cgc.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.rolvatech.cgc.APIClient;
import com.rolvatech.cgc.R;
import com.rolvatech.cgc.utils.AlertDialogManager;
import com.rolvatech.cgc.utils.PrefUtils;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ChangePasswordFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    TextInputEditText edtOldPassword, edtNewPassword, edtCnfPassword;

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
        if (!edtOldPassword.getText().toString().equals(PrefUtils.getStringPreference(getActivity(), PrefUtils.PASSWORD))) {
            alertDialogManager.showToast(getActivity(), "Enter correct old password");
        } else if (!edtOldPassword.getText().toString().equals(edtCnfPassword.getText().toString())) {
            alertDialogManager.showToast(getActivity(), "new and confirm password not matched");
        } else {
            changePassword();
        }

        return root;
    }

    public void changePassword() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("password", edtNewPassword.getText().toString());
            jsonObject.put("username", PrefUtils.getStringPreference(getActivity(), PrefUtils.KEY_USER));
            new APIClient(getActivity()).getApi().updatePassword("Bearer " + PrefUtils.getStringPreference(getActivity(), PrefUtils.TOKEN), jsonObject).enqueue(new Callback<JSONObject>() {
                @Override
                public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                    if (response.isSuccessful() || response.code() == 200) {
                        alertDialogManager.showAlertDialog(getActivity(), "Success", "Password changed successfully", true);
                    } else {
                        alertDialogManager.showAlertDialog(getActivity(), "Failure", "Password changing failure", false);
                    }
                }

                @Override
                public void onFailure(Call<JSONObject> call, Throwable t) {

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
