package com.example.mywardrobe.presenter;

import android.util.Log;
import android.widget.Toast;

import com.example.mywardrobe.activity.ModifyInfoActivity;
import com.example.mywardrobe.base.BasePresenter;
import com.example.mywardrobe.model.ModifyInfoModel;
import com.example.mywardrobe.utils.RequestManager;

import org.json.JSONException;
import org.json.JSONObject;

public class ModifyInfoPresenter extends BasePresenter<ModifyInfoActivity, ModifyInfoModel> {
    public final String TAG = "ModifyInfo";
    public String username = "";
    public String email = "";

    public interface ModifyInfoCallBack {
        void onModifyInfoSuccess(String message, int status);
        void onModifyInfoFail(String msg);
    }

    public interface GetUserInfoCallBack {
        void onGetUserInfoCallBackSuccess(String username, String email);
        void onGetUserInfoCallBackFail(String msg);
    }

    public ModifyInfoPresenter() {
        this.mModel = new ModifyInfoModel();
    }

    public void modifyInfo(final ModifyInfoCallBack callBack) {
        if (username.isEmpty() && email.isEmpty()) {
            Toast.makeText(this.mView.getApplicationContext(), "请至少改变一样信息", Toast.LENGTH_SHORT).show();
            return;
        }
        mModel.requestModifyInfo(new RequestManager.NetworkListener() {

            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {
                    String message = jsonObject.get("message").toString();
                    int status = jsonObject.getInt("status");
                    Log.e(TAG, message);
                    callBack.onModifyInfoSuccess(message, status);
                } catch (JSONException e) {
                    e.printStackTrace();
                    callBack.onModifyInfoFail("解析失败，Error：" + e.getLocalizedMessage());
                    Log.e(TAG, " Error：" + e.getLocalizedMessage());
                }
            }

            @Override
            public void onFail(String msg) {
                callBack.onModifyInfoFail("修改失败, Error: " + msg);
            }
        }, this);
    }

    public void getUserInfo(final ModifyInfoPresenter.GetUserInfoCallBack getUserInfoCallBack) {
        mModel.requestGetUserInfo(new RequestManager.NetworkListener() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {
                    String username = jsonObject.get("username").toString();
                    String email = jsonObject.get("email").toString();
                    getUserInfoCallBack.onGetUserInfoCallBackSuccess(username, email);
                } catch (JSONException e) {
                    e.printStackTrace();
                    getUserInfoCallBack.onGetUserInfoCallBackFail("解析失败，Error：" + e.getLocalizedMessage());
                    Log.e(TAG, " Error：" + e.getLocalizedMessage());
                }
            }

            @Override
            public void onFail(String msg) {
                getUserInfoCallBack.onGetUserInfoCallBackFail("加载失败，Error：" + msg);
            }
        }, this);
    }


}
