package com.example.mywardrobe.presenter;

import android.util.Log;

import com.example.mywardrobe.activity.OutfitDetailActivity;
import com.example.mywardrobe.base.BasePresenter;
import com.example.mywardrobe.entity.Clothes;
import com.example.mywardrobe.entity.Outfit;
import com.example.mywardrobe.model.OutfitDetailModel;
import com.example.mywardrobe.utils.RequestManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OutfitDetailPresenter extends BasePresenter<OutfitDetailActivity, OutfitDetailModel> {

    private String TAG = "OutfitDetailPresenter";
    public String username;
    public String outfitName;
    public List<Clothes> allClothes = new ArrayList<>();
    public List<Integer> modifyData = new ArrayList<>();
    public List<Outfit> data = new ArrayList<>();
    public int deleteID;
    public JSONArray addData = new JSONArray();
    public JSONArray deleteData = new JSONArray();

    public OutfitDetailPresenter(){this.mModel = new OutfitDetailModel();}

    public interface ListClothesCallback{
        void onListClothesSuccess(List<Clothes> clothesList);
        void onListClothesFailed(String msg);
    }

    public interface addClothesCallback {
        void onAddClothesSuccess(String message,int status);
        void onAddClothesFailed(String msg);
    }

    public interface deleteClothesCallback {
        void onDeleteClothesSuccess(String message,int status);
        void onDeleteClothesFailed(String msg);
    }

    public interface ShowOutfitClothesCallBack {
        void onShowOutfitClothesSuccess(List<Outfit> outfits);
        void onShowOutfitClothesFailed(String msg);
    }

    public void showOutfitClothes(final OutfitDetailPresenter.ShowOutfitClothesCallBack showOutfitClothesCallBack) {
        mModel.showOutfitClothes(new RequestManager.NetworkListener() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {
                    Log.i(TAG, "jsonObject=" + jsonObject.toString());
                    JSONArray jsonArray = jsonObject.getJSONArray("outfits");
                    data.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject outfit = (JSONObject) jsonArray.get(i);
                        String name = outfit.getString("outfitName");
                        List<Clothes> clothes = new ArrayList<>();
                        JSONArray clothesArray = outfit.getJSONArray("clothes");
                        for(int j=0;j<clothesArray.length();j++){
                            Clothes cloth = new Clothes();
                            JSONObject object = (JSONObject) clothesArray.get(j);
                            cloth.setId(object.getInt("id"));
                            cloth.setImageUrl(object.getString("imageUrl"));
                            clothes.add(cloth);
                        }
                        Log.i("outfit clothes", clothes.toString());
                        data.add(new Outfit(name, clothes));
                    }
                    showOutfitClothesCallBack.onShowOutfitClothesSuccess(data);
                } catch (JSONException e) {
                    e.printStackTrace();
                    showOutfitClothesCallBack.onShowOutfitClothesFailed("解析数据失败, Error：" + e.getLocalizedMessage());
                    Log.e("解析数据失败", " Error：" + e.getLocalizedMessage());
                }
            }

            @Override
            public void onFail(String msg) {
                showOutfitClothesCallBack.onShowOutfitClothesFailed("加载失败");
            }
        }, this);
    }

    public void listClothes(final OutfitDetailPresenter.ListClothesCallback listClothesCallback){
        mModel.requestAllClothes(new RequestManager.NetworkListener() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try{
                    JSONArray jsonArray = jsonObject.getJSONArray("clothes");
                    allClothes.clear();
                    for(int i =0;i<jsonArray.length();i++){
                        JSONObject object = (JSONObject)jsonArray.get(i);
                        Clothes clothes = new Clothes();
                        clothes.setId(object.getInt("id"));
                        clothes.setImageUrl(object.getString("imageUrl"));
                        allClothes.add(clothes);
                    }
                    listClothesCallback.onListClothesSuccess(allClothes);
                }catch (JSONException e){
                    e.printStackTrace();
                    listClothesCallback.onListClothesFailed("解析失败，Error：" + e.getLocalizedMessage());
                    Log.e("解析数据失败", " Error：" + e.getLocalizedMessage());
                }
            }

            @Override
            public void onFail(String msg) {
                listClothesCallback.onListClothesFailed("加载失败，Error:" + msg);
            }
        },this);
    }

    public void addClothes(final OutfitDetailPresenter.addClothesCallback addClothesCallback) {
        //add
        for (int i = 0; i < modifyData.size(); i++) {
            Integer id = modifyData.get(i);
            addData.put(id);
        }

        if (addData != null) {
            mModel.add(new RequestManager.NetworkListener() {
                @Override
                public void onSuccess(JSONObject jsonObject) {
                    try {
                        String message = jsonObject.get("message").toString();
                        int status = jsonObject.getInt("status");
                        Log.e("message", message);
                        addClothesCallback.onAddClothesSuccess(message, status);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        addClothesCallback.onAddClothesFailed("解析失败， Error:" + e.getLocalizedMessage());
                        Log.e("上传数据解析失败", "Error:" + e.getLocalizedMessage());
                    }
                }

                @Override
                public void onFail(String msg) {
                    addClothesCallback.onAddClothesFailed("修改Outfit失败，Error:" + msg);
                }
            }, this);
        }
    }

    public void deleteClothes(final OutfitDetailPresenter.deleteClothesCallback deleteClothesCallback){
        deleteData.put(deleteID);
        mModel.delete(new RequestManager.NetworkListener() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {
                    Log.i(TAG, jsonObject.toString());
                    String message = jsonObject.get("message").toString();
                    int status = 200;
                    deleteClothesCallback.onDeleteClothesSuccess(message, status);
                } catch (JSONException e) {
                    e.printStackTrace();
                    deleteClothesCallback.onDeleteClothesFailed("解析失败， Error:" + e.getLocalizedMessage());
                    Log.e("解析失败", "Error:" + e.getLocalizedMessage());
                }
            }

            @Override
            public void onFail(String msg) {
                deleteClothesCallback.onDeleteClothesFailed("修改Outfit失败，Error:" + msg);
            }
        }, this);
    }


}
