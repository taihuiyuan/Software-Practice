package com.example.mywardrobe.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mywardrobe.base.BaseActivity;
import com.example.mywardrobe.entity.Clothes;
import com.example.mywardrobe.presenter.OutfitAddPresenter;
import com.example.mywardrobe.presenter.OutfitPresenter;
import com.example.mywardrobe.R;
import java.util.ArrayList;
import java.util.List;


public class OutfitAddActivity extends BaseActivity<OutfitAddPresenter> {
    String TAG = "OutfitAddActivity";

    private List<Clothes> allClothes = new ArrayList<>();
    private Button btnBack;
    private Button btnSubmit;
    private EditText etOutfitName;
    OutfitAddAdapter adapter;

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_addoutfit);
        initView();
        initAdapter();
        showAllClothes();

        etOutfitName = (EditText)findViewById(R.id.outfitName);

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();
            }
        });

        btnSubmit = findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Integer> choices = adapter.getChoices();
                String outfitName = etOutfitName.getText().toString();
                if(choices.isEmpty()){
                    Toast.makeText(getApplicationContext(), "请选择至少一件衣物", Toast.LENGTH_SHORT).show();
                }else if(outfitName.isEmpty()){
                    Toast.makeText(getApplicationContext(), "请输入穿搭名字", Toast.LENGTH_SHORT).show();
                }else {
                    mPresenter.outfitName = etOutfitName.getText().toString();
                    mPresenter.choices = choices;
                    doSubmit();
                }
            }
        });


    }

    private void showAllClothes(){
        mPresenter.listClothes(new OutfitAddPresenter.ListClothesCallback() {
            @Override
            public void onListClothesSuccess(List<Clothes> clothesList) {
                allClothes.clear();
                allClothes.addAll(clothesList);
                Log.i(TAG,allClothes.toString());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onListClothesFailed(String msg) {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }
        });
    }



    @Override
    public int getLayoutId() {
        return R.layout.activity_addoutfit;
    }

    @Override
    public void initView() {
        mPresenter = new OutfitAddPresenter();
        mPresenter.attachView(this);

        SharedPreferences mSharedPreferences = getSharedPreferences("loginData", Context.MODE_PRIVATE);
        mPresenter.username = mSharedPreferences.getString("username","");
        Log.i(TAG, "username:"+mPresenter.username);

    }

    public void initAdapter(){
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.rvAddOutfit);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        adapter = new OutfitAddAdapter(allClothes);
        recyclerView.setAdapter(adapter);
    }

    public void back(){
        Intent intent = new Intent(OutfitAddActivity.this, NavbarActivity.class);
        startActivity(intent);
    }

    private void doSubmit(){
        mPresenter.submitChoices(new OutfitAddPresenter.submitCallback() {
            @Override
            public void onSubmitSuccess(String message, int status) {
                if(status == 200){
                    Toast.makeText(getApplicationContext(), "添加成功", Toast.LENGTH_SHORT).show();
                    back();
                }else {
                    Toast.makeText(getApplicationContext(), "添加失败", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onSubmitFail(String msg) {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }
        });
    }
}
