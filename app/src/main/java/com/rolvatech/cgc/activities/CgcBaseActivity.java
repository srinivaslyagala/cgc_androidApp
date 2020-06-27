package com.rolvatech.cgc.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.snackbar.Snackbar;
import com.rolvatech.cgc.R;
import com.rolvatech.cgc.dialog.CgcDialog;


/**
 * Created by admin on 9/21/2017.
 */

public abstract class CgcBaseActivity extends AppCompatActivity {

    public LinearLayout llContent, llFooter, llLoading, llLogOut;


    public LayoutInflater mLayoutInflater;

  //  private Toolbar mToolbar;

   // private TextView tvVersion, tvApplicationType, tvScreenName, tvUserName, tvClient, tvSite, tvMessage, tvEnvironment;

    private Snackbar snackbar;

    private Toast mToast;

    public Button btnLogout;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_cgc_base);
        initializeControls();
        initialize();
    }

    private void initializeControls() {
        mLayoutInflater = getLayoutInflater();
        llContent = (LinearLayout) findViewById(R.id.llContent);
        llLoading = (LinearLayout) findViewById(R.id.llLoading);
        llLogOut = (LinearLayout) findViewById(R.id.llLogOut);

      //  tvMessage = (TextView) findViewById(R.id.tvMessage);
        btnLogout = (Button) findViewById(R.id.btnLogout);
    }

    public void setActiveContentView(int resource) {
        addContentView(mLayoutInflater.inflate(resource, null));
    }

    public void setActiveContentView(View resource) {
        addContentView(resource);
    }

    @SuppressLint("SetTextI18n")
    private void addContentView(View view) {
        llContent.addView(view, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

//        tvVersion.setText(getString(R.string.version) + " " + getVersionName());
//        tvApplicationType.setText("Matrix IFL");
//        tvEnvironment.setText(PrefUtils.getStringPreference(this, PrefUtils.SYNC_TIME) + "Upload" + PrefUtils.getStringPreference(this, PrefUtils.UPLOAD_TIME));
        //tvEnvironment.setText(this.getTitle());
    }

    public String getVersionName() {
        try {
            return getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

//    public void setScreeName(String screenName) {
//        tvScreenName.setText(screenName);
//    }

//    public void setFlavour(String flavour) {
//
//        tvUserName.setText(flavour);
//
//
//    }

//    public void setVisibility() {
//        tvEnvironment.setVisibility(View.VISIBLE);
//
//    }

    public void hideToolbar() {
//        mToolbar.setVisibility(View.GONE);
    }

    public abstract void initialize();

    public void showSnackBar(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (snackbar == null) {
                    snackbar = Snackbar
                            .make(llContent, message, Snackbar.LENGTH_LONG);

                    TextView tv = (TextView) snackbar.getView().findViewById(R.id.snackbar_text);
                    View view = snackbar.getView();
                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
                    params.gravity = Gravity.TOP;
                    view.setLayoutParams(params);
                    tv.setTextColor(Color.WHITE);
                }
                snackbar.setText(message);
                snackbar.show();
            }
        });
    }

    public void showToast(final String message) {
        runOnUiThread(new Runnable() {
            @SuppressLint("ShowToast")
            @Override
            public void run() {
                if (mToast == null) {
                    mToast = Toast
                            .makeText(CgcBaseActivity.this, message, Toast.LENGTH_LONG);
                }
                mToast.setText(message);
                mToast.show();
            }
        });
    }

    public void hideSnackBar() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (snackbar != null) {
                    snackbar.dismiss();
                }
            }
        });
    }

    public void showLoader() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                llLoading.setVisibility(View.VISIBLE);
            }
        });
    }

    public void showLoader(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                llLoading.setVisibility(View.VISIBLE);
                //tvMessage.setText(message);
            }
        });
    }

    public void hideLoader() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                llLoading.setVisibility(View.GONE);
            }
        });
    }

    public void showCustomDialog(final String title, final String content,
                                 final String positiveText, final String negativeText) {
        new CgcDialog.Builder(CgcBaseActivity.this)
                .setTitle(title)
                .setContent(content)
                .setPositiveText(positiveText)
                .setNegativeText(negativeText)
                .show();
    }

    public void showKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    public void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void testMetshod() {

    }
}
