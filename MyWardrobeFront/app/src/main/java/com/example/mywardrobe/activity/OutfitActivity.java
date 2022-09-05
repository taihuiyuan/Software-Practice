package com.example.mywardrobe.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mywardrobe.R;
import com.example.mywardrobe.base.BaseActivity;
import com.example.mywardrobe.entity.Clothes;
import com.example.mywardrobe.entity.Outfit;
import com.example.mywardrobe.presenter.OutfitPresenter;

import java.util.ArrayList;
import java.util.List;

public class OutfitActivity extends BaseActivity<OutfitPresenter> {
    String TAG = "OutfitActivity";

    List<Outfit> outfitList = new ArrayList<>();
    OutfitListAdapter adapter;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_outfit);
        initView();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_outfit;
    }

    @Override
    public void initView() {
        mPresenter = new OutfitPresenter();
        mPresenter.attachView(this);

        SharedPreferences mSharedPreferences = getSharedPreferences("loginData", Context.MODE_PRIVATE);

        mPresenter.username = mSharedPreferences.getString("username","");
        Log.i(TAG, "username="+mPresenter.username);

        initAdapter();
        showOutfits();
    }

    private void showOutfits(){
        mPresenter.listOutfits(new OutfitPresenter.ListOutfitCallback() {
            @Override
            public void onListOutfitSuccess(List<Outfit> dataList) {
                outfitList.clear();
                outfitList.addAll(dataList);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onListOutfitFailed(String msg) {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void initAdapter(){
        RecyclerView rvOutfit = (RecyclerView) findViewById(R.id.rvOutfit);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvOutfit.setLayoutManager(linearLayoutManager);
        adapter = new OutfitListAdapter(outfitList);

        adapter.setItemClick(new OutfitListAdapter.ItemClick() {
            @Override
            public void onItemButtonClick(String outfitName, List<Clothes> clothes) {
                Log.i("outfitDetail","click outfit"+outfitName);
                toChoose(outfitName,clothes);
            }

            @Override
            public void onItemDeleteClick(String outfitName) {
                AlertDialog.Builder builder = new AlertDialog.Builder(OutfitActivity.this);
                builder.setTitle("删除穿搭");
                builder.setMessage("是否删除穿搭"+outfitName);
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        delete(outfitName);
                    }
                });
                builder.show();
            }
        });
        rvOutfit.setAdapter(adapter);
    }

    @Override
    protected void onResume(){
        super.onResume();
        setContentView(R.layout.activity_outfit);
        initView();
    }

    public void toChoose(String name,List<Clothes> clothes){
        Intent intent = new Intent(OutfitActivity.this,OutfitDetailActivity.class);

        SharedPreferences.Editor editor = getSharedPreferences("loginData", MODE_PRIVATE).edit();
        editor.putString("outfitName", name);
        editor.commit();

        startActivity(intent);
    }

    public void delete(String outfitName){
        if(outfitName.isEmpty()){
            Toast.makeText(getApplicationContext(), "获取穿搭名称失败", Toast.LENGTH_SHORT).show();
        }else {
            mPresenter.deleteOutfitName = outfitName;
            mPresenter.deleteOutfit(new OutfitPresenter.deleteOutfitCallback() {
                @Override
                public void onDeleteOutfitSuccess(String message, int status) {
                    if (status == 200) {
                        Toast.makeText(getApplicationContext(), "删除成功！", Toast.LENGTH_SHORT).show();
                        Refresh();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "删除失败"+message, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onDeleteOutfitFailed(String msg) {
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                }
            });
        }

    }

    private void Refresh(){
        this.showOutfits();
    }

}
