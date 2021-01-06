package com.lxkj.shortvideo.ui.fragment.competition;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.lxkj.shortvideo.R;
import com.lxkj.shortvideo.ui.fragment.TitleFragment;

import androidx.annotation.Nullable;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Time:2021/1/6
 * <p>
 * Author:李迪迦
 * <p>
 * Interface:报名参赛
 */
public class ApplyFra extends TitleFragment implements View.OnClickListener {
    Unbinder unbinder;

    @Override
    public String getTitleName() {
        return "我要参赛";
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        rootView = inflater.inflate(R.layout.fra_apply, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        initView();
        return rootView;
    }
    public void initView(){

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
