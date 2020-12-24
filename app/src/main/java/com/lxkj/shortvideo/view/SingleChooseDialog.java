package com.lxkj.shortvideo.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.bigkoo.pickerview.adapter.ArrayWheelAdapter;
import com.contrarywind.listener.OnItemSelectedListener;
import com.contrarywind.view.WheelView;
import com.lxkj.shortvideo.R;

import java.util.List;

/**
 * Created by kxn on 2018/9/7 0007.
 */

public class SingleChooseDialog extends Dialog {

    TextView tvTitle;
    WheelView wheelView;
    TextView tvCancle;
    TextView tvSure;
    private Context context;
    private List<String> list;
    private String title;
    private int position = 0;
    OnItemClick onItemClick;

    public SingleChooseDialog(Context context, String title, List<String> list, OnItemClick onItemClick) {
        super(context, R.style.MyDialogStyle);
        this.context = context;
        this.list = list;
        this.title = title;
        this.onItemClick = onItemClick;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        init();
    }

    public void init() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_single_choose, null);
        setContentView(view);
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
        wheelView = (WheelView) view.findViewById(R.id.wheelView);
        tvCancle = (TextView) view.findViewById(R.id.tv_cancle);
        tvSure = (TextView) view.findViewById(R.id.tv_sure);

        tvTitle.setText(title);

        wheelView.setCyclic(false);
        wheelView.setTextSize(16);
        wheelView.setAdapter(new ArrayWheelAdapter(list));
        wheelView.setCurrentItem(0);
        wheelView.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                position = index;
            }
        });

        tvSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClick.onItemClick(position);
                dismiss();
            }
        });
        tvCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        lp.width = (int) (d.widthPixels * 0.8); // 宽带度设置为屏幕的0.8
        dialogWindow.setAttributes(lp);
    }

    public interface OnItemClick {
        void onItemClick(int position);
    }


}


