package com.lxkj.shortvideo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by kxn on 2017/3/24.
 */
public class FeedBackGridView extends GridView {
    public boolean isOnMeasure ;
    public FeedBackGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FeedBackGridView(Context context) {
        super(context);
    }

    public FeedBackGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        isOnMeasure = true;
        int expandSpec = MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        isOnMeasure = false;
        super.onLayout(changed, l, t, r, b);
    }

}