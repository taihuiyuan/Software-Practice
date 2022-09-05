package com.example.mywardrobe.activity;

import android.annotation.SuppressLint;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.mywardrobe.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class NavbarActivity extends AppCompatActivity {

    /**
     * Navbar相关逻辑，没有数据所以不用presenter和model
     */
    private String TAG = "NavbarActivity";
    private ViewPager mViewPager;
    private RadioGroup mRadioGroup;
    private Button btnSearch;
    Button btnAllClothes;
    private RadioButton tabHome,tabOutfit,tabMine;
    FloatingActionButton btnUpload;
    private List<View> mViews;
    private Intent intentHome, intentOutfit, intentMine;
    private LocalActivityManager manager;
    private String username;
    private ViewPagerAdapter adapter = new ViewPagerAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navbar);

        manager = new LocalActivityManager(this, true);
        manager.dispatchCreate(savedInstanceState);

        initView();

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int item = NavbarActivity.this.mViewPager.getCurrentItem();
                if (item == 0){
                    toUpload(v);
                }else if (item == 1){
                    toAddOutfit(v);
                }
            }
        });

        //对底边栏单选按钮进行监听
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rbHome:
                        mViewPager.setCurrentItem(0);
                        btnUpload.setVisibility(View.VISIBLE);
                        break;
                    case R.id.rbOutfit:
                        mViewPager.setCurrentItem(1);
                        btnUpload.setVisibility(View.VISIBLE);
                        break;
                    case R.id.rbMine:
                        mViewPager.setCurrentItem(2);
                        btnUpload.setVisibility(View.INVISIBLE);
                        break;
                }
                mViewPager.getAdapter().notifyDataSetChanged();
            }
        });

    }

    private void initView() {
        mViewPager=findViewById(R.id.viewpager);
        mRadioGroup=findViewById(R.id.rgTab);
        tabHome=findViewById(R.id.rbHome);
        tabOutfit=findViewById(R.id.rbOutfit);
        tabMine=findViewById(R.id.rbMine);
        btnSearch=findViewById(R.id.btnSearch);
        btnUpload = findViewById(R.id.btnUpload);
        btnAllClothes = findViewById(R.id.btnAllClothes);

        mViews=new ArrayList<View>();//加载，添加视图
        intentHome = new Intent(NavbarActivity.this, HomeActivity.class);
        View vHome = manager.startActivity("viewID", intentHome).getDecorView();

        intentOutfit = new Intent(NavbarActivity.this, OutfitActivity.class);
        View vOutfit = manager.startActivity("viewID", intentOutfit).getDecorView();


        intentMine = new Intent(NavbarActivity.this, MineActivity.class);
        View vMine = manager.startActivity("viewID", intentMine).getDecorView();

        SharedPreferences mSharedPreferences=getSharedPreferences("loginData",NavbarActivity.MODE_PRIVATE);
        username = mSharedPreferences.getString("username","");

        mViews.add(vHome);
        mViews.add(vOutfit);
        mViews.add(vMine);

        mViewPager.setAdapter(adapter);//设置一个适配器

        //对viewpager监听，让分页和底部图标保持一起滑动
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override   //让viewpager滑动的时候，下面的图标跟着变动
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        tabHome.setChecked(true);
                        tabOutfit.setChecked(false);
                        tabMine.setChecked(false);
                        btnUpload.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        tabHome.setChecked(false);
                        tabOutfit.setChecked(true);
                        tabMine.setChecked(false);
                        btnUpload.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        tabHome.setChecked(false);
                        tabOutfit.setChecked(false);
                        tabMine.setChecked(true);
                        btnUpload.setVisibility(View.INVISIBLE);
                        break;
                }
                mViewPager.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toSearch();
            }
        });

        btnAllClothes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toChoose();
            }
        });
    }

    private void toChoose() {
        SharedPreferences.Editor editor = getSharedPreferences("loginData", MODE_PRIVATE).edit();
        editor.putString("categoryName", "");
        editor.commit();
        Intent intent = new Intent(this, ChooseActivity.class);
        startActivity(intent);
    }

    //ViewPager适配器
    private class ViewPagerAdapter extends PagerAdapter {
        private int mChildCount = 0;


        @Override
        public int getItemPosition(Object object) {
            return PagerAdapter.POSITION_NONE;
        }


        @Override
        public int getCount() {
            return mViews.size();
        }


        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view==object;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView(mViews.get(position));
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            container.addView(mViews.get(position));
            return mViews.get(position);
        }
    }

    public void toUpload(View view) {
        Intent intent = new Intent(NavbarActivity.this, UploadActivity.class);
        startActivity(intent);
    }

    public void toAddOutfit(View view) {
        Intent intent = new Intent(NavbarActivity.this, OutfitAddActivity.class);
        startActivity(intent);
    }

    public void toSearch() {
        Intent intent = new Intent(NavbarActivity.this, SearchActivity.class);
        startActivity(intent);
    }
}
