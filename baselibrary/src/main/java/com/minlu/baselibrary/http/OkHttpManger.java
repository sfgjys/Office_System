package com.minlu.baselibrary.http;

import okhttp3.OkHttpClient;

/**
 * Created by user on 2016/7/25.
 */
public class OkHttpManger {

    private static OkHttpManger okHttpManger;
    private OkHttpClient okHttpClient;

    private OkHttpManger() {
        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient().newBuilder().cookieJar(new CookiesManager()).build();
        }
    }


    /**
     * 获取本类的单例对象
     */
    public static OkHttpManger getInstance() {
        if (okHttpManger == null) { //线程1，线程2
            synchronized (OkHttpManger.class) {
                if (okHttpManger == null) {
                    okHttpManger = new OkHttpManger();
                }
            }
        }
        return okHttpManger;
    }

    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

}
