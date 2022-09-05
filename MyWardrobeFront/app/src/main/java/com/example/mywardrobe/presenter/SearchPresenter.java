package com.example.mywardrobe.presenter;

import android.util.Log;

import com.example.mywardrobe.activity.SearchActivity;
import com.example.mywardrobe.base.BasePresenter;
import com.example.mywardrobe.entity.Clothes;
import com.example.mywardrobe.model.SearchModel;
import com.example.mywardrobe.utils.RequestManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchPresenter extends BasePresenter<SearchActivity, SearchModel> {
    private String TAG = "SearchPresenter";
    public String username;
    public String keyword;


    public List<Clothes> clothesData = new ArrayList<>();
    public SearchPresenter() {
        this.mModel = new SearchModel();
    }

    public interface GetClothesCallback {
        void onGetClothesSuccess(List<Clothes> ClothesList);
        void onGetClothesFailed(String msg);
    }


    public void getClothes(final SearchPresenter.GetClothesCallback getClothesCallback) {

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
