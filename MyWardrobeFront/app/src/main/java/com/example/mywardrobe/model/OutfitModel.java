package com.example.mywardrobe.model;

import android.util.Log;

import com.example.mywardrobe.base.BaseModel;
import com.example.mywardrobe.presenter.OutfitPresenter;
import com.example.mywardrobe.utils.RequestApi;
import com.example.mywardrobe.utils.RequestManager;

import org.json.JSONException;
import org.json.JSONObject;

public class OutfitModel extends BaseModel<OutfitPresenter> {
    String TAG = "OutfitModel";

    public void requestOutfits(RequestManager.NetworkListener networkListener, OutfitPresenter mPresenter){
        Log.i(TAG,"username="+mPresenter.username);
        JSONObject data = new JSONObject();
        String url = RequestApi.GetOutfits + "/?username="+ mPresenter.username;
        this.request(url, RequestType.GET, data, networkListener);
    }

    public void deleteOutfit(RequestManager.NetworkListener networkListener,OutfitPresenter mPresenter){
        Log.i(TAG,"username="+mPresenter.username);
        JSONObject data = new JSONObject();
        try {
            data.put("username",mPresenter.username);
            data.put("outfitName",mPresenter.deleteOutfitName);
        }catch (JSONException e){
            e.printStackTrace();
        }
        this.request(RequestApi.DeleteOutfit,RequestType.POST,data,networkListener);
    }

}
