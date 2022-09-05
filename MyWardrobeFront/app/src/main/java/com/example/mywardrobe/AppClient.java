package com.example.mywardrobe;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

public class AppClient extends Application {
    @SuppressLint("StaticFieldLeak")
    private static Context context;

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this.getApplicationContext();
    }
}
