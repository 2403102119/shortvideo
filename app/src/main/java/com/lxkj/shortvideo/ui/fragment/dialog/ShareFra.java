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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lxkj.shortvideo.AppConsts;
import com.lxkj.shortvideo.R;
import com.lxkj.shortvideo.utils.AppUtil;
import com.lxkj.shortvideo.utils.ShareUtil;
import com.umeng.socialize.bean.SHARE_MEDIA;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by kxn on 2020/1/8 0008.
 */
public class ShareFra extends DialogFragment implements View.OnClickListener {
    Window window;
    View frView;
    Unbinder unbinder;
    @BindView(R.id.llWx)
    LinearLayout llWx;
    @BindView(R.id.llPyq)
    LinearLayout llPyq;
    @BindView(R.id.llQQ)
    LinearLayout llQQ;
    @BindView(R.id.tvCancel)
    TextView tvCancel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 去掉默认的标题
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        frView = inflater.inflate(R.layout.dialog_fra_share, null);
        unbinder = ButterKnife.bind(this, frView);
        // 下面这些设置必须在此方法(onStart())中才有效

        tvCancel.setOnClickListener(this::onClick);
        llPyq.setOnClickListener(this::onClick);
        llWx.setOnClickListener(this::onClick);
        llQQ.setOnClickListener(this::onClick);
        return frView;
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
            case R.id.tvCancel:
                dismiss();
                break;
            case R.id.llWx:
                ShareUtil.getInstance().share(getActivity(), AppConsts.SHAREURL,AppConsts.SHAREDES, SHARE_MEDIA.WEIXIN);
                dismiss();
                break;
            case R.id.llPyq:
                ShareUtil.getInstance().share(getActivity(), AppConsts.SHAREURL,AppConsts.SHAREDES, SHARE_MEDIA.WEIXIN_CIRCLE);
                dismiss();
                break;
            case R.id.llQQ:
                ShareUtil.getInstance().share(getActivity(), AppConsts.SHAREURL,AppConsts.SHAREDES, SHARE_MEDIA.QQ);
                dismiss();
                break;
        }
    }

    public interface OnItemClick {
        void onItemClick(int positon);
    }
}

