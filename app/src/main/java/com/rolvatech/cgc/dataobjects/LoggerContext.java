package com.rolvatech.cgc.dataobjects;

import java.io.Serializable;


public class LoggerContext implements Serializable {

    private String url;

    private String userName;

    private String method;

    private Object headers;

    private Object requestBody;

    private Object responseBody;

    private String responseStatus;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Object getHeaders() {
        return headers;
    }

    public void setHeaders(Object headers) {
        this.headers = headers;
    }

    public Object getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(Object requestBody) {
        this.requestBody = requestBody;
    }

    public Object getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(Object responseBody) {
        this.responseBody = responseBody;
    }

    public String getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(String responseStatus) {
        this.responseStatus = responseStatus;
    }

}
