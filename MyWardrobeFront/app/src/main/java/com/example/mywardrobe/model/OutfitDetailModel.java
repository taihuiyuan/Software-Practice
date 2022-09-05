package com.example.mywardrobe.model;

import android.util.Log;

import com.example.mywardrobe.base.BaseModel;
import com.example.mywardrobe.presenter.OutfitDetailPresenter;
import com.example.mywardrobe.utils.RequestApi;
import com.example.mywardrobe.utils.RequestManager;

import org.json.JSONObject;

public class OutfitDetailModel extends BaseModel<OutfitDetailPresenter> {
    String TAG = "OutfitDetailModel";

    public void showOutfitClothes(RequestManager.NetworkListener networkListener,OutfitDetailPresenter mPresenter){
        JSONObject data = new JSONObject();
        String url = RequestApi.GetOutfits + "/?username="+ mPresenter.username;
        this.request(url, RequestType.GET, data, networkListener);
    }

    public void add(RequestManager.NetworkListener networkListener,OutfitDetailPresenter mPresenter){
        JSONObject data = new JSONObject();
        try{
            data.put("username",mPresenter.username);
            data.put("outfitName",mPresenter.outfitName);
            data.put("clothes",mPresenter.addData);
        }catch (Exception e){
            e.printStackTrace();
        }
        this.request(RequestApi.AddClothes,RequestType.POST,data,networkListener);
        this.request(RequestApi.DeleteClothesFromOutfit,RequestType.POST,data,networkListener);

    }

    public void delete(RequestManager.NetworkListener networkListener,OutfitDetailPresenter mPresenter){
        JSONObject data = new JSONObject();
        try{
            data.put("username",mPresenter.username);
            data.put("outfitName",mPresenter.outfitName);
            data.put("clothes",mPresenter.deleteData);
        }catch (Exception e){
            e.printStackTrace();
        }
        this.request(RequestApi.DeleteClothesFromOutfit,RequestType.POST,data,networkListener);
    }

    public void requestAllClothes(RequestManager.NetworkListener networkListener, OutfitDetailPresenter mPresenter){
        Log.i(TAG,"username="+mPresenter.username);
        JSONObject data = new JSONObject();
        String url = RequestApi.GetAllClothes + "/?username="+ mPresenter.username;
        this.request(url, BaseModel.RequestType.GET, data, networkListener);
    }
}
