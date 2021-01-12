package com.lxkj.shortvideo.ui.fragment.competition;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.lxkj.shortvideo.R;
import com.lxkj.shortvideo.adapter.LoveRateAdapter;
import com.lxkj.shortvideo.bean.DataListBean;
import com.lxkj.shortvideo.bean.ResultBean;
import com.lxkj.shortvideo.http.BaseCallback;
import com.lxkj.shortvideo.http.Url;
import com.lxkj.shortvideo.ui.fragment.TitleFragment;
import com.lxkj.shortvideo.utils.StringUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.HashMap;
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
 * Time:2021/1/6
 * <p>
 * Author:李迪迦
 * <p>
 * Interface:喜爱率
 */
public class LoveRateFra extends TitleFragment implements View.OnClickListener {
    Unbinder unbinder;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.smart)
    SmartRefreshLayout smart;
    @BindView(R.id.tvPass)
    TextView tvPass;
    @BindView(R.id.tvNG)
    TextView tvNG;

    private ArrayList<DataListBean> listBeans;
    private int page = 1, totalPage = 1;
    private LoveRateAdapter loveRateAdapter;

    private String wid, title, type = "1";

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

        wid = getArguments().getString("wid");
        title = getArguments().getString("title");
        act.titleTv.setText(title);

        listBeans = new ArrayList<DataListBean>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        loveRateAdapter = new LoveRateAdapter(getContext(), listBeans);
        recyclerView.setAdapter(loveRateAdapter);
        loveRateAdapter.setOnItemClickListener(new LoveRateAdapter.OnItemClickListener() {
            @Override
            public void OnItemClickListener(int firstPosition) {//关注
                if (listBeans.get(firstPosition).focused.equals("1")){
                    focusMember(listBeans.get(firstPosition).id,"0",firstPosition);
                }else {
                    focusMember(listBeans.get(firstPosition).id,"1",firstPosition);
                }

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
                competitionWorksRemarkList();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
                competitionWorksRemarkList();
                refreshLayout.setNoMoreData(false);
            }
        });

        competitionWorksRemarkList();


        tvPass.setOnClickListener(this);
        tvNG.setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvPass:
                type = "1";
                competitionWorksRemarkList();
                break;
            case R.id.tvNG:
                type = "2";
                competitionWorksRemarkList();
                break;
        }
    }

    /**
     * 关注/取消关注用户
     */
    private void focusMember(String toMid,String focused,int position) {
        Map<String, Object> params = new HashMap<>();
        params.put("mid", userId);
        params.put("toMid", toMid);
        params.put("type", focused);
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

                listBeans.get(position).focused = focused;
                loveRateAdapter.notifyDataSetChanged();

            }

            @Override
            public void onError(Response response, int code, Exception e) {
            }
        });
    }


    /**
     * 喜爱率列表
     */
    private void competitionWorksRemarkList() {
        Map<String, Object> params = new HashMap<>();
        params.put("mid", userId);
        params.put("wid", wid);
        params.put("type", type);
        params.put("pageNo", page + "");
        params.put("pageSize", "10");
        mOkHttpHelper.post_json(getContext(), Url.competitionWorksRemarkList, params, new BaseCallback<ResultBean>() {
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

                tvPass.setText("PASS (喜爱) "+resultBean.passRatio+"%");
                tvNG.setText("NG "+resultBean.ngRatio+"%");

                if (!StringUtil.isEmpty(resultBean.totalPage))
                    totalPage = Integer.parseInt(resultBean.totalPage);
                smart.finishLoadMore();
                smart.finishRefresh();
                if (page == 1) {
                    listBeans.clear();
                    loveRateAdapter.notifyDataSetChanged();
                }
                if (null != resultBean.dataList)
                    listBeans.addAll(resultBean.dataList);
//                if (listBeans.size() == 0) {
//                    llNoData.setVisibility(View.VISIBLE);
//                    recyclerView.setVisibility(View.GONE);
//                } else {
//                    recyclerView.setVisibility(View.VISIBLE);
//                    llNoData.setVisibility(View.GONE);
//                }
                loveRateAdapter.notifyDataSetChanged();


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
