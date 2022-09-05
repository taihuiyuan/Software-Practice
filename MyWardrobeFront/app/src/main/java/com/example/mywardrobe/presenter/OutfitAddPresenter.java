package com.example.mywardrobe.presenter;

import android.util.Log;

import com.example.mywardrobe.activity.OutfitAddActivity;
import com.example.mywardrobe.base.BasePresenter;
import com.example.mywardrobe.entity.Clothes;
import com.example.mywardrobe.model.OutfitAddModel;
import com.example.mywardrobe.utils.RequestManager;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class OutfitAddPresenter extends BasePresenter<OutfitAddActivity, OutfitAddModel> {
    private String TAG = "OutfitAddPresenter";
    public String username;
    public String outfitName;
    public List<Clothes> allClothes = new ArrayList<>();
    public List<Integer> choices = new ArrayList<>();
    public JSONArray clothes = new JSONArray();

    public OutfitAddPresenter(){this.mModel = new OutfitAddModel();}

    public interface ListClothesCallback{
        void onListClothesSuccess(List<Clothes> clothesList);
        void onListClothesFailed(String msg);
    }

    public interface submitCallback{
        void onSubmitSuccess(String message,int status);
        void onSubmitFail(String msg);
    }


    public void listClothes(final OutfitAddPresenter.ListClothesCallback listClothesCallback){

        mModel.requestAllClothes(new RequestManager.NetworkListener() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try{
                    Log.i(TAG,"jsonObject=" + jsonObject.toString());
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
                }catch(JSONException e){
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

    public void submitChoices(final OutfitAddPresenter.submitCallback submitCallback){
        for(int i =0;i<choices.size();i++){
            clothes.put(choices.get(i));
        }
        Log.i("clothes",clothes.toString());
        mModel.submit(new RequestManager.NetworkListener() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {
                    String message = jsonObject.get("message").toString();
                    int status = jsonObject.getInt("status");
                    Log.e("message", message);
                    submitCallback.onSubmitSuccess(message, status);
                } catch (JSONException e) {
                    e.printStackTrace();
                    submitCallback.onSubmitFail("解析失败， Error:" + e.getLocalizedMessage());
                    Log.e("上传数据解析失败", "Error:" + e.getLocalizedMessage());
                }
            }

            @Override
            public void onFail(String msg) {
                submitCallback.onSubmitFail("上传失败， Error:" + msg);
            }
        },this);
    }

}
