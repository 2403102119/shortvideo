package com.lxkj.shortvideo.view;

import android.content.Context;
import android.os.Build;
import androidx.annotation.RequiresApi;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.lxkj.shortvideo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kxn on 2019/12/3 0003.
 */
public class ActivationCodeBox extends RelativeLayout {
    private Context context;
    private TextView[] textViews;
    private static int MAXlength = 6;
    private String inputContent;
    private EditText etCode;
    private List<String> codes = new ArrayList<>();
    public ActivationCodeBox(Context context) {
        super(context);
        this.context = context;
        loadView();
    }

    public ActivationCodeBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        loadView();
    }

    public ActivationCodeBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        loadView();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ActivationCodeBox(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
        loadView();
    }
    private void loadView(){
        View view = LayoutInflater.from(context).inflate(R.layout.activation_code_box, this);
        initView(view);
        initEvent();
    }
    private void initView(View view){
        textViews = new TextView[MAXlength];
        textViews[0] = (TextView) view.findViewById(R.id.tv_code1);
        textViews[1] = (TextView) view.findViewById(R.id.tv_code2);
        textViews[2] = (TextView) view.findViewById(R.id.tv_code3);
        textViews[3] = (TextView) view.findViewById(R.id.tv_code4);
        textViews[4] = (TextView) view.findViewById(R.id.tv_code5);
        textViews[5] = (TextView) view.findViewById(R.id.tv_code6);
        etCode = (EditText) view.findViewById(R.id.et_code);
    }
    private void initEvent(){
        //验证码输入
        etCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {

                inputContent = etCode.getText().toString();
                if (inputCompleteListener != null) {
                    if (inputContent.length() >= MAXlength) {
                        inputCompleteListener.inputComplete(inputContent);
                    }
                }
                for (int i = 0; i < MAXlength; i++) {
                    if (i < inputContent.length()) {
                        textViews[i].setText(String.valueOf(inputContent.charAt(i)));
                    } else {
                        textViews[i].setText("");
                    }
                }
            }
        });
    }

    public void clear(){
        etCode.setText("");
    }

    private InputCompleteListener inputCompleteListener;
    public void setInputCompleteListener(InputCompleteListener inputCompleteListener) {
        this.inputCompleteListener = inputCompleteListener;
    }
    public interface InputCompleteListener {
        void inputComplete(String code);
    }
}
