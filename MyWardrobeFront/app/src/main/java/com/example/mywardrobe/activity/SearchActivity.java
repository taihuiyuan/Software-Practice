package com.example.mywardrobe.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mywardrobe.base.BaseActivity;
import com.example.mywardrobe.entity.Clothes;
import com.example.mywardrobe.presenter.SearchPresenter;
import com.example.mywardrobe.R;


import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends BaseActivity<SearchPresenter> {
    String TAG = "SearchActivity";
    private Button btnCancel;
    private SearchView svClothes;

    //clothes List
    private List<Clothes> clothesList = new ArrayList<>();
    ClothesListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initView();

        btnCancel = (Button) findViewById(R.id.btnCancel);
        svClothes = (SearchView) findViewById(R.id.svClothes);
        svClothes.setIconifiedByDefault(false);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();
            }
        });

        svClothes.clearFocus();
        svClothes.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s){
                if (TextUtils.isEmpty(s)){
                    Toast.makeText(getApplicationContext(), "搜索内容不能为空", Toast.LENGTH_LONG).show();
                }
                Log.i(TAG,"search: "+s);
                mPresenter.keyword = s;
                getClothes();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (TextUtils.isEmpty(s)){
                    Toast.makeText(getApplicationContext(), "搜索内容不能为空", Toast.LENGTH_LONG).show();
                }
                return false;
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    public void initView() {
        mPresenter = new SearchPresenter();
        mPresenter.attachView(this);

        SharedPreferences mSharedPreferences = getSharedPreferences("loginData", Context.MODE_PRIVATE);
        mPresenter.username = mSharedPreferences.getString("username","");
        Log.i(TAG, "username:"+mPresenter.username);
        initAdapter();
    }

    private void getClothes(){
        mPresenter.getClothes(new SearchPresenter.GetClothesCallback() {
            @Override
            public void onGetClothesSuccess(List<Clothes> data) {
                clothesList.clear();
                clothesList.addAll(data);
                if (data.size() == 0){
                    Toast.makeText(getApplicationContext(), "搜索结果为空", Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(getApplicationContext(), "搜索成功", Toast.LENGTH_LONG).show();
                }
                Log.i(TAG, clothesList.toString());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onGetClothesFailed(String msg) {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void initAdapter() {
        RecyclerView rvSearch = (RecyclerView) findViewById(R.id.rvSearch);
        rvSearch.setLayoutManager(new GridLayoutManager(this,3));
        adapter = new ClothesListAdapter(clothesList);

        adapter.setItemClick(new ClothesListAdapter.ItemClick() {
            @Override
            public void onItemClick(int id) {
                Log.i(TAG, "click clothes "+id);
                toDetail(id);
            }
        } );
        rvSearch.setAdapter(adapter);
    }

    public void toDetail(int clothesID) {
        SharedPreferences.Editor editor = getSharedPreferences("loginData", MODE_PRIVATE).edit();
        editor.putInt("clothesID", clothesID);
        editor.commit();
        Intent intent = new Intent(SearchActivity.this, ClothesDetailActivity.class);
        startActivity(intent);
    }

    public void back() {
        SearchActivity.this.finish();
    }
}
