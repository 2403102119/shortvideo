package com.lxkj.shortvideo.ui.fragment.classical;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lxkj.shortvideo.R;
import com.lxkj.shortvideo.adapter.ClassicalAdapter;
import com.lxkj.shortvideo.bean.DataListBean;
import com.lxkj.shortvideo.bean.ResultBean;
import com.lxkj.shortvideo.biz.ActivitySwitcher;
import com.lxkj.shortvideo.http.BaseCallback;
import com.lxkj.shortvideo.http.Url;
import com.lxkj.shortvideo.ui.fragment.TitleFragment;
import com.lxkj.shortvideo.ui.fragment.competition.WorkDetails;
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
 * Time:2020/12/30
 * <p>
 * Author:李迪迦
 * <p>
 * Interface:经典
 */
public class ClassicalFra extends TitleFragment {
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

    private ArrayList<DataListBean> listBeans;
    private int page = 1, totalPage = 1;
    private ClassicalAdapter classicalAdapter;
    private String id,type;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        rootView = inflater.inflate(R.layout.layout_list, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        initView();
        return rootView;
    }

    public void initView() {

        id = getArguments().getString("id");
        type = getArguments().getString("type");

        listBeans = new ArrayList<DataListBean>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        classicalAdapter = new ClassicalAdapter(getContext(), listBeans);
        recyclerView.setAdapter(classicalAdapter);
        classicalAdapter.setOnItemClickListener(new ClassicalAdapter.OnItemClickListener() {
            @Override
            public void OnItemClickListener(int firstPosition) {
                Bundle bundle = new Bundle();
                bundle.putString("wid",listBeans.get(firstPosition).id);
                ActivitySwitcher.startFragment(getActivity(), WorkDetails.class,bundle);
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
                remarkWorksList();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
                remarkWorksList();
                refreshLayout.setNoMoreData(false);
            }
        });
        remarkWorksList();
    }

    /**
     * 经典视频列表
     */
    private void remarkWorksList() {
        Map<String, Object> params = new HashMap<>();
        params.put("mid", userId);
        params.put("ccid", id);
        params.put("keywords", "");
        params.put("type", type);
        params.put("pageNo", page + "");
        params.put("pageSize", "10");
        mOkHttpHelper.post_json(getContext(), Url.remarkWorksList, params, new BaseCallback<ResultBean>() {
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
                    classicalAdapter.notifyDataSetChanged();
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
                classicalAdapter.notifyDataSetChanged();


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
