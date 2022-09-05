package com.example.mywardrobe.model;

import android.nfc.Tag;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.mywardrobe.base.BaseModel;
import com.example.mywardrobe.presenter.LoginPresenter;
import com.example.mywardrobe.utils.RequestApi;
import com.example.mywardrobe.utils.RequestManager;
import com.google.gson.Gson;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

public class LoginModel extends BaseModel<LoginPresenter> {
    String TAG = "LoginModel";
    public void requestLogin(RequestManager.NetworkListener networkListener,LoginPresenter mPresenter)  {

        JSONObject data = new JSONObject();
        try {
            data.put("username",mPresenter.username);
            data.put("password",mPresenter.password);

        } catch (Exception e) {
            e.printStackTrace();
        }

        this.request(RequestApi.Login, RequestType.POST, data, networkListener);
    }
}
