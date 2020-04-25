package com.example.wordcheck.Service;
import android.content.Context;
import android.view.WindowManager;

/**
 * Created by 此文件打不开 on 2020/4/15.
 */
public class ScreenUtils {
    //获取屏幕宽度
    @SuppressWarnings("deprecation")//作用是屏蔽一些无关紧要的警告
    public static int getScreenWidth(Context context) {
        return ((WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay()
                .getWidth();
    }
    //获取屏幕宽度
    @SuppressWarnings("deprecation")//作用是屏蔽一些无关紧要的警告
    public static int getScreenHeight(Context context) {
        return ((WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay()
                .getHeight();
    }
}