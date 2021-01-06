package com.lxkj.shortvideo.ui.fragment.competition;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.lxkj.shortvideo.R;
import com.lxkj.shortvideo.biz.ActivitySwitcher;
import com.lxkj.shortvideo.ui.fragment.TitleFragment;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Time:2021/1/6
 * <p>
 * Author:李迪迦
 * <p>
 * Interface:查看详情
 */
public class LookDetailFra extends TitleFragment implements View.OnClickListener {
    Unbinder unbinder;
    @BindView(R.id.tvApply)
    TextView tvApply;

    @Override
    public String getTitleName() {
        return "赛事介绍";
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        rootView = inflater.inflate(R.layout.fra_lookdetails, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        initView();
        return rootView;
    }

    public void initView() {
        tvApply.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvApply://参赛
                ActivitySwitcher.startFragment(getActivity(), ApplyFra.class);
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
