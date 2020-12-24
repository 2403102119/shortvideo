package com.lxkj.shortvideo.view;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by kxn on 2019/12/19 0019.
 */
@SuppressLint("AppCompatCustomView")
public class GradientTextView  extends TextView {
    public GradientTextView(Context context) {
        super(context);
    }

    public GradientTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public GradientTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
