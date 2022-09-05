package com.example.mywardrobe.model;

import android.util.Log;

import com.example.mywardrobe.base.BaseModel;
import com.example.mywardrobe.presenter.HomePresenter;
import com.example.mywardrobe.presenter.LoginPresenter;
import com.example.mywardrobe.utils.InfoUtil;
import com.example.mywardrobe.utils.RequestApi;
import com.example.mywardrobe.utils.RequestManager;

import org.json.JSONObject;

public class HomeModel extends BaseModel<HomePresenter> {
    String TAG = "HomeModel";
    public void requestCategory(RequestManager.NetworkListener networkListener, HomePresenter mPresenter)  {
        Log.i(TAG,"username="+mPresenter.username);
        JSONObject data = new JSONObject();
        String url = RequestApi.GetCategory + "/?username="+ mPresenter.username;
        Log.i(TAG,"url="+url);
        this.request(url, RequestType.GET, data, networkListener);
    }

    public void addCategory(RequestManager.NetworkListener networkListener, HomePresenter mPresenter)  {
        JSONObject data = new JSONObject();
        try {
            data.put("username", mPresenter.username);
            data.put("categoryName", mPresenter.categoryName);
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.request(RequestApi.AddCategory, RequestType.POST, data, networkListener);
    }

    public void editCategory(RequestManager.NetworkListener networkListener, HomePresenter mPresenter)  {
        JSONObject data = new JSONObject();
        try {
            data.put("username", mPresenter.username);
            data.put("categoryName", mPresenter.categoryName);
            data.put("newCategoryName", mPresenter.newCategoryName);
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.request(RequestApi.EditCategory, RequestType.POST, data, networkListener);
    }

    public void deleteCategory(RequestManager.NetworkListener networkListener, HomePresenter mPresenter)  {
        JSONObject data = new JSONObject();
        try {
            data.put("username", mPresenter.username);
            data.put("categoryName", mPresenter.categoryName);
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.request(RequestApi.DeleteCategory, RequestType.POST, data, networkListener);
    }
}
