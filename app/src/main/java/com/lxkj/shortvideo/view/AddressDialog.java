package com.lxkj.shortvideo.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.lxkj.shortvideo.R;
import com.lxkj.shortvideo.utils.ToastUtil;

/**
 * Created by kxn on 2020/5/7 0007.
 */
public class AddressDialog extends Dialog {

    private Context context;      // 上下文
    TextView tvConsignee, tvAddress, tvTel, tvZipcode, tvCopyTel, tvCopyAddress;

    public AddressDialog(Context context, String consignee, String address, String tel, String zipcode, boolean isCan) {
        super(context, R.style.Theme_dialog); //dialog的样式
        this.context = context;
        Window window = getWindow();
        window.setGravity(Gravity.CENTER); // 此处可以设置dialog显示的位置为居中
        setContentView(R.layout.dialog_address_jfdh);
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
        tvConsignee = findViewById(R.id.tvConsignee);
        tvAddress = findViewById(R.id.tvAddress);
        tvTel = findViewById(R.id.tvTel);
        tvZipcode = findViewById(R.id.tvZipcode);
        tvCopyTel = findViewById(R.id.tvCopyTel);
        tvCopyAddress = findViewById(R.id.tvCopyAddress);
        tvConsignee.setText("姓名：" + consignee);
        tvAddress.setText(address);
        tvTel.setText("电话：" + tel);
        tvZipcode.setText("邮编：" + zipcode);

        tvCopyTel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                ClipboardManager cm = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
                // 创建普通字符型ClipData
                ClipData mClipData = ClipData.newPlainText("Label", tel);
                // 将ClipData内容放到系统剪贴板里。
                cm.setPrimaryClip(mClipData);
                ToastUtil.show("复制成功");
            }
        });
        tvCopyAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                ClipboardManager cm = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
                // 创建普通字符型ClipData
                ClipData mClipData = ClipData.newPlainText("Label", address);
                // 将ClipData内容放到系统剪贴板里。
                cm.setPrimaryClip(mClipData);
                ToastUtil.show("复制成功");
            }
        });

        findViewById(R.id.ivClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }



}

