package com.example.mywardrobe.presenter;

import android.util.Log;
import android.widget.Toast;

import com.example.mywardrobe.activity.LoginActivity;
import com.example.mywardrobe.base.BasePresenter;
import com.example.mywardrobe.model.*;
import com.example.mywardrobe.utils.InfoUtil;
import com.example.mywardrobe.utils.RequestManager;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginPresenter extends BasePresenter <LoginActivity, LoginModel> {
    public String username = "";
    public String password = "";

    public interface LoginCallback {
        void onLoginSuccess(String message, int status);
        void onLoginFail(String msg);
    }

    public LoginPresenter() {
        this.mModel = new LoginModel();
    }

    /**
     * 登录
     * @param loginCallback 登录回调
     */
    public void login(final LoginCallback loginCallback) {
        if (username.isEmpty()) {
            Toast.makeText(this.mView.getApplicationContext(), "用户名不能为空", Toast.LENGTH_LONG).show();
            return;
        }
        if (password.isEmpty()) {
            Toast.makeText(this.mView.getApplicationContext(), "密码不能为空", Toast.LENGTH_LONG).show();
            return;
        }
        mModel.requestLogin(new RequestManager.NetworkListener() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {
                    String message = jsonObject.get("message").toString();
                    int status = jsonObject.getInt("status");
                    Log.e("message", message);
                    loginCallback.onLoginSuccess(message, status);
                } catch (JSONException e) {
                    e.printStackTrace();
                    loginCallback.onLoginFail("解析失败，Error：" + e.getLocalizedMessage());
                    Log.e("登录解析数据失败", " Error：" + e.getLocalizedMessage());
                }
            }

            @Override
            public void onFail(String msg) {
                loginCallback.onLoginFail("登录失败，Error：" + msg);
            }
        }, this);
    }

}
