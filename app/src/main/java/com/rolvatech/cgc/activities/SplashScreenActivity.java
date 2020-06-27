package com.rolvatech.cgc.activities;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


import com.rolvatech.cgc.R;
import com.rolvatech.cgc.utils.PrefUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.rolvatech.cgc.BuildConfig;

/**
 * Created by viya on 12-12-2017.
 */

public class SplashScreenActivity extends AppCompatActivity {

    private static final int PERMISSIONS_REQUEST = 1234;
    // private static PowerManager.WakeLock wakeLock;
    ProgressBar progressBar;


//    @SuppressLint("InvalidWakeLockTag")
//    public static void acquire(Context context) {
//        if (wakeLock != null) wakeLock.release();
//
//        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
//        wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK |
//                PowerManager.ACQUIRE_CAUSES_WAKEUP |
//                PowerManager.ON_AFTER_RELEASE, "WakeLock");
//        wakeLock.acquire(10 * 60 * 1000L /*10 minutes*/);
//
//    }

//    public static void release() {
//        if (wakeLock != null) wakeLock.release();
//        wakeLock = null;
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

//        this.llFooter.setVisibility(View.GONE);

        // instantiate it within the onCreate method
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermissions();
        } else {
            startNextActivity();
        }
        progressBar = findViewById(R.id.progress_circular);
        // PrefUtils.setStringPreference(SplashScreenActivity.this, PrefUtils.KEY_URL, BuildConfig.SERVER_URL);
        /**
         * On a post-Android 6.0 devices, check if the required permissions have
         * been granted.
         */

    }

    private void startNextActivity() {
        new Handler().postDelayed(() -> {
            progressBar.setVisibility(View.GONE);
            try {
                startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
                finish();
            } catch (Exception e) {

            }
        }, 1000);
    }

    /**
     * See if we now have all of the required dangerous permissions. Otherwise,
     * tell the user that they cannot continue without granting the permissions,
     * and then request the permissions again.
     */
    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST) {
            checkPermissions();
        }
    }

    /**
     * Check if the required permissions have been granted, and
     * {@link #startNextActivity()} if they have. Otherwise
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkPermissions() {
        String[] ungrantedPermissions = requiredPermissionsStillNeeded();
        if (ungrantedPermissions.length == 0) {
            startNextActivity();
        } else {
            requestPermissions(ungrantedPermissions, PERMISSIONS_REQUEST);
        }
    }

    /**
     * Get the list of required permissions by searching the manifest. If you
     * don't think the default behavior is working, then you could try
     * overriding this function to return something like:
     * <p>
     * <pre>
     * <code>
     * return new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
     * </code>
     * </pre>
     */
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

    /**
     * Convert the array of required permissions to a {@link Set} to remove
     * redundant elements. Then remove already granted permissions, and return
     * an array of ungranted permissions.
     */
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

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("", "debug test");
    }
}

