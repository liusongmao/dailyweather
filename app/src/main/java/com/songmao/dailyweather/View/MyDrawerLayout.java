package com.songmao.dailyweather.View;

import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Acer on 2017/5/11.
 */

public class MyDrawerLayout extends DrawerLayout{
    private int left;
    private int right;
    private int top;
    private int bottom;
    public MyDrawerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            //处理滑动冲突
            case MotionEvent.ACTION_MOVE:
                int x = (int) ev.getX();
                int y = (int) ev.getY();
                if (x > left && x <right && y > top && y < bottom){
                    return false;
                }
                break;
            default:
        }
        return super.onInterceptTouchEvent(ev);
    }

    public void setRecyclerView(int left, int top, int right, int bottom){
        this.left = left;
        this.right = right;
        this.top = top;
        this.bottom = bottom;
    }
}
