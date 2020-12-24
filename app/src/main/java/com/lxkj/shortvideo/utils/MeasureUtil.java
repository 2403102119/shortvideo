package com.lxkj.shortvideo.utils;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

/**
 * Created by kxn on 2017/7/5.
 */
public class MeasureUtil {
    //动态测量listview高度
    public static void initListViewMeasure(ListView lv, BaseAdapter adapter){
        //动态测量listview高度
        int totalHeight = 0;
        for (int i = 0, len = adapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = adapter.getView(i, null, lv);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = lv.getLayoutParams();
        params.height = totalHeight+ (lv.getDividerHeight() * (adapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        lv.setLayoutParams(params);
    }
}
