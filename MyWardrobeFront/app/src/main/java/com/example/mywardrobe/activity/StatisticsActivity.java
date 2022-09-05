package com.example.mywardrobe.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.mywardrobe.R;
import com.example.mywardrobe.base.BaseActivity;
import com.example.mywardrobe.entity.Statistics;
import com.example.mywardrobe.presenter.StatisticsPresenter;
import com.example.mywardrobe.utils.DefaultValueFormate;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StatisticsActivity extends BaseActivity<StatisticsPresenter> {
    PieChart pieChart1;
    PieChart pieChart2;
    PieChart pieChart3;
    PieChart pieChart4;
    Button btnBack;
    List<Statistics> data = new ArrayList<>();

    private static final String Tab1 = "类别";
    private static final String Tab2 = "位置";
    private static final String Tab3 = "季节";
    private static final String Tab4 = "价格";

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_statistics);
        initView();

        pieChart1 = findViewById(R.id.consume_pie1_chart);
        pieChart2 = findViewById(R.id.consume_pie2_chart);
        pieChart3 = findViewById(R.id.consume_pie3_chart);
        pieChart4 = findViewById(R.id.consume_pie4_chart);
        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_statistics;
    }

    @Override
    public void initView() {
        mPresenter = new StatisticsPresenter();
        mPresenter.attachView(this);

        SharedPreferences mSharedPreferences = getSharedPreferences("loginData", Context.MODE_PRIVATE);
        mPresenter.username = mSharedPreferences.getString("username","");

        statisticsByCategory();
        statisticsByLocation();
        statisticsBySeason();
        statisticsByPrice();
    }

    private void statisticsByCategory(){
        mPresenter.statisticByCategory(new StatisticsPresenter.StatisticByCategoryCallback() {
            @Override
            public void onStatisticByCategorySuccess(List<Statistics> list) {
                data.clear();
                data.addAll(list);
                ArrayList<PieEntry> entries = new ArrayList<>();
                for (Statistics s: data){
                    if (s.getName().length()>4){
                        String sub = s.getName().substring(0,4);
                        s.setName(sub+"...");
                    }
                    entries.add(new PieEntry(s.getNumber(), s.getName()));
                }

                ArrayList<Integer> colors = new ArrayList<>();
                for (int i=0;i<entries.size();i++){
                    Random rng = new Random();
                    int red = rng.nextInt(256);
                    int green = rng.nextInt(256);
                    int blue = rng.nextInt(256);
                    colors.add(Color.rgb(red, green, blue));
                }
                bingTu(pieChart1, Tab1, entries, colors);
            }

            @Override
            public void onStatisticByCategoryFailed(String msg) {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void statisticsByLocation(){
        mPresenter.statisticByLocation(new StatisticsPresenter.StatisticByLocationCallback() {
            @Override
            public void onStatisticByLocationSuccess(List<Statistics> list) {
                data.clear();
                data.addAll(list);
                ArrayList<PieEntry> entries = new ArrayList<>();
                for (Statistics s: data){
                    if (s.getName().length()>4){
                        String sub = s.getName().substring(0,4);
                        s.setName(sub+"...");
                    }
                    entries.add(new PieEntry(s.getNumber(), s.getName()));
                }
                ArrayList<Integer> colors = new ArrayList<>();
                for (int i=0;i<entries.size();i++){
                    Random rng = new Random();
                    int red = rng.nextInt(256);
                    int green = rng.nextInt(256);
                    int blue = rng.nextInt(256);
                    colors.add(Color.rgb(red, green, blue));
                }
                bingTu(pieChart2, Tab2, entries, colors);
            }

            @Override
            public void onStatisticByLocationFailed(String msg) {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void statisticsBySeason(){
        mPresenter.statisticBySeason(new StatisticsPresenter.StatisticBySeasonCallback() {
            @Override
            public void onStatisticBySeasonSuccess(List<Statistics> list) {
                data.clear();
                data.addAll(list);
                ArrayList<PieEntry> entries = new ArrayList<>();
                for (Statistics s: data){
                    if (s.getName().equals("spring")){
                        s.setName("春天");
                    }else if (s.getName().equals("summer")){
                        s.setName("夏天");
                    }else if (s.getName().equals("autumn")){
                        s.setName("秋天");
                    }else if (s.getName().equals("winter")){
                        s.setName("冬天");
                    }
                    entries.add(new PieEntry(s.getNumber(), s.getName()));
                }

                ArrayList<Integer> colors = new ArrayList<>();
                colors.add(Color.rgb(255, 0, 0));
                colors.add(Color.rgb(255, 0, 255));
                colors.add(Color.rgb(244, 164, 96));
                colors.add(Color.rgb(30, 144, 255));
                bingTu(pieChart3, Tab3, entries, colors);
            }

            @Override
            public void onStatisticBySeasonFailed(String msg) {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void statisticsByPrice(){
        mPresenter.statisticByPrice(new StatisticsPresenter.StatisticByPriceCallback() {
            @Override
            public void onStatisticByPriceSuccess(List<Statistics> list) {
                data.clear();
                data.addAll(list);
                ArrayList<PieEntry> entries = new ArrayList<>();
                for (Statistics s: data){
                    entries.add(new PieEntry(s.getNumber(), s.getName()));
                }

                ArrayList<Integer> colors = new ArrayList<>();
                colors.add(Color.rgb(0, 255, 0));
                colors.add(Color.rgb(255, 255, 0));
                colors.add(Color.rgb(255, 0, 0));
                colors.add(Color.rgb(255, 0, 255));
                colors.add(Color.rgb(244, 164, 96));
                colors.add(Color.rgb(30, 144, 255));

                bingTu(pieChart4, Tab4, entries, colors);
            }

            @Override
            public void onStatisticByPriceFailed(String msg) {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void bingTu(PieChart pieChart, String centerText, ArrayList<PieEntry> pieEntries, ArrayList<Integer> colors) {
        pieChart.setUsePercentValues(false); //设置为显示百分比
        pieChart.setDescription("");
        pieChart.setDescriptionTextSize(20f);
        pieChart.setExtraOffsets(5, 5, 5, 5);//设置饼状图距离上下左右的偏移量
        pieChart.setDrawCenterText(true); //设置可以绘制中间的文字
        pieChart.setCenterTextColor(Color.BLACK); //中间的文本颜色
        pieChart.setCenterTextSize(18);  //设置中间文本文字的大小
        pieChart.setDrawHoleEnabled(true); //绘制中间的圆形
        pieChart.setHoleColor(Color.WHITE);//饼状图中间的圆的绘制颜色
        pieChart.setHoleRadius(40f);//饼状图中间的圆的半径大小
        pieChart.setTransparentCircleColor(Color.BLACK);//设置圆环的颜色
        pieChart.setTransparentCircleAlpha(100);//设置圆环的透明度[0,255]
        pieChart.setTransparentCircleRadius(40f);//设置圆环的半径值
        pieChart.setRotationEnabled(false);//设置饼状图是否可以旋转(默认为true)
        pieChart.setRotationAngle(10);//设置饼状图旋转的角度

        Legend l = pieChart.getLegend(); //设置比例图
        l.setMaxSizePercent(100);
        l.setTextSize(10);
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART_CENTER);//设置每个tab的显示位置（这个位置是指下图右边小方框部分的位置 ）
        l.setXEntrySpace(10f);
        l.setYEntrySpace(5f);//设置tab之间Y轴方向上的空白间距值
        l.setYOffset(0f);

        //饼状图上字体的设置
        pieChart.setDrawEntryLabels(false);//设置是否绘制Label
        // pieChart.setEntryLabelColor(Color.BLACK);//设置绘制Label的颜色
        pieChart.setEntryLabelTextSize(23f);//设置绘制Label的字体大小

        int total = 0;
        for (PieEntry pieEntry: pieEntries){
            total += pieEntry.getValue();
        }

        pieChart.setCenterText(centerText+"\n共"+total+"件");//设置圆环中间的文字
        PieDataSet pieDataSet = new PieDataSet(pieEntries, "");
        if (colors!=null) pieDataSet.setColors(colors);

        pieDataSet.setSliceSpace(0f);//设置选中的Tab离两边的距离
        pieDataSet.setSelectionShift(5f);//设置选中的tab的多出来的
        PieData pieData = new PieData();
        pieData.setDataSet(pieDataSet);

        //各个饼状图所占比例数字的设置
        pieData.setValueFormatter(new DefaultValueFormate("件"));
        pieData.setValueTextSize(18f);
        pieData.setValueTextColor(Color.BLUE);

        pieChart.setData(pieData);
        pieChart.highlightValues(null);
        pieChart.invalidate();
    }

    private void back(){
        StatisticsActivity.this.finish();
    }
}
