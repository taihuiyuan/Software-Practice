package com.example.mywardrobe.presenter;

import android.util.Log;
import android.widget.Toast;

import com.example.mywardrobe.activity.RegisterActivity;
import com.example.mywardrobe.base.BasePresenter;
import com.example.mywardrobe.model.LoginModel;
import com.example.mywardrobe.model.RegisterModel;
import com.example.mywardrobe.utils.RequestManager;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterPresenter extends BasePresenter<RegisterActivity, RegisterModel> {
    public String username = "";
    public String password = "";
    //public String repeatPassword = "";
    public String email = "";

    public interface RegisterCallback {
        void onRegisterSuccess(String message, int status);
        void onRegisterFail(String msg);
    }

    public RegisterPresenter() {
        this.mModel = new RegisterModel();
    }

    /**
     * 注册
     * @param registerCallback 注册回调
     */
    public void register(final RegisterPresenter.RegisterCallback registerCallback) {
        if (username.isEmpty()) {
            Toast.makeText(this.mView.getApplicationContext(), "用户名不能为空", Toast.LENGTH_LONG).show();
            return;
        }
        if (password.isEmpty()) {
            Toast.makeText(this.mView.getApplicationContext(), "密码不能为空", Toast.LENGTH_LONG).show();
            return;
        }
//        if (!password.equals(repeatPassword)) {
//            Toast.makeText(this.mView.getApplicationContext(), "两次输入密码不一致", Toast.LENGTH_LONG).show();
//            return;
//        }
        mModel.requestRegister(new RequestManager.NetworkListener() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {
                    String message = jsonObject.get("message").toString();
                    int status = jsonObject.getInt("status");
                    Log.e("uploadFile","message" + message);
                    Log.e("uploadFile", "status"+status);
                    registerCallback.onRegisterSuccess(message, status);
                } catch (JSONException e) {
                    e.printStackTrace();
                    registerCallback.onRegisterFail("解析失败，Error：" + e.getLocalizedMessage());
                    Log.e("注册解析数据失败", " Error：" + e.getLocalizedMessage());
                }
            }

            @Override
            public void onFail(String msg) {
                registerCallback.onRegisterFail("注册失败，Error：" + msg);
            }
        }, this);
    }
}
