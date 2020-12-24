package com.lxkj.shortvideo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.widget.Toolbar;

import com.lxkj.shortvideo.R;


/**
 * Created by kxn on 2019/12/12 0012.
 */
public class SimpleToolbar extends Toolbar {
    public ImageView ivBack;

    public SimpleToolbar(Context context) {
        this(context, null);
    }

    public SimpleToolbar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimpleToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_toolbar_custom, this);
        ivBack = (ImageView) findViewById(R.id.ivBack);
    }


    public void setClickListener(View.OnClickListener onClickListener) {
        ivBack.setOnClickListener(onClickListener);
    }

}
