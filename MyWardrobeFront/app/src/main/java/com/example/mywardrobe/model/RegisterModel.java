package com.example.mywardrobe.model;

import com.example.mywardrobe.base.BaseModel;
import com.example.mywardrobe.presenter.RegisterPresenter;
import com.example.mywardrobe.utils.RequestApi;
import com.example.mywardrobe.utils.RequestManager;

import org.json.JSONObject;

public class RegisterModel extends BaseModel<RegisterPresenter> {
    public void requestRegister(RequestManager.NetworkListener networkListener, RegisterPresenter mPresenter) {

        JSONObject data = new JSONObject();
        try {
            data.put("username",mPresenter.username);
            data.put("password",mPresenter.password);
            data.put("email",mPresenter.email);

        } catch (Exception e) {
            e.printStackTrace();
        }
        this.request(RequestApi.Register, RequestType.POST, data, networkListener);
    }
}
