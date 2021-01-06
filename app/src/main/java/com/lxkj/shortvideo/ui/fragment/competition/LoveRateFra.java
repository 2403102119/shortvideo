package com.lxkj.shortvideo.ui.fragment.competition;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.lxkj.shortvideo.R;
import com.lxkj.shortvideo.adapter.FansAdapter;
import com.lxkj.shortvideo.adapter.LoveRateAdapter;
import com.lxkj.shortvideo.adapter.RankAdapter;
import com.lxkj.shortvideo.bean.DataListBean;
import com.lxkj.shortvideo.biz.ActivitySwitcher;
import com.lxkj.shortvideo.ui.fragment.TitleFragment;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Time:2021/1/6
 * <p>
 * Author:李迪迦
 * <p>
 * Interface:喜爱率
 */
public class LoveRateFra extends TitleFragment {
    Unbinder unbinder;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private ArrayList<DataListBean> listBeans;
    private int page = 1, totalPage = 1;
    private LoveRateAdapter loveRateAdapter;

    @Override
    public String getTitleName() {
        return "作品标题";
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        rootView = inflater.inflate(R.layout.fra_loverate, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        initView();
        return rootView;
    }

    public void initView() {
        listBeans = new ArrayList<DataListBean>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        loveRateAdapter = new LoveRateAdapter(getContext(), listBeans);
        recyclerView.setAdapter(loveRateAdapter);
        loveRateAdapter.setOnItemClickListener(new LoveRateAdapter.OnItemClickListener() {
            @Override
            public void OnItemClickListener(int firstPosition) {


            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
