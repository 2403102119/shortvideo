package com.lxkj.shortvideo.ui.fragment.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.lxkj.shortvideo.R;


/**
 * Created by kxn on 2019/4/11 0011.
 */
public class CustomHintDialog extends Dialog {

    private Context context;      // 上下文
    private TextView contentTv,titleTv,confirmTv;

    public CustomHintDialog(Context context, String title, String hint,String btn) {
        super(context, R.style.Theme_dialog); //dialog的样式
        this.context = context;
        Window window = getWindow();
        window.setGravity(Gravity.CENTER); // 此处可以设置dialog显示的位置为居中
        setContentView(R.layout.layout_custom_hint);
        WindowManager windowManager = ((Activity)context).getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = display.getWidth()*4/5; // 设置dialog宽度为屏幕的4/5
        lp.dimAmount = 0.5f;//dimAmount在0.0f和1.0f之间，0.0f完全不暗，即背景是可见的 ，1.0f时候，背景全部变黑暗。
        getWindow().setAttributes(lp);
        //要达到背景全变暗的效果，需设置getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND); 否则，背景无效果
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        setCanceledOnTouchOutside(true);// 点击Dialog外部消失
        //遍历控件id,添加点击事件
        contentTv = findViewById(R.id.contentTv);
        contentTv.setText(hint);
        titleTv = findViewById(R.id.tvTitle);
        titleTv.setText(title);
        confirmTv = findViewById(R.id.confirmTv);
        confirmTv.setText(btn);
        confirmTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

}

