package com.lxkj.shortvideo.utils;

import com.google.android.material.tabs.TabLayout;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.reflect.Field;

public class TabLayoutUtil {

    public static void optimisedTabLayout(final TabLayout tabLayout, final int subWidth){
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                try{
                    Field mTabStripFiled = tabLayout.getClass().getDeclaredField("mTabStrip");
                    mTabStripFiled.setAccessible(true);

                    LinearLayout mTabStrip = (LinearLayout)mTabStripFiled.get(tabLayout);

                    for (int i = 0; i < mTabStrip.getChildCount(); i++) {

                        int testWidth = 0;

                        View tabView = mTabStrip.getChildAt(i);

                        //拿到tabView的mTextView属性
                        Field mTextViewField = tabView.getClass().getDeclaredField("mTextView");
                        mTextViewField.setAccessible(true);

                        TextView mTextView = (TextView) mTextViewField.get(tabView);

                        tabView.setPadding(0, 0, 0, 0);


                        //因为我想要的效果是   字多宽线就多宽，所以测量mTextView的宽度

                        testWidth = mTextView.getWidth();
                        if (testWidth == 0) {
                            mTextView.measure(0, 0);
                            testWidth = mTextView.getMeasuredWidth();
                        }

                        //设置tab左右间距为10dp  注意这里不能使用Padding 因为源码中线的宽度是根据 tabView的宽度来设置的
                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tabView.getLayoutParams();
                        params.width = testWidth ;
                        params.leftMargin = (subWidth-testWidth)/2;
                        params.rightMargin = (subWidth-testWidth)/2;
                        tabView.setLayoutParams(params);

                        tabView.postInvalidate();
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }


}
