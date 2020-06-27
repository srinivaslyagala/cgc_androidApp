package com.rolvatech.cgc.application;

import android.content.Context;
import android.net.TrafficStats;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.evernote.android.job.JobManager;
import com.facebook.stetho.Stetho;
import com.rolvatech.cgc.httpimage.FileSystemPersistence;
import com.rolvatech.cgc.httpimage.HttpImageManager;
import com.rolvatech.cgc.utils.FilesStorage;
import com.rolvatech.cgc.utils.PrefUtils;

import java.io.File;
import java.util.Timer;

public class CGCApplication extends MultiDexApplication {
    private static CGCApplication cgcApplication;

    private HttpImageManager mHttpImageManager;
   // private LogoutListener listener;
   // private Timer timer;
    public static CGCApplication app() {
        return cgcApplication;
    }

//    public static boolean deleteFile(File file) {
//        boolean deletedAll = true;
//        if (file != null) {
//            if (file.isDirectory()) {
//                String[] children = file.list();
//                for (String aChildren : children) {
//                    deletedAll = deleteFile(new File(file, aChildren)) && deletedAll;
//                }
//            } else {
//                deletedAll = file.delete();
//            }
//        }
//
//        return deletedAll;
//    }

    public HttpImageManager getHttpImageManager() {
        return mHttpImageManager;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate() {
        super.onCreate();
        cgcApplication = this;
//        Stetho.initialize(Stetho.newInitializerBuilder(this)
//                .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
//                .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
//                .build());
        mHttpImageManager = new HttpImageManager(
                HttpImageManager.createDefaultMemoryCache(),
                new FileSystemPersistence(FilesStorage.getImageCacheDirectory()));
        //JobManager.create(this).addJobCreator(new IFLJobCreator());
        Log.e("bytes", String.valueOf(TrafficStats.getTotalRxBytes()));
    }

//    public void clearApplicationData() {
//        File cacheDirectory = getCacheDir();
//        File applicationDirectory = new File(cacheDirectory.getParent());
//        if (applicationDirectory.exists()) {
//            String[] fileNames = applicationDirectory.list();
//            for (String fileName : fileNames) {
//                if (!fileName.equals("lib")) {
//                    deleteFile(new File(applicationDirectory, fileName));
//                }
//            }
//        }
//        PrefUtils.clearPreference(this);
//    }
}
