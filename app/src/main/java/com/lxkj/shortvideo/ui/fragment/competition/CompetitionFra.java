package com.lxkj.shortvideo.ui.fragment.competition;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lxkj.shortvideo.R;
import com.lxkj.shortvideo.adapter.CompetitionAdapter;
import com.lxkj.shortvideo.bean.DataListBean;
import com.lxkj.shortvideo.bean.ResultBean;
import com.lxkj.shortvideo.biz.ActivitySwitcher;
import com.lxkj.shortvideo.http.BaseCallback;
import com.lxkj.shortvideo.http.Url;
import com.lxkj.shortvideo.ui.fragment.TitleFragment;
import com.lxkj.shortvideo.utils.PicassoUtil;
import com.lxkj.shortvideo.utils.StringUtil;
import com.lzy.ninegrid.ImageInfo;
import com.makeramen.roundedimageview.RoundedImageView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.ymex.widget.banner.Banner;
import cn.ymex.widget.banner.callback.BindViewCallBack;
import cn.ymex.widget.banner.callback.CreateViewCallBack;
import cn.ymex.widget.banner.callback.OnClickBannerListener;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Time:2020/12/28
 * <p>
 * Author:李迪迦
 * <p>
 * Interface:
 */
public class CompetitionFra extends TitleFragment {
    Unbinder unbinder;
    @BindView(R.id.ivNoData)
    ImageView ivNoData;
    @BindView(R.id.tvNoData)
    TextView tvNoData;
    @BindView(R.id.llNoData)
    LinearLayout llNoData;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.smart)
    SmartRefreshLayout smart;
    @BindView(R.id.banner)
    Banner banner;
    private List<String> BanString = new ArrayList<>();
    private ArrayList<ImageInfo> imageInfo = new ArrayList<>();
    private ArrayList<DataListBean> listBeans;
    private int page = 1, totalPage = 1;
    private CompetitionAdapter competitionAdapter;
    private String id;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        rootView = inflater.inflate(R.layout.fra_competition, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        initView();
        return rootView;
    }

    public void initView() {

        id = getArguments().getString("id");



        listBeans = new ArrayList<DataListBean>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        competitionAdapter = new CompetitionAdapter(getContext(), listBeans);
        recyclerView.setAdapter(competitionAdapter);
        competitionAdapter.setOnItemClickListener(new CompetitionAdapter.OnItemClickListener() {
            @Override
            public void OnItemClickListener(int firstPosition) {//赛事详情
                Bundle bundle = new Bundle();
                bundle.putString("id", listBeans.get(firstPosition).id);
                bundle.putString("title", listBeans.get(firstPosition).name);
                ActivitySwitcher.startFragment(getActivity(), EventDetailsFra.class, bundle);
            }
        });
        smart.setEnableNestedScroll(true);
        smart.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if (page >= totalPage) {
                    refreshLayout.setNoMoreData(true);
                    return;
                }
                page++;
                competitionList();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
                competitionList();
                refreshLayout.setNoMoreData(false);
            }
        });
        competitionList();
        carouselList();
    }


    /**
     * 赛事列表
     */
    private void competitionList() {
        Map<String, Object> params = new HashMap<>();
        params.put("mid", userId);
        params.put("ccid", id);
        params.put("keywords", "");
        params.put("pageNo", page + "");
        params.put("pageSize", "10");
        mOkHttpHelper.post_json(getContext(), Url.competitionList, params, new BaseCallback<ResultBean>() {
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
                if (!StringUtil.isEmpty(resultBean.totalPage))
                    totalPage = Integer.parseInt(resultBean.totalPage);
                smart.finishLoadMore();
                smart.finishRefresh();
                if (page == 1) {
                    listBeans.clear();
                    competitionAdapter.notifyDataSetChanged();
                }
                if (null != resultBean.dataList)
                    listBeans.addAll(resultBean.dataList);
                if (listBeans.size() == 0) {
                    llNoData.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                } else {
                    recyclerView.setVisibility(View.VISIBLE);
                    llNoData.setVisibility(View.GONE);
                }
                competitionAdapter.notifyDataSetChanged();


            }

            @Override
            public void onError(Response response, int code, Exception e) {
            }
        });
    }

    /**
     * 轮播图列表
     */
    private void carouselList() {
        Map<String, Object> params = new HashMap<>();
        params.put("mid", userId);
        mOkHttpHelper.post_json(getContext(), Url.carouselList, params, new BaseCallback<ResultBean>() {
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
                BanString.clear();
                if (null != resultBean.dataList) {
                    for (int i = 0; i < resultBean.dataList.size(); i++) {
                        BanString.add(resultBean.dataList.get(i).image);
                        ImageInfo info = new ImageInfo();
                        imageInfo.add(info);
                    }
                }
                banner  //创建布局
                        .createView(new CreateViewCallBack() {
                            @Override
                            public View createView(Context context, ViewGroup parent, int viewType) {
                                View view = LayoutInflater.from(context).inflate(R.layout.custom_banner_page, null);
                                return view;
                            }
                        })
                        //布局处理
                        .bindView(new BindViewCallBack<View, String>() {
                            @Override
                            public void bindView(View view, String data, int position) {
                                RoundedImageView imageView = view.findViewById(R.id.iv_pic);
                                PicassoUtil.setImag(getContext(), data, imageView);
                            }
                        })
                        //点击事件
                        .setOnClickBannerListener(new OnClickBannerListener<View, String>() {
                            @Override
                            public void onClickBanner(View view, String data, int position) {
                                Bundle bundle = new Bundle();
                                bundle.putString("id", resultBean.dataList.get(position).competition.id);
                                bundle.putString("title", resultBean.dataList.get(position).competition.name);
                                ActivitySwitcher.startFragment(getActivity(), EventDetailsFra.class, bundle);
                            }
                        })
                        //填充数据
                        .execute(BanString);


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
