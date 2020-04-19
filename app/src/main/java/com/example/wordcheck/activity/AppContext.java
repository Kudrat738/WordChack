package com.example.wordcheck.activity;

import android.app.Application;
import android.content.Context;

/**
 * Created by 此文件打不开 on 2020/3/26.
 */

public class AppContext extends Application {
    private static Context instance;
    @Override
    public void onCreate(){
        instance=getApplicationContext();
    }
    public static Context getContext(){
        return instance;
    }

}
