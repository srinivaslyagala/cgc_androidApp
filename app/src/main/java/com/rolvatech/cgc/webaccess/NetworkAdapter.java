package com.rolvatech.cgc.webaccess;

import android.util.Log;

import java.util.List;

import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import static com.rolvatech.cgc.webaccess.NetworkUtils.Method.DELETE;
import static com.rolvatech.cgc.webaccess.NetworkUtils.Method.GET;
import static com.rolvatech.cgc.webaccess.NetworkUtils.Method.HEAD;
import static com.rolvatech.cgc.webaccess.NetworkUtils.Method.PATCH;
import static com.rolvatech.cgc.webaccess.NetworkUtils.Method.POST;
import static com.rolvatech.cgc.webaccess.NetworkUtils.Method.PUT;


/**
 * Created by Admin on 25-07-2017.
 */

public class NetworkAdapter {

    private OkHttpClient client = new OkHttpClient();

    private static NetworkAdapter networkAdapter;

    private NetworkAdapter() {

    }

    public static NetworkAdapter getInstance() {
        if (networkAdapter == null) {
            synchronized (NetworkAdapter.class) {
                networkAdapter = new NetworkAdapter();
            }
        }
        return networkAdapter;
    }

    public void addRequestInQueue(Request request, Callback callback) {
        client.newCall(request).enqueue(callback);
    }

    public Request getRequest(NetworkRequestModel requestModel) {
        return new RequestBuilder(requestModel).build();
    }


    private class RequestBuilder {
        private NetworkRequestModel requestModel;

        private RequestBuilder(NetworkRequestModel requestModel) {
            this.requestModel = requestModel;
        }

        Request build() {
            return requestModel.getMethod() != null ?
                    addHeaderWithUrl(requestModel.getMethod()) : null;
        }

        private Request addHeaderWithUrl(String method) {
            Request.Builder builder = new Request.Builder();

            List<NetworkRequestModel.Header> headers = requestModel.getHeaders();
            if (headers != null) {
                for (NetworkRequestModel.Header header : headers) {
                    builder.header(header.getKey(), header.getValue());
                }
            }
            builder.url(addQueryParamsToUrl());

            switch (method) {

                case GET:
                    Log.v("ggg", "get method");
                    break;

                case POST:
                    builder.post(getRequestBody());
                    break;

                case PUT:
                    builder.put(getRequestBody());
                    break;

                case PATCH:
                    builder.patch(getRequestBody());
                    break;

                case DELETE:
                    builder.delete(getRequestBody());
                    break;

                case HEAD:
                    builder.head();
                    break;

                default:
                    return null;

            }
            return builder.build();
        }

        private RequestBody getRequestBody() {
            return RequestBody.create(getMediaType(null), requestModel.getBody() != null ? requestModel.getBody() : "");

        }

        private MediaType getMediaType(NetworkUtils.CallbackMediaType type) {
            MediaType mediaType = null;
            if (type == NetworkUtils.CallbackMediaType.TEXT) {

            } else {

                mediaType = MediaType.parse("application/json; charset=utf-8");
            }
            return mediaType;

        }

        private String addQueryParamsToUrl() {
            HttpUrl.Builder builder = HttpUrl.parse(requestModel.getUrl()).newBuilder();
            List<NetworkRequestModel.QueryParam> params = requestModel.getParams();
            if (params != null) {
                for (NetworkRequestModel.QueryParam param : params) {
                    builder.addQueryParameter(param.getKey(), param.getValue());
                }
            }
            return builder.build().toString();
        }
    }
}
