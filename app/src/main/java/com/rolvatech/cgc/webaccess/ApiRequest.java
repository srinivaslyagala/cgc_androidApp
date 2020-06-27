package com.rolvatech.cgc.webaccess;

import java.io.IOException;

import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by admin on 10/2/2017.
 */

public class ApiRequest {

    private static ApiRequest apiRequest;

    private NetworkRequestModel mNetworkRequestModel;


    private ApiRequest() {

    }

    public static ApiRequest getInstance() {
        if (apiRequest == null) {
            synchronized (NetworkAdapter.class) {
                apiRequest = new ApiRequest();

            }
        }
        return apiRequest;
    }

    public ApiRequest setRequestModel(NetworkRequestModel networkRequestModel) {
        mNetworkRequestModel = networkRequestModel;
        return this;
    }

    public void callApi(final ApiResponseListener apiResponseListener) {
        NetworkAdapter mNetworkAdapter = NetworkAdapter.getInstance();
        Request request = mNetworkAdapter.getRequest(mNetworkRequestModel);
        if (request != null) {
            mNetworkAdapter.addRequestInQueue(request, new Callback() {
                @Override
                public void onFailure(okhttp3.Call call, IOException e) {
                    apiResponseListener.onFailure(call, e);
                }

                @Override
                public void onResponse(okhttp3.Call call, Response response) throws IOException {
                    apiResponseListener.onResponse(call, response);
                }
            });
        }
    }
}
