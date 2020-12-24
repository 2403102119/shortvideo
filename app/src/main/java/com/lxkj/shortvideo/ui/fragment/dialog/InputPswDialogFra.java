package com.lxkj.shortvideo.ui.fragment.dialog;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.lxkj.shortvideo.AppConsts;
import com.lxkj.shortvideo.R;
import com.lxkj.shortvideo.utils.AppUtil;
import com.lxkj.shortvideo.utils.KeyboardUtil;
import com.lxkj.shortvideo.utils.Md5;
import com.lxkj.shortvideo.utils.ToastUtil;
import com.lxkj.shortvideo.view.ActivationCodeBox;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by kxn on 2020/3/10 0010.
 */
public class InputPswDialogFra extends DialogFragment implements View.OnClickListener {
    Window window;
    View frView;
    Unbinder unbinder;
    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.activationCode)
    ActivationCodeBox activationCode;
    @BindView(R.id.tvDone)
    TextView tvDone;
    OnSuccessListener onSuccessListener;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 去掉默认的标题
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        frView = inflater.inflate(R.layout.dialog_fra_input_psw, null);
        unbinder = ButterKnife.bind(this, frView);
        // 下面这些设置必须在此方法(onStart())中才有效

        tvDone.setOnClickListener(this::onClick);
        ivBack.setOnClickListener(this::onClick);
        //验证码输入完成
        activationCode.setInputCompleteListener(new ActivationCodeBox.InputCompleteListener() {
            @Override
            public void inputComplete(String code) {
                KeyboardUtil.hideKeyboard(getActivity());
                if (Md5.encode(code).equals(AppConsts.paypassword)) {
                    if (null != onSuccessListener)
                        onSuccessListener.onSuccess(Md5.encode(code));
                    dismiss();
                } else {
                    ToastUtil.show("密码不正确，请重新输入");
                    activationCode.clear();
                }
            }
        });
        return frView;
    }

    public InputPswDialogFra setOnSuccessListener(OnSuccessListener onSuccessListener) {
        this.onSuccessListener = onSuccessListener;
        return this;
    }


    @Override
    public void onResume() {
        super.onResume();
        window = getDialog().getWindow();
        // 如果不设置这句代码, 那么弹框就会与四边都有一定的距离
        window.setBackgroundDrawableResource(android.R.color.transparent);
        // 设置动画
        window.setWindowAnimations(R.style.ani_bottom);
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.BOTTOM;
        // 如果不设置宽度,那么即使你在布局中设置宽度为 match_parent 也不会起作用
        params.width = AppUtil.getScreenWidth(getContext());
        window.setAttributes(params);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                dismiss();
                break;
            case R.id.tvDone:
                dismiss();
                break;
        }
    }

    public interface OnSuccessListener {
        void onSuccess(String payWord);
    }
}

