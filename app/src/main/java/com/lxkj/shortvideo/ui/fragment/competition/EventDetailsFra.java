package com.lxkj.shortvideo.ui.fragment.competition;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
import com.lxkj.shortvideo.adapter.PingFenAdapter;
import com.lxkj.shortvideo.bean.DataListBean;
import com.lxkj.shortvideo.bean.ResultBean;
import com.lxkj.shortvideo.biz.ActivitySwitcher;
import com.lxkj.shortvideo.http.BaseCallback;
import com.lxkj.shortvideo.http.Url;
import com.lxkj.shortvideo.ui.fragment.TitleFragment;
import com.lxkj.shortvideo.ui.fragment.dialog.ShareFra;
import com.lxkj.shortvideo.ui.fragment.login.LoginFra;
import com.lxkj.shortvideo.ui.fragment.shortvideo.VideoFra;
import com.lxkj.shortvideo.ui.fragment.system.WebFra;
import com.lxkj.shortvideo.utils.PicassoUtil;
import com.lxkj.shortvideo.utils.SharePrefUtil;
import com.lxkj.shortvideo.utils.StringUtil;
import com.lxkj.shortvideo.utils.ToastUtil;
import com.lzy.ninegrid.ImageInfo;
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
import cn.ymex.widget.banner.Banner;
import cn.ymex.widget.banner.callback.BindViewCallBack;
import cn.ymex.widget.banner.callback.CreateViewCallBack;
import cn.ymex.widget.banner.callback.OnClickBannerListener;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Time:2021/1/5
 * <p>
 * Author:李迪迦
 * <p>
 * Interface:赛事详情
 */
