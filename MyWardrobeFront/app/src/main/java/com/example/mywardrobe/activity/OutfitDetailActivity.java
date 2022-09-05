package com.example.mywardrobe.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mywardrobe.base.BaseActivity;
import com.example.mywardrobe.entity.Clothes;
import com.example.mywardrobe.R;
import com.example.mywardrobe.entity.Outfit;
import com.example.mywardrobe.presenter.OutfitDetailPresenter;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;


public class OutfitDetailActivity extends BaseActivity<OutfitDetailPresenter> {
    String TAG = "OutfitDetailActivity";

    private List<Clothes> clothesList = new ArrayList<>();
    private List<Clothes> allClothes = new ArrayList<>();
    private Button btnBack;
    private Button btnModify;
    private Button btnShow;
    private Button btnCancel;
    private TextView tvOutfitName;
    private TextView title;
    private RecyclerView rvAllClothes;
    OutfitDetailAdapter adapter;
    OutfitAddAdapter allClothesAdapter;

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_outfitdetail);
        initView();
        initAdapter();

        tvOutfitName = this.findViewById(R.id.outfitDetailName);

        tvOutfitName.setText(mPresenter.outfitName);

        btnShow = findViewById(R.id.btnEdit);
        title = findViewById(R.id.tv_title);
        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title.setVisibility(View.VISIBLE);
                showAllClothes();
                btnModify.setVisibility(View.VISIBLE);
                btnCancel.setVisibility(View.VISIBLE);
            }
        });


        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){back();}
        });

        btnModify = findViewById(R.id.btn_modify);
        btnModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Integer> choices = allClothesAdapter.getChoices();
                if(choices.isEmpty()){
                    Toast.makeText(getApplicationContext(), "请选择至少一件衣物", Toast.LENGTH_SHORT).show();
                }else {
                    mPresenter.modifyData = choices;
                    doModify();
                }
            }
        });

        btnCancel = findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title.setVisibility(View.GONE);
                rvAllClothes.setVisibility(View.GONE);
                btnModify.setVisibility(View.GONE);
                btnCancel.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void initView() {
        mPresenter = new OutfitDetailPresenter();
        mPresenter.attachView(this);

        SharedPreferences mSharedPreferences = getSharedPreferences("loginData", Context.MODE_PRIVATE);
        mPresenter.username = mSharedPreferences.getString("username","");
        mPresenter.outfitName = mSharedPreferences.getString("outfitName", "");

        showDetails();
    }

    private void showDetails(){
        mPresenter.showOutfitClothes(new OutfitDetailPresenter.ShowOutfitClothesCallBack() {
            @Override
            public void onShowOutfitClothesSuccess(List<Outfit> outfits) {
                for (Outfit o: outfits){
                    if (o.getOutfitName().equals(mPresenter.outfitName)){
                        clothesList.clear();
                        clothesList.addAll(o.getClothes());
                        adapter.notifyDataSetChanged();
                        return;
                    }
                }
            }

            @Override
            public void onShowOutfitClothesFailed(String msg) {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void initAdapter(){
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.rvOutfitClothes);
        recyclerView.setLayoutManager(new GridLayoutManager(this,3));
        adapter = new OutfitDetailAdapter(clothesList);
        adapter.setItemClick(new OutfitDetailAdapter.ItemClick() {
            @Override
            public void onItemClick(int id) {
                toDetail(id);
            }
        });

        adapter.setOnItemLongClickListener(new OutfitDetailAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(OutfitDetailActivity.this);
                builder.setTitle("删除衣物");
                builder.setMessage("是否删除该衣物");
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteClothes(position);
                    }
                });
                builder.show();
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void deleteClothes(int position) {
        Clothes clothes = clothesList.get(position);
        mPresenter.deleteID = clothes.getId();
        mPresenter.deleteClothes(new OutfitDetailPresenter.deleteClothesCallback() {
            @Override
            public void onDeleteClothesSuccess(String message, int status) {
                if(status == 200){
                    Toast.makeText(getApplicationContext(), "删除成功", Toast.LENGTH_SHORT).show();
                    refresh();
                }else {
                    Toast.makeText(getApplicationContext(), "删除失败", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onDeleteClothesFailed(String msg) {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void refresh(){
        this.showDetails();
    }

    private void back(){
        OutfitDetailActivity.this.finish();
    }

    private void doModify(){
        mPresenter.addClothes(new OutfitDetailPresenter.addClothesCallback() {
            @Override
            public void onAddClothesSuccess(String message, int status) {
                if(status == 200){
                    Toast.makeText(getApplicationContext(), "添加成功", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getApplicationContext(), "添加失败", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onAddClothesFailed(String msg) {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }
        });

        toOutfit();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_outfitdetail;
    }

    public void showAllClothes(){
        rvAllClothes = (RecyclerView) findViewById(R.id.rvAllClothes);
        rvAllClothes.setLayoutManager(new GridLayoutManager(this,2));
        allClothesAdapter = new OutfitAddAdapter(allClothes);

        rvAllClothes.setAdapter(allClothesAdapter);

        mPresenter.listClothes(new OutfitDetailPresenter.ListClothesCallback() {
            @Override
            public void onListClothesSuccess(List<Clothes> clothesList) {
                allClothes.clear();
                List<Clothes> clothes_have = OutfitDetailActivity.this.clothesList;
                for (Clothes c: clothesList){
                    int flag = 0;
                    for (Clothes c_have: clothes_have){
                        if (c_have.getId().equals(c.getId())) {
                            flag = 1;
                            break;
                        }
                    }
                    if (flag == 0){
                        allClothes.add(c);
                    }
                }
                allClothesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onListClothesFailed(String msg) {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }
        });
        rvAllClothes.setVisibility(View.VISIBLE);
    }

    private void toOutfit(){
//        OutfitAddActivity.this.finish();
        Intent intent = new Intent(OutfitDetailActivity.this, NavbarActivity.class);
        startActivity(intent);
    }


    public void toDetail(int clothesID) {
        SharedPreferences.Editor editor = getSharedPreferences("loginData", MODE_PRIVATE).edit();
        editor.putInt("clothesID", clothesID);
        Log.e("debug", "clothesId1"+clothesID);
        editor.commit();
        Intent intent = new Intent(OutfitDetailActivity.this, ClothesDetailActivity.class);
        startActivity(intent);
    }

}
