package com.example.mywardrobe.utils;

import android.text.TextUtils;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;

public class DefaultValueFormate implements ValueFormatter {
    protected DecimalFormat mFormat;

    public DefaultValueFormate() {
        setup("");
    }

    public DefaultValueFormate(String endDes) {
        setup(endDes);
    }

    private void setup(String endDes) {
        if (TextUtils.isEmpty(endDes)) {
            // ###.## 表示可以精确到小数点后两位，即 double 类型
            mFormat = new DecimalFormat("###.##");
        } else {
            mFormat = new DecimalFormat("###.##" + endDes);
        }
    }

    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        return mFormat.format(value);
    }

    public int getDecimalDigits() {
        return 1;
    }
}
