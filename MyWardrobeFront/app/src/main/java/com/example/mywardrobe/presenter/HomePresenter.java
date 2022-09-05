package com.example.mywardrobe.presenter;

import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.example.mywardrobe.activity.HomeActivity;
import com.example.mywardrobe.base.BasePresenter;
import com.example.mywardrobe.entity.Category;
import com.example.mywardrobe.model.HomeModel;
import com.example.mywardrobe.utils.RequestManager;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomePresenter extends BasePresenter<HomeActivity, HomeModel> {
    private String TAG = "HomePresenter";
    public String username;
    public String categoryName;
    public String newCategoryName;
    public List<Category> data = new ArrayList<>();

    public HomePresenter() {
        this.mModel = new HomeModel();
    }

    public interface ListCategoryCallback {
        void onListCategorySuccess(List<Category> categoryList);
        void onListCategoryFailed(String msg);
    }

    public interface AddCategoryCallback {
        void onAddCategorySuccess(String message, int status);
        void onAddCategoryFailed(String msg);
    }

    public interface EditCategoryCallback {
        void onEditCategorySuccess(String message, int status);
        void onEditCategoryFailed(String msg);
    }

    public interface DeleteCategoryCallback {
        void onDeleteCategorySuccess(String message, int status);
        void onDeleteCategoryFailed(String msg);
    }

    /**
     * 列出分类
     * @param  listCategoryCallback 列出所有分类回调
     */
    public void listCategory(final HomePresenter.ListCategoryCallback listCategoryCallback) {
       mModel.requestCategory(new RequestManager.NetworkListener() {
           @Override
           public void onSuccess(JSONObject jsonObject) {
               try {
                   Log.i(TAG, "jsonObject="+jsonObject.toString());
                   JSONArray jsonArray = jsonObject.getJSONArray("categories");
                   data.clear();
                   for(int i=0; i<jsonArray.length();i++){
                       JSONObject category = (JSONObject) jsonArray.get(i);
                       int id = category.getInt("id");
                       String name = category.get("name").toString();
                       data.add(new Category(id,name));
                   }
                   //返回category列表
                   listCategoryCallback.onListCategorySuccess(data);
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

    public void addCategory(final HomePresenter.AddCategoryCallback addCategoryCallback) {
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

    public void editCategory(final HomePresenter.EditCategoryCallback editCategoryCallback) {
        mModel.editCategory(new RequestManager.NetworkListener() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {
                    Log.i(TAG, "jsonObject="+jsonObject.toString());
                    String message = jsonObject.get("message").toString();
                    int status = jsonObject.getInt("status");
                    Log.e("message", message);
                    editCategoryCallback.onEditCategorySuccess(message, status);
                } catch (JSONException e) {
                    e.printStackTrace();
                    editCategoryCallback.onEditCategoryFailed("解析失败，Error：" + e.getLocalizedMessage());
                    Log.e("解析数据失败", " Error：" + e.getLocalizedMessage());
                }
            }

            @Override
            public void onFail(String msg) {
                editCategoryCallback.onEditCategoryFailed("加载失败，Error：" + msg);
            }
        }, this);
    }

    public void deleteCategory(final HomePresenter.DeleteCategoryCallback deleteCategoryCallback) {
        mModel.deleteCategory(new RequestManager.NetworkListener() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {
                    Log.i(TAG, "jsonObject="+jsonObject.toString());
                    String message = jsonObject.get("message").toString();
                    int status = jsonObject.getInt("status");
                    Log.e("message", message);
                    deleteCategoryCallback.onDeleteCategorySuccess(message, status);
                } catch (JSONException e) {
                    e.printStackTrace();
                    deleteCategoryCallback.onDeleteCategoryFailed("解析失败，Error：" + e.getLocalizedMessage());
                    Log.e("解析数据失败", " Error：" + e.getLocalizedMessage());
                }
            }

            @Override
            public void onFail(String msg) {
                deleteCategoryCallback.onDeleteCategoryFailed("加载失败，Error：" + msg);
            }
        }, this);
    }
}
