package com.lxkj.shortvideo.ui.fragment.competition;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.lxkj.shortvideo.AppConsts;
import com.lxkj.shortvideo.HcbApp;
import com.lxkj.shortvideo.R;
import com.lxkj.shortvideo.adapter.CommentAdapter;
import com.lxkj.shortvideo.bean.DataListBean;
import com.lxkj.shortvideo.bean.ResultBean;
import com.lxkj.shortvideo.biz.ActivitySwitcher;
import com.lxkj.shortvideo.http.BaseCallback;
import com.lxkj.shortvideo.http.Url;
import com.lxkj.shortvideo.ui.fragment.TitleFragment;
import com.lxkj.shortvideo.ui.fragment.dialog.ShareFra;
import com.lxkj.shortvideo.ui.fragment.login.LoginFra;
import com.lxkj.shortvideo.ui.fragment.shortvideo.VideoFra;
import com.lxkj.shortvideo.utils.SharePrefUtil;
import com.lxkj.shortvideo.utils.StringUtil;
import com.lxkj.shortvideo.utils.ToastUtil;
import com.makeramen.roundedimageview.RoundedImageView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.jzvd.JZDataSource;
import cn.jzvd.JzvdStd;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Time:2021/1/6
 * <p>
 * Author:李迪迦
 * <p>
 * Interface:
 */
