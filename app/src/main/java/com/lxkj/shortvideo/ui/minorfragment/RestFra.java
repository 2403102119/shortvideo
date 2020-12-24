package com.lxkj.shortvideo.ui.minorfragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lxkj.shortvideo.R;
import com.lxkj.shortvideo.ui.fragment.TitleFragment;

import androidx.annotation.Nullable;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Time:2020/10/28
 * <p>
 * Author:李迪迦
 * <p>
 * Interface:其他手机号登录
 */
public class RestFra extends TitleFragment {
    Unbinder unbinder;

    @Override
    public String getTitleName() {
        return "";
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fra_rest, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        initView();
        return rootView;
    }
    public void  initView(){

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
