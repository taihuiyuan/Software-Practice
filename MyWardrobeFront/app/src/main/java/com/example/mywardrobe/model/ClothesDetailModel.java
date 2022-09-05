package com.example.mywardrobe.model;

import android.util.Log;

import com.example.mywardrobe.base.BaseModel;
import com.example.mywardrobe.presenter.ChoosePresenter;
import com.example.mywardrobe.presenter.ClothesDetailPresenter;
import com.example.mywardrobe.utils.RequestApi;
import com.example.mywardrobe.utils.RequestManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.InetAddress;

public class ClothesDetailModel extends BaseModel<ClothesDetailPresenter> {

    //GET方法,获取衣物详情信息
    public void requestGetClothesDetail(RequestManager.NetworkListener networkListener, ClothesDetailPresenter clothesDetailPresenter) {
        JSONObject data = new JSONObject();
        String url = RequestApi.GetClothesDetail + "/?username=" + clothesDetailPresenter.username + "&clothesID=" + clothesDetailPresenter.clothesID;
        Log.e("debug", "url:"+url);
        this.request(url, RequestType.GET, data, networkListener);
    }

    //POST方法，修改衣物详情
    public void requestModifyClothesDetail(RequestManager.NetworkListener networkListener, ClothesDetailPresenter clothesDetailPresenter) {
        JSONObject data = new JSONObject();
        try {
            data.put("username", clothesDetailPresenter.username);
            data.put("categoryName", clothesDetailPresenter.categoryName);
            data.put("color", clothesDetailPresenter.color);
            data.put("season", clothesDetailPresenter.season);
            data.put("price", clothesDetailPresenter.price);
            data.put("location", clothesDetailPresenter.location);
            data.put("note", clothesDetailPresenter.note);
            data.put("clothesID", clothesDetailPresenter.clothesID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        this.request(RequestApi.ModifyClothesDetail, RequestType.POST, data, networkListener);
    }

    //GET方法，获取衣物分类列表
    public void requestListCategory(RequestManager.NetworkListener networkListener, ClothesDetailPresenter mPresenter) {
        JSONObject data = new JSONObject();
        String url = RequestApi.GetCategory + "/?username="+ mPresenter.username;
        this.request(url, RequestType.GET, data, networkListener);
    }

    //GET方法，获取衣物位置列表
    public void requestListLocation(RequestManager.NetworkListener networkListener, ClothesDetailPresenter mPresenter) {
        JSONObject data  = new JSONObject();
        String url = RequestApi.GetLocation + "/?username="+ mPresenter.username;
        this.request(url, RequestType.GET, data, networkListener);
    }

    //POST方法，增加衣物分类
    public void requestAddCategory(RequestManager.NetworkListener networkListener, ClothesDetailPresenter mPresenter) {
        JSONObject data = new JSONObject();
        try {
            data.put("username", mPresenter.username);
            data.put("categoryName", mPresenter.newCategoryName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        this.request(RequestApi.AddCategory, RequestType.POST, data, networkListener);
    }

    //POST方法，增加衣物位置
    public void requestAddLocation(RequestManager.NetworkListener networkListener, ClothesDetailPresenter mPresenter) {
        JSONObject data = new JSONObject();
        try {
            data.put("username", mPresenter.username);
            data.put("locationName", mPresenter.newLocation);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        this.request(RequestApi.AddLocation, RequestType.POST, data, networkListener);
    }

    //POST方法，删除衣物位置
    public void requestDeleteLocation(RequestManager.NetworkListener networkListener, ClothesDetailPresenter mPresenter) {
        JSONObject data = new JSONObject();
        try {
            data.put("username", mPresenter.username);
            data.put("locationName", mPresenter.deleteLocation);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        this.request(RequestApi.DeleteLocation, RequestType.POST, data, networkListener);
    }

    //GET方法，获取衣物的历史位置
    public void requestGetHistoricalLocation(RequestManager.NetworkListener networkListener, ClothesDetailPresenter mPresenter) {
        JSONObject data = new JSONObject();
        String url = RequestApi.GetHistoricalLocation + "/?username="+ mPresenter.username + "&clothesID=" + mPresenter.clothesID;
        this.request(url, RequestType.GET, data, networkListener);
    }

}
