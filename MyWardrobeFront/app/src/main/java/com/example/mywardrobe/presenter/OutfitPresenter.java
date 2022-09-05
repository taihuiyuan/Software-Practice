package com.example.mywardrobe.presenter;

import android.util.Log;

import com.example.mywardrobe.activity.HomeActivity;
import com.example.mywardrobe.activity.OutfitActivity;
import com.example.mywardrobe.base.BasePresenter;
import com.example.mywardrobe.entity.Category;
import com.example.mywardrobe.entity.Clothes;
import com.example.mywardrobe.entity.Outfit;
import com.example.mywardrobe.model.OutfitModel;
import com.example.mywardrobe.utils.RequestManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OutfitPresenter extends BasePresenter<OutfitActivity, OutfitModel> {
    private String TAG = "OutfitPresenter";
    public String username;
    public List<Outfit> data = new ArrayList<>();
    public String deleteOutfitName;
    public String outfitName;
    public String newOutfitName;

    public OutfitPresenter(){this.mModel = new OutfitModel();}

    public interface ListOutfitCallback {
        void onListOutfitSuccess(List<Outfit> outfitList);
        void onListOutfitFailed(String msg);
    }

    public void listOutfits(final OutfitPresenter.ListOutfitCallback listOutfitCallback){
        //test
//        data.clear();
//        List<Clothes> list = new ArrayList<>();
//        Clothes clothes = new Clothes();
//        clothes.setId(14);
//        clothes.setImageUrl("http://129.211.165.110:8001/api/file/20220520142251817.jpg");
//        list.add(clothes);
//        data.add(new Outfit("outfit1",list));
//        data.add(new Outfit("outfit2",list));
//        data.add(new Outfit("outfit3",list));
//        Log.i(TAG, "jsonObject=" + data.toString());
//        listOutfitCallback.onListOutfitSuccess(data);

        mModel.requestOutfits(new RequestManager.NetworkListener() {
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

                    listOutfitCallback.onListOutfitSuccess(data);
                } catch (JSONException e) {
                    e.printStackTrace();
                    listOutfitCallback.onListOutfitFailed("解析失败，Error：" + e.getLocalizedMessage());
                    Log.e("解析数据失败", " Error：" + e.getLocalizedMessage());
                }
            }

            @Override
            public void onFail(String msg) {
                listOutfitCallback.onListOutfitFailed("加载失败，Error：" + msg);
            }
        },this);
    }

    public interface deleteOutfitCallback{
        void onDeleteOutfitSuccess(String message,int status);
        void onDeleteOutfitFailed(String msg);
    }

    public void deleteOutfit(final OutfitPresenter.deleteOutfitCallback deleteOutfitCallback){
        mModel.deleteOutfit(new RequestManager.NetworkListener() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {
                    String message = jsonObject.get("message").toString();
                    int status = jsonObject.getInt("status");
                    Log.e("message", message);
                    deleteOutfitCallback.onDeleteOutfitSuccess(message, status);
                } catch (JSONException e) {
                    e.printStackTrace();
                    deleteOutfitCallback.onDeleteOutfitFailed("解析失败， Error:" + e.getLocalizedMessage());
                    Log.e("删除穿搭解析失败", "Error:" + e.getLocalizedMessage());
                }
            }

            @Override
            public void onFail(String msg) {
                deleteOutfitCallback.onDeleteOutfitFailed("删除失败, Error" + msg);
            }
        },this);
    }

}
