package com.lxkj.shortvideo.view;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import android.util.AttributeSet;

/**
 * Created by kxn on 2019/11/8 0008.
 */
public class MyScrollView extends NestedScrollView {
    private OnScrollListener onScrollListener;
    public MyScrollView(@NonNull Context context) {
        super(context);
    }

    public MyScrollView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyScrollView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (onScrollListener != null) {
            onScrollListener.onScroll(t);
        }
    }

    /**
     * 接口对外公开
     * @param onScrollListener
     */
    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }

    /**
     *
     * 滚动的回调接口
     *
     * @author xiaanming
     *
     */
    public interface OnScrollListener{
        /**
         * 回调方法， 返回MyScrollView滑动的Y方向距离
         * @param scrollY
         *              、
         */
        void onScroll(int scrollY);
    }

}
