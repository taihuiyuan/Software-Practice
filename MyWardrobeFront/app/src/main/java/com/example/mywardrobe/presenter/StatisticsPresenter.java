package com.example.mywardrobe.presenter;

import android.util.Log;

import com.example.mywardrobe.activity.StatisticsActivity;
import com.example.mywardrobe.activity.UploadActivity;
import com.example.mywardrobe.base.BasePresenter;
import com.example.mywardrobe.entity.Category;
import com.example.mywardrobe.entity.Statistics;
import com.example.mywardrobe.model.StatisticsModel;
import com.example.mywardrobe.model.UploadModel;
import com.example.mywardrobe.utils.RequestManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class StatisticsPresenter extends BasePresenter<StatisticsActivity, StatisticsModel> {
    private String TAG = "StatisticPresenter";

    public String username;
    public List<Statistics> data = new ArrayList<>();

    public StatisticsPresenter(){
        this.mModel = new StatisticsModel();
    }

    public interface StatisticByCategoryCallback {
        void onStatisticByCategorySuccess(List<Statistics> list);
        void onStatisticByCategoryFailed(String msg);
    }

    public void statisticByCategory(final StatisticsPresenter.StatisticByCategoryCallback statisticByCategoryCallback){
        mModel.statisticByCategory(new RequestManager.NetworkListener() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {
                    Log.i(TAG, "jsonObject="+jsonObject.toString());
                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    data.clear();
                    for(int i=0; i<jsonArray.length();i++){
                        JSONObject item = (JSONObject) jsonArray.get(i);
                        int number = item.getInt("number");
                        String name = item.get("name").toString();
                        data.add(new Statistics(name, number));
                    }
                    statisticByCategoryCallback.onStatisticByCategorySuccess(data);
                } catch (JSONException e) {
                    e.printStackTrace();
                    statisticByCategoryCallback.onStatisticByCategoryFailed("解析失败，Error：" + e.getLocalizedMessage());
                    Log.e("解析数据失败", " Error：" + e.getLocalizedMessage());
                }
            }

            @Override
            public void onFail(String msg) {
                statisticByCategoryCallback.onStatisticByCategoryFailed("加载失败，Error：" + msg);
            }
        }, this);
    }

    public interface StatisticByLocationCallback {
        void onStatisticByLocationSuccess(List<Statistics> list);
        void onStatisticByLocationFailed(String msg);
    }

    public void statisticByLocation(final StatisticsPresenter.StatisticByLocationCallback statisticByLocationCallback){
        mModel.statisticByLocation(new RequestManager.NetworkListener() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {
                    Log.i(TAG, "jsonObject="+jsonObject.toString());
                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    data.clear();
                    for(int i=0; i<jsonArray.length();i++){
                        JSONObject item = (JSONObject) jsonArray.get(i);
                        int number = item.getInt("number");
                        String name = item.get("name").toString();
                        data.add(new Statistics(name, number));
                    }
                    statisticByLocationCallback.onStatisticByLocationSuccess(data);
                } catch (JSONException e) {
                    e.printStackTrace();
                    statisticByLocationCallback.onStatisticByLocationFailed("解析失败，Error：" + e.getLocalizedMessage());
                    Log.e("解析数据失败", " Error：" + e.getLocalizedMessage());
                }
            }

            @Override
            public void onFail(String msg) {
                statisticByLocationCallback.onStatisticByLocationFailed("加载失败，Error：" + msg);
            }
        }, this);
    }

    public interface StatisticBySeasonCallback {
        void onStatisticBySeasonSuccess(List<Statistics> list);
        void onStatisticBySeasonFailed(String msg);
    }

    public void statisticBySeason(final StatisticsPresenter.StatisticBySeasonCallback statisticBySeasonCallback){
        mModel.statisticBySeason(new RequestManager.NetworkListener() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {
                    Log.i(TAG, "jsonObject="+jsonObject.toString());
                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    data.clear();
                    for(int i=0; i<jsonArray.length();i++){
                        JSONObject item = (JSONObject) jsonArray.get(i);
                        int number = item.getInt("number");
                        String name = item.get("name").toString();
                        data.add(new Statistics(name, number));
                    }
                    statisticBySeasonCallback.onStatisticBySeasonSuccess(data);
                } catch (JSONException e) {
                    e.printStackTrace();
                    statisticBySeasonCallback.onStatisticBySeasonFailed("解析失败，Error：" + e.getLocalizedMessage());
                    Log.e("解析数据失败", " Error：" + e.getLocalizedMessage());
                }
            }

            @Override
            public void onFail(String msg) {
                statisticBySeasonCallback.onStatisticBySeasonFailed("加载失败，Error：" + msg);
            }
        }, this);
    }

    public interface StatisticByPriceCallback {
        void onStatisticByPriceSuccess(List<Statistics> list);
        void onStatisticByPriceFailed(String msg);
    }

    public void statisticByPrice(final StatisticsPresenter.StatisticByPriceCallback statisticByPriceCallback){
        mModel.statisticByPrice(new RequestManager.NetworkListener() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {
                    Log.i(TAG, "jsonObject="+jsonObject.toString());
                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    data.clear();
                    for(int i=0; i<jsonArray.length();i++){
                        JSONObject item = (JSONObject) jsonArray.get(i);
                        int number = item.getInt("number");
                        String name = item.get("name").toString();
                        data.add(new Statistics(name, number));
                    }
                    statisticByPriceCallback.onStatisticByPriceSuccess(data);
                } catch (JSONException e) {
                    e.printStackTrace();
                    statisticByPriceCallback.onStatisticByPriceFailed("解析失败，Error：" + e.getLocalizedMessage());
                    Log.e("解析数据失败", " Error：" + e.getLocalizedMessage());
                }
            }

            @Override
            public void onFail(String msg) {
                statisticByPriceCallback.onStatisticByPriceFailed("加载失败，Error：" + msg);
            }
        }, this);
    }
}
