package com.example.mywardrobe.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mywardrobe.base.BaseActivity;
import com.example.mywardrobe.entity.Category;
import com.example.mywardrobe.entity.Clothes;
import com.example.mywardrobe.entity.Location;
import com.example.mywardrobe.presenter.ChoosePresenter;
import com.example.mywardrobe.presenter.HomePresenter;
import com.example.mywardrobe.utils.MyChildren;
import com.example.mywardrobe.utils.MyFiltrateBean;
import com.zj.filters.FiltrateBean;
import com.zj.filters.FlowPopWindow;

import java.util.ArrayList;
import java.util.List;
import com.example.mywardrobe.R;


public class ChooseActivity extends BaseActivity<ChoosePresenter> {
    String TAG = "ChooseActivity";

    private Button btnBack;
    private Button btnFilter;
    private Button btnSearch;
    TextView chooseTitle;
    //筛选框控件
    private FlowPopWindow flowPopWindow;
    //筛选框数据
    private List<FiltrateBean> dictList = new ArrayList<>();
    //location List
    private List<Location> locations = new ArrayList<>();
    //clothes List
    private List<Clothes> clothesList = new ArrayList<>();

    ClothesListAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);
        getClothes();
        initView();
        initParam();

        btnBack = (Button) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();
            }
        });

        chooseTitle = findViewById(R.id.chooseTitle);
        if (!mPresenter.categoryName.equals("")){
            chooseTitle.setText(mPresenter.categoryName);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_choose;
    }

    @Override
    public void initView() {
        mPresenter = new ChoosePresenter();
        mPresenter.attachView(this);

        SharedPreferences mSharedPreferences = getSharedPreferences("loginData", Context.MODE_PRIVATE);
        mPresenter.username = mSharedPreferences.getString("username","");
        Log.i(TAG, "username:"+mPresenter.username);

        mPresenter.categoryName = mSharedPreferences.getString("categoryName","");
        Log.i(TAG, "categoryName:"+mPresenter.categoryName);


        btnFilter = findViewById(R.id.btnFilter);
        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flowPopWindow = new FlowPopWindow(ChooseActivity.this,dictList);
                flowPopWindow.showAsDropDown(btnFilter);
                flowPopWindow.setOnConfirmClickListener(new FlowPopWindow.OnConfirmClickListener() {
                    @Override
                    public void onConfirmClick() {
                        mPresenter.map.clear();
                        for (FiltrateBean fb : dictList) {
                            List<MyFiltrateBean.Children> cdList = fb.getChildren();
                            for (int x = 0; x < cdList.size(); x++) {
                                MyChildren children = (MyChildren) cdList.get(x);
                                if (children.isSelected())
                                    mPresenter.map.put(((MyFiltrateBean) fb).getValue(), children.getKey());
                            }
                        }
                        getClothes();
                    }
                });

            }
        });

        btnSearch = (Button) findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toSearch();
            }
        });
    }


    private void initParam() {
        String[] seasons = {"春天", "夏天","秋天","冬天"};
        String[] colors = {"黑色系","白色系","灰色系","裸色系","红色系","橙色系","黄色系","绿色系","蓝色系","紫色系","其它"};
        String[][] prices = {{"低于50","0,50"}, {"50-100","50,100"}, {"100-200","100,200"}, {"200-500","200,500"}, {"500-1000","500,1000"}, {"高于1000","1000,9999999999"}};


        MyFiltrateBean fb1 = new MyFiltrateBean();
        fb1.setValue("season");
        fb1.setTypeName("季节");
        List<MyFiltrateBean.Children> childrenList = new ArrayList<>();
        for (String season : seasons) {
            MyChildren cd = new MyChildren();
            cd.setValue(season);
            cd.setKey(season);
            childrenList.add(cd);
        }
        fb1.setChildren(childrenList);

        MyFiltrateBean fb2 = new MyFiltrateBean();
        fb2.setTypeName("颜色");
        fb2.setValue("color");
        List<MyFiltrateBean.Children> childrenList2 = new ArrayList<>();
        for (String colr : colors) {
            MyChildren cd = new MyChildren();
            cd.setValue(colr);
            cd.setKey(colr);
            childrenList2.add(cd);
        }
        fb2.setChildren(childrenList2);

        MyFiltrateBean fb3 = new MyFiltrateBean();
        fb3.setTypeName("价格区间");
        fb3.setValue("priceRange");
        List<MyFiltrateBean.Children> childrenList3 = new ArrayList<>();
        for (String[] pricePair : prices) {
            MyChildren cd = new MyChildren();
            cd.setValue(pricePair[0]);
            cd.setKey(pricePair[1]);
            childrenList3.add(cd);
        }
        fb3.setChildren(childrenList3);

        dictList.add(fb1);
        dictList.add(fb2);
        dictList.add(fb3);
        initAdapter();
        getLocations();

    }

    private void getLocations(){
        mPresenter.getLocations(new ChoosePresenter.GetLocationsCallback() {
            @Override
            public void onGetLocationsSuccess(List<Location> data) {
                locations.clear();
                locations.addAll(data);

                MyFiltrateBean fb4 = new MyFiltrateBean();
                fb4.setTypeName("位置");
                fb4.setValue("location");
                List<MyFiltrateBean.Children> childrenList = new ArrayList<>();
                for (Location location : locations) {
                    MyChildren cd = new MyChildren();
                    cd.setValue(location.getName());
                    //传入的是location name
                    cd.setKey(location.getName());
                    childrenList.add(cd);
                }
                fb4.setChildren(childrenList);
                Log.i(TAG, "locations: "+ locations);
                dictList.add(fb4);
            }

            @Override
            public void onGetLocationsFailed(String msg) {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getClothes(){
        mPresenter.getClothes(new ChoosePresenter.GetClothesCallback() {
            @Override
            public void onGetClothesSuccess(List<Clothes> data) {
                clothesList.clear();
                clothesList.addAll(data);
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
        RecyclerView rvChoose = (RecyclerView) findViewById(R.id.rvChoose);
        rvChoose.setLayoutManager(new GridLayoutManager(this,3));
        adapter = new ClothesListAdapter(clothesList);

        adapter.setItemClick(new ClothesListAdapter.ItemClick() {
            @Override
            public void onItemClick(int id) {
                Log.i(TAG, "click clothes "+id);
                toDetail(id);
            }
        } );

        adapter.setOnItemLongClickListener(new ClothesListAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ChooseActivity.this);
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

        rvChoose.setAdapter(adapter);
    }

    private void deleteClothes(int position) {
        Clothes clothes = clothesList.get(position);
        mPresenter.clothesID = clothes.getId();
        mPresenter.deleteClothes(new ChoosePresenter.DeleteClothesCallBack() {
            @Override
            public void onDeleteClothesSuccess(String msg, int status) {
                if (status == 200) {
                    Toast.makeText(getApplicationContext(), "删除成功！", Toast.LENGTH_SHORT).show();
                    refresh();
                }
                else {
                    Toast.makeText(getApplicationContext(), "删除失败："+msg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onDeleteClothesFailed(String msg) {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void refresh(){
        this.getClothes();
    }

    public void toDetail(int clothesID) {
        SharedPreferences.Editor editor = getSharedPreferences("loginData", MODE_PRIVATE).edit();
        editor.putInt("clothesID", clothesID);
        Log.e("debug", "clothesId1"+clothesID);
        editor.commit();
        Intent intent = new Intent(ChooseActivity.this, ClothesDetailActivity.class);
        startActivity(intent);
    }

    public void back() {
        ChooseActivity.this.finish();
    }

    public void toSearch() {
        Intent intent = new Intent(ChooseActivity.this, SearchActivity.class);
        startActivity(intent);
    }
}
