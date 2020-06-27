package com.rolvatech.cgc.utils;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.UUID;

/**
 * Created by viya on 16-10-2017.
 */

public class Methods {


    public static boolean isOnline(Context context) {
        boolean status = false;
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = connectivityManager.getNetworkInfo(0);
            if (netInfo != null
                    && netInfo.getState() == NetworkInfo.State.CONNECTED) {
                status = true;
            } else {
                netInfo = connectivityManager.getNetworkInfo(1);
                if (netInfo != null
                        && netInfo.getState() == NetworkInfo.State.CONNECTED)
                    status = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return status;
    }

    /**
     * get unique device id, reuire permission READ_PHONE_STATE
     *
     * @param context
     * @return unique id string
     */
    public static String getDeviceId(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String deviceID = telephonyManager.getDeviceId();
        // tablet device will return null
        if (TextUtils.isEmpty(deviceID)) {
            // maybe change when factory reset
            deviceID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            if (TextUtils.isEmpty(deviceID)) {
                deviceID = UUID.randomUUID().toString();
            }
        }
        return deviceID;
    }


    public static String getExtentionOfFile(String ex) {
        try {
            return ex.contains(".") ? ex.substring(ex.lastIndexOf(".") + 1, ex.length()) : "";
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static void exportDB(String packageName, String SAMPLE_DB_NAME) {
        File sd = Environment.getExternalStorageDirectory();
        File data = Environment.getDataDirectory();
        FileChannel source = null;
        FileChannel destination = null;
        String currentDBPath = "/data/" + packageName + "/databases/" + SAMPLE_DB_NAME;
        String backupDBPath = SAMPLE_DB_NAME;
        File currentDB = new File(data, currentDBPath);
        File backupDB = new File(sd, backupDBPath);
        try {
            source = new FileInputStream(currentDB).getChannel();
            destination = new FileOutputStream(backupDB).getChannel();
            destination.transferFrom(source, 0, source.size());
            source.close();
            destination.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Activity scanForActivity(Context cont) {
        if (cont == null)
            return null;
        else if (cont instanceof Activity)
            return (Activity) cont;
        else if (cont instanceof ContextWrapper)
            return scanForActivity(((ContextWrapper) cont).getBaseContext());

        return null;
    }


}
