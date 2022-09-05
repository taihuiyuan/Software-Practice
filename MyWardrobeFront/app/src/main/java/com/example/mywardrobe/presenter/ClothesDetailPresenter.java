package com.example.mywardrobe.presenter;

import android.util.Log;
import android.widget.Toast;

import com.example.mywardrobe.activity.ClothesDetailActivity;
import com.example.mywardrobe.base.BasePresenter;
import com.example.mywardrobe.entity.Category;
import com.example.mywardrobe.entity.Location;
import com.example.mywardrobe.model.ClothesDetailModel;
import com.example.mywardrobe.response.ClothesDetailResponse;
import com.example.mywardrobe.utils.RequestManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ClothesDetailPresenter extends BasePresenter<ClothesDetailActivity, ClothesDetailModel> {
    private final String TAG = "ClothesDetail";
    public String username;
    public Integer clothesID;
    public String categoryName;
    public String color;
    public String season;
    public String price;
    public String location;
    public String note;
    public String newCategoryName;
    public String newLocation;
    public String deleteLocation;

    private ArrayList<Category> categories = new ArrayList<>();
    private ArrayList<Location> locations = new ArrayList<>();


    public interface GetClothesDetailCallBack {
        void onGetClothesDetailCallBackSuccess(ClothesDetailResponse response);
        void onGetClothesDetailCallBackFail(String msg);
    }

    public interface ModifyClothesDetailCallBack {
        void onModifyClothesDetailSuccess(String message, int status);
        void onModifyClothesDetailFail(String msg);
    }

    public interface ListCategoryCallBack {
        void onListCategoryCallBackSuccess(List<Category> categoryList);
        void onListCategoryFailed(String msg);
    }

    public interface ListLocationCallBack {
        void onListLocationCallBackSuccess(List<Location> list);
        void onListLocationCallBackFailed(String msg);
    }

    public interface AddCategoryCallBack {
        void onAddCategorySuccess(String message, int status);
        void onAddCategoryFailed(String msg);
    }

    public interface AddLocationCallBack {
        void onAddLocationSuccess(String message, int status);
        void onAddLocationFailed(String msg);
    }

    public interface DeleteLocationCallBack {
        void onDeleteLocationSuccess(String message, int status);
        void onDeleteLocationFailed(String msg);
    }

    public interface GetHistoricalLocationCallBack {
        void onGetHistoricalLocationSuccess(List<String> locations);
        void onGetHistoricalLocationFailed(String msg);
    }

    public ClothesDetailPresenter() {
        this.mModel = new ClothesDetailModel();
    }

    public void getClothesDetail(final ClothesDetailPresenter.GetClothesDetailCallBack callBack) {
        mModel.requestGetClothesDetail(new RequestManager.NetworkListener() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {
                    JSONObject object = jsonObject.getJSONObject("clothes");
                    Integer id1 = object.getInt("id");
                    String categoryName1 = object.get("categoryName").toString();
                    String color1 = object.get("color").toString();
                    Log.e("debug", color1);
                    String season1 = object.get("season").toString();
                    String price1 = object.get("price").toString();
                    String location1 = object.get("location").toString();
                    String note1 = object.get("note").toString();
                    String imageUrl = object.get("imageUrl").toString();
                    ClothesDetailResponse response = new ClothesDetailResponse(id1, categoryName1, color1, season1, price1, location1, note1, imageUrl);
                    callBack.onGetClothesDetailCallBackSuccess(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                    callBack.onGetClothesDetailCallBackFail("解析失败，Error：" + e.getLocalizedMessage());
                    Log.e(TAG, " Error：" + e.getLocalizedMessage());
                }
            }

            @Override
            public void onFail(String msg) {
                callBack.onGetClothesDetailCallBackFail("加载失败，Error：" + msg);
            }
        }, this);
    }

    public void modifyClothesDetail(final ModifyClothesDetailCallBack callBack) {
        mModel.requestModifyClothesDetail(new RequestManager.NetworkListener() {

            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {
                    String message = jsonObject.get("message").toString();
                    int status  = jsonObject.getInt("status");
                    callBack.onModifyClothesDetailSuccess(message, status);
                } catch (JSONException e) {
                    e.printStackTrace();
                    callBack.onModifyClothesDetailFail("解析失败，Error：" + e.getLocalizedMessage());
                    Log.e(TAG, " Error：" + e.getLocalizedMessage());
                }
            }

            @Override
            public void onFail(String msg) {
                callBack.onModifyClothesDetailFail("修改失败, Error: " + msg);
            }
        }, this);
    }

    public void listCategory(final ClothesDetailPresenter.ListCategoryCallBack callback) {
        mModel.requestListCategory(new RequestManager.NetworkListener() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {
                    Log.i(TAG, "jsonObject="+jsonObject.toString());
                    JSONArray jsonArray = jsonObject.getJSONArray("categories");
                    categories.clear();
                    for(int i=0; i<jsonArray.length();i++){
                        JSONObject category = (JSONObject) jsonArray.get(i);
                        int id = category.getInt("id");
                        String name = category.get("name").toString();
                        categories.add(new Category(id,name));
                    }
                    //返回category列表
                    callback.onListCategoryCallBackSuccess(categories);
                } catch (JSONException e) {
                    e.printStackTrace();
                    callback.onListCategoryFailed("解析失败，Error：" + e.getLocalizedMessage());
                    Log.e("解析数据失败", " Error：" + e.getLocalizedMessage());
                }
            }

            @Override
            public void onFail(String msg) {
                callback.onListCategoryFailed("加载失败，Error：" + msg);
            }
        }, this);
    }

    public void listLocation(final ListLocationCallBack callBack) {
        mModel.requestListLocation(new RequestManager.NetworkListener() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {
                    Log.i(TAG, "jsonObject="+jsonObject.toString());
                    JSONArray jsonArray = jsonObject.getJSONArray("locations");
                    locations.clear();
                    for(int i=0; i<jsonArray.length();i++){
                        JSONObject location = (JSONObject) jsonArray.get(i);
                        int id = location.getInt("id");
                        String name = location.get("name").toString();
                        locations.add(new Location(id,name));
                    }
                    //返回Location列表
                    callBack.onListLocationCallBackSuccess(locations);
                } catch (JSONException e) {
                    e.printStackTrace();
                    callBack.onListLocationCallBackFailed("解析失败，Error：" + e.getLocalizedMessage());
                    Log.e("解析数据失败", " Error：" + e.getLocalizedMessage());
                }
            }

            @Override
            public void onFail(String msg) {
                callBack.onListLocationCallBackFailed("加载失败，Error：" + msg);
            }
        }, this);
    }

    public void addCategory(final ClothesDetailPresenter.AddCategoryCallBack callBack ){
        mModel.requestAddCategory(new RequestManager.NetworkListener() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {
                    Log.i(TAG, "jsonObject="+jsonObject.toString());
                    String message = jsonObject.get("message").toString();
                    int status = jsonObject.getInt("status");
                    Log.e("message", message);
                    callBack.onAddCategorySuccess(message, status);
                } catch (JSONException e) {
                    e.printStackTrace();
                    callBack.onAddCategoryFailed("解析失败，Error：" + e.getLocalizedMessage());
                    Log.e("解析数据失败", " Error：" + e.getLocalizedMessage());
                }
            }

            @Override
            public void onFail(String msg) {
                callBack.onAddCategoryFailed("加载失败，Error：" + msg);
            }
        }, this);
    }

    public void addLocation(final AddLocationCallBack callBack) {
        mModel.requestAddLocation(new RequestManager.NetworkListener() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {
                    Log.i(TAG, "jsonObject="+jsonObject.toString());
                    String message = jsonObject.get("message").toString();
                    int status = jsonObject.getInt("status");
                    Log.e("message", message);
                    callBack.onAddLocationSuccess(message, status);
                } catch (JSONException e) {
                    e.printStackTrace();
                    callBack.onAddLocationFailed("解析失败，Error：" + e.getLocalizedMessage());
                    Log.e("解析数据失败", " Error：" + e.getLocalizedMessage());
                }
            }

            @Override
            public void onFail(String msg) {
                callBack.onAddLocationFailed("加载失败，Error：" + msg);
            }
        }, this);
    }

    public void deleteLocation(final DeleteLocationCallBack callBack){
        mModel.requestDeleteLocation(new RequestManager.NetworkListener() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {
                    Log.i(TAG, "jsonObject="+jsonObject.toString());
                    String message = jsonObject.get("message").toString();
                    int status = jsonObject.getInt("status");
                    Log.e("message", message);
                    callBack.onDeleteLocationSuccess(message, status);
                } catch (JSONException e) {
                    e.printStackTrace();
                    callBack.onDeleteLocationFailed("解析失败，Error：" + e.getLocalizedMessage());
                    Log.e("解析数据失败", " Error：" + e.getLocalizedMessage());
                }
            }

            @Override
            public void onFail(String msg) {
                callBack.onDeleteLocationFailed("加载失败，Error：" + msg);
            }
        }, this);
    }

    public void getHistoricalLocation(final GetHistoricalLocationCallBack callBack) {
        mModel.requestGetHistoricalLocation(new RequestManager.NetworkListener() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {
                    JSONArray history = new JSONArray();
                    List<String> result = new ArrayList<>();
                    history = jsonObject.getJSONArray("history");
                    for (int i = 0; i < history.length(); i++) {
                        JSONObject data = new JSONObject();
                        data = history.getJSONObject(i);
                        String temp = data.getString("location");
                        temp += "  " + data.getString("time");
                        result.add(temp);
                    }
                    callBack.onGetHistoricalLocationSuccess(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                    callBack.onGetHistoricalLocationFailed("解析失败，Error：" + e.getLocalizedMessage());
                }
            }

            @Override
            public void onFail(String msg) {
                callBack.onGetHistoricalLocationFailed("加载失败，Error：" + msg);
            }
        }, this);
    }
}
