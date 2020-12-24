package com.lxkj.shortvideo.view;

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
import com.lxkj.shortvideo.utils.PicassoUtil;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by kxn on 2020/4/13 0013.
 */
public class AndienceDoDialog extends Dialog {

    private Context context;      // 上下文
    TextView tvName, tvJy, tvRemove;
    CircleImageView ivHead;

    public AndienceDoDialog(Context context, String head, String name,  boolean isCan) {
        super(context, R.style.Theme_dialog); //dialog的样式
        this.context = context;
        Window window = getWindow();
        window.setGravity(Gravity.CENTER); // 此处可以设置dialog显示的位置为居中
        setContentView(R.layout.dialog_audience_do);
        WindowManager windowManager = ((Activity) context).getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = display.getWidth() * 4 / 5; // 设置dialog宽度为屏幕的4/5
        lp.dimAmount = 0.5f;//dimAmount在0.0f和1.0f之间，0.0f完全不暗，即背景是可见的 ，1.0f时候，背景全部变黑暗。
        getWindow().setAttributes(lp);
        //要达到背景全变暗的效果，需设置getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND); 否则，背景无效果
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        setCanceledOnTouchOutside(isCan);// 点击Dialog外部消失
        //遍历控件id,添加点击事件
        tvName = findViewById(R.id.tvName);
        tvJy = findViewById(R.id.tvJy);
        tvRemove = findViewById(R.id.tvRemove);
        ivHead = (CircleImageView) findViewById(R.id.ivHead);
        tvName.setText(name);
        PicassoUtil.setImag(context,head,ivHead);
        tvJy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.OnJyClick();
            }
        });

        tvRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                listener.OnRemoveClick();
            }
        });
        findViewById(R.id.ivClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    private OnButtonClick listener;

    public interface OnButtonClick {
        void OnJyClick();
        void OnRemoveClick();
    }

    public void setOnButtonClickListener(OnButtonClick listener) {
        this.listener = listener;
    }

}


