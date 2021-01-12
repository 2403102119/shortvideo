package com.lxkj.shortvideo.ui.fragment.competition;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.lxkj.shortvideo.R;
import com.lxkj.shortvideo.bean.ResultBean;
import com.lxkj.shortvideo.biz.ActivitySwitcher;
import com.lxkj.shortvideo.http.BaseCallback;
import com.lxkj.shortvideo.http.Url;
import com.lxkj.shortvideo.ui.fragment.TitleFragment;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Request;
import okhttp3.Response;

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
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvSaishijieshao)
    TextView tvSaishijieshao;
    @BindView(R.id.tvJiangpinjieshao)
    TextView tvJiangpinjieshao;
    @BindView(R.id.tvShuoming)
    TextView tvShuoming;
    @BindView(R.id.tvTime)
    TextView tvTime;
    @BindView(R.id.tvNumber)
    TextView tvNumber;

    private String cid,entered;

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

        cid = getArguments().getString("cid");
        entered = getArguments().getString("entered");

        if (entered.equals("1")){
            tvApply.setText("已参赛");
            tvApply.setEnabled(false);
        }else {
            tvApply.setEnabled(true);
            tvApply.setText("我要参赛");
        }

        competitionDetail();

        tvApply.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvApply://参赛
                Bundle bundle = new Bundle();
                bundle.putString("cid",cid);
                ActivitySwitcher.startFragment(getActivity(), ApplyFra.class,bundle);
                break;
        }
    }


    /**
     * 赛事详情
     */
    private void competitionDetail() {
        Map<String, Object> params = new HashMap<>();
        params.put("mid", userId);
        params.put("cid", cid);
        mOkHttpHelper.post_json(getContext(), Url.competitionDetail, params, new BaseCallback<ResultBean>() {
            @Override
            public void onBeforeRequest(Request request) {
            }

            @Override
            public void onFailure(Request request, Exception e) {
            }

            @Override
            public void onResponse(Response response) {

            }

            @Override
            public void onSuccess(Response response, ResultBean resultBean) {
                tvTitle.setText(resultBean.name);
                tvSaishijieshao.setText(resultBean.intro);
                tvJiangpinjieshao.setText(resultBean.awardIntro);
                tvShuoming.setText(resultBean.acceptAwardIntro);
                tvTime.setText(resultBean.enterEndDate);
                tvNumber.setText(resultBean.enterCount+"人");
            }

            @Override
            public void onError(Response response, int code, Exception e) {
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
