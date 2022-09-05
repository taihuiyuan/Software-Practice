package com.example.mywardrobe.utils;

import android.net.wifi.p2p.WifiP2pManager;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.JsonArray;

import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RequestManager {
    public static final int GET_ARRAY = 0;
    private static final int GET_OBJECT = 1;

    private static final int TIME_OUT = 60; //超时时间
    //单例模式
    private volatile static RequestManager requestManager = null;
    private static OkHttpClient myOkHttpClient;

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    //private static final MediaType Multipart = MediaType.parse("multipart/form-data");

    public static RequestManager getInstance() {
        if(null == requestManager) {
            synchronized (RequestManager.class) {
                if(null == requestManager) {
                    requestManager = new RequestManager();
                }
            }
        }
        return requestManager;
    }


    private RequestManager() {
        myOkHttpClient = new OkHttpClient.Builder()
                .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(TIME_OUT, TimeUnit.SECONDS)
                .followRedirects(true)
                .hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String s, SSLSession sslSession) {
                        return false;
                    }
                })
                .addInterceptor(new Interceptor() {
                    @NonNull
                    @Override
                    public Response intercept(@NonNull Chain chain) throws IOException {
                        Request request = chain.request()
                                .newBuilder()
                                .addHeader("Content-Type", "application/json; charset=UTF-8")
                                .addHeader("Connection","keep-alive")
                                .addHeader("Accept","*/*")
                                .addHeader("Cookie","add cookies here")
                                .build();
                        return chain.proceed(request);
                    }
                }).build();

    }



    /**
     * Post
     */
    public void post(final String url, final JSONObject params, final NetworkListener networkListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                JSONObject par = params == null ? new JSONObject() : params;
                try {
                    RequestBody body = RequestBody.create(JSON, par.toString());
                    Request request = new Request.Builder()
                            .url(url)
                            .post(body)
                            .addHeader("Accept-Encoding", "identity")
                            .build();//创建一个Request对象
                    Response response = myOkHttpClient.newCall(request).execute();
                    int status = response.code();
                    String responseData = response.body().string();
                    Log.i("post request url", url);
                    Log.i("post request params", par.toString());
                    Log.i("post request rdata", responseData);
                    if (responseData.length() == 0) {
                        new Handler(Looper.getMainLooper()).post(new Runnable() {// 此时还在非UI线程，因此要转发
                            @Override
                            public void run() {
                                networkListener.onFail("解析数据失败，Error: 返回数据为空");
                            }
                        });
                        Log.e("解析数据失败","请求url："+ url + " Error: 返回数据为空");
                        return;
                    }
                    final JSONObject jsonObject = new JSONObject(responseData);
                    jsonObject.put("status", status);
                    new Handler(Looper.getMainLooper()).post(new Runnable() {// 此时还在非UI线程，因此要转发
                        @Override
                        public void run() {
                            networkListener.onSuccess(jsonObject);
                        }
                    });
                } catch (final Exception e) {
                    e.printStackTrace();
                    Log.e("网络请求失败","请求url："+ url + " Error:" + e.getLocalizedMessage());
                    new Handler(Looper.getMainLooper()).post(new Runnable() {// 此时还在非UI线程，因此要转发
                        @Override
                        public void run() {
                            networkListener.onFail("网络请求失败，Error:" + e.getLocalizedMessage());
                        }
                    });
                }
            }
        }).start();
    }

    /**
     *  Get
     */
    public void get(final String url, final NetworkListener networkListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Request request = new Request.Builder()
                            .url(url)
                            .get()
                            .build();//创建一个Request对象
                    Response response = myOkHttpClient.newCall(request).execute();
                    String responseData = response.body().string();
                    final JSONObject jsonObject = new JSONObject(responseData);//新建json对象实例
                    Log.i("get request url", url);
                    Log.i("get request rdata", responseData);
                    new Handler(Looper.getMainLooper()).post(new Runnable() {// 此时还在非UI线程，因此要转发
                        @Override
                        public void run() {
                            networkListener.onSuccess(jsonObject);
                        }
                    });
                } catch (final Exception e) {
                    e.printStackTrace();
                    Log.e("网络请求失败","请求url："+ url + " Error:" + e.getLocalizedMessage());
                    new Handler(Looper.getMainLooper()).post(new Runnable() {// 此时还在非UI线程，因此要转发
                        @Override
                        public void run() {
                            networkListener.onFail("解析数据失败，Error:" + e.getLocalizedMessage());
                        }
                    });
                }
            }
        }).start();
    }


    public interface NetworkListener {
        void onSuccess(JSONObject jsonObject);
        void onFail(String msg);
    }

}
