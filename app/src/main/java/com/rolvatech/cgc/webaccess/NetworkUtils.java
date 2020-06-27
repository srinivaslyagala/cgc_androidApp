package com.rolvatech.cgc.webaccess;

/**
 * Created by Admin on 26-07-2017.
 */

public class NetworkUtils {

    public enum CallbackMediaType {
        JSON(0), TEXT(1);
        private int val;

        CallbackMediaType(int val) {
            this.val = val;
        }

        public int getVal() {
            return val;
        }
    }

    public interface RequestComponents {
        String HEADER = "HEADER", QUERY_PARAM = "QUERY_PARAM", URL = "URL";
    }

    public interface Method {
        String GET = "GET", POST = "POST", PUT = "PUT", PATCH = "PATCH", DELETE = "DELETE", HEAD = "HEAD";
    }
}
