package com.example.wordcheck.Service;
import android.content.Context;
import android.content.Intent;
import android.view.WindowManager;
/**
 * Created by 此文件打不开 on 2020/4/15.
 */
public class FloatWindowManager {
    // 小悬浮窗对象
    private FloatWindowSmallView smallWindow;
    // 大悬浮窗对象
    private FloatWindowBigView bigWindow;
    // 用于控制在屏幕上添加或移除悬浮窗
    private WindowManager mWindowManager;
    // FloatWindowManager的单例
    private static FloatWindowManager floatWindowManager;
    // 上下文对象
    private Context context;
    private FloatWindowManager(Context context) {
        this.context = context;
    }

    public static FloatWindowManager getInstance(Context context) {
        if (floatWindowManager == null) {
            floatWindowManager = new FloatWindowManager(context);
        }
        return floatWindowManager;
    }
    //创建小悬浮窗，必须为应用程序的Context.
    public void createSmallWindow(Context context, int layoutResId,
                                  int rootLayoutId) {
        WindowManager windowManager = getWindowManager();
        if (smallWindow == null) {
            smallWindow = new FloatWindowSmallView(context, layoutResId,
                    rootLayoutId);
            windowManager.addView(smallWindow, smallWindow.smallWindowParams);
        }
    }
    //将小悬浮窗从屏幕上移除

    public void removeSmallWindow() {
        if (smallWindow != null) {
            WindowManager windowManager = getWindowManager();
            windowManager.removeView(smallWindow);
            smallWindow = null;
        }
    }
    public void setOnClickListener(FloatWindowSmallView.OnClickListener listener) {
        if (smallWindow != null) {
            smallWindow.setOnClickListener(listener);
        }
    }
    //创建大悬浮窗，必须为应用程序的Context.

    public void createBigWindow(Context context) {
        WindowManager windowManager = getWindowManager();
        if (bigWindow == null) {
            bigWindow = new FloatWindowBigView(context);
            windowManager.addView(bigWindow, bigWindow.bigWindowParams);
        }
    }
    //将大悬浮窗从屏幕上移除
    public void removeBigWindow() {
        if (bigWindow != null) {
            WindowManager windowManager = getWindowManager();
            windowManager.removeView(bigWindow);
            bigWindow = null;
        }
    }
    public void removeAll() {
        context.stopService(new Intent(context, FloatWindowService.class));
        removeSmallWindow();
        removeBigWindow();
    }
    //是否有悬浮窗显示(包括小悬浮窗和大悬浮)
    public boolean isWindowShowing() {
        return smallWindow != null || bigWindow != null;
    }
    //如果WindowManager还未创建，则创建新的WindowManager返回。否则返回当前已创建的WindowManager
    private WindowManager getWindowManager() {
        if (mWindowManager == null) {
            mWindowManager = (WindowManager) context
                    .getSystemService(Context.WINDOW_SERVICE);
        }
        return mWindowManager;
    }
}
