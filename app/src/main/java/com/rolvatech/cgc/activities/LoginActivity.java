package com.rolvatech.cgc.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

//import com.amitshekhar.DebugDB;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.JsonObject;
import com.rolvatech.cgc.BuildConfig;
import com.rolvatech.cgc.R;
import com.rolvatech.cgc.utils.AlertDialogManager;
import com.rolvatech.cgc.utils.Constants;
import com.rolvatech.cgc.utils.PrefUtils;
import com.rolvatech.cgc.webaccess.ApiRequest;
import com.rolvatech.cgc.webaccess.ApiResponseListener;
import com.rolvatech.cgc.webaccess.NetworkRequestModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import dmax.dialog.SpotsDialog;
import okhttp3.Call;
import okhttp3.Response;

import static com.rolvatech.cgc.webaccess.NetworkUtils.Method.POST;

@SuppressLint("Registered")
public class LoginActivity extends AppCompatActivity {

    TextInputEditText emailAddress, password;
    private Button btnLogin;
    ProgressBar progressBar;
    TextView textView, forgot_password, sign_up;
    boolean isDownloading = false;
    AlertDialogManager alertDialogManager;
    private static final int PERMISSIONS_REQUEST = 1234;
    // private Toast mToast;

    @RequiresApi(api = Build.VERSION_CODES.M)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initializeControls();
        alertDialogManager = new AlertDialogManager();
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        // instantiate it within the onCreate method
        checkPermissions();
        //Log.e("address", DebugDB.getAddressLog());
        progressBar = findViewById(R.id.progress_circular);
        textView = findViewById(R.id.downloading_text);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  moveForward();
                if (TextUtils.isEmpty(emailAddress.getText().toString())) {
                    Toast.makeText(getApplicationContext(), getString(R.string.please_enter_username), Toast.LENGTH_LONG).show();
                } else if (TextUtils.isEmpty(password.getText().toString())) {
                    Toast.makeText(getApplicationContext(), getString(R.string.please_enter_password), Toast.LENGTH_LONG).show();
                } else {
                    if (!isNetworkAvailable()) {
                        validateOffline();
                    } else {

                        try {
                            //   showLoader();
                            JSONObject jsonObject = new JSONObject();

                            //JSONObject loginJsonObject = new JSONObject();
                            jsonObject.put("username", emailAddress.getText().toString());
                            jsonObject.put("password", password.getText().toString());
                            //jsonObject.put("", loginJsonObject);

                            Log.e("request"
                                    , jsonObject.toString());


                            NetworkRequestModel model = new NetworkRequestModel();
                            model.setMethod(POST);
                            model.setBody(jsonObject.toString());
                            model.setUrl(com.rolvatech.cgc.BuildConfig.SERVER_URL
                                    + Constants.API_LOGIN);
showDialog();
                            ApiRequest.getInstance().setRequestModel(model)
                                    .callApi(new ApiResponseListener() {
                                        @Override
                                        public void onResponse(Call call, final Response response) {
                                            hideDialog();
                                            if (response.isSuccessful() && response.code() == 200) {
                                                try {
                                                    assert response.body() != null;
//                                                    JSONObject jsonObject = new JSONObject(response.body().string());
//                                                    //Log.e("response_auth", jsonObject.toString());
//
//                                                    JSONObject loginJsonObject = jsonObject.optJSONObject("loginResponse");
                                                    JSONObject jsonObject = new JSONObject(response.body().string());
                                                    if (jsonObject.optJSONObject("userDetails") != null) {
                                                        Log.e("splash", jsonObject.toString());

                                                        PrefUtils.setStringPreference(LoginActivity.this, PrefUtils.USER_NAME,
                                                                jsonObject.
                                                                        optJSONObject("userDetails").optString("userName"));

                                                        PrefUtils.setStringPreference(LoginActivity.this, PrefUtils.USER_ID,
                                                                jsonObject.optJSONObject("userDetails").optString("id"));

                                                        PrefUtils.setStringPreference(LoginActivity.this, PrefUtils.FIRST_NAME,
                                                                jsonObject.optJSONObject("userDetails").optString("firstName"));
                                                        PrefUtils.setStringPreference(LoginActivity.this, PrefUtils.LAST_NAME,
                                                                jsonObject.optJSONObject("userDetails").optString("lastName"));
                                                        PrefUtils.setStringPreference(LoginActivity.this, PrefUtils.EMAIL,
                                                                jsonObject.optJSONObject("userDetails").optString("email"));
                                                        PrefUtils.setStringPreference(LoginActivity.this, PrefUtils.CONTACT,
                                                                jsonObject.optJSONObject("userDetails").optString("contact"));
                                                        PrefUtils.setStringPreference(LoginActivity.this, PrefUtils.PASSWORD,
                                                                jsonObject.optJSONObject("userDetails").optString("password"));
                                                        PrefUtils.setStringPreference(LoginActivity.this, PrefUtils.INSTITUTE_ID,
                                                                jsonObject.
                                                                        optJSONObject("userDetails").optJSONObject("institute").optString("id"));

                                                        PrefUtils.setStringPreference(LoginActivity.this, PrefUtils.INSTITUTE_NAME,
                                                                jsonObject.
                                                                        optJSONObject("userDetails").optJSONObject("institute").optString("name"));
                                                        PrefUtils.setStringPreference(LoginActivity.this, PrefUtils.INSTITUTE_CONTACT,
                                                                jsonObject.
                                                                        optJSONObject("userDetails").optJSONObject("institute").optString("contact"));
                                                        PrefUtils.setStringPreference(LoginActivity.this, PrefUtils.INSTITUTE_EMAIL,
                                                                jsonObject.
                                                                        optJSONObject("userDetails").optJSONObject("institute").optString("email"));
                                                        PrefUtils.setStringPreference(LoginActivity.this, PrefUtils.INSTITUTE_ADDRESS,
                                                                jsonObject.
                                                                        optJSONObject("userDetails").optJSONObject("institute").optString("address"));
                                                        PrefUtils.setStringPreference(LoginActivity.this, PrefUtils.ENVIRONMENT,
                                                                BuildConfig.FLAVOR);
                                                        PrefUtils.setStringPreference(LoginActivity.this, PrefUtils.PROFILE_IMAGE,
                                                                jsonObject.
                                                                        optJSONObject("userDetails").optString("profileImage"));
                                                        PrefUtils.setStringPreference(LoginActivity.this,
                                                                PrefUtils.TOKEN,
                                                                jsonObject.optString("token"));
//                                                        PrefUtils.setStringPreference(LoginActivity.this,
//                                                                PrefUtils.ROLE,
//                                                                jsonObject.optString("roles"));
                                                        PrefUtils.setStringPreference(LoginActivity.this, PrefUtils.ENVIRONMENT,
                                                                BuildConfig.FLAVOR);
                                                        PrefUtils.setBooleanPreference(LoginActivity.this, PrefUtils.IS_LOGIN, true);
                                                        Log.e("image", jsonObject.optJSONObject("userDetails").
                                                                optString("profileImage"));
                                                        Log.e("email", jsonObject.optJSONObject("userDetails").
                                                                optString("email"));
//
                                                        int count = PrefUtils.getIntegerPreference(LoginActivity.this, PrefUtils.KEY_IS_FIRSTTIME, 0);
                                                        if (count == 0)
                                                            PrefUtils.setIntegerPreference(LoginActivity.this, PrefUtils.KEY_IS_FIRSTTIME, 1);
                                                        else
                                                            PrefUtils.setIntegerPreference(LoginActivity.this, PrefUtils.KEY_IS_FIRSTTIME, count + 1);
                                                        moveForward();
                                                    } else {
                                                        // hideLoader();
                                                        Toast.makeText(LoginActivity.this, getString(R.string.login_denied), Toast.LENGTH_LONG).show();
                                                        PrefUtils.setStringPreference(LoginActivity.this, PrefUtils.KEY_USER, "");
                                                        PrefUtils.setBooleanPreference(LoginActivity.this, PrefUtils.IS_LOGIN, false);
                                                    }

                                                } catch (JSONException | IOException e) {
                                                    e.printStackTrace();
                                                }

                                            } else {
                                                try {
                                                    assert response.body() != null;
                                                    String error=response.body().string();
                                                    Log.e("error", error);
                                                    JSONObject jsonResponse=new JSONObject(error);
                                                    String message="";
                                                    if(jsonResponse!=null){
                                                        JSONArray details=jsonResponse.optJSONArray("details");
                                                        List<String> detailsList=new ArrayList();
                                                        for (int i=0;i<details.length();i++){
                                                            detailsList.add(details.optString(i));
                                                        }
                                                        if(detailsList.contains("USER_DISABLED")){
                                                            message="Your Account is not Active. Contact Admin.";
                                                        }else if(detailsList.contains("INVALID_CREDENTIALS")){
                                                            message="username/password incorrect.";
                                                        }
                                                    }else{
                                                        message="Something went wrong! Please try again.";
                                                    }
                                                    String finalMessage = message;
                                                    toastMessage(finalMessage);
                                                    PrefUtils.setStringPreference(LoginActivity.this, PrefUtils.KEY_USER, "");
                                                    PrefUtils.setBooleanPreference(LoginActivity.this, PrefUtils.IS_LOGIN, false);
                                                }catch(IOException | JSONException e){
                                                    e.printStackTrace();
                                                }
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call call, IOException e) {
                                            hideDialog();
                                            Log.e("error", Objects.requireNonNull(e.getMessage()));
                                            validateOffline();
                                        }

                                    });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

            }
        });
        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void toastMessage(String finalMessage) {
        Thread thread = new Thread(){
            public void run(){
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast toast=Toast.makeText(LoginActivity.this, finalMessage, Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.BOTTOM,50,50);
                        toast.show();
                    }
                });
            }
        };
        thread.start();
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void validateOffline() {
        //hideLoader();
        boolean allowOffline = PrefUtils.getBooleanPreference(LoginActivity.this,
                PrefUtils.ALLOW_OFFLINE, false);

        if (allowOffline) {
            moveForward();
        } else {
            Toast.makeText(LoginActivity.this, getString(R.string.login_failure), Toast.LENGTH_LONG).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkPermissions() {
        String[] ungrantedPermissions = requiredPermissionsStillNeeded();
        if (ungrantedPermissions.length == 0) {
            // startNextActivity();
        } else {
            requestPermissions(ungrantedPermissions, PERMISSIONS_REQUEST);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private String[] requiredPermissionsStillNeeded() {

        Set<String> permissions = new HashSet<>(Arrays.asList(getRequiredPermissions()));
        for (Iterator<String> i = permissions.iterator(); i.hasNext(); ) {
            String permission = i.next();
            if (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED) {
                Log.d(SplashScreenActivity.class.getSimpleName(),
                        "Permission: " + permission + " already granted.");
                i.remove();
            } else {
            }
        }
        return permissions.toArray(new String[permissions.size()]);
    }

    public String[] getRequiredPermissions() {
        String[] permissions = null;
        try {
            permissions = getPackageManager().getPackageInfo(getPackageName(),
                    PackageManager.GET_PERMISSIONS).requestedPermissions;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (permissions == null) {
            return new String[0];
        } else {
            return permissions.clone();
        }
    }

    private void moveForward() {
//        Intent intent = new Intent(Intent.ACTION_VIEW,
//                Uri.parse(getPackageName() + "://appstart"));
//        intent.addCategory(Intent.CATEGORY_BROWSABLE);
//        intent.setAction(Intent.ACTION_VIEW);
        Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
        PrefUtils.setStringPreference(LoginActivity.this,
                PrefUtils.KEY_USER, emailAddress.getText().toString());

        // if (intent.resolveActivity(getPackageManager()) != null) {
        startActivity(intent);
        finish();
        //}
    }


    private void initializeControls() {
        btnLogin = findViewById(R.id.btnLogin);
        emailAddress = findViewById(R.id.edtUsername);
        password = findViewById(R.id.edtPassword);
        forgot_password = findViewById(R.id.forgot_password);
        sign_up = findViewById(R.id.sign_up);
    }

    AlertDialog spotsDialog;

    private void showDialog() {

        if (spotsDialog == null) {
            spotsDialog = new SpotsDialog.Builder()
                    .setContext(LoginActivity.this)
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
