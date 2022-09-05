package com.example.mywardrobe.presenter;

import android.util.Log;

import com.example.mywardrobe.activity.ChooseActivity;
import com.example.mywardrobe.base.BasePresenter;
import com.example.mywardrobe.entity.Clothes;
import com.example.mywardrobe.entity.Location;
import com.example.mywardrobe.model.ChooseModel;
import com.example.mywardrobe.utils.RequestManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChoosePresenter extends BasePresenter<ChooseActivity, ChooseModel> {
    private String TAG = "ChoosePresenter";
    public String username;
    public String categoryName;
    public int clothesID;
    public Map map = new HashMap();

    public List<Location> locationData = new ArrayList<>();
    public List<Clothes> clothesData = new ArrayList<>();



    public ChoosePresenter() {
        this.mModel = new ChooseModel();
    }

    public interface GetLocationsCallback {
        void onGetLocationsSuccess(List<Location> locationList);
        void onGetLocationsFailed(String msg);
    }

    public interface DeleteClothesCallBack {
        void onDeleteClothesSuccess(String msg, int status);
        void onDeleteClothesFailed(String msg);
    }

    public interface GetClothesCallback {
        void onGetClothesSuccess(List<Clothes> ClothesList);
        void onGetClothesFailed(String msg);
    }


    /**
     * 获取所有位置
     * @param  getLocationsCallback 获取所有位置回调
     */
    public void getLocations(final ChoosePresenter.GetLocationsCallback getLocationsCallback) {
        mModel.requestLocations(new RequestManager.NetworkListener() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {
                    Log.i(TAG, "jsonObject="+jsonObject.toString());
                    JSONArray jsonArray = jsonObject.getJSONArray("locations");
                    locationData.clear();
                    for(int i=0; i<jsonArray.length();i++){
                        JSONObject location = (JSONObject) jsonArray.get(i);
                        int id = location.getInt("id");
                        String name = location.get("name").toString();
                        locationData.add(new Location(id,name));
                    }
                    //返回location列表
                    getLocationsCallback.onGetLocationsSuccess(locationData);
                } catch (Exception e) {
                    e.printStackTrace();
                    getLocationsCallback.onGetLocationsFailed("解析失败，Error：" + e.getLocalizedMessage());
                    Log.e("解析数据失败", " Error：" + e.getLocalizedMessage());
                }
            }

            @Override
            public void onFail(String msg) {
                getLocationsCallback.onGetLocationsFailed("加载失败，Error：" + msg);
            }
        }, this);
    }

    public void deleteClothes(final ChoosePresenter.DeleteClothesCallBack deleteClothesCallBack) {
        mModel.deleteClothes(new RequestManager.NetworkListener() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {
                    Log.i(TAG, jsonObject.toString());
                    String message = jsonObject.get("message").toString();
                    int status = 200;
                    deleteClothesCallBack.onDeleteClothesSuccess(message, status);
                } catch (JSONException e) {
                    e.printStackTrace();
                    deleteClothesCallBack.onDeleteClothesFailed("解析失败，Error：" + e.getLocalizedMessage());
                }
            }

            @Override
            public void onFail(String msg) {
                deleteClothesCallBack.onDeleteClothesFailed("解析失败，Error：" + msg);
            }
        }, this);
    }


    public void getClothes(final ChoosePresenter.GetClothesCallback getClothesCallback) {

        mModel.requestClothes(new RequestManager.NetworkListener() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {

                    Log.i(TAG, "jsonObject="+jsonObject.toString());
                    JSONArray jsonArray = jsonObject.getJSONArray("clothes");
                    clothesData.clear();
                    for(int i=0; i<jsonArray.length();i++){
                        JSONObject clothes = (JSONObject) jsonArray.get(i);
                        int id = clothes.getInt("id");
                        String imageUrl = clothes.get("imageUrl").toString();
                        clothesData.add(new Clothes(id,imageUrl));
                    }
                    //返回location列表
                    getClothesCallback.onGetClothesSuccess(clothesData);
                } catch (Exception e) {
                    e.printStackTrace();
                    getClothesCallback.onGetClothesFailed("解析失败，Error：" + e.getLocalizedMessage());
                    Log.e("解析数据失败", " Error：" + e.getLocalizedMessage());
                }
            }

            @Override
            public void onFail(String msg) {
                getClothesCallback.onGetClothesFailed("加载失败，Error：" + msg);
            }
        }, this);
    }


}
