package com.lxkj.shortvideo.widget.controller;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.AttributeSet;


import com.dueeeke.videoplayer.controller.BaseVideoController;

/**
 * 抖音
 * Created by dueeeke on 2018/1/6.
 */

public class TikTokController extends BaseVideoController {

    public TikTokController(@NonNull Context context) {
        super(context);
    }

    public TikTokController(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TikTokController(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    {
        //显示调试信息
    }

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    public boolean showNetWarning() {
        //不显示移动网络播放警告
        return false;
    }
}
