package com.example.wordcheck.util;

import android.content.Context;
import android.example.wordcheck.R;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

/**
 * Created by 此文件打不开 on 2020/3/27.
 */

public class MyListView extends ListView{
   /* //手指按下的X,Y坐标
    private int startX;
    private int startY;
    //手指移动结束时的X,Y坐标
    private int endX;
    private int endY;
    //用户手势滑动的距离
    private int moveLength;
    //是否正在滑动
    private boolean isSliding;
    //布局加载器
    private LayoutInflater mInflater;
    //pop弹出
    private PopupWindow mPopupWindow;
    //pop高度
    private int mPopupWindowHeight;
    //pop宽度
    private int mPopupWindowWidth;
    //删除按钮
    private Button mBtnDelete;
    //删除按钮的回调接口
    public BtnDeleteClickListener mListener;
    //当前手指触摸的View
    private View mCurrentView;
    //当前手指触摸的位置
    private int mCurrentViewPos;*/

    public MyListView(Context context) {
        super(context);
    }

    public MyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
   /*     mInflater = LayoutInflater.from(context);
        //getScaledTouchSlop是一个距离，表示滑动的时候，手的移动要大于这个距离才开始移动控件,如果小于这个距离就不触发移动控件。
        moveLength = ViewConfiguration.get(context).getScaledTouchSlop();
        //获取到删除按钮的布局文件
        View view = mInflater.inflate(R.layout.btn_delete, null);
        //查找删除按钮
        mBtnDelete = (Button) view.findViewById(R.id.btn_delete);
        //实例化POP,并且绑定视图，和设置宽高
        mPopupWindow = new PopupWindow(view, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //先调用下measure,否则拿不到宽和高
        mPopupWindow.getContentView().measure(0, 0);
        //获取button的宽高
        mPopupWindowHeight = mPopupWindow.getContentView().getMeasuredHeight();
        mPopupWindowWidth = mPopupWindow.getContentView().getMeasuredWidth();*/


    }

    public MyListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = View.MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                View.MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
  /*  @Override
    public boolean dispatchTouchEvent(MotionEvent ev){
        //获取点击类型
        int actiion = ev.getAction();
        //获取按下时的x,y坐标
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        switch (actiion)
        {
            //按下
            case MotionEvent.ACTION_DOWN:
                startX = x;
                startY = y;
                //如果当前的pop显示，则将其隐藏掉
                if (mPopupWindow.isShowing())
                {
                    dismissPopWindow();
                    return false;
                }
                // 获得当前手指按下时的item的位置
                mCurrentViewPos = pointToPosition(startX, startY);
                // 获得当前手指按下时的item
                View view = getChildAt(mCurrentViewPos - getFirstVisiblePosition());
                mCurrentView = view;
                break;
            //移动
            case MotionEvent.ACTION_MOVE:
                endX = x;
                endY = y;
                int dx = endX - startX;
                int dy = endY - startY;
                /**
                 * 判断是否是从右到左的滑动
                 */
    /*
                if (endX < startX && Math.abs(dx) > moveLength && Math.abs(dy) < moveLength)
                {
                    isSliding = true;
                }
                break;


    }
return isSliding;
}
    @Override
    public boolean onTouchEvent(MotionEvent ev){
        int action = ev.getAction();
        if (isSliding)
        {
            switch (action)
            {
                case MotionEvent.ACTION_UP:
                    isSliding = false;
                    break;
                case MotionEvent.ACTION_MOVE:
                    int[] location = new int[2];
                    mCurrentView.getLocationOnScreen(location);
                    //设置pop显示的位置
                    mPopupWindow.showAtLocation
                            (mCurrentView, Gravity.LEFT | Gravity.TOP,
                                    location[0] + mCurrentView.getWidth(),
                                    location[1] + mCurrentView.getHeight() / 2 - mPopupWindowHeight / 2);
                    mBtnDelete.setOnClickListener(new OnClickListener()
                    {
                        @Override
                        public void onClick(View view)
                        {
                            setBtnDeleteClickListener(mListener);
                            if (mListener != null)
                            {

                                mListener.btnClick(mCurrentViewPos);
                                mPopupWindow.dismiss();
                            }
                        }
                    });
                    break;
            }
        }
        return super.onTouchEvent(ev);

    }
    public void setBtnDeleteClickListener(BtnDeleteClickListener listener)
    {
        mListener = listener;
    }

    public interface BtnDeleteClickListener
    {
        void btnClick(int position);
    }


    private void dismissPopWindow() {
        if (mPopupWindow != null && mPopupWindow.isShowing())
        {
            mPopupWindow.dismiss();
        }

    }*/
    }


