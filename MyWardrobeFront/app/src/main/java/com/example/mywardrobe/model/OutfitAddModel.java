package com.example.mywardrobe.model;

import android.util.Log;

import com.example.mywardrobe.base.BaseModel;
import com.example.mywardrobe.presenter.OutfitAddPresenter;
import com.example.mywardrobe.presenter.OutfitPresenter;
import com.example.mywardrobe.utils.RequestApi;
import com.example.mywardrobe.utils.RequestManager;

import org.json.JSONObject;

public class OutfitAddModel extends BaseModel<OutfitAddPresenter> {
    String TAG = "OutfitModel";

    public void requestAllClothes(RequestManager.NetworkListener networkListener, OutfitAddPresenter mPresenter){
        Log.i(TAG,"username="+mPresenter.username);
        JSONObject data = new JSONObject();
        String url = RequestApi.GetAllClothes + "/?username="+ mPresenter.username;
        this.request(url, BaseModel.RequestType.GET, data, networkListener);
    }

    public void submit(RequestManager.NetworkListener networkListener,OutfitAddPresenter mPresenter){
        JSONObject data = new JSONObject();
        try{
            data.put("username",mPresenter.username);
            data.put("outfitName",mPresenter.outfitName);
            data.put("clothes",mPresenter.clothes);
        } catch (Exception e){
            e.printStackTrace();
        }
        this.request(RequestApi.AddOutfit,RequestType.POST,data,networkListener);
    }

}
