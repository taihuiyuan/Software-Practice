package com.example.mywardrobe.model;

import android.util.Log;

import com.example.mywardrobe.base.BaseModel;
import com.example.mywardrobe.presenter.ChoosePresenter;
import com.example.mywardrobe.utils.RequestApi;
import com.example.mywardrobe.utils.RequestManager;

import org.json.JSONObject;

public class ChooseModel extends BaseModel<ChoosePresenter> {
    String TAG = "ChooseModel";
    public void requestLocations(RequestManager.NetworkListener networkListener, ChoosePresenter mPresenter)  {
        Log.i(TAG,"username="+mPresenter.username);
        JSONObject data = new JSONObject();
        String url = RequestApi.GetLocation + "/?username="+ mPresenter.username;
        this.request(url, RequestType.GET, data, networkListener);
    }

    public void deleteClothes(RequestManager.NetworkListener networkListener, ChoosePresenter mPresenter)  {
        JSONObject data = new JSONObject();
        String url = RequestApi.DeleteClothes + "/?username="+ mPresenter.username+"&clothesID="+mPresenter.clothesID;
        this.request(url, RequestType.GET, data, networkListener);
    }

    public void requestClothes(RequestManager.NetworkListener networkListener, ChoosePresenter mPresenter)  {
        JSONObject data = new JSONObject();
        try {
            data.put("username",mPresenter.username);
            data.put("categoryName",mPresenter.categoryName);
            if(mPresenter.map.containsKey("season")) {
                data.put("season",mPresenter.map.get("season"));
            }
            if(mPresenter.map.containsKey("priceRange")) {
                data.put("priceRange",mPresenter.map.get("priceRange"));
            }
            if(mPresenter.map.containsKey("color")) {
                data.put("color",mPresenter.map.get("color"));
            }
            if(mPresenter.map.containsKey("location")) {
                data.put("location", mPresenter.map.get("location"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        this.request(RequestApi.Filter, RequestType.POST, data, networkListener);
    }

}
