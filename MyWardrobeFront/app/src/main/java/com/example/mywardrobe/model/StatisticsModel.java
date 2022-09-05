package com.example.mywardrobe.model;

import android.util.Log;

import com.example.mywardrobe.base.BaseModel;
import com.example.mywardrobe.presenter.StatisticsPresenter;
import com.example.mywardrobe.presenter.UploadPresenter;
import com.example.mywardrobe.utils.RequestApi;
import com.example.mywardrobe.utils.RequestManager;

import org.json.JSONObject;

public class StatisticsModel extends BaseModel<StatisticsPresenter> {
    String TAG = "Statistics";

    public void statisticByCategory(RequestManager.NetworkListener networkListener, StatisticsPresenter mPresenter) {
        JSONObject data = new JSONObject();
        String url = RequestApi.StatisticByCategory + "/?username="+ mPresenter.username;
        Log.i(TAG,"url="+url);
        this.request(url, RequestType.GET, data, networkListener);
    }

    public void statisticByLocation(RequestManager.NetworkListener networkListener, StatisticsPresenter mPresenter) {
        JSONObject data = new JSONObject();
        String url = RequestApi.StatisticByLocation + "/?username="+ mPresenter.username;
        Log.i(TAG,"url="+url);
        this.request(url, RequestType.GET, data, networkListener);
    }

    public void statisticBySeason(RequestManager.NetworkListener networkListener, StatisticsPresenter mPresenter) {
        JSONObject data = new JSONObject();
        String url = RequestApi.StatisticBySeason + "/?username="+ mPresenter.username;
        Log.i(TAG,"url="+url);
        this.request(url, RequestType.GET, data, networkListener);
    }

    public void statisticByPrice(RequestManager.NetworkListener networkListener, StatisticsPresenter mPresenter) {
        JSONObject data = new JSONObject();
        String url = RequestApi.StatisticByPrice + "/?username="+ mPresenter.username;
        Log.i(TAG,"url="+url);
        this.request(url, RequestType.GET, data, networkListener);
    }
}
