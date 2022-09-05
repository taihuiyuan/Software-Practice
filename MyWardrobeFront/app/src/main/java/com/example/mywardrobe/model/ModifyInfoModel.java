package com.example.mywardrobe.model;

import com.example.mywardrobe.base.BaseModel;
import com.example.mywardrobe.presenter.ModifyInfoPresenter;
import com.example.mywardrobe.utils.RequestApi;
import com.example.mywardrobe.utils.RequestManager;

import org.json.JSONException;
import org.json.JSONObject;

public class ModifyInfoModel extends BaseModel<ModifyInfoPresenter> {
    public final String TAG = "ModifyInfo";

    //POST方法，修改用户信息
    public void requestModifyInfo(RequestManager.NetworkListener networkListener, ModifyInfoPresenter modifyInfoPresenter) {
        JSONObject data = new JSONObject();
        try {
            data.put("username", modifyInfoPresenter.username);
            data.put("email", modifyInfoPresenter.email);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        this.request(RequestApi.ModifyUserInfo, RequestType.POST, data, networkListener);
    }

    //GET方法，获取用户信息
    public void requestGetUserInfo(RequestManager.NetworkListener networkListener, ModifyInfoPresenter modifyInfoPresenter) {
        JSONObject data = new JSONObject();
        String url = RequestApi.GetUserInfo + "/?username=" + modifyInfoPresenter.username;
        this.request(url, RequestType.GET, data, networkListener);
    }
}
