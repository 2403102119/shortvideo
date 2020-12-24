package com.lxkj.shortvideo.view;

import android.content.Context;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;


public class SquareImage extends androidx.appcompat.widget.AppCompatImageView {
    public SquareImage(Context context) {
        super(context);
    }

    public SquareImage(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareImage(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        this.setMeasuredDimension(View.getDefaultSize(0, widthMeasureSpec), View.getDefaultSize(0, heightMeasureSpec));

        int childWidthSize = getMeasuredWidth();

        //设置高度与宽度一样
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize, MeasureSpec.EXACTLY);
        heightMeasureSpec = widthMeasureSpec;

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
