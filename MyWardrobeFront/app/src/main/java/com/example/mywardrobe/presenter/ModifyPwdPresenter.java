package com.example.mywardrobe.presenter;

import android.util.Log;
import android.widget.Toast;

import com.example.mywardrobe.activity.ModifyPwdActivity;
import com.example.mywardrobe.base.BasePresenter;
import com.example.mywardrobe.model.ModifyPwdModel;
import com.example.mywardrobe.utils.RequestManager;

import org.json.JSONException;
import org.json.JSONObject;

public class ModifyPwdPresenter extends BasePresenter<ModifyPwdActivity, ModifyPwdModel> {
    public final String TAG = "ModifyPwd";
    public String username = "";
    public String oldPassword = "";
    public String newPassword = "";

    public interface ModifyPwdCallBack {
        void onModifyPwdSuccess(String message, int status);
        void onModifyPwdFail(String msg);
    }

    public ModifyPwdPresenter() {
        this.mModel = new ModifyPwdModel();
    }

    public void modifyPwd(final ModifyPwdCallBack callBack) {
        if (oldPassword.isEmpty() || newPassword.isEmpty()) {
            Toast.makeText(this.mView.getApplicationContext(), "输入不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (oldPassword.equals(newPassword)) {
            Toast.makeText(this.mView.getApplicationContext(), "旧密码和新密码不能相同", Toast.LENGTH_SHORT).show();
            return;
        }
        mModel.requestModifyPwd(new RequestManager.NetworkListener() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {
                    String message = jsonObject.get("message").toString();
                    int status = jsonObject.getInt("status");
                    callBack.onModifyPwdSuccess(message, status);
                } catch (JSONException e) {
                    e.printStackTrace();
                    callBack.onModifyPwdFail("解析失败，Error：" + e.getLocalizedMessage());
                    Log.e(TAG, " Error：" + e.getLocalizedMessage());
                }
            }

            @Override
            public void onFail(String msg) {
                callBack.onModifyPwdFail("修改失败， Error" + msg);
            }
        }, this);
    }
}
