package com.lxkj.shortvideo.ui.fragment.homemine;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lxkj.shortvideo.R;
import com.lxkj.shortvideo.adapter.HomeDynamicAdapter;
import com.lxkj.shortvideo.adapter.LikeAdapter;
import com.lxkj.shortvideo.bean.DataListBean;
import com.lxkj.shortvideo.biz.ActivitySwitcher;
import com.lxkj.shortvideo.ui.fragment.TitleFragment;
import com.lxkj.shortvideo.ui.fragment.competition.WorkDetails;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Time:2021/1/4
 * <p>
 * Author:李迪迦
 * <p>
 * Interface:个人主页
 */
public class HomepageFra extends TitleFragment implements View.OnClickListener {
    Unbinder unbinder;
    @BindView(R.id.llTitle)
    LinearLayout llTitle;
    @BindView(R.id.tvLike)
    TextView tvLike;
    @BindView(R.id.viLike)
    View viLike;
    @BindView(R.id.llLike)
    LinearLayout llLike;
    @BindView(R.id.tvWork)
    TextView tvWork;
    @BindView(R.id.viWork)
    View viWork;
    @BindView(R.id.llwork)
    LinearLayout llwork;
    @BindView(R.id.tvDynamic)
    TextView tvDynamic;
    @BindView(R.id.viDynamic)
    View viDynamic;
    @BindView(R.id.llDynamic)
    LinearLayout llDynamic;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.imFinish)
    ImageView imFinish;
    @BindView(R.id.llFans)
    LinearLayout llFans;
    @BindView(R.id.llAttention)
    LinearLayout llAttention;
    @BindView(R.id.tvCompile)
    TextView tvCompile;
    private List<DataListBean> listBeans;
    private LikeAdapter likeAdapter;
    private HomeDynamicAdapter dynamicAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        rootView = inflater.inflate(R.layout.fra_homepage, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        act.hindNaviBar();
        initView();
        return rootView;
    }

    public void initView() {
        llLike.setOnClickListener(this);
        llwork.setOnClickListener(this);
        llDynamic.setOnClickListener(this);
        imFinish.setOnClickListener(this);
        llFans.setOnClickListener(this);
        llAttention.setOnClickListener(this);
        tvCompile.setOnClickListener(this);

        listBeans = new ArrayList<DataListBean>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        likeAdapter = new LikeAdapter(getContext(), listBeans);
        recyclerView.setAdapter(likeAdapter);
        likeAdapter.setOnItemClickListener(new LikeAdapter.OnItemClickListener() {
            @Override
            public void OnItemClickListener(int firstPosition) {
                ActivitySwitcher.startFragment(getActivity(), WorkDetails.class);
            }

            @Override
            public void OnDelateClickListener(int firstPosition) {

            }
        });

    }


    @Override
    public void onClick(View v) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        switch (v.getId()) {
            case R.id.llLike://喜欢
                tvLike.setTextColor(act.getResources().getColor(R.color.colorBlack));
                tvDynamic.setTextColor(act.getResources().getColor(R.color.txt_66));
                tvWork.setTextColor(act.getResources().getColor(R.color.txt_66));
                viLike.setBackgroundColor(act.getResources().getColor(R.color.main_color));
                viDynamic.setBackgroundColor(act.getResources().getColor(R.color.space));
                viWork.setBackgroundColor(act.getResources().getColor(R.color.space));

                listBeans = new ArrayList<DataListBean>();

                recyclerView.setLayoutManager(layoutManager);
                likeAdapter = new LikeAdapter(getContext(), listBeans);
                recyclerView.setAdapter(likeAdapter);
                likeAdapter.setOnItemClickListener(new LikeAdapter.OnItemClickListener() {
                    @Override
                    public void OnItemClickListener(int firstPosition) {//详情
                        ActivitySwitcher.startFragment(getActivity(), WorkDetails.class);
                    }

                    @Override
                    public void OnDelateClickListener(int firstPosition) {

                    }
                });
                break;
            case R.id.llwork://作品
                tvLike.setTextColor(act.getResources().getColor(R.color.txt_66));
                tvDynamic.setTextColor(act.getResources().getColor(R.color.txt_66));
                tvWork.setTextColor(act.getResources().getColor(R.color.colorBlack));
                viLike.setBackgroundColor(act.getResources().getColor(R.color.space));
                viDynamic.setBackgroundColor(act.getResources().getColor(R.color.space));
                viWork.setBackgroundColor(act.getResources().getColor(R.color.main_color));

                listBeans = new ArrayList<DataListBean>();
                recyclerView.setLayoutManager(layoutManager);
                likeAdapter = new LikeAdapter(getContext(), listBeans);
                recyclerView.setAdapter(likeAdapter);
                likeAdapter.setOnItemClickListener(new LikeAdapter.OnItemClickListener() {
                    @Override
                    public void OnItemClickListener(int firstPosition) {
                        ActivitySwitcher.startFragment(getActivity(), WorkDetails.class);
                    }

                    @Override
                    public void OnDelateClickListener(int firstPosition) {

                    }
                });
                break;
            case R.id.llDynamic://动态
                tvLike.setTextColor(act.getResources().getColor(R.color.txt_66));
                tvDynamic.setTextColor(act.getResources().getColor(R.color.colorBlack));
                tvWork.setTextColor(act.getResources().getColor(R.color.txt_66));
                viLike.setBackgroundColor(act.getResources().getColor(R.color.space));
                viDynamic.setBackgroundColor(act.getResources().getColor(R.color.main_color));
                viWork.setBackgroundColor(act.getResources().getColor(R.color.space));

                listBeans = new ArrayList<DataListBean>();
                recyclerView.setLayoutManager(layoutManager);
                dynamicAdapter = new HomeDynamicAdapter(getContext(), listBeans);
                recyclerView.setAdapter(dynamicAdapter);
                dynamicAdapter.setOnItemClickListener(new HomeDynamicAdapter.OnItemClickListener() {
                    @Override
                    public void OnItemClickListener(int firstPosition) {
                    }

                });
                break;
            case R.id.imFinish:
                act.finishSelf();
                break;
            case R.id.llFans://粉絲
                ActivitySwitcher.startFragment(getActivity(), FansFra.class);
                break;
            case R.id.llAttention://关注
                ActivitySwitcher.startFragment(getActivity(), AttentionFra.class);
                break;
            case R.id.tvCompile://编辑个人资料
                ActivitySwitcher.startFragment(getActivity(), CompileFra.class);
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
