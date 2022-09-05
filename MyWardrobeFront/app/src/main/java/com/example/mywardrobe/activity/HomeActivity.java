package com.example.mywardrobe.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mywardrobe.R;
import com.example.mywardrobe.base.BaseActivity;
import com.example.mywardrobe.entity.Category;
import com.example.mywardrobe.presenter.HomePresenter;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends BaseActivity<HomePresenter> {
    String TAG = "HomeActivity";

    List<Category> categoryList = new ArrayList<>();
    CategoryListAdapter adapter;
    private int item = 0;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_home);
        initView();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_home;
    }

    @Override
    public void initView() {
        mPresenter = new HomePresenter();
        mPresenter.attachView(this);

        SharedPreferences mSharedPreferences = getSharedPreferences("loginData", Context.MODE_PRIVATE);
        mPresenter.username = mSharedPreferences.getString("username","");
        Log.i(TAG, "username="+mPresenter.username);

        initAdapter();
        showCategory();
    }

    private void showCategory(){
        mPresenter.listCategory(new HomePresenter.ListCategoryCallback() {
            @Override
            public void onListCategorySuccess(List<Category> dataList) {
                categoryList.clear();
                categoryList.addAll(dataList);
                Log.i(TAG, categoryList.toString());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onListCategoryFailed(String msg) {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void initAdapter() {
        RecyclerView rvCategory = (RecyclerView) findViewById(R.id.rvCategory);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvCategory.setLayoutManager(linearLayoutManager);
        adapter = new CategoryListAdapter(categoryList);

        adapter.setItemClick(new CategoryListAdapter.ItemClick() {
            @Override
            public void onItemButtonClick(String name, int id) {
                Log.i(TAG, "click category "+name);
                toChoose(name);
            }
        } );

        adapter.setOnItemLongClickListener(new CategoryListAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                final String[] choices = {"新增分类","修改分类名","删除分类"};

                builder.setTitle("操作");
                builder.setSingleChoiceItems(choices, item , new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        item = which;
                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(HomeActivity.this);
                        final EditText editText = new EditText(HomeActivity.this);
                        builder1.setView(editText);
                        switch (item){
                            case 0:
                                dialog.dismiss();
                                builder1.setTitle("新增分类");
                                editText.setHint("请输入新增的分类名");
                                builder1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (editText.getText().length() > 15){
                                            Toast.makeText(HomeActivity.this,"分类名不能超过15字",Toast.LENGTH_SHORT).show();
                                        }else {
                                            addCategory(editText.getText().toString());
                                        }
                                    }
                                });
                                builder1.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                builder1.show();
                                break;
                            case 1:
                                dialog.dismiss();
                                if (position == 0){
                                    Toast.makeText(HomeActivity.this,"未分类无法修改",Toast.LENGTH_SHORT).show();
                                }else {
                                    builder1.setTitle("修改分类名");
                                    editText.setHint("请输入分类"+categoryList.get(position).getName()+"修改后的分类名");
                                    builder1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (editText.getText().length() > 15){
                                                Toast.makeText(HomeActivity.this,"分类名不能超过15字",Toast.LENGTH_SHORT).show();
                                            }else {
                                                editCategory(categoryList.get(position).getName(), editText.getText().toString());
                                            }
                                        }
                                    });
                                    builder1.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                                    builder1.show();
                                }
                                break;
                            case 2:
                                if (position != 0){
                                    deleteCategory(position);
                                }else {
                                    Toast.makeText(HomeActivity.this,"未分类无法删除",Toast.LENGTH_SHORT).show();
                                }
                                break;
                        }
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            }

        });

        rvCategory.setAdapter(adapter);
    }

    private void editCategory(String categoryName, String newCategoryName) {
        mPresenter.categoryName = categoryName;
        mPresenter.newCategoryName = newCategoryName;
        mPresenter.editCategory(new HomePresenter.EditCategoryCallback() {
            @Override
            public void onEditCategorySuccess(String message, int status) {
                if (status == 200) {
                    Toast.makeText(getApplicationContext(), "修改成功！", Toast.LENGTH_SHORT).show();
                    refresh();
                }
                else {
                    Toast.makeText(getApplicationContext(), "修改失败："+message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onEditCategoryFailed(String msg) {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void addCategory(String categoryName) {
        mPresenter.categoryName = categoryName;
        mPresenter.addCategory(new HomePresenter.AddCategoryCallback() {
            @Override
            public void onAddCategorySuccess(String message, int status) {
                if (status == 200) {
                    Toast.makeText(getApplicationContext(), "添加成功！", Toast.LENGTH_SHORT).show();
                    refresh();
                }
                else {
                    Toast.makeText(getApplicationContext(), "添加失败："+message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onAddCategoryFailed(String msg) {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void deleteCategory(int position) {
        mPresenter.categoryName = categoryList.get(position).getName();
        mPresenter.deleteCategory(new HomePresenter.DeleteCategoryCallback() {

            @Override
            public void onDeleteCategorySuccess(String message, int status) {
                if (status == 200) {
                    Toast.makeText(getApplicationContext(), "删除成功！", Toast.LENGTH_SHORT).show();
                    refresh();
                }
                else {
                    Toast.makeText(getApplicationContext(), "删除失败："+message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onDeleteCategoryFailed(String msg) {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.activity_home);
        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        setContentView(R.layout.activity_home);
        initView();
    }

    //传给choose页面的是categoryName
    public void toChoose(String categoryName) {
        Intent intent = new Intent(HomeActivity.this, ChooseActivity.class);

        SharedPreferences mSharedPreferences = getSharedPreferences("loginData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString("categoryName", categoryName);
        editor.commit();
        startActivity(intent);
    }

    private void refresh(){
        this.showCategory();
    }
}
