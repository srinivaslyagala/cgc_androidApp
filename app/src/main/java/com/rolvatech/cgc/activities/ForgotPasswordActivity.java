package com.rolvatech.cgc.activities;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.rolvatech.cgc.R;
import com.rolvatech.cgc.utils.Constants;
import com.rolvatech.cgc.utils.PrefUtils;
import com.rolvatech.cgc.webaccess.ApiRequest;
import com.rolvatech.cgc.webaccess.ApiResponseListener;
import com.rolvatech.cgc.webaccess.NetworkRequestModel;

import java.io.IOException;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Response;

import static com.rolvatech.cgc.webaccess.NetworkUtils.Method.GET;

public class ForgotPasswordActivity extends CgcBaseActivity {

    TextInputEditText emailAddress;
    private Button btnSubmit;
    TextView sign_in, sign_up;
    //boolean isDownloading = false;

    @Override
    public void initialize() {
        setContentView(R.layout.activity_forgot_password);
        initializeControls();
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(emailAddress.getText().toString())) {
                    showToast(getString(R.string.please_enter_username));
                } else {
                    if (isNetworkAvailable()) {
                        try {

                            showLoader();


                            NetworkRequestModel model = new NetworkRequestModel();
                            model.setMethod(GET);
                            model.setUrl(PrefUtils.getStringPreference(ForgotPasswordActivity.this, PrefUtils.KEY_URL)
                                    + Constants.API_FORGOT_PASSWORD + "?" + emailAddress.getText().toString());

                            ApiRequest.getInstance().setRequestModel(model)
                                    .callApi(new ApiResponseListener() {
                                        @Override
                                        public void onResponse(Call call, final Response response) {
                                            if (response.isSuccessful() && response.code() == 200) {
                                                try {
                                                    assert response.body() != null;
//                                                    JSONObject jsonObject = new JSONObject(response.body().string());
//                                                    //Log.e("response_auth", jsonObject.toString());
//
//                                                    JSONObject loginJsonObject = jsonObject.optJSONObject("loginResponse");
                                                    hideLoader();
                                                    showToast(getString(R.string.login_denied));
                                                    PrefUtils.setStringPreference(ForgotPasswordActivity.this, PrefUtils.KEY_USER, "");
                                                    Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                                                    startActivity(intent);

                                                } finally {
                                                    hideLoader();
                                                }

                                            } else {
                                                ForgotPasswordActivity.this.hideLoader();
                                                ForgotPasswordActivity.this.showCustomDialog(ForgotPasswordActivity.this.getString(R.string.alert), ForgotPasswordActivity.this.getString(R.string.login_failure), ForgotPasswordActivity.this.getString(R.string.ok), null);
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call call, IOException e) {
                                            Log.e("error", Objects.requireNonNull(e.getMessage()));

                                        }

                                    });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initializeControls() {
        btnSubmit = findViewById(R.id.btnSubmit);
        emailAddress = findViewById(R.id.edtUsername);
        sign_in = findViewById(R.id.sign_in);
        sign_up = findViewById(R.id.sign_up);
    }
}
