package com.example.mywardrobe.utils;

import android.app.Presentation;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class FileImageUpload {
    private static final String TAG = "uploadFile";
    private static final String URL = "http://129.211.165.110:8001/clothes/image";
    private static String imageUrl = "";


    public String uploadFile(String path, String filename) {
        OkHttpClient okthttp = new OkHttpClient();
        File file = new File(path);
        if (path.isEmpty() || !file.exists()) {
            return "图片不存在";
        }
        if (file.length() > 1048576){
            return "图片大小需小于1MB";
        }
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("file", filename, RequestBody.create(new File(path), MediaType.parse("multipart/form-data")))
                .addFormDataPart("filename", filename)
                .build();
        FutureTask<String> task = new FutureTask<String>(() -> {
            try {
                ResponseBody responseBody = okthttp.newCall(
                        new Request.Builder().post(body).url(URL).build()
                ).execute().body();
                String responseData = responseBody.string();
                Log.e(TAG, "response"+responseData.toString());
                final JSONObject jsonObject = new JSONObject(responseData);
                imageUrl = jsonObject.get("imageUrl").toString();
                Log.e(TAG, imageUrl);
            }
            catch (IOException e) {
                return "图片上传失败";
            }
            return "";
        });
        try {
            new Thread(task).start();
            return task.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return "图片上传失败";
    }

    public String getImageUrl() {
        return imageUrl;
    }


}
