package com.lxkj.shortvideo.ui.fragment.message;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.lxkj.shortvideo.R;
import com.lxkj.shortvideo.biz.ActivitySwitcher;
import com.lxkj.shortvideo.ui.fragment.TitleFragment;
import com.lxkj.shortvideo.ui.fragment.homemine.IssueFra;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Time:2020/12/31
 * <p>
 * Author:李迪迦
 * <p>
 * Interface:
 */
public class LeaterFra extends TitleFragment implements View.OnClickListener {
    Unbinder unbinder;
    @BindView(R.id.tvSelectFriend)
    TextView tvSelectFriend;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        rootView = inflater.inflate(R.layout.layout_leater, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        initView();
        return rootView;
    }

    public void initView() {
        tvSelectFriend.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvSelectFriend://选择好友
                ActivitySwitcher.startFragment(getActivity(), SelectFriendFra.class);
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
