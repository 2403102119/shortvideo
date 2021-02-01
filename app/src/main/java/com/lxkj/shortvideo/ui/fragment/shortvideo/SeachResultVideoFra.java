package com.lxkj.shortvideo.ui.fragment.shortvideo;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.lxkj.shortvideo.R;
import com.lxkj.shortvideo.adapter.CommentAdapter;
import com.lxkj.shortvideo.adapter.SeachResultVideoAdapter;
import com.lxkj.shortvideo.bean.DataListBean;
import com.lxkj.shortvideo.bean.ResultBean;
import com.lxkj.shortvideo.biz.ActivitySwitcher;
import com.lxkj.shortvideo.http.BaseCallback;
import com.lxkj.shortvideo.http.Url;
import com.lxkj.shortvideo.ui.fragment.TitleFragment;
import com.lxkj.shortvideo.ui.fragment.competition.EventDetailsFra;
import com.lxkj.shortvideo.ui.fragment.competition.UserHomeFra;
import com.lxkj.shortvideo.utils.StringUtil;
import com.lxkj.shortvideo.utils.ToastUtil;
import com.makeramen.roundedimageview.RoundedImageView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.zhy.view.flowlayout.TagAdapter;

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
 * Time:2021/1/28
 * <p>
 * Author:李迪迦
 * <p>
 * Interface:搜索视频结果页
 */