public class EventDetailsFra extends TitleFragment implements View.OnClickListener, View.OnTouchListener {
    Unbinder unbinder;
    @BindView(R.id.navi_title)
    TextView naviTitle;
    @BindView(R.id.navi_left)
    ImageView naviLeft;
    @BindView(R.id.navi_right_txt)
    TextView naviRightTxt;
    @BindView(R.id.navi_right_img)
    ImageView naviRightImg;
    @BindView(R.id.llRank)
    LinearLayout llRank;
    @BindView(R.id.relBackGroundView)
    RelativeLayout relBackGroundView;
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.tvLookDetail)
    TextView tvLookDetail;
    @BindView(R.id.tvtitle)
    TextView tvtitle;
    @BindView(R.id.tvPass)
    TextView tvPass;
    @BindView(R.id.tvNg)
    TextView tvNg;
    @BindView(R.id.tvPingfen)
    TextView tvPingfen;
    @BindView(R.id.tvTishiyu)
    TextView tvTishiyu;
    @BindView(R.id.jzVideo)
    JzvdStd jzVideo;
    @BindView(R.id.imretreat)
    ImageView imretreat;
    @BindView(R.id.imadvance)
    ImageView imadvance;
    @BindView(R.id.ryPingfen)
    RecyclerView ryPingfen;
    @BindView(R.id.tvTijiaopingfen)
    TextView tvTijiaopingfen;
    @BindView(R.id.llPingfen)
    LinearLayout llPingfen;
    @BindView(R.id.riIcon)
    RoundedImageView riIcon;
    @BindView(R.id.tvnickname)
    TextView tvnickname;
    @BindView(R.id.tvGuanzhu)
    TextView tvGuanzhu;
    @BindView(R.id.llUser)
    LinearLayout llUser;
    @BindView(R.id.imVideo)
    ImageView imVideo;
    @BindView(R.id.tvTime)
    TextView tvTime;
    @BindView(R.id.llTime)
    LinearLayout llTime;
    @BindView(R.id.riVideo)
    RelativeLayout riVideo;
    @BindView(R.id.tagFlow)
    TagFlowLayout tagFlow;
    @BindView(R.id.tvCommentName)
    TextView tvCommentName;
    @BindView(R.id.etPinglun)
    TextView etPinglun;
    @BindView(R.id.llPinglun)
    LinearLayout llPinglun;
    @BindView(R.id.tvPinglun)
    TextView tvPinglun;
    @BindView(R.id.imCollected)
    ImageView imCollected;
    @BindView(R.id.tvCollectCount)
    TextView tvCollectCount;
    @BindView(R.id.imfenxiang)
    ImageView imfenxiang;
    @BindView(R.id.tvshareCount)
    TextView tvshareCount;
    @BindView(R.id.imGuanggao)
    ImageView imGuanggao;
    @BindView(R.id.imGuanbi)
    TextView imGuanbi;
    @BindView(R.id.rlGuanggao)
    RelativeLayout rlGuanggao;
    @BindView(R.id.mLinearLayout)
    LinearLayout mLinearLayout;


    private ArrayList<DataListBean> listBeans;
    private ArrayList<DataListBean> PopupcommentList;
    private ArrayList<DataListBean> PingfenBeans;
    private int page = 1, totalPage = 1;
    private CommentAdapter commentAdapter;
    private boolean CountDownTime = false;
    private String id, title, toMid, focused, pcid = "", entered, wid;
    private List<String> BanString = new ArrayList<>();
    private List<DataListBean> dataListBeans = new ArrayList<>();
    private ArrayList<ImageInfo> imageInfo = new ArrayList<>();
    private int position = 0;
    private TagAdapter<String> adapter;
    private String competitionCategoryId, competitionNumber, url;
    private PingFenAdapter pingFenAdapter;
    private List<DataListBean> pingfenlis = new ArrayList<>();

    //手指按下的点为(x1, y1)手指离开屏幕的点为(x2, y2)
    float x1 = 0;
    float x2 = 0;
    float y1 = 0;
    float y2 = 0;
    private PopupWindow popupWindow,popupWindow1;
    private LinearLayout ll_all_item,ll_all_item1;
    private RelativeLayout ll_all,ll_all1;
    private ImageView im_close;
    private String PNWorkstype = "0",commentCount,fengmian;
    private SmartRefreshLayout smart;
    private  TextView tvPupupTitle;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        rootView = inflater.inflate(R.layout.fra_eventdetails, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        act.hindNaviBar();
        initView();
        return rootView;
    }

    public void initView() {

        id = getArguments().getString("id");
        title = getArguments().getString("title");
        naviTitle.setText(title);


        PingfenBeans = new ArrayList<DataListBean>();
        ryPingfen.setLayoutManager(new LinearLayoutManager(getContext()));
        pingFenAdapter = new PingFenAdapter(getContext(), PingfenBeans);
        ryPingfen.setAdapter(pingFenAdapter);
        pingFenAdapter.setOnItemClickListener(new PingFenAdapter.OnItemClickListener() {
            @Override
            public void OnItemClickListener(int firstPosition, String rating) {
                pingfenlis.clear();
                DataListBean dataListBean = new DataListBean();
                dataListBean.itemId = PingfenBeans.get(firstPosition).id;
                dataListBean.score = rating;
                pingfenlis.add(dataListBean);
            }

        });


        riIcon.setOnClickListener(this);
        llRank.setOnClickListener(this);
        naviLeft.setOnClickListener(this);
        tvLookDetail.setOnClickListener(this);
        naviRightTxt.setOnClickListener(this);
        imCollected.setOnClickListener(this);
        imretreat.setOnClickListener(this);
        imadvance.setOnClickListener(this);
        tvGuanzhu.setOnClickListener(this);
        tvPinglun.setOnClickListener(this);
        llUser.setOnClickListener(this);
        tvPass.setOnClickListener(this);
        tvNg.setOnClickListener(this);
        imfenxiang.setOnClickListener(this);
        tvPingfen.setOnClickListener(this);
        llPingfen.setOnClickListener(this);
        tvTijiaopingfen.setOnClickListener(this);
        imGuanbi.setOnClickListener(this);
        rlGuanggao.setOnClickListener(this);
        llPinglun.setOnClickListener(this);



        competitionDetail();
        competitionWorksList();

        imVideo.setOnTouchListener(this);

    }


    public CountDownTimer timer = new CountDownTimer(10000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            if (null != tvTime) {
                tvTime.setText("倒计时" + (millisUntilFinished / 1000) + "s");
            }

            CountDownTime = false;
            llTime.setVisibility(View.VISIBLE);
            imVideo.setVisibility(View.VISIBLE);
            tvTime.setVisibility(View.VISIBLE);


        }

        @Override
        public void onFinish() {
            tvTime.setVisibility(View.GONE);

            jzVideo.startVideo();
            CountDownTime = true;
        }
    };

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.imVideo:
                if (StringUtil.isEmpty(SharePrefUtil.getString(getContext(), AppConsts.UID, ""))){
                    ToastUtil.show("请先登录");
                    ActivitySwitcher.startFragment(getActivity(), LoginFra.class);
                    return false;
                }
                //继承了Activity的onTouchEvent方法，直接监听点击事件
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    //当手指按下的时候
                    x1 = event.getX();
                    y1 = event.getY();
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    //当手指离开的时候
                    x2 = event.getX();
                    y2 = event.getY();
                    if (y1 - y2 > 50) {//上滑

                        if (competitionCategoryId.equals("2") && competitionNumber.equals("1")) {
                            if (CountDownTime) {
                                if (PNWorkstype.equals("0")) {//是否已滑动

                                    llTime.setVisibility(View.GONE);
                                    imVideo.setVisibility(View.GONE);
                                    rlGuanggao.setVisibility(View.GONE);
                                    PNWorks("1", StringUtil.formatTurnSecond(jzVideo.currentTimeTextView.getText().toString()) + "");
                                }

                            }
                        }
                    } else if (y2 - y1 > 50) {//下滑
                    } else if (x1 - x2 > 50) {//左滑
                        if (competitionCategoryId.equals("2") && competitionNumber.equals("1")) {
                            PNWorkstype = "0";

                            riVideo.setAnimation(AnimationUtils.makeInAnimation(getActivity(), false));

                            PNWorks("2", StringUtil.formatTurnSecond(jzVideo.currentTimeTextView.getText().toString()) + "");
                        }
                    } else if (x2 - x1 > 50) {//右滑
                    }
                }
                break;
        }

        return true;
    }

    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        switch (v.getId()) {
            case R.id.riIcon://用户主页
                if (StringUtil.isEmpty(SharePrefUtil.getString(getContext(), AppConsts.UID, ""))){
                    ToastUtil.show("请先登录");
                    ActivitySwitcher.startFragment(getActivity(), LoginFra.class);
                    return;
                }
                bundle.putString("toMid", toMid);
                ActivitySwitcher.startFragment(getActivity(), UserHomeFra.class, bundle);
                break;
            case R.id.llRank://排行榜
                bundle.putString("cid", id);
                bundle.putString("title", title);
                ActivitySwitcher.startFragment(getActivity(), RankFra.class, bundle);
                break;
            case R.id.navi_left:
                act.finishSelf();
                break;
            case R.id.tvLookDetail://查看详情
                if (StringUtil.isEmpty(SharePrefUtil.getString(getContext(), AppConsts.UID, ""))){
                    ToastUtil.show("请先登录");
                    ActivitySwitcher.startFragment(getActivity(), LoginFra.class);
                    return;
                }
                bundle.putString("cid", id);
                bundle.putString("entered", entered);
                ActivitySwitcher.startFragment(getActivity(), LookDetailFra.class, bundle);
                break;
            case R.id.navi_right_txt://报名
//                bundle.putString("cid", id);
//                ActivitySwitcher.startFragment(getActivity(), ApplyFra.class, bundle);
                bundle.putString("cid", id);
                bundle.putString("entered", entered);
                ActivitySwitcher.startFragment(getActivity(), LookDetailFra.class, bundle);
                break;
            case R.id.imCollected://收藏
                if (StringUtil.isEmpty(SharePrefUtil.getString(getContext(), AppConsts.UID, ""))){
                    ToastUtil.show("请先登录");
                    ActivitySwitcher.startFragment(getActivity(), LoginFra.class);
                    return;
                }
                if (dataListBeans.get(position).collected.equals("1")) {
                    collectWorks("0");
                } else {
                    collectWorks("1");
                }
                break;
            case R.id.imretreat://上一个
                if (position == 0) {
                    ToastUtil.show("已是第一个");
                    return;
                }
                riVideo.setAnimation(AnimationUtils.makeInAnimation(getActivity(), true));
                position = position - 1;
                setData(dataListBeans.get(position));
                break;
            case R.id.imadvance://下一个
                if (position + 1 == dataListBeans.size()) {
                    ToastUtil.show("已是最后一个");
                    return;
                }
                riVideo.setAnimation(AnimationUtils.makeInAnimation(getActivity(), false));
                position = position + 1;
                setData(dataListBeans.get(position));
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
            case R.id.llUser:
                pcid = "";
                tvCommentName.setText(dataListBeans.get(position).member.nickname);
                Glide.with(getContext()).applyDefaultRequestOptions(new RequestOptions()
                        .error(R.mipmap.touxiang)
                        .placeholder(R.mipmap.touxiang))
                        .load(dataListBeans.get(position).member.avatar)
                        .into(riIcon);
                break;
            case R.id.tvPass://PASS
                if (StringUtil.isEmpty(SharePrefUtil.getString(getContext(), AppConsts.UID, ""))){
                    ToastUtil.show("请先登录");
                    ActivitySwitcher.startFragment(getActivity(), LoginFra.class);
                    return;
                }
                rlGuanggao.setVisibility(View.GONE);
                PNWorks("1", StringUtil.formatTurnSecond(jzVideo.currentTimeTextView.getText().toString()) + "");
                break;
            case R.id.tvNg://NG
                if (StringUtil.isEmpty(SharePrefUtil.getString(getContext(), AppConsts.UID, ""))){
                    ToastUtil.show("请先登录");
                    ActivitySwitcher.startFragment(getActivity(), LoginFra.class);
                    return;
                }
                PNWorks("2", StringUtil.formatTurnSecond(jzVideo.currentTimeTextView.getText().toString()) + "");
                break;
            case R.id.imfenxiang:
                if (StringUtil.isEmpty(SharePrefUtil.getString(getContext(), AppConsts.UID, ""))){
                    ToastUtil.show("请先登录");
                    ActivitySwitcher.startFragment(getActivity(), LoginFra.class);
                    return;
                }
                shareWorks(wid);
                break;
            case R.id.llPingfen:

                break;
            case R.id.tvPingfen://评分
                llPingfen.setVisibility(View.VISIBLE);
                break;
            case R.id.tvTijiaopingfen://提交评分
                if (StringUtil.isEmpty(SharePrefUtil.getString(getContext(), AppConsts.UID, ""))){
                    ToastUtil.show("请先登录");
                    ActivitySwitcher.startFragment(getActivity(), LoginFra.class);
                    return;
                }
                giveWorksScore(wid);
                break;
            case R.id.imGuanbi://关闭广告
                rlGuanggao.setVisibility(View.GONE);
                break;
            case R.id.rlGuanggao:
                bundle.putString("title", "秀评");
                bundle.putString("url", url);
                ActivitySwitcher.startFragment(getActivity(), WebFra.class, bundle);
                break;
            case R.id.llPinglun://评论
                PopupcommentList();
                ll_all_item.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.in_from_bottom));
                popupWindow1.showAtLocation(getView(), Gravity.BOTTOM, 0, 0);
                break;

        }
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
        tvPupupTitle.setText("共"+commentCount+"条评论");

        PopupcommentList = new ArrayList<DataListBean>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        commentAdapter = new CommentAdapter(getContext(), PopupcommentList);
        recyclerView.setAdapter(commentAdapter);
        commentAdapter.setOnItemClickListener(new CommentAdapter.OnItemClickListener() {
            @Override
            public void OnItemClickListener(int firstPosition) {
                if (StringUtil.isEmpty(SharePrefUtil.getString(getContext(), AppConsts.UID, ""))){
                    ToastUtil.show("请先登录");
                    ActivitySwitcher.startFragment(getActivity(), LoginFra.class);
                    return;
                }
                pcid = PopupcommentList.get(firstPosition).id;
                Popupcomment(PopupcommentList.get(firstPosition).member.avatar, PopupcommentList.get(firstPosition).member.nickname, PopupcommentList.get(firstPosition).createDate, PopupcommentList.get(firstPosition).content);
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
                if (PopupcommentList.get(firstPosition).liked.equals("1")) {
                    likeWorksComment(PopupcommentList.get(firstPosition).id, "0", firstPosition);
                } else {
                    likeWorksComment(PopupcommentList.get(firstPosition).id, "1", firstPosition);
                }
            }

            @Override
            public void OnDianzanItemClickListener(int position, int firstPosition) {
                if (StringUtil.isEmpty(SharePrefUtil.getString(getContext(), AppConsts.UID, ""))){
                    ToastUtil.show("请先登录");
                    ActivitySwitcher.startFragment(getActivity(), LoginFra.class);
                    return;
                }
                if (PopupcommentList.get(position).subCommentList.get(firstPosition).liked.equals("1")) {
                    likeWorksComment1(PopupcommentList.get(position).subCommentList.get(firstPosition).id, "0", position, firstPosition);
                } else {
                    likeWorksComment1(PopupcommentList.get(position).subCommentList.get(firstPosition).id, "1", position, firstPosition);
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
     * 设置手机屏幕亮度显示正常
     */
    private void lighton() {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = 1f;
        getActivity().getWindow().setAttributes(lp);
    }

    /**
     * 作品分享
     */
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
                AppConsts.SHAREDES = tvtitle.getText().toString();
                AppConsts.FENGMIAN = fengmian;
                AppConsts.miaoshu = naviTitle.getText().toString();
                AppConsts.SHAREURL = "http://8.136.116.205/videoPage?fmid="+wid;
                new ShareFra().show(getFragmentManager(), "Menu");
            }

            @Override
            public void onError(Response response, int code, Exception e) {
            }
        });
    }

    /**
     * 作品分享
     */
    private void giveWorksScore(String wid) {
        Map<String, Object> params = new HashMap<>();
        params.put("mid", userId);
        params.put("wid", wid);
        params.put("scores", pingfenlis);
        mOkHttpHelper.post_json(getContext(), Url.giveWorksScore, params, new BaseCallback<ResultBean>() {
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
                llPingfen.setVisibility(View.GONE);
                rlGuanggao.setVisibility(View.GONE);
            }

            @Override
            public void onError(Response response, int code, Exception e) {
            }
        });
    }

    /**
     * 赛事详情
     */
    private void competitionDetail() {
        Map<String, Object> params = new HashMap<>();
        params.put("mid", userId);
        params.put("cid", id);
        mOkHttpHelper.post_json(getContext(), Url.competitionDetail, params, new BaseCallback<ResultBean>() {
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
                if (null != resultBean.carouselImages) {
                    for (int i = 0; i < resultBean.carouselImages.size(); i++) {
                        BanString.add(resultBean.carouselImages.get(i));
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
                                bundle.putString("cid", id);
                                bundle.putString("entered", entered);
                                ActivitySwitcher.startFragment(getActivity(), LookDetailFra.class, bundle);
                            }
                        })
                        //填充数据
                        .execute(BanString);

                entered = resultBean.entered;

            }

            @Override
            public void onError(Response response, int code, Exception e) {
            }
        });
    }

    /**
     * 赛事作品列表
     */
    private void competitionWorksList() {
        Map<String, Object> params = new HashMap<>();
        params.put("mid", userId);
        params.put("cid", id);
        mOkHttpHelper.post_json(getContext(), Url.competitionWorksList, params, new BaseCallback<ResultBean>() {
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
                dataListBeans.clear();
                dataListBeans.addAll(resultBean.dataList);

                setData(dataListBeans.get(position));
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
        params.put("wid", dataListBeans.get(position).id);
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
                    imCollected.setImageResource(R.mipmap.yishoucang);
                    tvCollectCount.setText((Integer.parseInt(tvCollectCount.getText().toString()) + 1) + "");
                    dataListBeans.get(position).collected = "1";
                } else {
                    imCollected.setImageResource(R.mipmap.dianzan);
                    tvCollectCount.setText((Integer.parseInt(tvCollectCount.getText().toString()) - 1) + "");
                    dataListBeans.get(position).collected = "0";
                }
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
        params.put("wid", dataListBeans.get(position).id);
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
                smart.finishLoadMore();
                smart.finishRefresh();
                PopupcommentList.clear();
                PopupcommentList.addAll(resultBean.dataList);
                commentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Response response, int code, Exception e) {
            }
        });
    }

    /**
     * 获取广告
     */
    private void getAdvertising() {
        Map<String, Object> params = new HashMap<>();
        params.put("mid", userId);
        params.put("count", "2");
        mOkHttpHelper.post_json(getContext(), Url.getAdvertising, params, new BaseCallback<ResultBean>() {
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
                        .error(R.mipmap.imageerror)
                        .placeholder(R.mipmap.imageerror))
                        .load(resultBean.advertising.get(0).image)
                        .into(imVideo);
                Glide.with(getContext()).applyDefaultRequestOptions(new RequestOptions()
                        .error(R.mipmap.imageerror)
                        .placeholder(R.mipmap.imageerror))
                        .load(resultBean.advertising.get(0).image)
                        .into(imGuanggao);
                url = resultBean.advertising.get(0).url;
            }

            @Override
            public void onError(Response response, int code, Exception e) {
            }
        });
    }

    /**
     * 关注/取消关注用户
     */
    private void focusMember(String focused) {
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
                if (focused.equals("1")) {

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

    /**
     * 发布评论
     */
    private void pubWorksComment(String content) {
        Map<String, Object> params = new HashMap<>();
        params.put("mid", userId);
        params.put("wid", dataListBeans.get(position).id);
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
                tvPupupTitle.setText("共"+(Integer.parseInt(commentCount)+1)+"条评论");
                worksCommentList();
            }

            @Override
            public void onError(Response response, int code, Exception e) {
            }
        });
    }

    /**
     * 作品PASS或NG
     */
    private void PNWorks(String type, String duration) {
        Map<String, Object> params = new HashMap<>();
        params.put("mid", userId);
        params.put("wid", dataListBeans.get(position).id);
        params.put("type", type);
        params.put("duration", duration);
        mOkHttpHelper.post_json(getContext(), Url.PNWorks, params, new BaseCallback<ResultBean>() {
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
                PNWorkstype = "1";
                if (type.equals("1")){

                }else {
                    if (position + 1 == dataListBeans.size()) {
                        ToastUtil.show("已是最后一个");
                        return;
                    }
                    position = position + 1;
                    setData(dataListBeans.get(position));
                }
//                if (competitionCategoryId.equals("2") && competitionNumber.equals("1") && type.equals("1")) {
//
//                } else {
//
//                }


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
                PopupcommentList.get(position).liked = type;
                if (type.equals("1")) {
                    PopupcommentList.get(position).likedCount = (Integer.parseInt(PopupcommentList.get(position).likedCount) + 1) + "";
                } else {
                    PopupcommentList.get(position).likedCount = (Integer.parseInt(PopupcommentList.get(position).likedCount) - 1) + "";
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
                PopupcommentList.get(position).subCommentList.get(position1).liked = type;
                if (type.equals("1")) {
                    PopupcommentList.get(position).subCommentList.get(position1).likedCount = (Integer.parseInt(PopupcommentList.get(position).subCommentList.get(position1).likedCount) + 1) + "";
                } else {
                    PopupcommentList.get(position).subCommentList.get(position1).likedCount = (Integer.parseInt(PopupcommentList.get(position).subCommentList.get(position1).likedCount) - 1) + "";
                }
                commentAdapter.notifyDataSetChanged();


            }

            @Override
            public void onError(Response response, int code, Exception e) {
            }
        });
    }

    /**
     * 评分项列表
     */
    private void worksScoreItemList() {
        Map<String, Object> params = new HashMap<>();
        params.put("mid", userId);
        params.put("competitionCategoryId", competitionCategoryId);
        mOkHttpHelper.post_json(getContext(), Url.worksScoreItemList, params, new BaseCallback<ResultBean>() {
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
                PingfenBeans.clear();
                PingfenBeans.addAll(resultBean.dataList);
                pingFenAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Response response, int code, Exception e) {
            }
        });
    }

    public void setData(DataListBean data) {
        competitionCategoryId = data.competitionCategoryId;
        competitionNumber = data.competitionNumber;
        wid = data.id;
        tvtitle.setText(data.title);
        tvCollectCount.setText(data.collectCount);
        tvshareCount.setText(data.shareCount);
        tvnickname.setText(data.member.nickname);
        toMid = data.member.id;
        tvCommentName.setText(data.member.nickname);

        rlGuanggao.setVisibility(View.VISIBLE);

        Glide.with(getContext()).applyDefaultRequestOptions(new RequestOptions()
                .error(R.mipmap.touxiang)
                .placeholder(R.mipmap.touxiang))
                .load(data.member.avatar)
                .into(riIcon);
        if (data.collected.equals("1")) {
            imCollected.setImageResource(R.mipmap.yishoucang);
        } else {
            imCollected.setImageResource(R.mipmap.dianzan);
        }
        if (data.focused.equals("1")) {
            tvGuanzhu.setText("已关注");
        } else {
            tvGuanzhu.setText("+关注");
        }

        focused = data.focused;

        DataListBean dataListBean = new DataListBean();
        dataListBean.itemId = data.id;
        dataListBean.score = "5";
        pingfenlis.add(dataListBean);

        LinkedHashMap map = new LinkedHashMap();
        String proxyUrl = HcbApp.getProxy(mContext).getProxyUrl(data.video);
        map.put("高清", proxyUrl);
        JZDataSource jzDataSource = new JZDataSource(map, "");
        jzDataSource.looping = true;
        jzVideo.setUp(jzDataSource
                , JzvdStd.SCREEN_NORMAL);
        Glide.with(this).applyDefaultRequestOptions(new RequestOptions()
                .error(R.mipmap.imageerror)
                .placeholder(R.mipmap.imageerror))
                .load(data.video + AppConsts.ViDEOEND)
                .into(jzVideo.thumbImageView);

        fengmian = data.video + AppConsts.ViDEOEND;

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
                bundle.putString("video", data.video);
                ActivitySwitcher.startFragment(getActivity(), VideoFra.class, bundle);
                getActivity().overridePendingTransition(R.anim.anim_zoom_in, R.anim.anim_stay);
            }
        });

        List<String> list = new ArrayList<>();
        for (int i = 0; i < data.subVideos.size(); i++) {
            list.add(data.subVideos.get(i).title);
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
                String proxyUrl = HcbApp.getProxy(mContext).getProxyUrl(data.subVideos.get(position).video);
                map.put("高清", proxyUrl);
                JZDataSource jzDataSource = new JZDataSource(map, "");
                jzDataSource.looping = true;
                jzVideo.setUp(jzDataSource
                        , JzvdStd.SCREEN_NORMAL);
                jzVideo.startVideo();
                return true;
            }
        });

        commentCount = data.commentCount;
        if (data.competitionCategoryId.equals("2")) {//音乐类型
            timer.start();
            if (data.competitionNumber.equals("1")) {//第一轮
                tvPingfen.setVisibility(View.GONE);
                tvNg.setVisibility(View.GONE);
                tvPass.setVisibility(View.GONE);

                tvTishiyu.setText("向上滑动/向左滑动，通过/淘汰该作品");
            } else if (data.competitionNumber.equals("2")) {//第二轮
                tvPingfen.setVisibility(View.VISIBLE);
                tvNg.setVisibility(View.GONE);
                tvPass.setVisibility(View.GONE);
                tvTishiyu.setText("点击“评分”，给该作品进行打分");

                worksScoreItemList();
            }
        } else {
            CountDownTime = false;
            llTime.setVisibility(View.GONE);
            imVideo.setVisibility(View.GONE);
            tvTime.setVisibility(View.GONE);


            if (data.competitionNumber.equals("1")) {//第一轮
                tvPingfen.setVisibility(View.GONE);
                tvNg.setVisibility(View.VISIBLE);
                tvPass.setVisibility(View.VISIBLE);
                tvTishiyu.setText("点击PASS/NG,通过/淘汰该作品");
            } else if (data.competitionNumber.equals("2")) {
                tvPingfen.setVisibility(View.VISIBLE);
                tvNg.setVisibility(View.GONE);
                tvPass.setVisibility(View.GONE);
                tvTishiyu.setText("点击“评分”，给该作品进行打分");
                worksScoreItemList();
            }

        }


        jzVideo.startVideo();

        getAdvertising();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        JzvdStd.releaseAllVideos();//在销毁活动时，关闭饺子视频
        timer.cancel();
    }

    @Override
    public void onStop() {
        super.onStop();
        JzvdStd.releaseAllVideos();//在销毁活动时，关闭饺子视频
        timer.cancel();
    }

    @Override
    public void onResume() {
        super.onResume();
//        PNWorkstype = "0";
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


}
