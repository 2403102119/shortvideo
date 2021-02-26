package com.lxkj.shortvideo.ui.fragment.shortvideo;

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
import com.dueeeke.videoplayer.player.VideoView;
import com.dueeeke.videoplayer.util.L;
import com.lxkj.shortvideo.AppConsts;
import com.lxkj.shortvideo.R;
import com.lxkj.shortvideo.adapter.CommentAdapter;
import com.lxkj.shortvideo.adapter.Tiktok2Adapter;
import com.lxkj.shortvideo.bean.DataListBean;
import com.lxkj.shortvideo.bean.ResultBean;
import com.lxkj.shortvideo.biz.ActivitySwitcher;
import com.lxkj.shortvideo.http.BaseCallback;
import com.lxkj.shortvideo.http.Url;
import com.lxkj.shortvideo.ui.fragment.TitleFragment;
import com.lxkj.shortvideo.ui.fragment.competition.EventDetailsFra;
import com.lxkj.shortvideo.ui.fragment.competition.UserHomeFra;
import com.lxkj.shortvideo.ui.fragment.dialog.ShareFra;
import com.lxkj.shortvideo.ui.fragment.login.LoginFra;
import com.lxkj.shortvideo.ui.tiktok.Utils;
import com.lxkj.shortvideo.utils.SharePrefUtil;
import com.lxkj.shortvideo.utils.StringUtil;
import com.lxkj.shortvideo.utils.ToastUtil;
import com.lxkj.shortvideo.utils.cache.PreloadManager;
import com.lxkj.shortvideo.widget.VerticalViewPager;
import com.lxkj.shortvideo.widget.controller.TikTokController;
import com.makeramen.roundedimageview.RoundedImageView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.zhy.view.flowlayout.TagAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Time:2020/12/29
 * <p>
 * Author:李迪迦
 * <p>
 * Interface:
 */
public class ShortVideoFra extends TitleFragment implements View.OnClickListener {
    Unbinder unbinder;
    @BindView(R.id.vvp)
    VerticalViewPager mViewPager;
    @BindView(R.id.ivNoData)
    TextView ivNoData;


    private ArrayList<DataListBean> listBeans;
    private int page = 1, totalPage = 1;

    private String id;

    private Tiktok2Adapter mTikTokAdapter;
    VideoView mVideoView;
    private TikTokController mController;
    private int mCurPos;
    private int mIndex;
    private List<DataListBean> mVideoList = new ArrayList<>();
    private PreloadManager mPreloadManager;
    private boolean isStart = false;
    private boolean isVisible = false;
    /**
     * 标志位，View已经初始化完成。
     */
    private boolean isPrepared;
    private String position ="0";
    private PopupWindow popupWindow;
    private LinearLayout ll_all_item;
    private RelativeLayout ll_all;
    private ImageView im_close;
    private CommentAdapter commentAdapter;
    private String pcid;
    private String  commentCount;
    private PopupWindow  popupWindow1;
    private LinearLayout  ll_all_item1;
    private RelativeLayout  ll_all1;
    private SmartRefreshLayout smart;
    private TextView tvPupupTitle;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        rootView = inflater.inflate(R.layout.fra_shortvideo, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        initView();
        return rootView;
    }

    public void initView() {
        initViewPager();
        initVideoView();
        mPreloadManager = PreloadManager.getInstance(getContext());
        worksList();
        isPrepared = true;

        mVideoView.start();


        id = getArguments().getString("id");
    }