public class WorkDetails extends TitleFragment implements View.OnClickListener {
    Unbinder unbinder;
    @BindView(R.id.jzVideo)
    JzvdStd jzVideo;
    @BindView(R.id.riIcon)
    RoundedImageView riIcon;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvGuanzhu)
    TextView tvGuanzhu;
    @BindView(R.id.llDetail)
    LinearLayout llDetail;
    @BindView(R.id.tagFlow)
    TagFlowLayout tagFlow;
    @BindView(R.id.tvCommentName)
    TextView tvCommentName;
    @BindView(R.id.etComment)
    TextView etComment;
    @BindView(R.id.tvSent)
    TextView tvSent;
    @BindView(R.id.llPinglun)
    LinearLayout llPinglun;
    @BindView(R.id.imShoucang)
    ImageView imShoucang;
    @BindView(R.id.tvShoucang)
    TextView tvShoucang;
    @BindView(R.id.imfenxiang)
    ImageView imfenxiang;
    @BindView(R.id.tvFenxiang)
    TextView tvFenxiang;


    private ArrayList<DataListBean> listBeans;
    private int page = 1, totalPage = 1;
    private CommentAdapter commentAdapter;
    private String wid, pcid, focused, toMid;
    private int position = 0;
    private TagAdapter<String> adapter;
    private String collected, commentCount;
    private PopupWindow popupWindow, popupWindow1;
    private LinearLayout ll_all_item, ll_all_item1;
    private RelativeLayout ll_all, ll_all1;
    private ImageView im_close;
    private String PNWorkstype = "0",fengmian;
    private SmartRefreshLayout smart;
    private TextView tvPupupTitle;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        rootView = inflater.inflate(R.layout.fra_workdetaile, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        initView();
        return rootView;
    }

    public void initView() {

        wid = getArguments().getString("wid");


        tvSent.setOnClickListener(this);
        llDetail.setOnClickListener(this);
        imShoucang.setOnClickListener(this);
        tvGuanzhu.setOnClickListener(this);
        tvGuanzhu.setOnClickListener(this);
        imfenxiang.setOnClickListener(this);
        llPinglun.setOnClickListener(this);


        competitionWorksDetail();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvSent:
                if (StringUtil.isEmpty(SharePrefUtil.getString(getContext(), AppConsts.UID, ""))){
                    ToastUtil.show("请先登录");
                    ActivitySwitcher.startFragment(getActivity(), LoginFra.class);
                    return;
                }
                if (StringUtil.isEmpty(etComment.getText().toString())) {
                    ToastUtil.show("请输入评论内容");
                    return;
                }
                pubWorksComment(etComment.getText().toString());
                break;
            case R.id.llDetail:
                if (StringUtil.isEmpty(SharePrefUtil.getString(getContext(), AppConsts.UID, ""))){
                    ToastUtil.show("请先登录");
                    ActivitySwitcher.startFragment(getActivity(), LoginFra.class);
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putString("toMid", toMid);
                ActivitySwitcher.startFragment(getActivity(), UserHomeFra.class, bundle);
                break;
            case R.id.imShoucang://收藏
                if (StringUtil.isEmpty(SharePrefUtil.getString(getContext(), AppConsts.UID, ""))){
                    ToastUtil.show("请先登录");
                    ActivitySwitcher.startFragment(getActivity(), LoginFra.class);
                    return;
                }
                if (collected.equals("1")) {
                    collected = "0";
                    collectWorks("0");
                } else {
                    collected = "1";
                    collectWorks("1");
                }
                break;
            case R.id.tvGuanzhu://关注
                if (StringUtil.isEmpty(SharePrefUtil.getString(getContext(), AppConsts.UID, ""))){
                    ToastUtil.show("请先登录");
                    ActivitySwitcher.startFragment(getActivity(), LoginFra.class);
                    return;
                }
                if (focused.equals("1")) {
                    focused = "0";
                    focusMember("0");
                } else {
                    focused = "1";
                    focusMember("1");
                }
                break;
            case R.id.imfenxiang:
                if (StringUtil.isEmpty(SharePrefUtil.getString(getContext(), AppConsts.UID, ""))){
                    ToastUtil.show("请先登录");
                    ActivitySwitcher.startFragment(getActivity(), LoginFra.class);
                    return;
                }
                shareWorks(wid);
                break;
            case R.id.llPinglun://评论
                PopupcommentList();
                ll_all_item.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.in_from_bottom));
                popupWindow1.showAtLocation(getView(), Gravity.BOTTOM, 0, 0);
                break;
        }
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
        smart = view.findViewById(R.id.smart);
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
                if (StringUtil.isEmpty(SharePrefUtil.getString(getContext(), AppConsts.UID, ""))){
                    ToastUtil.show("请先登录");
                    ActivitySwitcher.startFragment(getActivity(), LoginFra.class);
                    return;
                }
                pcid = listBeans.get(firstPosition).id;
                Popupcomment(listBeans.get(firstPosition).member.avatar, listBeans.get(firstPosition).member.nickname, listBeans.get(firstPosition).createDate, listBeans.get(firstPosition).content);
                ll_all_item1.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.in_from_bottom));
                popupWindow.showAtLocation(getView(), Gravity.BOTTOM, 0, 0);
            }

            @Override
            public void OnDianzanClickListener(int firstPosition) {
                if (StringUtil.isEmpty(SharePrefUtil.getString(getContext(), AppConsts.UID, ""))){
                    ToastUtil.show("请先登录");
                    ActivitySwitcher.startFragment(getActivity(), LoginFra.class);
                    return;
                }
                if (listBeans.get(firstPosition).liked.equals("1")) {
                    likeWorksComment(listBeans.get(firstPosition).id, "0", firstPosition);
                } else {
                    likeWorksComment(listBeans.get(firstPosition).id, "1", firstPosition);
                }
            }

            @Override
            public void OnDianzanItemClickListener(int position, int firstPosition) {
                if (StringUtil.isEmpty(SharePrefUtil.getString(getContext(), AppConsts.UID, ""))){
                    ToastUtil.show("请先登录");
                    ActivitySwitcher.startFragment(getActivity(), LoginFra.class);
                    return;
                }
                if (listBeans.get(position).subCommentList.get(firstPosition).liked.equals("1")) {
                    likeWorksComment1(listBeans.get(position).subCommentList.get(firstPosition).id, "0", position, firstPosition);
                } else {
                    likeWorksComment1(listBeans.get(position).subCommentList.get(firstPosition).id, "1", position, firstPosition);
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
                worksCommentList();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
                worksCommentList();
                refreshLayout.setNoMoreData(false);
            }
        });
        smart.autoRefresh();
        tvPinglun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtil.isEmpty(SharePrefUtil.getString(getContext(), AppConsts.UID, ""))){
                    ToastUtil.show("请先登录");
                    ActivitySwitcher.startFragment(getActivity(), LoginFra.class);
                    return;
                }
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
                if (StringUtil.isEmpty(SharePrefUtil.getString(getContext(), AppConsts.UID, ""))){
                    ToastUtil.show("请先登录");
                    ActivitySwitcher.startFragment(getActivity(), LoginFra.class);
                    return;
                }
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
     * 赛事作品详情
     */
    private void competitionWorksDetail() {
        Map<String, Object> params = new HashMap<>();
        params.put("mid", userId);
        params.put("wid", wid);
        mOkHttpHelper.post_json(getContext(), Url.competitionWorksDetail, params, new BaseCallback<ResultBean>() {
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
                act.titleTv.setText(resultBean.title);
                tvShoucang.setText(resultBean.collectCount);
                tvFenxiang.setText(resultBean.shareCount);
                tvName.setText(resultBean.member.nickname);
                toMid = resultBean.member.id;
                tvCommentName.setText(resultBean.member.nickname);
                Glide.with(getContext()).applyDefaultRequestOptions(new RequestOptions()
                        .error(R.mipmap.touxiang)
                        .placeholder(R.mipmap.touxiang))
                        .load(resultBean.member.avatar)
                        .into(riIcon);
                if (resultBean.collected.equals("1")) {
                    imShoucang.setImageResource(R.mipmap.yishoucang);
                } else {
                    imShoucang.setImageResource(R.mipmap.dianzan);
                }

                collected = resultBean.collected;

                if (resultBean.focused.equals("1")) {
                    tvGuanzhu.setText("已关注");
                } else {
                    tvGuanzhu.setText("+关注");
                }

                focused = resultBean.focused;

                LinkedHashMap map = new LinkedHashMap();
                String proxyUrl = HcbApp.getProxy(mContext).getProxyUrl(resultBean.video);
                map.put("高清", proxyUrl);
                JZDataSource jzDataSource = new JZDataSource(map, "");
                jzDataSource.looping = true;
                jzVideo.setUp(jzDataSource
                        , JzvdStd.SCREEN_NORMAL);

                jzVideo.startVideo();
                Glide.with(getActivity()).applyDefaultRequestOptions(new RequestOptions()
                        .error(R.mipmap.imageerror)
                        .placeholder(R.mipmap.imageerror))
                        .load(resultBean.video+AppConsts.ViDEOEND)
                        .into(jzVideo.thumbImageView);
                fengmian = resultBean.video+AppConsts.ViDEOEND;

                jzVideo.titleTextView.setVisibility(View.GONE);
                jzVideo.replayTextView.setVisibility(View.GONE);
                jzVideo.backButton.setVisibility(View.GONE);
                jzVideo.batteryTimeLayout.setVisibility(View.GONE);
                jzVideo.tinyBackImageView.setVisibility(View.GONE);
                jzVideo.videoCurrentTime.setVisibility(View.GONE);
                jzVideo.clarity.setVisibility(View.GONE);
                jzVideo.mRetryLayout.setVisibility(View.GONE);
                jzVideo.mRetryBtn.setVisibility(View.GONE);
                jzVideo.clarity.setVisibility(View.GONE);
                jzVideo.fullscreenButton.setVisibility(View.VISIBLE);
                jzVideo.currentTimeTextView.setVisibility(View.GONE);
                jzVideo.thumbImageView.setVisibility(View.VISIBLE);

                jzVideo.fullscreenButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putString("video", resultBean.video);
                        ActivitySwitcher.startFragment(getActivity(), VideoFra.class, bundle);
                        getActivity().overridePendingTransition(R.anim.anim_zoom_in, R.anim.anim_stay);
                    }
                });

                List<String> list = new ArrayList<>();
                for (int i = 0; i < resultBean.subVideos.size(); i++) {
                    list.add(resultBean.subVideos.get(i).title);
                }
                adapter = new TagAdapter<String>(list) {
                    @Override
                    public View getView(FlowLayout parent, int position, String s) {
                        TextView view = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.item_lable, parent, false);
                        view.setText(s);
                        return view;
                    }
                };
                tagFlow.setAdapter(adapter);

                tagFlow.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
                    @Override
                    public boolean onTagClick(View view, int position, FlowLayout parent) {
                        LinkedHashMap map = new LinkedHashMap();
                        String proxyUrl = HcbApp.getProxy(mContext).getProxyUrl(resultBean.subVideos.get(position).video);
                        map.put("高清", proxyUrl);
                        JZDataSource jzDataSource = new JZDataSource(map, "");
                        jzDataSource.looping = true;
                        jzVideo.setUp(jzDataSource
                                , JzvdStd.SCREEN_NORMAL);
                        jzVideo.startVideo();
                        return true;
                    }
                });

