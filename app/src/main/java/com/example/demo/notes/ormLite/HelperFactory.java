package com.example.demo.notes.ormLite;

import android.content.Context;

import com.j256.ormlite.android.apptools.OpenHelperManager;

public class HelperFactory{

    private static DatabaseHelper databaseHelper;

    public static DatabaseHelper getInstanse(){
        return databaseHelper;
    }

    public static void initHelper(Context context){
        databaseHelper = OpenHelperManager.getHelper(context, DatabaseHelper.class);
    }

    public static void releaseHelper(){
        OpenHelperManager.releaseHelper();
        databaseHelper = null;
    }
}