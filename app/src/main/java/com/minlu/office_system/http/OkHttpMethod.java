package com.minlu.office_system.http;

import com.minlu.baselibrary.http.OkHttpManger;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by user on 2017/3/29.
 */

public class OkHttpMethod {

    private static OkHttpClient mOkHttpClient = OkHttpManger.getInstance().getOkHttpClient();

    private static Request getPostRequest(String url, Map<String, String> requestParam) {
        FormBody.Builder builder = new FormBody.Builder();

        if (requestParam != null) {
            Set<String> paramFields = requestParam.keySet();
            for (String paramField : paramFields) {
                builder.add(paramField, requestParam.get(paramField));
            }
        }

        return new Request.Builder()
                .url(url)
                .post(builder.build())
                .build();
    }

    public static Response synPostRequest(String url, Map<String, String> requestParam) {
        Response response;
        try {
            response = mOkHttpClient.newCall(getPostRequest(url, requestParam)).execute();
        } catch (IOException e) {
            e.printStackTrace();
            response = null;
        }
        return response;
    }

    public static void asynPostRequest(String url, Map<String, String> requestParam, Callback responseCallback) {
        mOkHttpClient.newCall(getPostRequest(url, requestParam)).enqueue(responseCallback);
    }


}