    /**
     * 如果是与ViewPager一起使用，调用的是setUserVisibleHint
     * @param isVisibleToUser 是否显示出来了
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            if (isPrepared && !mVideoView.isPlaying() && isStart && isVisibleToUser)
                mVideoView.start();
            isVisible = true;
        } else {
            if (isPrepared && mVideoView.isPlaying())
                mVideoView.pause();
            isVisible = false;
        }
        if (mVideoList.size() == 0 && isPrepared)
            worksList();

    }

    @Override
    public void onPause() {
        super.onPause();
        if (isPrepared && mVideoView.isPlaying())
            mVideoView.pause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isPrepared && !mVideoView.isPlaying() && isStart && isVisible)
            mVideoView.start();
        if (mVideoList.size() == 0 && isPrepared)
            worksList();
    }


    private void initVideoView() {
        mVideoView = new VideoView(getContext());
        mVideoView.setLooping(true);
        //以下只能二选一，看你的需求
//        mVideoView.setRenderViewFactory(TikTokRenderViewFactory.create());
        mVideoView.setScreenScaleType(VideoView.SCREEN_SCALE_DEFAULT);
        mController = new TikTokController(getContext());
        mVideoView.setVideoController(mController);
    }

    private void initViewPager() {
        mViewPager.setOffscreenPageLimit(2);
        mTikTokAdapter = new Tiktok2Adapter(mVideoList);
        mViewPager.setAdapter(mTikTokAdapter);
        mViewPager.setOverScrollMode(View.OVER_SCROLL_NEVER);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            private int mCurItem;
            /**
             * VerticalViewPager是否反向滑动
             */
            private boolean mIsReverseScroll;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                if (position == mCurItem) {
                    return;
                }
                mIsReverseScroll = position < mCurItem;
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (position == mCurPos) return;
                startPlay(position);
                if (position > mVideoList.size() - 3) {
                    if (totalPage == page) {
                        if (totalPage == 1){
                            page = 1;
                            worksList();
                        }
                    } else if (page < totalPage) {
                        page++;
                        worksList();
                    }
                }else {
                    if (totalPage == 1 && page == 2){
                        page++;
                        worksList();
                    }else  if (totalPage > page){
                        page++;
                        worksList();
                    }

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
                if (state == VerticalViewPager.SCROLL_STATE_DRAGGING) {
                    mCurItem = mViewPager.getCurrentItem();
                }
                if (state == VerticalViewPager.SCROLL_STATE_IDLE) {
                    mPreloadManager.resumePreload(mCurPos, mIsReverseScroll);
                } else {
                    mPreloadManager.pausePreload(mCurPos, mIsReverseScroll);
                }
            }
        });
    }

    private void initStart() {
        isStart = true;
        mViewPager.setCurrentItem(Integer.parseInt(position));
        mViewPager.post(new Runnable() {
            @Override
            public void run() {
                startPlay(Integer.parseInt(position));
            }
        });
    }

    private void startPlay(int position) {
        int count = mViewPager.getChildCount();
        for (int i = 0; i < count; i++) {
            View itemView = mViewPager.getChildAt(i);
            Tiktok2Adapter.ViewHolder viewHolder = (Tiktok2Adapter.ViewHolder) itemView.getTag();
            viewHolder.tvLaiyuan.setOnClickListener(this);
            viewHolder.llDetail.setOnClickListener(this);
            viewHolder.llPinglun.setOnClickListener(this);
            viewHolder.imfenxiang.setOnClickListener(this);
            if (viewHolder.mPosition == position) {
                mVideoView.release();
                Utils.removeViewFormParent(mVideoView);
                DataListBean tiktokBean = mVideoList.get(position);
                String playUrl = mPreloadManager.getPlayUrl(tiktokBean.video);
                mVideoView.setUrl(playUrl);
                mController.addControlComponent(viewHolder.mTikTokView, true);
                viewHolder.mPlayerContainer.addView(mVideoView, 0);
                if (isPrepared && !mVideoView.isPlaying() && isStart && isVisible)
                    mVideoView.start();
                mCurPos = position;
                break;
            }
        }
    }
    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        switch (v.getId()){
            case R.id.tvLaiyuan://来源
                bundle.putString("id",mVideoList.get(mCurPos).competition.id);
                bundle.putString("title",mVideoList.get(mCurPos).competition.name);
                ActivitySwitcher.startFragment(getActivity(), EventDetailsFra.class,bundle);
                break;
            case R.id.llDetail://用户
                if (StringUtil.isEmpty(SharePrefUtil.getString(getContext(), AppConsts.UID, ""))){
                    ToastUtil.show("请先登录");
                    ActivitySwitcher.startFragment(getActivity(), LoginFra.class);
                    return;
                }
                bundle.putString("toMid", mVideoList.get(mCurPos).member.id);
                ActivitySwitcher.startFragment(getActivity(), UserHomeFra.class, bundle);
                break;
            case R.id.llPinglun://评论
                PopupcommentList();
                ll_all_item.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.in_from_bottom));
                popupWindow1.showAtLocation(getView(), Gravity.BOTTOM, 0, 0);
                break;
            case R.id.imfenxiang:
                AppConsts.SHAREDES = mVideoList.get(mCurPos).title;
                AppConsts.FENGMIAN = mVideoList.get(mCurPos).video+AppConsts.ViDEOEND;
                AppConsts.miaoshu = mVideoList.get(mCurPos).competition.name;
                AppConsts.SHAREURL = "http://8.136.116.205/videoPage?fmid="+mVideoList.get(mCurPos).id;
                new ShareFra().show(getFragmentManager(), "Menu");
                break;
        }
    }

    /**
     * 短视频列表
     */
    private void worksList() {
        Map<String, Object> params = new HashMap<>();
        params.put("mid", userId);
        params.put("ccid", id);
        params.put("keywords", AppConsts.keywords);
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
                totalPage = Integer.parseInt(resultBean.totalPage);
                if (null != resultBean.dataList)
                    mVideoList.addAll(resultBean.dataList);
                mTikTokAdapter.notifyDataSetChanged();
                if (mVideoList.size() > 0) {
                    ivNoData.setVisibility(View.GONE);
                    if (!isStart)
                        initStart();

                } else {
                    ivNoData.setVisibility(View.VISIBLE);
                }

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
        popupWindow1 = new PopupWindow(getContext());
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        WindowManager.LayoutParams wl = getActivity().getWindow().getAttributes();
        wl.alpha = 0.6f;   //这句就是设置窗口里崆件的透明度的．０.０全透明．１.０不透明．
        getActivity().getWindow().setAttributes(wl);
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
        commentCount = mVideoList.get(mCurPos).commentCount;
        tvPupupTitle.setText("共" + commentCount + "条评论");

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
                ll_all_item1.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.in_from_bottom));
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
        popupWindow = new PopupWindow(getContext());
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        WindowManager.LayoutParams wl = getActivity().getWindow().getAttributes();
//        wl.flags=WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        wl.alpha = 0.6f;   //这句就是设置窗口里崆件的透明度的．０.０全透明．１.０不透明．
        getActivity().getWindow().setAttributes(wl);
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
     * 发布评论
     */
    private void pubWorksComment(String content) {
        Map<String, Object> params = new HashMap<>();
        params.put("mid", userId);
        params.put("wid", mVideoList.get(mCurPos).id);
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
        params.put("wid", mVideoList.get(mCurPos).id);
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


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


}
