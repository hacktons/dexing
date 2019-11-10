package cn.hacktons.dexing.demo;

import android.app.Application;
import android.content.Context;

import cn.hacktons.dexing.Multidex;

public class CustomApplication extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        Multidex.enableLog();
        Multidex.install(this, R.layout.custom_loading);
    }
}
