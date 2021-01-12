package com.lxkj.shortvideo.ui.fragment.competition;

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
 * Time:2021/1/5
 * <p>
 * Author:李迪迦
 * <p>
 * Interface:用户主页
 */
public class UserHomeFra extends TitleFragment implements View.OnClickListener {
    Unbinder unbinder;
    @BindView(R.id.imFinish)
    ImageView imFinish;
    @BindView(R.id.llTitle)
    LinearLayout llTitle;
    @BindView(R.id.llFans)
    LinearLayout llFans;
    @BindView(R.id.llAttention)
    LinearLayout llAttention;
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
    @BindView(R.id.smart)
    SmartRefreshLayout smart;
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
    @BindView(R.id.ivNoData)
    ImageView ivNoData;
    @BindView(R.id.tvNoData)
    TextView tvNoData;
    @BindView(R.id.llNoData)
    LinearLayout llNoData;
    @BindView(R.id.tvGuanzhu)
    TextView tvGuanzhu;
    @BindView(R.id.tvPingbi)
    TextView tvPingbi;
    @BindView(R.id.llSixin)
    LinearLayout llSixin;

    private List<DataListBean> listBeans;
    private LikeAdapter likeAdapter;
    private HomeDynamicAdapter dynamicAdapter;
    private String toMid;
    private int page = 1, totalPage = 1;
    private String type = "1", focused, shielded;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        rootView = inflater.inflate(R.layout.fra_userhome, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        act.hindNaviBar();
        initView();
        return rootView;
    }


    public void initView() {

        toMid = getArguments().getString("toMid");

        llLike.setOnClickListener(this);
        llwork.setOnClickListener(this);
        llDynamic.setOnClickListener(this);
        imFinish.setOnClickListener(this);
        llFans.setOnClickListener(this);
        llAttention.setOnClickListener(this);
        tvGuanzhu.setOnClickListener(this);
        tvPingbi.setOnClickListener(this);
        tvGuanzhu.setOnClickListener(this);

        listBeans = new ArrayList<DataListBean>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        likeAdapter = new LikeAdapter(getContext(), listBeans);
        recyclerView.setAdapter(likeAdapter);
        likeAdapter.setOnItemClickListener(new LikeAdapter.OnItemClickListener() {
            @Override
            public void OnItemClickListener(int firstPosition) {//详情
                Bundle bundle = new Bundle();
                bundle.putString("wid", listBeans.get(firstPosition).id);
                ActivitySwitcher.startFragment(getActivity(), WorkDetails.class, bundle);
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
                    toMemberMomentsList();
                } else {
                    toMemberWorksList();
                }
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
                if (type.equals("3")) {
                    toMemberMomentsList();
                } else {
                    toMemberWorksList();
                }
                refreshLayout.setNoMoreData(false);
            }
        });


        toMemberWorksList();

    }


    @Override
    public void onClick(View v) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        Bundle bundle = new Bundle();
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
                    public void OnItemClickListener(int firstPosition) {
                        ActivitySwitcher.startFragment(getActivity(), WorkDetails.class);
                    }

                    @Override
                    public void OnDelateClickListener(int firstPosition) {

                    }
                });
                toMemberWorksList();
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
                        ActivitySwitcher.startFragment(getActivity(), WorkDetails.class);
                    }

                    @Override
                    public void OnDelateClickListener(int firstPosition) {

                    }
                });
                toMemberWorksList();
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
                    }

                });

                toMemberMomentsList();

                break;
            case R.id.imFinish:
                act.finishSelf();
                break;
            case R.id.llFans://粉絲
//                bundle.putString("toMid",toMid);
//                ActivitySwitcher.startFragment(getActivity(), FansFra.class,bundle);
                break;
            case R.id.llAttention://关注
//                ActivitySwitcher.startFragment(getActivity(), AttentionFra.class);
                break;
            case R.id.tvPingbi://屏蔽
                if (shielded.equals("1")) {
                    shieldMember("0");
                } else {
                    shieldMember("1");
                }
                break;
            case R.id.tvGuanzhu://关注
                if (focused.equals("1")) {
                    focusMember("0");
                } else {
                    focusMember("1");
                }
                break;
        }
    }

    /**
     * 对方用户主页
     */
    private void toMemberHome() {
        Map<String, Object> params = new HashMap<>();
        params.put("mid", SharePrefUtil.getString(getContext(), AppConsts.UID, ""));
        params.put("toMid", toMid);
        OkHttpHelper.getInstance().post_json(getContext(), Url.toMemberHome, params, new BaseCallback<ResultBean>() {
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
                if (resultBean.focused.equals("1")) {
                    if (resultBean.beFocused.equals("1")) {
                        tvGuanzhu.setText("互相关注");
                        llSixin.setVisibility(View.VISIBLE);
                    } else {
                        tvGuanzhu.setText("已关注");
                        llSixin.setVisibility(View.GONE);
                    }
                } else {
                    tvGuanzhu.setText("+关注");
                    llSixin.setVisibility(View.GONE);
                }
                shielded = resultBean.shielded;
                focused = resultBean.focused;
                if (resultBean.shielded.equals("1")) {
                    tvPingbi.setText("已屏蔽");
                } else {
                    tvPingbi.setText("屏蔽此人");
                }

            }

            @Override
            public void onError(Response response, int code, Exception e) {
            }
        });
    }

    /**
     * 对方喜欢或发布的作品列表
     */
    private void toMemberWorksList() {
        Map<String, Object> params = new HashMap<>();
        params.put("mid", userId);
        params.put("type", type);
        params.put("toMid", toMid);
        params.put("pageNo", page + "");
        params.put("pageSize", "10");
        mOkHttpHelper.post_json(getContext(), Url.toMemberWorksList, params, new BaseCallback<ResultBean>() {
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
     * 对方发布的动态列表
     */
    private void toMemberMomentsList() {
        Map<String, Object> params = new HashMap<>();
        params.put("mid", userId);
        params.put("type", type);
        params.put("toMid", toMid);
        params.put("pageNo", page + "");
        params.put("pageSize", "10");
        mOkHttpHelper.post_json(getContext(), Url.toMemberMomentsList, params, new BaseCallback<ResultBean>() {
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

    /**
     * 屏蔽/取消屏蔽用户
     */
    private void shieldMember(String type) {
        Map<String, Object> params = new HashMap<>();
        params.put("mid", userId);
        params.put("type", type);
        params.put("toMid", toMid);
        mOkHttpHelper.post_json(getContext(), Url.shieldMember, params, new BaseCallback<ResultBean>() {
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

                if (resultBean.focused.equals("1")) {
                    tvPingbi.setText("已屏蔽");
                } else {
                    tvPingbi.setText("屏蔽此人");
                }

            }

            @Override
            public void onError(Response response, int code, Exception e) {
            }
        });
    }

    /**
     * 关注/取消关注用户
     */
    private void focusMember(String type) {
        Map<String, Object> params = new HashMap<>();
        params.put("mid", userId);
        params.put("type", type);
        params.put("toMid", toMid);
        mOkHttpHelper.post_json(getContext(), Url.focusMember, params, new BaseCallback<ResultBean>() {
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

                if (resultBean.focused.equals("1")) {
                    tvPingbi.setText("已屏蔽");
                } else {
                    tvPingbi.setText("屏蔽此人");
                }

            }

            @Override
            public void onError(Response response, int code, Exception e) {
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        toMemberHome();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
