package com.lxkj.shortvideo.ui.fragment.homemine;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;


import com.lxkj.shortvideo.AppConsts;
import com.lxkj.shortvideo.R;
import com.lxkj.shortvideo.actlink.NaviRightListener;
import com.lxkj.shortvideo.ui.fragment.TitleFragment;
import com.lxkj.shortvideo.utils.StringUtil;
import com.lxkj.shortvideo.utils.ToastUtil;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Time:2020/12/6
 * <p>
 * Author:李迪迦
 * <p>
 * Interface:
 */
public class NameFra extends TitleFragment implements NaviRightListener {
    Unbinder unbinder;
    @BindView(R.id.et)
    EditText et;

    @Override
    public String getTitleName() {
        return "花木森林";
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        rootView = inflater.inflate(R.layout.fra_name, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        initView();
        return rootView;
    }

    public void initView() {
        act.titleTv.setText(getArguments().getString("title"));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public String rightText() {
        return "保存";
    }

    @Override
    public void onRightClicked(View v) {
        if (StringUtil.isEmpty(et.getText().toString())) {
            ToastUtil.show("请输入您要设置的内容");
            return;
        }
        Intent intent = new Intent();
        intent.putExtra("et",et.getText().toString());
        act.setResult(111, intent);
        act.finishSelf();
    }
}
