package com.rolvatech.cgc.webaccess;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Admin on 26-07-2017.
 */

public class NetworkRequestModel implements Serializable {

    public List<Header> headers;
    public String url;
    public List<QueryParam> params;
    public String method;
    public String body;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public List<Header> getHeaders() {
        return headers;
    }

    public void setHeaders(List<Header> headers) {
        this.headers = headers;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<QueryParam> getParams() {
        return params;
    }

    public void setParams(List<QueryParam> params) {
        this.params = params;
    }

    public class Header extends BasicKeyValue{
        public Header(){
            super(null,null);
        }
        public Header(String key, String value){
            super(key,value);
        }
    }

    public class QueryParam extends BasicKeyValue{
        public QueryParam(){
            super(null,null);
        }
        public QueryParam(String key, String value){
            super(key,value);
        }
    }

    private abstract class BasicKeyValue{
        public BasicKeyValue(String key, String value){
            this.key = key;
            this.value = value;
        }
        public String key,value;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

}
