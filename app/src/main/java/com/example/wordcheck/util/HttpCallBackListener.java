package com.example.wordcheck.util;
import android.example.wordcheck.R;
import java.io.BufferedReader;
import java.io.InputStream;

/**
 * Created by 此文件打不开 on 2020/3/29.
 */
public interface HttpCallBackListener {

    //当Http访问完成时回调onFinish方法
    void onFinish(InputStream inputStream);

    //当Http访问失败时回调onError方法
    void onError();
}
