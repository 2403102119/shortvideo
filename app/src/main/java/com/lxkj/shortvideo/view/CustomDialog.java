package com.lxkj.shortvideo.view;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.lxkj.shortvideo.R;


/**
 * Created by kxn on 2018/6/22 0010.
 */

public class CustomDialog extends Dialog implements View.OnClickListener{

    private Context context;      // 上下文
    private int layoutResID;      // 布局文件id
    private int[] listenedItems;  // 要监听的控件id
    private boolean isCan = true;
    public CustomDialog(Context context, int layoutResID, int[] listenedItems, boolean isCan) {
        super(context, R.style.Theme_dialog); //dialog的样式
        this.context = context;
        this.layoutResID = layoutResID;
        this.listenedItems = listenedItems;
        this.isCan = isCan;
        Window window = getWindow();
        window.setGravity(Gravity.CENTER); // 此处可以设置dialog显示的位置为居中
        setContentView(layoutResID);
        WindowManager windowManager = ((Activity)context).getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = display.getWidth()*4/5; // 设置dialog宽度为屏幕的4/5
        lp.dimAmount = 0.5f;//dimAmount在0.0f和1.0f之间，0.0f完全不暗，即背景是可见的 ，1.0f时候，背景全部变黑暗。
        getWindow().setAttributes(lp);
        //要达到背景全变暗的效果，需设置getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND); 否则，背景无效果
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        setCanceledOnTouchOutside(isCan);// 点击Dialog外部消失
        //遍历控件id,添加点击事件
        for (int id : listenedItems) {
            findViewById(id).setOnClickListener(this);
        }
    }

    private OnCustomItemClickListener listener;
    public interface OnCustomItemClickListener {
        void OnCustomItemClick(CustomDialog dialog, View view);
    }
    public void setOnCustomItemClickListener(OnCustomItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        dismiss();//注意：我在这里加了这句话，表示只要按任何一个控件的id,弹窗都会消失，不管是确定还是取消。
        listener.OnCustomItemClick(this, view);
    }
}
