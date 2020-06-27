package com.rolvatech.cgc.utils;


public class Constants {
    public static boolean APK_MODE_RELEASE = false;

    public static String APP_TAG = "ceva";

    public static String API_AREA_TASK = "areaTask";

    public static String API_USER = "user/";

    public static String API_INSTITUTE = "institute/";

    public static String API_LOGIN = "authenticate";

    public static String API_UPDATE_SUB_TASK = API_AREA_TASK + "/updateSubTask/";

    public static String API_GET_SUB_TASK = API_AREA_TASK + "/getSubTasks/";

    public static String API_FORGOT_PASSWORD = "forgotPassword/";

    public static String API_ADD_SUB_TASK_TO_CHILD = API_AREA_TASK + "/addSubTasktoChild/";

    public static String API_GET_CHILD_TASKS_BY_AREA = API_AREA_TASK + "/getChildTasksByArea/";

    public static String API_GET_CHILD_TASKS = API_AREA_TASK + "/getChildTasks/";

    public static String API_ASSIGN_TASK_TO_CHILD = API_AREA_TASK + "/assignTaskToChild";

    public static final String API_GET_AREAS = API_AREA_TASK + "/getAreas/";

    public static String API_UPDATE_AREA = API_AREA_TASK + "/updateArea/";

    public static String API_CREATE_AREA = API_AREA_TASK + "/createArea/";

    public static String API_CREATE_TASK = API_AREA_TASK + "/createTask/";

    public static String API_GET_ALL_TASKS_BY_AREA_ORDER = API_AREA_TASK + "/getAllTasksByAreaOrder/";

    public static String API_GET_TASKS_BY_AREA = API_AREA_TASK + "/getTasksByArea/";

    public static String API_REGISTER_ADMIN = API_USER + "registerAdmin/";

    public static String API_REGISTER_STAFF = API_USER + "registerStaff/";

    public static String API_REGISTER_CHILD = API_USER + "registerChild/";

    public static String API_GET_UNASSIGNED_CHILDREN = API_USER + "getunAssignedChildren/";

    public static String API_GET_USER = API_USER + "getUser/";

    public static String API_GET_STATISTICS = API_USER + "getStatistics/";

    public static String API_GET_STAFF_LIST = API_USER + "getStaffList/";

    public static String API_GET_ALL_CHILDREN = API_USER + "getAllChildrens/";

    public static String API_DEACTIVATE_USER = API_USER + "deactivateUser/";

    public static String API_ASSIGN_STAFF_TO_CHILD = API_USER + "assignStafftoChild/";

    public static String API_DE_ASSIGN_STAFF_TO_CHILD = API_USER + "deAssignStafftoChild/";

    public static String API_UPDATE_PROFILE = API_USER + "updateProfile/";

    public static String API_UPLOAD_IMAGE = API_USER + "uploadImage/";

    public static String API_UPDATE_PASSWORD = API_USER + "updatePassword/";

    public static String API_GET_PROFILE_IMAGE = API_USER + "getProfileImage/";

    public static String API_REGISTER_INSTITUTE = API_INSTITUTE + "registerInstitute";

    public static String API_GET_INSTITUTE = API_INSTITUTE + "getInstitute/";

    public static final String DEFAULT_FOLDER = "ceva";

    public static final String IMAGE_FOLDER = "ifl-images";

    public static final String APP_FOLDER = "ceva-ifl";

    public static final String LOG_FOLDER = "ifl-logs";

    public static final int CAMERA_REQUEST_CODE = 700;
}
