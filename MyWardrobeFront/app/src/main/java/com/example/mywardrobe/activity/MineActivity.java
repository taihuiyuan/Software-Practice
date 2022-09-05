package com.example.mywardrobe.activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.mywardrobe.R;

import com.example.mywardrobe.base.BaseActivity;
import com.example.mywardrobe.base.BasePresenter;
import com.example.mywardrobe.entity.Statistics;
import com.example.mywardrobe.presenter.StatisticsPresenter;
import com.example.mywardrobe.utils.DefaultValueFormate;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MineActivity extends BaseActivity<StatisticsPresenter> {
    Button btnModifyInfo;
    Button btnModifyPwd;
    Button btnStatistics;
    Button btnLogOut;
    TextView tvUsername;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_mine);
        initView();

        tvUsername = findViewById(R.id.username);
        btnModifyInfo = (Button) findViewById(R.id.btnModifyInfo);
        btnModifyPwd = (Button) findViewById(R.id.btnModifyPwd);
        btnStatistics = findViewById(R.id.btnStatistics);
        btnLogOut = findViewById(R.id.btnLogOut);

        SharedPreferences mSharedPreferences = getSharedPreferences("loginData", MODE_PRIVATE);
        tvUsername.setText("用户："+mSharedPreferences.getString("username",""));

        btnModifyPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MineActivity.this, ModifyPwdActivity.class);
                startActivity(intent);
            }
        });

        btnModifyInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MineActivity.this, ModifyInfoActivity.class);
                startActivity(intent);
            }
        });

        btnStatistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MineActivity.this, StatisticsActivity.class);
                startActivity(intent);
            }
        });

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MineActivity.this);
                builder.setTitle("退出登录");
                builder.setMessage("确定退出登录吗");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences mSharedPreferences = getSharedPreferences("loginData", MODE_PRIVATE);
                        SharedPreferences.Editor editor = mSharedPreferences.edit();
                        editor.clear();
                        editor.commit();

                        Intent intent = new Intent(MineActivity.this, LoginActivity.class);
                        startActivity(intent);
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
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_mine;
    }

    @Override
    public void initView() {

    }
}