public class SeachResultVideoFra extends TitleFragment implements View.OnClickListener {
    Unbinder unbinder;
    @BindView(R.id.llSeach)
    LinearLayout llSeach;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.smart)
    SmartRefreshLayout smart;
    @BindView(R.id.etSearch)
    EditText etSearch;
    @BindView(R.id.imGuanbi)
    ImageView imGuanbi;
    @BindView(R.id.imFinish)
    ImageView imFinish;
    private String key;
    private ArrayList<DataListBean> listBeans;
    private ArrayList<DataListBean> listBeans_popup;
    private int page = 1, totalPage = 1;
    private SeachResultVideoAdapter seachResultVideoAdapter;
    private String wid, pcid,commentCount;
    private PopupWindow popupWindow, popupWindow1;
    private LinearLayout ll_all_item, ll_all_item1;
    private RelativeLayout ll_all, ll_all1;
    private ImageView im_close;
    private TextView tvPupupTitle;
    private CommentAdapter commentAdapter;
    private SmartRefreshLayout smart1;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        rootView = inflater.inflate(R.layout.fra_seachresultvideo, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        act.hindNaviBar();
        initView();
        return rootView;
    }

    public void initView() {
        key = getArguments().getString("key");
        etSearch.setText(key);

        listBeans_popup = new ArrayList<DataListBean>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        seachResultVideoAdapter = new SeachResultVideoAdapter(getContext(), listBeans_popup);
        recyclerView.setAdapter(seachResultVideoAdapter);
        seachResultVideoAdapter.setOnItemClickListener(new SeachResultVideoAdapter.OnItemClickListener() {
            @Override
            public void OnItemClickListener(int firstPosition) {//详情
                Bundle bundle = new Bundle();
                bundle.putString("toMid", listBeans_popup.get(firstPosition).member.id);
                ActivitySwitcher.startFragment(getActivity(), UserHomeFra.class, bundle);
            }

            @Override
            public void OnBigClickListener(int firstPosition) {//大屏
                Bundle bundle = new Bundle();
                bundle.putString("video", listBeans_popup.get(firstPosition).video);
                ActivitySwitcher.startFragment(getActivity(), VideoFra.class, bundle);
                getActivity().overridePendingTransition(R.anim.anim_zoom_in, R.anim.anim_stay);
            }

            @Override
            public void OnShoucangClickListener(int firstPosition) {//收藏
                if (listBeans_popup.get(firstPosition).collected.equals("1")){
                    collectWorks("0",listBeans_popup.get(firstPosition).id,firstPosition);
                }else {
                    collectWorks("1",listBeans_popup.get(firstPosition).id,firstPosition);
                }
            }

            @Override
            public void OnPinglunClickListener(int firstPosition) {//评论
                wid = listBeans_popup.get(firstPosition).id;
                commentCount = listBeans_popup.get(firstPosition).commentCount;
                PopupcommentList();
                ll_all_item.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.in_from_bottom));
                popupWindow1.showAtLocation(getView(), Gravity.BOTTOM, 0, 0);
            }

            @Override
            public void OnLaiyuanClickListener(int firstPosition) {
                Bundle bundle = new Bundle();
                bundle.putString("id",listBeans_popup.get(firstPosition).competition.id);
                bundle.putString("title",listBeans_popup.get(firstPosition).competition.name);
                ActivitySwitcher.startFragment(getActivity(), EventDetailsFra.class,bundle);
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
                worksList();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
                worksList();
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
                        worksList();
                    } else {
                        ToastUtil.show("关键字不能为空");
                    }
                    return true;
                }
                return false;
            }
        });

        imGuanbi.setOnClickListener(this);
        imFinish.setOnClickListener(this);

        worksList();
    }

    /**
     * 短视频列表
     */
    private void worksList() {
        Map<String, Object> params = new HashMap<>();
        params.put("mid", userId);
        params.put("ccid", "");
        params.put("keywords", etSearch.getText().toString());
        params.put("pageNo", page + "");
        params.put("pageSize", "10");
        mOkHttpHelper.post_json(getContext(), Url.worksList, params, new BaseCallback<ResultBean>() {
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
                    listBeans_popup.clear();
                    seachResultVideoAdapter.notifyDataSetChanged();
                }
                if (null != resultBean.dataList)
                    listBeans_popup.addAll(resultBean.dataList);
//                if (listBeans.size() == 0) {
//                    llNoData.setVisibility(View.VISIBLE);
//                    recyclerView.setVisibility(View.GONE);
//                } else {
//                    recyclerView.setVisibility(View.VISIBLE);
//                    llNoData.setVisibility(View.GONE);
//                }
                seachResultVideoAdapter.notifyDataSetChanged();

            }

            @Override
            public void onError(Response response, int code, Exception e) {
            }
        });
    }

    /**
     * 作品收藏/取消收藏
     */
    private void collectWorks(String type,String wid,int position) {
        Map<String, Object> params = new HashMap<>();
        params.put("mid", userId);
        params.put("wid", wid);
        params.put("type", type);
        mOkHttpHelper.post_json(getContext(), Url.collectWorks, params, new BaseCallback<ResultBean>() {
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
                if (type.equals("0")) {
                    listBeans_popup.get(position).collected = "0";
                } else {
                    listBeans_popup.get(position).collected = "1";
                }
                seachResultVideoAdapter.notifyDataSetChanged();

            }

            @Override
            public void onError(Response response, int code, Exception e) {
            }
        });
    }

    /**
     * 评论列表
     */
    public void PopupcommentList() {
        popupWindow1 = new PopupWindow(mContext);
        act.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        WindowManager.LayoutParams wl = act.getWindow().getAttributes();
        wl.alpha = 0.6f;   //这句就是设置窗口里崆件的透明度的．０.０全透明．１.０不透明．
        act.getWindow().setAttributes(wl);
        View view = getLayoutInflater().inflate(R.layout.popup_coupon_list, null);
        ll_all_item = view.findViewById(R.id.ll_all_item);
        popupWindow1.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow1.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow1.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow1.setFocusable(true);
        popupWindow1.setOutsideTouchable(true);
        popupWindow1.setContentView(view);
        popupWindow1.setAnimationStyle(R.style.ani_bottom);
        ll_all = view.findViewById(R.id.ll_all);
        im_close = view.findViewById(R.id.im_close);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        smart1 = view.findViewById(R.id.smart);
        tvPupupTitle = view.findViewById(R.id.tvTitle);
        TextView tvPinglun = view.findViewById(R.id.tvPinglun);
        EditText etPinglun = view.findViewById(R.id.etPinglun);
        tvPupupTitle.setText("共" + commentCount + "条评论");

        listBeans = new ArrayList<DataListBean>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        commentAdapter = new CommentAdapter(getContext(), listBeans);
        recyclerView.setAdapter(commentAdapter);
        commentAdapter.setOnItemClickListener(new CommentAdapter.OnItemClickListener() {
            @Override
            public void OnItemClickListener(int firstPosition) {
                pcid = listBeans.get(firstPosition).id;
                Popupcomment(listBeans.get(firstPosition).member.avatar, listBeans.get(firstPosition).member.nickname, listBeans.get(firstPosition).createDate, listBeans.get(firstPosition).content);
                ll_all_item1.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.in_from_bottom));
                popupWindow.showAtLocation(getView(), Gravity.BOTTOM, 0, 0);
            }

            @Override
            public void OnDianzanClickListener(int firstPosition) {
                if (listBeans.get(firstPosition).liked.equals("1")) {
                    likeWorksComment(listBeans.get(firstPosition).id, "0", firstPosition);
                } else {
                    likeWorksComment(listBeans.get(firstPosition).id, "1", firstPosition);
                }
            }

            @Override
            public void OnDianzanItemClickListener(int position, int firstPosition) {
                if (listBeans.get(position).subCommentList.get(firstPosition).liked.equals("1")) {
                    likeWorksComment1(listBeans.get(position).subCommentList.get(firstPosition).id, "0", position, firstPosition);
                } else {
                    likeWorksComment1(listBeans.get(position).subCommentList.get(firstPosition).id, "1", position, firstPosition);
                }
            }
        });
        smart1.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if (page >= totalPage) {
                    refreshLayout.setNoMoreData(true);
                    return;
                }
                page++;
                worksCommentList();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
                worksCommentList();
                refreshLayout.setNoMoreData(false);
            }
        });
        smart1.autoRefresh();
        tvPinglun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtil.isEmpty(etPinglun.getText().toString())) {
                    ToastUtil.show("请输入评论内容");
                    return;
                }
                pcid = "";
                pubWorksComment(etPinglun.getText().toString());
                etPinglun.setText("");
            }
        });
        im_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow1.dismiss();
                ll_all.clearAnimation();
                lighton();
            }
        });
        popupWindow1.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                lighton();
            }
        });

    }

    /**
     * 二级评论
     */
    public void Popupcomment(String url, String name, String time, String content) {
        popupWindow = new PopupWindow(mContext);
        act.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        WindowManager.LayoutParams wl = act.getWindow().getAttributes();
//        wl.flags=WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        wl.alpha = 0.6f;   //这句就是设置窗口里崆件的透明度的．０.０全透明．１.０不透明．
        act.getWindow().setAttributes(wl);
        View view = getLayoutInflater().inflate(R.layout.popup_coupon, null);
        ll_all_item1 = view.findViewById(R.id.ll_all_item);
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setContentView(view);
        popupWindow.setAnimationStyle(R.style.ani_bottom);
        ll_all1 = view.findViewById(R.id.ll_all);
        ImageView im_close = view.findViewById(R.id.im_close);
        RoundedImageView ri_icon = view.findViewById(R.id.ri_icon);
        TextView tv_name = view.findViewById(R.id.tv_name);
        TextView tv_time = view.findViewById(R.id.tv_time);
        TextView tv_content = view.findViewById(R.id.tv_content);
        TextView tvPinglun = view.findViewById(R.id.tvPinglun);
        EditText etpinglun = view.findViewById(R.id.etPinglun);
        tv_name.setText(name);
        tv_time.setText(time);
        tv_content.setText(content);
        Glide.with(getContext()).applyDefaultRequestOptions(new RequestOptions()
                .error(R.mipmap.touxiang)
                .placeholder(R.mipmap.touxiang))
                .load(url)
                .into(ri_icon);

        tvPinglun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtil.isEmpty(etpinglun.getText().toString())) {
                    ToastUtil.show("请输入评论内容");
                    return;
                }
                pubWorksComment(etpinglun.getText().toString());
                etpinglun.setText("");
            }
        });
        im_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                ll_all1.clearAnimation();
                lighton();
            }
        });
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                lighton();
            }
        });

    }

    /**
     * 设置手机屏幕亮度显示正常
     */
    private void lighton() {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = 1f;
        getActivity().getWindow().setAttributes(lp);
    }

    /**
     * 作品评论列表
     */
    private void worksCommentList() {
        Map<String, Object> params = new HashMap<>();
        params.put("mid", userId);
        params.put("wid", wid);
        params.put("pageNo", page + "");
        mOkHttpHelper.post_json(getContext(), Url.worksCommentList, params, new BaseCallback<ResultBean>() {
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
                smart1.finishLoadMore();
                smart1.finishRefresh();
                if (page == 1) {
                    listBeans.clear();
                    commentAdapter.notifyDataSetChanged();
                }
                if (null != resultBean.dataList)
                    listBeans.addAll(resultBean.dataList);

                commentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Response response, int code, Exception e) {
            }
        });
    }


    /**
     * 评论点赞/取消点赞
     */
    private void likeWorksComment(String cid, String type, int position) {
        Map<String, Object> params = new HashMap<>();
        params.put("mid", userId);
        params.put("cid", cid);
        params.put("type", type);
        mOkHttpHelper.post_json(getContext(), Url.likeWorksComment, params, new BaseCallback<ResultBean>() {
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
                listBeans.get(position).liked = type;
                if (type.equals("1")) {
                    listBeans.get(position).likedCount = (Integer.parseInt(listBeans.get(position).likedCount) + 1) + "";
                } else {
                    listBeans.get(position).likedCount = (Integer.parseInt(listBeans.get(position).likedCount) - 1) + "";
                }
                commentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Response response, int code, Exception e) {
            }
        });
    }

    /**
     * 评论点赞/取消点赞
     */
    private void likeWorksComment1(String cid, String type, int position, int position1) {
        Map<String, Object> params = new HashMap<>();
        params.put("mid", userId);
        params.put("cid", cid);
        params.put("type", type);
        mOkHttpHelper.post_json(getContext(), Url.likeWorksComment, params, new BaseCallback<ResultBean>() {
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
                listBeans.get(position).subCommentList.get(position1).liked = type;
                if (type.equals("1")) {
                    listBeans.get(position).subCommentList.get(position1).likedCount = (Integer.parseInt(listBeans.get(position).subCommentList.get(position1).likedCount) + 1) + "";
                } else {
                    listBeans.get(position).subCommentList.get(position1).likedCount = (Integer.parseInt(listBeans.get(position).subCommentList.get(position1).likedCount) - 1) + "";
                }
                commentAdapter.notifyDataSetChanged();


            }

            @Override
            public void onError(Response response, int code, Exception e) {
            }
        });
    }



    /**
     * 发布评论
     */
    private void pubWorksComment(String content) {
        Map<String, Object> params = new HashMap<>();
        params.put("mid", userId);
        params.put("wid", wid);
        params.put("pcid", pcid);
        params.put("content", content);
        mOkHttpHelper.post_json(getContext(), Url.pubWorksComment, params, new BaseCallback<ResultBean>() {
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
                ToastUtil.show(resultBean.resultNote);
                tvPupupTitle.setText("共" + (Integer.parseInt(commentCount) + 1) + "条评论");
                worksCommentList();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imGuanbi:
                etSearch.setText("");
                worksList();
                break;
            case R.id.imFinish:
                act.finishSelf();
                break;
        }
    }
}
