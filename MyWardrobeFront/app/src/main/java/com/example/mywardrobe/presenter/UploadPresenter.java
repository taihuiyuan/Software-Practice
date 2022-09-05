package com.example.mywardrobe.presenter;

import android.util.Log;
import android.widget.Toast;

import com.example.mywardrobe.activity.UploadActivity;
import com.example.mywardrobe.base.BasePresenter;
import com.example.mywardrobe.entity.Category;
import com.example.mywardrobe.entity.Location;
import com.example.mywardrobe.model.UploadModel;
import com.example.mywardrobe.utils.RequestManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UploadPresenter extends BasePresenter<UploadActivity, UploadModel> {
    private String TAG = "UploadPresenter";
    public String username = "";
    public String imageUrl = "";
    public String categoryName = "";
    public String newCategoryName = "";
    public String color = "";
    public String season = "";
    public String price = "";
    public String location = "";
    public String newLocation = "";
    public String note = "";

    private ArrayList<Category> categories = new ArrayList<>();
    private ArrayList<Location> locations = new ArrayList<>();

    public interface UploadCallback {
        void onUploadSuccess(String message, int status);
        void onUploadFail(String msg);
    }

    public interface UploadImageCallBack {
        void onUploadImageSuccess(String message, int status);
        void onUploadImageFail();
    }

    public UploadPresenter() {
        this.mModel = new UploadModel();
    }

    public void upload(final UploadCallback uploadCallback) {
        //这里可以对前端数据进行检查
        if (username.isEmpty()) {
            Toast.makeText(this.mView, "未登陆状态下不能上传", Toast.LENGTH_SHORT).show();
        }
        if (imageUrl.isEmpty()) {
            Toast.makeText(this.mView, "图片不能为空", Toast.LENGTH_SHORT).show();
        }
        mModel.requestUpload(new RequestManager.NetworkListener() {

            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {
                    String message = jsonObject.get("message").toString();
                    int status = jsonObject.getInt("status");
                    Log.e("message", message);
                    uploadCallback.onUploadSuccess(message, status);
                } catch (JSONException e) {
                    e.printStackTrace();
                    uploadCallback.onUploadFail("解析失败， Error:" + e.getLocalizedMessage());
                    Log.e("上传数据解析失败", "Error:" + e.getLocalizedMessage());
                }

            }

            @Override
            public void onFail(String msg) {
                uploadCallback.onUploadFail("上传失败, Error" + msg);
            }
        }, this);
    }

    public interface ListCategoryCallback {
        void onListCategorySuccess(List<Category> categoryList);
        void onListCategoryFailed(String msg);
    }

    public void listCategory(final UploadPresenter.ListCategoryCallback listCategoryCallback) {
        mModel.requestCategory(new RequestManager.NetworkListener() {
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
                    listCategoryCallback.onListCategorySuccess(categories);
                } catch (JSONException e) {
                    e.printStackTrace();
                    listCategoryCallback.onListCategoryFailed("解析失败，Error：" + e.getLocalizedMessage());
                    Log.e("解析数据失败", " Error：" + e.getLocalizedMessage());
                }
            }

            @Override
            public void onFail(String msg) {
                listCategoryCallback.onListCategoryFailed("加载失败，Error：" + msg);
            }
        }, this);
    }

    public interface ListLocationCallback {
        void onListLocationSuccess(List<Location> list);
        void onListLocationFailed(String msg);
    }

    public void listLocation(final UploadPresenter.ListLocationCallback listLocationCallback) {
        mModel.requestLocation(new RequestManager.NetworkListener() {
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
                    listLocationCallback.onListLocationSuccess(locations);
                } catch (JSONException e) {
                    e.printStackTrace();
                    listLocationCallback.onListLocationFailed("解析失败，Error：" + e.getLocalizedMessage());
                    Log.e("解析数据失败", " Error：" + e.getLocalizedMessage());
                }
            }

            @Override
            public void onFail(String msg) {
                listLocationCallback.onListLocationFailed("加载失败，Error：" + msg);
            }
        }, this);
    }

    public interface AddCategoryCallback {
        void onAddCategorySuccess(String message, int status);
        void onAddCategoryFailed(String msg);
    }

    public void addCategory(final UploadPresenter.AddCategoryCallback addCategoryCallback) {
        mModel.addCategory(new RequestManager.NetworkListener() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {
                    Log.i(TAG, "jsonObject="+jsonObject.toString());
                    String message = jsonObject.get("message").toString();
                    int status = jsonObject.getInt("status");
                    Log.e("message", message);
                    addCategoryCallback.onAddCategorySuccess(message, status);
                } catch (JSONException e) {
                    e.printStackTrace();
                    addCategoryCallback.onAddCategoryFailed("解析失败，Error：" + e.getLocalizedMessage());
                    Log.e("解析数据失败", " Error：" + e.getLocalizedMessage());
                }
            }

            @Override
            public void onFail(String msg) {
                addCategoryCallback.onAddCategoryFailed("加载失败，Error：" + msg);
            }
        }, this);
    }

    public interface AddLocationCallback {
        void onAddLocationSuccess(String message, int status);
        void onAddLocationFailed(String msg);
    }

    public void addLocation(final UploadPresenter.AddLocationCallback addLocationCallback) {
        mModel.addLocation(new RequestManager.NetworkListener() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {
                    Log.i(TAG, "jsonObject="+jsonObject.toString());
                    String message = jsonObject.get("message").toString();
                    int status = jsonObject.getInt("status");
                    Log.e("message", message);
                    addLocationCallback.onAddLocationSuccess(message, status);
                } catch (JSONException e) {
                    e.printStackTrace();
                    addLocationCallback.onAddLocationFailed("解析失败，Error：" + e.getLocalizedMessage());
                    Log.e("解析数据失败", " Error：" + e.getLocalizedMessage());
                }
            }

            @Override
            public void onFail(String msg) {
                addLocationCallback.onAddLocationFailed("加载失败，Error：" + msg);
            }
        }, this);
    }
}
