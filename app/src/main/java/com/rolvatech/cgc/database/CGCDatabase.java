//package com.rolvatech.cgc.database;
//
//import android.annotation.SuppressLint;
//import android.content.Context;
//import android.os.Environment;
//
//import androidx.room.Database;
//import androidx.room.Room;
//import androidx.room.RoomDatabase;
//import androidx.room.TypeConverters;
//
//import com.rolvatech.cgc.dataaccess.CgcDao;
//import com.rolvatech.cgc.dataobjects.AdminDTO;
//import com.rolvatech.cgc.dataobjects.AreaDTO;
//import com.rolvatech.cgc.dataobjects.ChildTaskDTO;
//import com.rolvatech.cgc.dataobjects.ImageData;
//import com.rolvatech.cgc.dataobjects.InstituteDTO;
//import com.rolvatech.cgc.dataobjects.JwtRequest;
//import com.rolvatech.cgc.dataobjects.JwtResponse;
//import com.rolvatech.cgc.dataobjects.StaffDTO;
//import com.rolvatech.cgc.dataobjects.SubTaskDTO;
//import com.rolvatech.cgc.dataobjects.TaskDTO;
//import com.rolvatech.cgc.dataobjects.UserDTO;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.nio.channels.FileChannel;
//
//@Database(entities = {AreaDTO.class, ChildTaskDTO.class, TaskDTO.class, AdminDTO.class, ImageData.class, InstituteDTO.class, JwtRequest.class, JwtResponse.class, StaffDTO.class, SubTaskDTO.class, UserDTO.class}, version = 1)
//@TypeConverters({DataConverters.class})
//public abstract class CGCDatabase extends RoomDatabase {
//    public static final String DATABASE_NAME = "cgc.db";
//    public static final String TABLE_NAME_USER = "user";
//    public static final String TABLE_NAME_AREA = "area";
//    public static final String TABLE_NAME_AREA_TASK = "area_task";
//    public static final String TABLE_NAME_CHILD_TASK = "child_task";
//    public static final String TABLE_NAME_IMAGE_DATA = "image_data";
//    public static final String TABLE_NAME_INSTITUTE = "institude";
//    public static final String TABLE_NAME_JWT_REQUEST = "jwt_request";
//    public static final String TABLE_NAME_JWT_RESPONSE = "jwt_response";
//    public static final String TABLE_NAME_STAFF = "staff";
//    public static final String TABLE_NAME_SUBTASK = "sub_task";
//    public static final String TABLE_NAME_TASK = "task";
//
//    private static final Object sLock = new Object();
//    private static CGCDatabase INSTANCE;
//
//    public static CGCDatabase getInstance(Context context) {
//        synchronized (sLock) {
//            if (INSTANCE == null) {
//                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
//                        CGCDatabase.class, DATABASE_NAME)
//                        // .addMigrations(MIGRATION_8_9)
//                        .build(); //.addMigrations(MIGRATION_5_6)
//            }
//            return INSTANCE;
//        }
//    }
//
//    public static void uploadDatabase(Context context) {
//
//        try {
//            File file = Environment.getExternalStorageDirectory();
//
//            if (file.canWrite()) {
//                @SuppressLint("SdCardPath")
//                String currentPath = "/data/data/" + context.getPackageName() + "/databases/" + DATABASE_NAME;
//                File currentDB = new File(currentPath);
//                File backupDB = new File(file, DATABASE_NAME);
//                if (currentDB.exists()) {
//                    FileChannel src = new FileInputStream(currentDB).getChannel();
//                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
//                    dst.transferFrom(src, 0, src.size());
//                    src.close();
//                    dst.close();
//                }
//            }
//        } catch (Exception ignored) {
//
//        }
//    }
//
//    public abstract CgcDao cgcDao();
//}
