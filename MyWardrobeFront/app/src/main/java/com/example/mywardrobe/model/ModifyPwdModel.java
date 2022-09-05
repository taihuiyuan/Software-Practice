package com.example.mywardrobe.model;

import com.example.mywardrobe.base.BaseModel;
import com.example.mywardrobe.presenter.ModifyPwdPresenter;
import com.example.mywardrobe.utils.RequestApi;
import com.example.mywardrobe.utils.RequestManager;

import org.json.JSONException;
import org.json.JSONObject;

public class ModifyPwdModel extends BaseModel<ModifyPwdPresenter> {
    public final String TAG = "ModifyPwd";

    //POST方法，修改用户密码
    public void requestModifyPwd(RequestManager.NetworkListener networkListener, ModifyPwdPresenter modifyPwdPresenter) {
        JSONObject data = new JSONObject();
        try {
            data.put("username", modifyPwdPresenter.username);
            data.put("oldPassword", modifyPwdPresenter.oldPassword);
            data.put("newPassword", modifyPwdPresenter.newPassword);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        this.request(RequestApi.ModifyUserPwd, RequestType.POST, data, networkListener);
    }
}
