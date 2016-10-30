package com.home.shitou.homeb.ScrollView;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.ScrollView;

import static android.R.attr.x;
import static android.R.attr.y;

/**
 * 在此写用途
 *
 * @version V1.0 <描述当前版本功能>
 * @FileName: com.home.shitou.homeb.ScrollView.ObservableScrollView.java
 * @author: myName
 * @date: 2016-10-28 06:40
 */
public class ObservableScrollView extends ScrollView{
    private static final String TAG = "ObservableScrollView";


    public ObservableScrollView(Context context) {
        super(context);
    }

    public ObservableScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ObservableScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ObservableScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /**
     * 这里面有四个参数，最常用的比如你想判断ScrollView的滑动方向，那么只需要判断 y > oldY即为向下滑动，反之向上滑动。
     * 比如判断ScrollView滑动到某个特定位置然后做些动画之类的
     * onScrollChanged()方法中的 x, y 其实就是 getScrollX(), getScrollY() 的值 (打 log 可以知道)
     */

    @Override
    protected void onScrollChanged(int l, int t, int oldX, int oldY) {
        super.onScrollChanged(l, t, oldX, oldY);
        if (onScrollChangedListener != null) {
            onScrollChangedListener.onScrollChanged(x, y, oldX, oldY);
        }
    }

    /**
     * 提供的外部接口
     */
    public interface OnScrollChangedListener {
        public void onScrollChanged(int x, int y, int oldX, int oldY);
    }

    private OnScrollChangedListener onScrollChangedListener;

    public void setOnScrollListener(OnScrollChangedListener onScrollChangedListener) {
        this.onScrollChangedListener = onScrollChangedListener;
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }
}
