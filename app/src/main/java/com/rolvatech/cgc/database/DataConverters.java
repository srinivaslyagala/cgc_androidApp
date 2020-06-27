//package com.rolvatech.cgc.database;
//
//import androidx.room.TypeConverter;
//
//import com.google.gson.Gson;
//import com.google.gson.reflect.TypeToken;
//import com.rolvatech.cgc.dataobjects.AreaDTO;
//import com.rolvatech.cgc.dataobjects.AreaTaskDTO;
//import com.rolvatech.cgc.dataobjects.ChildTaskDTO;
//import com.rolvatech.cgc.dataobjects.InstituteDTO;
//import com.rolvatech.cgc.dataobjects.JwtRequest;
//import com.rolvatech.cgc.dataobjects.JwtResponse;
//import com.rolvatech.cgc.dataobjects.StaffDTO;
//import com.rolvatech.cgc.dataobjects.SubTaskDTO;
//import com.rolvatech.cgc.dataobjects.TaskDTO;
//import com.rolvatech.cgc.dataobjects.UserDTO;
//
//import java.lang.reflect.Type;
//
//public class DataConverters {
//
//    @TypeConverter
//    public static AreaDTO fromAreaDTOString(String value) {
//        Type listType = new TypeToken<AreaDTO>() {
//        }.getType();
//        return new Gson().fromJson(value, listType);
//    }
//
//    @TypeConverter
//    public static String fromAreaDTOArrayList(AreaDTO list) {
//        Gson gson = new Gson();
//        return gson.toJson(list);
//    }
//
//    @TypeConverter
//    public static AreaTaskDTO fromAreaTaskDTOString(String value) {
//        Type listType = new TypeToken<AreaTaskDTO>() {
//        }.getType();
//        return new Gson().fromJson(value, listType);
//    }
//
//    @TypeConverter
//    public static String fromAreaTaskDTOArrayList(AreaTaskDTO list) {
//        Gson gson = new Gson();
//        return gson.toJson(list);
//    }
//
//    @TypeConverter
//    public static ChildTaskDTO fromChildTaskDTOString(String value) {
//        Type listType = new TypeToken<AreaTaskDTO>() {
//        }.getType();
//        return new Gson().fromJson(value, listType);
//    }
//
//    @TypeConverter
//    public static String fromChildTaskDTOArrayList(ChildTaskDTO list) {
//        Gson gson = new Gson();
//        return gson.toJson(list);
//    }
//
//    @TypeConverter
//    public static InstituteDTO fromInstituteDTOString(String value) {
//        Type listType = new TypeToken<InstituteDTO>() {
//        }.getType();
//        return new Gson().fromJson(value, listType);
//    }
//
//    @TypeConverter
//    public static String fromInstituteDTOArrayList(InstituteDTO list) {
//        Gson gson = new Gson();
//        return gson.toJson(list);
//    }
//
//    @TypeConverter
//    public static JwtRequest fromJwtRequestString(String value) {
//        Type listType = new TypeToken<JwtRequest>() {
//        }.getType();
//        return new Gson().fromJson(value, listType);
//    }
//
//    @TypeConverter
//    public static String fromJwtRequestArrayList(JwtRequest list) {
//        Gson gson = new Gson();
//        return gson.toJson(list);
//    }
//
//    @TypeConverter
//    public static JwtResponse fromJwtResponseString(String value) {
//        Type listType = new TypeToken<JwtResponse>() {
//        }.getType();
//        return new Gson().fromJson(value, listType);
//    }
//
//    @TypeConverter
//    public static String fromJwtResponseArrayList(JwtResponse list) {
//        Gson gson = new Gson();
//        return gson.toJson(list);
//    }
//
//    @TypeConverter
//    public static StaffDTO fromStaffDTOString(String value) {
//        Type listType = new TypeToken<StaffDTO>() {
//        }.getType();
//        return new Gson().fromJson(value, listType);
//    }
//
//    @TypeConverter
//    public static String fromStaffDTOArrayList(StaffDTO list) {
//        Gson gson = new Gson();
//        return gson.toJson(list);
//    }
//
//    @TypeConverter
//    public static SubTaskDTO fromSubTaskDTOString(String value) {
//        Type listType = new TypeToken<SubTaskDTO>() {
//        }.getType();
//        return new Gson().fromJson(value, listType);
//    }
//
//    @TypeConverter
//    public static String fromSubTaskDTOArrayList(SubTaskDTO list) {
//        Gson gson = new Gson();
//        return gson.toJson(list);
//    }
//
//    @TypeConverter
//    public static TaskDTO fromTaskDTOString(String value) {
//        Type listType = new TypeToken<TaskDTO>() {
//        }.getType();
//        return new Gson().fromJson(value, listType);
//    }
//
//    @TypeConverter
//    public static String fromTaskDTOArrayList(TaskDTO list) {
//        Gson gson = new Gson();
//        return gson.toJson(list);
//    }
//
//    @TypeConverter
//    public static UserDTO fromUserDTOString(String value) {
//        Type listType = new TypeToken<UserDTO>() {
//        }.getType();
//        return new Gson().fromJson(value, listType);
//    }
//
//    @TypeConverter
//    public static String fromUserDTOArrayList(UserDTO list) {
//        Gson gson = new Gson();
//        return gson.toJson(list);
//    }
//}
