package com.lxkj.shortvideo.utils;

import android.content.Context;


public class TextStyleUtils {

    public static int dip2px(Context context, int dipValue){
        float reSize = context.getResources().getDisplayMetrics().density;
        return (int)((dipValue * reSize) + 0.5);
    }
}
