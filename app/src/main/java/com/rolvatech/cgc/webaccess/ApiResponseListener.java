package com.rolvatech.cgc.webaccess;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by admin on 10/2/2017.
 */

public interface ApiResponseListener {
    void onResponse(Call call, Response response);
    void onFailure(Call call, IOException e);

}
