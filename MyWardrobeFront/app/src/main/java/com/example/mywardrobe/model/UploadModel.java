package com.example.mywardrobe.model;

import android.util.Log;

import com.example.mywardrobe.base.BaseModel;
import com.example.mywardrobe.presenter.HomePresenter;
import com.example.mywardrobe.presenter.UploadPresenter;
import com.example.mywardrobe.utils.RequestApi;
import com.example.mywardrobe.utils.RequestManager;

import org.json.JSONObject;

public class UploadModel extends BaseModel<UploadPresenter> {
    String TAG = "UploadModel";

    public void requestUpload(RequestManager.NetworkListener networkListener, UploadPresenter mPresenter) {
        JSONObject data = new JSONObject();
        try {
            data.put("username", mPresenter.username);
            data.put("imageUrl", mPresenter.imageUrl);
            data.put("categoryName", mPresenter.categoryName);
            data.put("color", mPresenter.color);
            data.put("season", mPresenter.season);
            data.put("price", mPresenter.price);
            data.put("location", mPresenter.location);
            data.put("note", mPresenter.note);
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.request(RequestApi.Upload, RequestType.POST, data, networkListener);
    }

    public void requestCategory(RequestManager.NetworkListener networkListener, UploadPresenter mPresenter)  {
        JSONObject data = new JSONObject();
        String url = RequestApi.GetCategory + "/?username="+ mPresenter.username;
        this.request(url, RequestType.GET, data, networkListener);
    }

    public void requestLocation(RequestManager.NetworkListener networkListener, UploadPresenter mPresenter)  {
        JSONObject data = new JSONObject();
        String url = RequestApi.GetLocation + "/?username="+ mPresenter.username;
        this.request(url, RequestType.GET, data, networkListener);
    }

    public void addCategory(RequestManager.NetworkListener networkListener, UploadPresenter mPresenter)  {
        JSONObject data = new JSONObject();
        try {
            data.put("username", mPresenter.username);
            data.put("categoryName", mPresenter.newCategoryName);
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.request(RequestApi.AddCategory, RequestType.POST, data, networkListener);
    }

    public void addLocation(RequestManager.NetworkListener networkListener, UploadPresenter mPresenter)  {
        JSONObject data = new JSONObject();
        try {
            data.put("username", mPresenter.username);
            data.put("locationName", mPresenter.newLocation);
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.request(RequestApi.AddLocation, RequestType.POST, data, networkListener);
    }
}
