package com.lxkj.shortvideo.ui.fragment.message;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.ImageViewerPopupView;
import com.lxj.xpopup.interfaces.OnSrcViewUpdateListener;
import com.lxj.xpopup.interfaces.XPopupImageLoader;
import com.lxkj.shortvideo.AppConsts;
import com.lxkj.shortvideo.R;
import com.lxkj.shortvideo.adapter.DynamicAdapter;
import com.lxkj.shortvideo.bean.DataListBean;
import com.lxkj.shortvideo.bean.ResultBean;
import com.lxkj.shortvideo.biz.ActivitySwitcher;
import com.lxkj.shortvideo.http.BaseCallback;
import com.lxkj.shortvideo.http.Url;
import com.lxkj.shortvideo.ui.fragment.TitleFragment;
import com.lxkj.shortvideo.ui.fragment.dialog.ShareFra;
import com.lxkj.shortvideo.ui.fragment.login.LoginFra;
import com.lxkj.shortvideo.utils.SharePrefUtil;
import com.lxkj.shortvideo.utils.StringUtil;
import com.lxkj.shortvideo.utils.ToastUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.io.File;
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
 * Time:2020/12/30
 * <p>
 * Author:李迪迦
 * <p>
 * Interface:动态
 */
public class DynamicFra extends TitleFragment implements View.OnClickListener {
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
    @BindView(R.id.tvPushState)
    TextView tvPushState;
    @BindView(R.id.etSearch)
    EditText etSearch;

    private ArrayList<DataListBean> listBeans;
    private int page = 1, totalPage = 1;
    private DynamicAdapter dynamicAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        rootView = inflater.inflate(R.layout.layout_dynamic, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        initView();
        return rootView;
    }

    public void initView() {
        listBeans = new ArrayList<DataListBean>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        dynamicAdapter = new DynamicAdapter(getContext(), listBeans);
        recyclerView.setAdapter(dynamicAdapter);
        dynamicAdapter.setOnItemClickListener(new DynamicAdapter.OnItemClickListener() {
            @Override
            public void OnItemClickListener(int firstPosition) {//关注
                if (StringUtil.isEmpty(SharePrefUtil.getString(getContext(), AppConsts.UID, ""))){
                    ToastUtil.show("请先登录");
                    ActivitySwitcher.startFragment(getActivity(), LoginFra.class);
                    return;
                }
                if (listBeans.get(firstPosition).focused.equals("0")){
                    focusMember(listBeans.get(firstPosition).member.id,"1");
                }else {
                    focusMember(listBeans.get(firstPosition).member.id,"0");
                }
            }

            @Override
            public void OnDetailClickListener(int firstPosition) {
                Bundle bundle = new Bundle();
                bundle.putString("fmid",listBeans.get(firstPosition).id);
                ActivitySwitcher.startFragment(getActivity(), DynamicDetailFra.class,bundle);
            }

            @Override
            public void OnFenxiangClickListener(int firstPosition) {//分享
                if (StringUtil.isEmpty(SharePrefUtil.getString(getContext(), AppConsts.UID, ""))){
                    ToastUtil.show("请先登录");
                    ActivitySwitcher.startFragment(getActivity(), LoginFra.class);
                    return;
                }
                AppConsts.SHAREDES = listBeans.get(firstPosition).content;
                if (listBeans.get(firstPosition).images.size()>0){
                    AppConsts.FENGMIAN = listBeans.get(firstPosition).images.get(0);
                }else {
                    AppConsts.FENGMIAN = "";
                }

                AppConsts.miaoshu = listBeans.get(firstPosition).member.nickname;
                AppConsts.SHAREURL = "http://8.136.116.205/photoPage?fmid="+listBeans.get(firstPosition).id;
                new ShareFra().show(getFragmentManager(), "Menu");
            }

            @Override
            public void Onchakandatu(int positiom, int firstPosition) {
                showImage(new ImageView(getContext()), firstPosition, listBeans.get(positiom).images);
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
                friendMomentsList();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
                friendMomentsList();
                refreshLayout.setNoMoreData(false);
            }
        });



        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                /*判断是否是“GO”键*/
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    /*隐藏软键盘*/
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm.isActive()) {
                        imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                    }
                    return true;
                }

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    /*隐藏软键盘*/
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm.isActive()) {
                        imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                    }
                    if (!TextUtils.isEmpty(etSearch.getText().toString())) {
                        friendMomentsList();
                    } else {
                        ToastUtil.show("关键字不能为空");
                    }
                    return true;
                }
                return false;
            }
        });


        tvPushState.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvPushState://发表动态
                if (StringUtil.isEmpty(SharePrefUtil.getString(getContext(), AppConsts.UID, ""))){
                    ToastUtil.show("请先登录");
                    ActivitySwitcher.startFragment(getActivity(), LoginFra.class);
                    return;
                }
                ActivitySwitcher.startFragment(getActivity(), PushStateFra.class);
                break;
        }
    }


    /**
     * 好友动态列表
     */
    private void friendMomentsList() {
        Map<String, Object> params = new HashMap<>();
        params.put("mid", userId);
        params.put("keywords",etSearch.getText().toString());
        params.put("pageNo", page + "");
        params.put("pageSize", "10");
        mOkHttpHelper.post_json(getContext(), Url.friendMomentsList, params, new BaseCallback<ResultBean>() {
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
     * 关注/取消关注用户
     */
    private void focusMember(String toMid,String type) {
        Map<String, Object> params = new HashMap<>();
        params.put("mid", userId);
        params.put("toMid",toMid);
        params.put("type",type);
        params.put("pageNo", page + "");
        params.put("pageSize", "10");
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
                friendMomentsList();


            }

            @Override
            public void onError(Response response, int code, Exception e) {
            }
        });
    }
    /**
     * 分享好友动态
     */
    private void shareFriendMoments(String fmid) {
        Map<String, Object> params = new HashMap<>();
        params.put("mid", userId);
        params.put("fmid",fmid);
        mOkHttpHelper.post_json(getContext(), Url.shareFriendMoments, params, new BaseCallback<ResultBean>() {
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
                new ShareFra().show(act.getSupportFragmentManager(), "Menu");
            }

            @Override
            public void onError(Response response, int code, Exception e) {
            }
        });
    }


    private void showImage(final ImageView iv, int position, List<String> list) {
        List<Object> urls = new ArrayList<>();
        urls.clear();
        urls.addAll(list);

        new XPopup.Builder(getContext()).asImageViewer(iv, position, urls, new OnSrcViewUpdateListener() {
            @Override
            public void onSrcViewUpdate(ImageViewerPopupView popupView, int position) {
                popupView.updateSrcView(iv);
            }
        }, new ImageLoader())
                .show();
    }

    class ImageLoader implements XPopupImageLoader {
        @Override
        public void loadImage(int position, @NonNull Object url, @NonNull ImageView imageView) {
            //必须指定Target.SIZE_ORIGINAL，否则无法拿到原图，就无法享用天衣无缝的动画
            Glide.with(imageView).load(url).apply(new RequestOptions().placeholder(R.mipmap.logo).override(Target.SIZE_ORIGINAL)).into(imageView);
        }

        @Override
        public File getImageFile(@NonNull Context context, @NonNull Object uri) {
            try {
                return Glide.with(context).downloadOnly().load(uri).submit().get();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        friendMomentsList();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


}
