package com.example.wordcheck.Service;
import java.lang.reflect.Field;
import android.annotation.SuppressLint;
import android.content.Context;
import android.example.wordcheck.R;
import android.graphics.PixelFormat;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.wordcheck.activity.AppContext;

/**
 * Created by 此文件打不开 on 2020/4/15.
 */
public class FloatWindowSmallView  extends LinearLayout {
    // 小悬浮窗的宽
    public int viewWidth;
    // 小悬浮窗的高
    public int viewHeight;
    // 系统状态栏的高度
    private static int statusBarHeight;
    // 用于更新小悬浮窗的位置
    private WindowManager windowManager;
    // 小悬浮窗的布局参数
    public WindowManager.LayoutParams smallWindowParams;
    // 记录当前手指位置在屏幕上的横坐标
    private float xInScreen;
    // 记录当前手指位置在屏幕上的纵坐标
    private float yInScreen;
    // 记录手指按下时在屏幕上的横坐标,用来判断单击事件
    private float xDownInScreen;
    // 记录手指按下时在屏幕上的纵坐标,用来判断单击事件
    private float yDownInScreen;
    // 记录手指按下时在小悬浮窗的View上的横坐标
    private float xInView;
    // 记录手指按下时在小悬浮窗的View上的纵坐标
    private float yInView;
    // 单击接口
    private OnClickListener listener;
    private Button percentView;
    private FloatWindowManager floatWindowManager;
    private boolean ischech;

    public FloatWindowSmallView(Context context, int layoutResId,
                                int rootLayoutId) {
        super(context);
        windowManager = (WindowManager)AppContext.getContext().getSystemService(AppContext.getContext().WINDOW_SERVICE);
        //将layout的xml布局文件实例化为View类对象
        LayoutInflater.from(context).inflate(layoutResId, this);
        View view = findViewById(rootLayoutId);
        ischech=true;
        //得到该view的高宽
        viewWidth = view.getLayoutParams().width;
        viewHeight = view.getLayoutParams().height;
        // 系统状态栏的高度
        statusBarHeight = getStatusBarHeight();
        floatWindowManager = FloatWindowManager.getInstance(context);
        percentView = (Button) findViewById(R.id.percent);
        smallWindowParams = new WindowManager.LayoutParams();
// 设置显示类型,可以在弹出小窗口的同时，点击Activity中未被覆盖的按钮等且不接受触摸屏事件。
        if (Build.VERSION.SDK_INT >= 26) {
            smallWindowParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        }else {
            smallWindowParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        }

// 显示图片格式,去除手机中图片显示渐变存在光晕效果
        smallWindowParams.format = PixelFormat.RGBA_8888;
// 设置交互模式,可以在弹出小窗口的同时，点击Activity中未被覆盖的按钮等
        smallWindowParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
// 设置对齐方式为左上，初始位置为屏幕的高度的一半紧挨着右边
        smallWindowParams.gravity = Gravity.LEFT | Gravity.TOP;
        smallWindowParams.width = viewWidth;
        smallWindowParams.height = viewHeight;
        smallWindowParams.x = ScreenUtils.getScreenWidth(context);
        smallWindowParams.y = ScreenUtils.getScreenHeight(context) / 2;
      init(context);
    }

    private void init(final Context context) {
        //单击显示大悬浮窗
        percentView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Log.d("测试","Click");
                if(ischech){
                floatWindowManager.createBigWindow(context);
            }else {
                ischech=true;
                }
            }
        });
        //长按退出小悬浮窗
        percentView.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v){
                floatWindowManager.removeAll();
                ischech=false;
                return ischech;
            }
        });
    }
//防止出现有可能会和点击事件发知生冲突警告所以加这个注解
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
// 手指按下时记录必要的数据,纵坐标的值都减去状态栏的高度
            case MotionEvent.ACTION_DOWN:
// 获取相对与小悬浮窗的坐标
                xInView = event.getX();
                yInView = event.getY();
// 按下时的坐标位置，只记录一次
                xDownInScreen = event.getRawX();
                yDownInScreen = event.getRawY() - statusBarHeight;
                break;
            case MotionEvent.ACTION_MOVE:
// 时时的更新当前手指在屏幕上的位置
                xInScreen = event.getRawX();
                yInScreen = event.getRawY() - statusBarHeight;
// 手指移动的时候更新小悬浮窗的位置
                updateViewPosition();
                break;
            case MotionEvent.ACTION_UP:
// 如果手指离开屏幕时，按下坐标与当前坐标相等，则视为触发了单击事件
                if (xDownInScreen == event.getRawX()
                        && yDownInScreen == (event.getRawY() - getStatusBarHeight())) {
                    if (listener != null) {
                        listener.click();
                    }
                }
                break;
        }
        //在touch中返回了内true,那么就不会响应容onClick事件
        return true;
    }
    //设置单击事件的回调接口
    public void setOnClickListener(OnClickListener listener) {
        this.listener = listener;
    }
    //更新小悬浮窗在屏幕中的位置
    private void updateViewPosition() {
        smallWindowParams.x = (int) (xInScreen - xInView);
        smallWindowParams.y = (int) (yInScreen - yInView);
        windowManager.updateViewLayout(this, smallWindowParams);
    }
    //获取状态栏的高度
    private int getStatusBarHeight() {
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object o = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = (Integer) field.get(o);
            return getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    //单击接口
    public interface OnClickListener {
        public void click();
    }
}
