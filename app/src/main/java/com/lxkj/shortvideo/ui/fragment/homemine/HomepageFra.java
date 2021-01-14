package com.lxkj.shortvideo.ui.fragment.homemine;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.gyf.immersionbar.ImmersionBar;
import com.lxkj.shortvideo.AppConsts;
import com.lxkj.shortvideo.R;
import com.lxkj.shortvideo.adapter.HomeDynamicAdapter;
import com.lxkj.shortvideo.adapter.LikeAdapter;
import com.lxkj.shortvideo.bean.DataListBean;
import com.lxkj.shortvideo.bean.ResultBean;
import com.lxkj.shortvideo.biz.ActivitySwitcher;
import com.lxkj.shortvideo.http.BaseCallback;
import com.lxkj.shortvideo.http.OkHttpHelper;
import com.lxkj.shortvideo.http.Url;
import com.lxkj.shortvideo.ui.fragment.TitleFragment;
import com.lxkj.shortvideo.ui.fragment.competition.WorkDetails;
import com.lxkj.shortvideo.ui.fragment.message.DynamicDetailFra;
import com.lxkj.shortvideo.utils.SharePrefUtil;
import com.lxkj.shortvideo.utils.StringUtil;
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
import okhttp3.Request;
import okhttp3.Response;

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
    @BindView(R.id.riIcon)
    RoundedImageView riIcon;
    @BindView(R.id.tvFans)
    TextView tvFans;
    @BindView(R.id.tvAttention)
    TextView tvAttention;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvMotto)
    TextView tvMotto;
    @BindView(R.id.tvSex)
    TextView tvSex;
    @BindView(R.id.tvAge)
    TextView tvAge;
    @BindView(R.id.tvSite)
    TextView tvSite;
    @BindView(R.id.smart)
    SmartRefreshLayout smart;
    @BindView(R.id.ivNoData)
    ImageView ivNoData;
    @BindView(R.id.tvNoData)
    TextView tvNoData;
    @BindView(R.id.llNoData)
    LinearLayout llNoData;
    private List<DataListBean> listBeans;
    private LikeAdapter likeAdapter;
    private HomeDynamicAdapter dynamicAdapter;
    private int page = 1, totalPage = 1;
    private String type = "1";

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
                Bundle bundle = new Bundle();
                bundle.putString("wid",listBeans.get(firstPosition).id);
                ActivitySwitcher.startFragment(getActivity(), WorkDetails.class,bundle);
            }

            @Override
            public void OnDelateClickListener(int firstPosition) {

            }
        });


        smart.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if (page >= totalPage) {
                    refreshLayout.setNoMoreData(true);
                    return;
                }
                page++;
                if (type.equals("3")) {
                    myMomentsList();
                } else {
                    myWorksList();
                }
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
                if (type.equals("3")) {
                    myMomentsList();
                } else {
                    myWorksList();
                }
                refreshLayout.setNoMoreData(false);
            }
        });


        myWorksList();
    }


    @Override
    public void onClick(View v) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        switch (v.getId()) {
            case R.id.llLike://喜欢
                type = "1";

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
                        Bundle bundle = new Bundle();
                        bundle.putString("wid",listBeans.get(firstPosition).id);
                        ActivitySwitcher.startFragment(getActivity(), WorkDetails.class,bundle);
                    }

                    @Override
                    public void OnDelateClickListener(int firstPosition) {

                    }
                });

                myWorksList();
                break;
            case R.id.llwork://作品
                type = "2";
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
                        Bundle bundle = new Bundle();
                        bundle.putString("wid",listBeans.get(firstPosition).id);
                        ActivitySwitcher.startFragment(getActivity(), WorkDetails.class,bundle);
                    }

                    @Override
                    public void OnDelateClickListener(int firstPosition) {

                    }
                });

                myWorksList();
                break;
            case R.id.llDynamic://动态

                type = "3";

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
                        Bundle bundle = new Bundle();
                        bundle.putString("fmid",listBeans.get(firstPosition).id);
                        ActivitySwitcher.startFragment(getActivity(), DynamicDetailFra.class,bundle);
                    }

                });

                myMomentsList();
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

    /**
     * 我的主页
     */
    private void memberHome() {
        Map<String, Object> params = new HashMap<>();
        params.put("mid", SharePrefUtil.getString(getContext(), AppConsts.UID, ""));
        OkHttpHelper.getInstance().post_json(getContext(), Url.memberHome, params, new BaseCallback<ResultBean>() {
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
                Glide.with(getContext()).applyDefaultRequestOptions(new RequestOptions()
                        .error(R.mipmap.touxiang)
                        .placeholder(R.mipmap.touxiang))
                        .load(resultBean.avatar)
                        .into(riIcon);

                tvName.setText(resultBean.nickname);
                tvMotto.setText(resultBean.motto);
                tvFans.setText(resultBean.focusedCount);
                tvAttention.setText(resultBean.toFocusedCount);
                tvAge.setText(resultBean.age + "岁");
                tvSite.setText(resultBean.province + resultBean.city + resultBean.district);
                if (StringUtil.isEmpty(resultBean.province)) {
                    tvSite.setVisibility(View.GONE);
                } else {
                    tvSite.setVisibility(View.VISIBLE);
                }
                if (resultBean.sex.equals("1")) {
                    tvSex.setText("男");
                } else {
                    tvSex.setText("女");
                }

            }

            @Override
            public void onError(Response response, int code, Exception e) {
            }
        });
    }

    /**
     * 我喜欢或发布的作品列表
     */
    private void myWorksList() {
        Map<String, Object> params = new HashMap<>();
        params.put("mid", userId);
        params.put("type", type);
        params.put("pageNo", page + "");
        params.put("pageSize", "10");
        mOkHttpHelper.post_json(getContext(), Url.myWorksList, params, new BaseCallback<ResultBean>() {
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
                    likeAdapter.notifyDataSetChanged();
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
                likeAdapter.notifyDataSetChanged();


            }

            @Override
            public void onError(Response response, int code, Exception e) {
            }
        });
    }


    /**
     * 我发布的动态列表
     */
    private void myMomentsList() {
        Map<String, Object> params = new HashMap<>();
        params.put("mid", userId);
        params.put("type", type);
        params.put("pageNo", page + "");
        params.put("pageSize", "10");
        mOkHttpHelper.post_json(getContext(), Url.myMomentsList, params, new BaseCallback<ResultBean>() {
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
                    dynamicAdapter.notifyDataSetChanged();
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
                dynamicAdapter.notifyDataSetChanged();


            }

            @Override
            public void onError(Response response, int code, Exception e) {
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        memberHome();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
