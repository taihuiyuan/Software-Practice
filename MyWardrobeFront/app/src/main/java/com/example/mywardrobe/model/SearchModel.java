package com.example.mywardrobe.model;

import android.util.Log;

import com.example.mywardrobe.base.BaseModel;
import com.example.mywardrobe.presenter.SearchPresenter;
import com.example.mywardrobe.utils.RequestApi;
import com.example.mywardrobe.utils.RequestManager;

import org.json.JSONObject;

public class SearchModel extends BaseModel<SearchPresenter> {
    String TAG = "SearchModel";


    public void requestClothes(RequestManager.NetworkListener networkListener, SearchPresenter mPresenter)  {
        JSONObject data = new JSONObject();
        String url = RequestApi.SearchClothes + "/?username="+ mPresenter.username+"&keyword="+mPresenter.keyword;
        Log.i(TAG, "url="+url);
        this.request(url, RequestType.GET, data, networkListener);
    }

}