//                tvPinglun.setText("共计" + resultBean.commentCount + "条评论");
                commentCount = resultBean.commentCount;
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
                smart.finishLoadMore();
                smart.finishRefresh();
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
     * 作品收藏/取消收藏
     */
    private void collectWorks(String type) {
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
                if (type.equals("1")) {
                    imShoucang.setImageResource(R.mipmap.yishoucang);
                    tvShoucang.setText((Integer.parseInt(tvShoucang.getText().toString()) + 1) + "");
                } else {
                    imShoucang.setImageResource(R.mipmap.dianzan);
                    tvShoucang.setText((Integer.parseInt(tvShoucang.getText().toString()) - 1) + "");
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
        params.put("toMid", toMid);
        params.put("type", type);
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
                if (type.equals("1")) {
                    tvGuanzhu.setText("已关注");
                } else {
                    tvGuanzhu.setText("+关注");
                }

            }

            @Override
            public void onError(Response response, int code, Exception e) {
            }
        });
    }

    private void shareWorks(String wid) {
        Map<String, Object> params = new HashMap<>();
        params.put("mid", userId);
        params.put("wid", wid);
        mOkHttpHelper.post_json(getContext(), Url.shareWorks, params, new BaseCallback<ResultBean>() {
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
                AppConsts.SHAREDES = act.titleTv.getText().toString();
                AppConsts.FENGMIAN = fengmian;
                AppConsts.miaoshu =  tvName.getText().toString();
                AppConsts.SHAREURL = "http://8.136.116.205/videoPage?fmid="+wid;
                new ShareFra().show(getFragmentManager(), "Menu");
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
    public void onDestroy() {
        super.onDestroy();
        JzvdStd.releaseAllVideos();//在销毁活动时，关闭饺子视频
    }

    @Override
    public void onStop() {
        super.onStop();
        JzvdStd.releaseAllVideos();//在销毁活动时，关闭饺子视频
    }
}
