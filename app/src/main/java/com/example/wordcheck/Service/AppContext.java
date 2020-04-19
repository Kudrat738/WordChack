package com.example.wordcheck.Service;

import android.app.Application;
import android.content.Context;

/**
 * Created by 此文件打不开 on 2020/4/16.
 */

public class AppContext extends Application {
    private static AppContext instance;
@Override
    public void onCreate(){
    super.onCreate();
    instance=this;
}
    public static Context getAppContext(){
        return instance;
    }
}
