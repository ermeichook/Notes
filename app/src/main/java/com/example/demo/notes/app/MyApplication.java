package com.example.demo.notes.app;

import android.app.Application;

import com.example.demo.notes.ormLite.HelperFactory;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        HelperFactory.initHelper(getApplicationContext());
    }

    @Override
    public void onTerminate() {
        HelperFactory.releaseHelper();
        super.onTerminate();
    }
}