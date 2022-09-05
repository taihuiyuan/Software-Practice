package com.example.mywardrobe.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.bumptech.glide.Glide;
import com.example.mywardrobe.R;
import com.example.mywardrobe.base.BaseActivity;
import com.example.mywardrobe.entity.Category;
import com.example.mywardrobe.entity.Location;
import com.example.mywardrobe.presenter.ClothesDetailPresenter;
import com.example.mywardrobe.presenter.UploadPresenter;
import com.example.mywardrobe.response.ClothesDetailResponse;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ClothesDetailActivity extends BaseActivity<ClothesDetailPresenter> {
    String TAG = "ClothesDetail";

    Button btnModifyClothesDetail;
    ImageView imgClothesDetail;

    Spinner spCategoryName;
    Spinner spColor;
    Spinner spSeason;
    Spinner spLocation;
    TextView addCategory;
    TextView setLocation;
    TextView seeHistoricalLocation;
    EditText etDetailNote;
    EditText etDetailPrice;
    Button btnBack;

    String etCategory;
    String etColor;
    String etSeason;
    String etLocation;

    private String category;
    private String color;
    private String season;
    private String price;
    private String location;
    private String note;
    private List<String> locationHistory = new ArrayList<>();

    private ArrayList<Category> categories = new ArrayList<>();
    private String[] colors = {"黑色系","白色系","灰色系","裸色系","红色系","橙色系","黄色系","绿色系","蓝色系","紫色系","其它"};
    private String[] seasons = {"春天","夏天","秋天","冬天"};
    private ArrayList<Location> locations = new ArrayList<>();

    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_detail);
        initView();

        etDetailPrice = (EditText) findViewById(R.id.etDetailPriceNew);
        etDetailNote = (EditText) findViewById(R.id.etDetailNoteNew);
        etDetailPrice.clearFocus();
        etDetailNote.clearFocus();
        imgClothesDetail = (ImageView) findViewById(R.id.imgClothesDetail);
        btnBack = findViewById(R.id.btnBack);

        btnModifyClothesDetail = (Button) findViewById(R.id.btModifyDetail);
        spCategoryName = (Spinner) findViewById(R.id.spDetailCName);
        spColor = (Spinner) findViewById(R.id.spDetailColor);
        spSeason = (Spinner) findViewById(R.id.spDetailSeason);
        spLocation = (Spinner) findViewById(R.id.spDetailLocation);
        addCategory = (TextView) findViewById(R.id.addCategoryDetail);
        setLocation = findViewById(R.id.setLocationDetail);
        seeHistoricalLocation = (TextView) findViewById(R.id.seeHistoricalLocation);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });

        spCategoryName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                etCategory = categories.get(pos).getName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(ClothesDetailActivity.this, "请选择分类", Toast.LENGTH_LONG).show();
            }
        });

        spColor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                etColor = colors[pos];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(ClothesDetailActivity.this, "请选择颜色", Toast.LENGTH_LONG).show();
            }
        });

        spSeason.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                etSeason = seasons[pos];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(ClothesDetailActivity.this, "请选择季节", Toast.LENGTH_LONG).show();
            }
        });

        spLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                etLocation = locations.get(pos).getName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(ClothesDetailActivity.this, "请选择位置", Toast.LENGTH_LONG).show();
            }
        });

        btnModifyClothesDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.categoryName = etCategory;
                mPresenter.color = etColor;
                mPresenter.season = etSeason;
                mPresenter.price = etDetailPrice.getText().toString();
                mPresenter.location = etLocation;
                mPresenter.note = etDetailNote.getText().toString();
                doModifyClothesDetail();
            }
        });

        addCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(ClothesDetailActivity.this);
                final EditText editText = new EditText(ClothesDetailActivity.this);
                builder1.setView(editText);
                builder1.setTitle("新增分类");
                editText.setHint("请输入新增的分类名");
                builder1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (editText.getText().length() > 15){
                            Toast.makeText(ClothesDetailActivity.this,"分类名不能超过15字",Toast.LENGTH_SHORT).show();
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
            }
        });

        setLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ClothesDetailActivity.this);
                builder.setTitle("位置管理");
                List<String> locationList = new ArrayList<>();
                locationList.add("+新增位置");
                for (Location l: locations){
                    locationList.add(l.getName());
                }
                String[] location = new String[locationList.size()];
                locationList.toArray(location);
                builder.setItems(location, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0){
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(ClothesDetailActivity.this);
                            final EditText editText = new EditText(ClothesDetailActivity.this);
                            builder1.setView(editText);
                            builder1.setTitle("新增位置");
                            editText.setHint("请输入新增的位置名");
                            builder1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (editText.getText().length() > 10){
                                        Toast.makeText(ClothesDetailActivity.this,"位置名不能超过10字",Toast.LENGTH_SHORT).show();
                                    }else {
                                        addLocation(editText.getText().toString());
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
                        }else {
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(ClothesDetailActivity.this);
                            builder1.setTitle("删除位置");
                            builder1.setMessage("确认要删除位置“"+locationList.get(which)+"”");
                            String deleteLocationName = locationList.get(which);
                            builder1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //Toast.makeText(ClothesDetailActivity.this,deleteLocationName,Toast.LENGTH_LONG).show();
                                    deleteLocation(deleteLocationName);
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
                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
            }
        });

        seeHistoricalLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder2 = new AlertDialog.Builder(ClothesDetailActivity.this);
                builder2.setTitle("历史位置");
                String[] location = new String[locationHistory.size()];
                locationHistory.toArray(location);
                Log.i("location", Arrays.toString(location));
                builder2.setItems(location, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(ClothesDetailActivity.this, location[which],
                                Toast.LENGTH_SHORT).show();
                    }
                });
                builder2.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder2.show();
            }
        });
    }

    private void deleteLocation(String locationName) {
        mPresenter.deleteLocation = locationName;
        mPresenter.deleteLocation(new ClothesDetailPresenter.DeleteLocationCallBack() {
            @Override
            public void onDeleteLocationSuccess(String message, int status) {
                if (status == 200) {
                    Toast.makeText(getApplicationContext(), "删除成功！", Toast.LENGTH_SHORT).show();
                    initSpinnerLocation();
                    getHistoricalLocation();
                }
                else {
                    Toast.makeText(getApplicationContext(), "删除失败："+message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onDeleteLocationFailed(String msg) {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void back() {
        this.finish();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_detail;
    }

    @Override
    public void initView() {
        mPresenter = new ClothesDetailPresenter();
        mPresenter.attachView(this);

        SharedPreferences sharedPreferences = getSharedPreferences("loginData", Context.MODE_PRIVATE);
        mPresenter.clothesID = sharedPreferences.getInt("clothesID", 0);
        SharedPreferences sharedPreferences1 = getSharedPreferences("loginData", Context.MODE_PRIVATE);
        mPresenter.username = sharedPreferences1.getString("username", "");

        showClothesDetail();
        getHistoricalLocation();
    }

    private void showClothesDetail() {
        mPresenter.getClothesDetail(new ClothesDetailPresenter.GetClothesDetailCallBack() {
            @Override
            public void onGetClothesDetailCallBackSuccess(ClothesDetailResponse response) {
                etDetailPrice.setText(response.getPrice());
                etDetailNote.setText(response.getNote());

                category = response.getCategoryName();
                color = response.getColor();
                Log.e("debug", "getcolor:" + color);
                season = response.getSeason();
                location = response.getLocation();

                initSpinnerCategory();
                initSpinnerColorAndSeason();
                initSpinnerLocation();

                Glide.with(ClothesDetailActivity.this).load(response.getImageUrl()).into(imgClothesDetail);
            }

            @Override
            public void onGetClothesDetailCallBackFail(String msg) {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void doModifyClothesDetail() {
        mPresenter.modifyClothesDetail(new ClothesDetailPresenter.ModifyClothesDetailCallBack() {
            @Override
            public void onModifyClothesDetailSuccess(String message, int status) {
                if (status == 200) {
                    String msg = "修改成功";
                    getHistoricalLocation();
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onModifyClothesDetailFail(String msg) {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initSpinnerColorAndSeason() {
        ArrayList<String> list1 = new ArrayList<>(Arrays.asList(colors));
        ArrayAdapter<String> adapter1;
        adapter1 = new ArrayAdapter<>(ClothesDetailActivity.this, R.layout.support_simple_spinner_dropdown_item, list1);
        spColor.setAdapter(adapter1);
        int position1 = adapter1.getPosition(color);
        Log.e("debug", "color!"+color);
        Log.e("debug", "position!"+position1);
        spColor.setSelection(position1);

        ArrayList<String> list2 = new ArrayList<>(Arrays.asList(seasons));
        ArrayAdapter<String> adapter2;
        adapter2 = new ArrayAdapter<>(ClothesDetailActivity.this, R.layout.support_simple_spinner_dropdown_item, list2);
        spSeason.setAdapter(adapter2);
        int position2 = adapter2.getPosition(season);
        spSeason.setSelection(position2);
    }

    private void initSpinnerCategory() {
        mPresenter.listCategory(new ClothesDetailPresenter.ListCategoryCallBack() {
            @Override
            public void onListCategoryCallBackSuccess(List<Category> categoryList) {
                ArrayList<String> listForSpinner = new ArrayList<>();
                ArrayAdapter<String> adapterForSpinner;
                categories.clear();
                categories.addAll(categoryList);
                for (Category c: categories){
                    listForSpinner.add(c.getName());
                }
                adapterForSpinner = new ArrayAdapter<>(ClothesDetailActivity.this, R.layout.support_simple_spinner_dropdown_item, listForSpinner);
                spCategoryName.setAdapter(adapterForSpinner);
                int position = adapterForSpinner.getPosition(category);
                spCategoryName.setSelection(position);
            }

            @Override
            public void onListCategoryFailed(String msg) {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void initSpinnerLocation() {
        mPresenter.listLocation(new ClothesDetailPresenter.ListLocationCallBack() {
            @Override
            public void onListLocationCallBackSuccess(List<Location> list) {
                ArrayList<String> listForSpinner = new ArrayList<>();
                ArrayAdapter<String> adapterForSpinner;
                locations.clear();
                locations.addAll(list);
                for (Location c: locations){
                    if (c.getName().length()>15){
                        c.setName(c.getName().substring(0,15)+"...");
                    }
                    listForSpinner.add(c.getName());
                }
                adapterForSpinner = new ArrayAdapter<>(ClothesDetailActivity.this, R.layout.support_simple_spinner_dropdown_item, listForSpinner);
                spLocation.setAdapter(adapterForSpinner);
                int position = adapterForSpinner.getPosition(location);
                spLocation.setSelection(position);
            }

            @Override
            public void onListLocationCallBackFailed(String msg) {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void addCategory(String categoryName) {
        mPresenter.newCategoryName = categoryName;
        mPresenter.addCategory(new ClothesDetailPresenter.AddCategoryCallBack() {
            @Override
            public void onAddCategorySuccess(String message, int status) {
                if (status == 200) {
                    Toast.makeText(getApplicationContext(), "添加成功！", Toast.LENGTH_SHORT).show();
                    initSpinnerCategory();
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

    private void addLocation(String location) {
        mPresenter.newLocation = location;
        mPresenter.addLocation(new ClothesDetailPresenter.AddLocationCallBack() {
            @Override
            public void onAddLocationSuccess(String message, int status) {
                if (status == 200) {
                    Toast.makeText(getApplicationContext(), "添加成功！", Toast.LENGTH_SHORT).show();
                    initSpinnerLocation();
                }
                else {
                    Toast.makeText(getApplicationContext(), "添加失败："+message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onAddLocationFailed(String msg) {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getHistoricalLocation() {
        mPresenter.getHistoricalLocation(new ClothesDetailPresenter.GetHistoricalLocationCallBack() {
            @Override
            public void onGetHistoricalLocationSuccess(List<String> locations) {
                locationHistory.clear();
                int i = 1;
                for (String location : locations) {
                    locationHistory.add(i+"."+location);
                    i++;
                }
            }

            @Override
            public void onGetHistoricalLocationFailed(String msg) {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }
        });
    }



}
