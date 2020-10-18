package com.rolvatech.cgc;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.rolvatech.cgc.dataobjects.AreaDTO;
import com.rolvatech.cgc.dataobjects.AreaTaskDTO;
import com.rolvatech.cgc.dataobjects.Child;
import com.rolvatech.cgc.dataobjects.ChildTaskDTO;
import com.rolvatech.cgc.dataobjects.InstituteDTO;
import com.rolvatech.cgc.dataobjects.StaffDTO;
import com.rolvatech.cgc.dataobjects.Stastics;
import com.rolvatech.cgc.dataobjects.SubTaskDTO;
import com.rolvatech.cgc.dataobjects.TaskDTO;
import com.rolvatech.cgc.dataobjects.UserDTO;
import com.rolvatech.cgc.model.UserDetailsResponse;
import com.rolvatech.cgc.utils.Constants;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;


/**
 * Created by Manoj on 25/06/16.
 */
public class APIClient {


    private ApiService mApiService;

    public APIClient(Context context) {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .connectTimeout(2, TimeUnit.MINUTES)
                .writeTimeout(2, TimeUnit.MINUTES)
                .readTimeout(2, TimeUnit.MINUTES)
                .build();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BuildConfig.SERVER_URL).client(client).addConverterFactory(GsonConverterFactory.create(gson)).build();
        mApiService = retrofit.create(ApiService.class);

    }

    public ApiService getApi() {

        return mApiService;
    }


    public interface ApiService {

        @GET("user/getStatistics")
        Call<Stastics> getStastics(@Header("Authorization") String accessToken);

        @GET("areaTask/getAreas")
        Call<List<AreaDTO>> getAreas(@Header("Authorization") String accessToken);

        @GET("user/getAllChildrens")
        Call<List<Child>> getChild(@Header("Authorization") String accessToken);

        @GET("institute/getInstitute")
        Call<List<InstituteDTO>> getInstitute(@Header("Authorization") String accessToken);

        @GET("user/getunAssignedChildren")
        Call<List<Child>> getunAssignedChildren(@Header("Authorization") String accessToken);

        @GET("user/getStaffList")
        Call<List<UserDTO>> getStaff(@Header("Authorization") String accessToken);

        @GET("areaTask/getAllTasksByAreaOrder")
        Call<List<AreaTaskDTO>> getAllTasksByAreaOrder(@Header("Authorization") String accessToken);

        @GET("areaTask/getTasksByArea")
        Call<JSONObject> getTasksByArea(@Header("Authorization") String accessToken, @Query("areaNumber") String areaNumber);

        @GET("areaTask/getChildTasks")
        Call<JSONObject> getChildTasks(@Header("Authorization") String accessToken, @Query("childId") String childId);

        @GET("areaTask/getSubTasks/{parentTaskId}")
        Call<TaskDTO> getSubTasks(@Header("Authorization") String accessToken, @Path("parentTaskId") String parentTaskId);

        @GET("user/uploadImage/{userId}")
        Call<JSONObject> uploadImage(@Header("Authorization") String accessToken, @Path("userId") String userId);

        @GET("user/getProfileImage/{userId}")
        Call<String> getProfileImage(@Header("Authorization") String accessToken, @Path("userId") String userId);

        @POST("user/registerChild")
        Call<UserDTO> registerChild(@Header("Authorization") String accessToken, @Body UserDTO user);

        @POST("user/registerAdmin")
        Call<JSONObject> registerAdmin(@Header("Authorization") String accessToken, @Body JSONObject jsonObject);

        @POST("user/registerStaff")
        Call<UserDTO> registerStaff(@Header("Authorization") String accessToken, @Body UserDTO user);

        @POST("institute/registerInstitute")
        Call<JSONObject> registerInstitute(@Header("Authorization") String accessToken, @Body JSONObject jsonObject);

        @POST("areaTask/createArea")
        Call<JSONObject> createArea(@Header("Authorization") String accessToken, @Header("areaName") String areaName);

        @POST("areaTask/createTask")
        Call<TaskDTO> createTask(@Header("Authorization") String accessToken, @Body TaskDTO jsonObject);

        @POST("areaTask/addSubTasktoChild")
        Call<SubTaskDTO> addSubTasktoChild(@Header("Authorization") String accessToken, @Body SubTaskDTO jsonObject);

        @POST("areaTask/updateSubTask")
        Call<SubTaskDTO> updateSubTask(@Header("Authorization") String accessToken, @Body SubTaskDTO subTaskDTO);

        @POST("user/updateProfile")
        Call<UserDTO> updateProfile(@Header("Authorization") String accessToken, @Body UserDTO user);

        @POST("user/updatePassword")
        Call<JSONObject> updatePassword(@Header("Authorization") String accessToken, @Body JSONObject jsonObject);

        @POST("areaTask/updateArea")
        Call<JSONObject> updateArea(@Header("Authorization") String accessToken, @Body JSONObject jsonObject);

        @POST("user/deAssignStafftoChild")
        Call<UserDTO> deAssignStafftoChild(@Header("Authorization") String accessToken, @Body UserDTO jsonObject);

        @POST("user/assignStafftoChild")
        Call<UserDTO> assignStafftoChild(@Header("Authorization") String accessToken, @Body UserDTO jsonObject);

        @POST("areaTask/assignTaskToChild")
        Call<ChildTaskDTO> assignTaskToChild(@Header("Authorization") String accessToken, @Body ChildTaskDTO childTaskDTO);

        @POST("areaTask/deassignTaskToChild")
        Call<ChildTaskDTO> deAssignTaskToChild(@Header("Authorization") String accessToken, @Body ChildTaskDTO childTaskDTO);

        //1
        @GET("user/getUser/{userId}")
        Call<UserDTO> getUserDetailsById(@Header("Authorization") String accessToken, @Path("userId") Long userId);

        @GET("user/deactivateUser/{userId}")
        Call<UserDTO> deactivateUser(@Header("Authorization") String accesToken, @Path("userId") Long userId);

        @GET("user/getStatistics")
        Call<JsonObject> getStatistics(@Header("Authorization") String accessToken);

        @GET("user/getChildrensforStaff/{staffId}")
        Call<List<Child>> getChildListForStaff(@Header("Authorization") String accessToken, @Path("staffId") Long staffId);

        @GET("areaTask/getChildTasksByArea/{childId}")
        Call<List<AreaTaskDTO>> getChildTasksByArea(@Header("Authorization") String accessToken, @Path("childId") Long childId);

        @GET("areaTask/getTasksByArea/{areaId}")
        Call<List<TaskDTO>> getTasksforArea(@Header("Authorization") String accessToken, @Path("areaId") Long areaId);

        @GET("user/getChildReport/{userId}")
        Call<UserDetailsResponse> getChildReportDetails(@Header("Authorization") String accessToken, @Path("userId") Long childId);
    }

}

