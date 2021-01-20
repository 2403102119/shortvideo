package com.lxkj.shortvideo.ui.fragment.competition;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.lxkj.shortvideo.utils.AnimationUtilUP;
import com.lxkj.shortvideo.utils.PicassoUtil;
import com.lxkj.shortvideo.utils.StringUtil;
import com.lxkj.shortvideo.utils.ToastUtil;
import com.lzy.ninegrid.ImageInfo;
import com.makeramen.roundedimageview.RoundedImageView;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
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
public class EventDetailsFra extends TitleFragment implements View.OnClickListener {
    Unbinder unbinder;
    @BindView(R.id.navi_title)
    TextView naviTitle;
    @BindView(R.id.navi_left)
    ImageView naviLeft;
    @BindView(R.id.navi_right_txt)
    TextView naviRightTxt;
    @BindView(R.id.navi_right_img)
    ImageView naviRightImg;
    @BindView(R.id.relBackGroundView)
    RelativeLayout relBackGroundView;
    @BindView(R.id.tvGuanzhu)
    TextView tvGuanzhu;
    @BindView(R.id.rycomment)
    RecyclerView rycomment;
    @BindView(R.id.riIcon)
    RoundedImageView riIcon;
    @BindView(R.id.llRank)
    LinearLayout llRank;
    @BindView(R.id.tvLookDetail)
    TextView tvLookDetail;
    @BindView(R.id.tvTime)
    TextView tvTime;
    @BindView(R.id.tvTime2)
    TextView tvTime2;
    @BindView(R.id.imVideo)
    ImageView imVideo;
    @BindView(R.id.llTime)
    LinearLayout llTime;
    @BindView(R.id.imComment)
    ImageView imComment;
    @BindView(R.id.llComment)
    LinearLayout llComment;
    @BindView(R.id.ns)
    NestedScrollView ns;
    @BindView(R.id.llRecycle)
    LinearLayout llRecycle;
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.tvtitle)
    TextView tvtitle;
    @BindView(R.id.tvnickname)
    TextView tvnickname;
    @BindView(R.id.riCommentIcon)
    RoundedImageView riCommentIcon;
    @BindView(R.id.tvCommentName)
    TextView tvCommentName;
    @BindView(R.id.tvCollectCount)
    TextView tvCollectCount;
    @BindView(R.id.tvshareCount)
    TextView tvshareCount;
    @BindView(R.id.imCollected)
    ImageView imCollected;
    @BindView(R.id.tvPass)
    TextView tvPass;
    @BindView(R.id.tvNg)
    TextView tvNg;
    @BindView(R.id.jzVideo)
    JzvdStd jzVideo;
    @BindView(R.id.tagFlow)
    TagFlowLayout tagFlow;
    @BindView(R.id.tvcommentCount)
    TextView tvcommentCount;
    @BindView(R.id.imretreat)
    ImageView imretreat;
    @BindView(R.id.imadvance)
    ImageView imadvance;
    @BindView(R.id.tvPinglun)
    TextView tvPinglun;
    @BindView(R.id.etPinglun)
    EditText etPinglun;
    @BindView(R.id.llUser)
    LinearLayout llUser;
    @BindView(R.id.imfenxiang)
    ImageView imfenxiang;

    private ArrayList<DataListBean> listBeans;
    private int page = 1, totalPage = 1;
    private CommentAdapter commentAdapter;
    private boolean CountDownTime = false;
    private String id, title, toMid, focused, pcid = "", entered;
    private List<String> BanString = new ArrayList<>();
    private List<DataListBean> dataListBeans = new ArrayList<>();
    private ArrayList<ImageInfo> imageInfo = new ArrayList<>();
    private int position = 0;
    private TagAdapter<String> adapter;

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


        listBeans = new ArrayList<DataListBean>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rycomment.setLayoutManager(layoutManager);
        commentAdapter = new CommentAdapter(getContext(), listBeans);
        rycomment.setAdapter(commentAdapter);
        commentAdapter.setOnItemClickListener(new CommentAdapter.OnItemClickListener() {
            @Override
            public void OnItemClickListener(int firstPosition) {
                pcid = listBeans.get(firstPosition).id;

                tvCommentName.setText(listBeans.get(firstPosition).member.nickname);

                Glide.with(getContext()).applyDefaultRequestOptions(new RequestOptions()
                        .error(R.mipmap.touxiang)
                        .placeholder(R.mipmap.touxiang))
                        .load(listBeans.get(firstPosition).member.avatar)
                        .into(riCommentIcon);
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

        ns.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (CountDownTime) {
                    llTime.setVisibility(View.GONE);
                    imVideo.setVisibility(View.GONE);
                    llComment.setVisibility(View.GONE);
                    imComment.setVisibility(View.GONE);


//                    llRecycle.setVisibility(View.VISIBLE);
                    mHandlercomment.postDelayed(comment, 1000);//延时10秒



                    tvPass.setEnabled(true);
                    tvNg.setEnabled(true);
                    tvPinglun.setEnabled(true);

                }

            }
        });

        timer.start();

        competitionDetail();
        competitionWorksList();

        mHandlergift.postDelayed(gift, 2000);//延时10秒


    }

    final Handler mHandlergift = new Handler();
    Runnable gift = new Runnable() {
        @Override
        public void run() {
            if (null!=llUser){
                llUser.setVisibility(View.VISIBLE);
                // 向右边移入
                llUser.setAnimation(AnimationUtils.makeInAnimation(getActivity(), true));
            }
        }
    };
    final Handler mHandlercomment = new Handler();
    Runnable comment = new Runnable() {
        @Override
        public void run() {
            if (null!=llUser){
                llRecycle.setVisibility(View.VISIBLE);
                // 向上移入
                llRecycle.setAnimation(AnimationUtilUP.moveToViewLocation());
            }
        }
    };

    public CountDownTimer timer = new CountDownTimer(10000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            if (null != tvTime) {
                tvTime.setText("倒计时" + (millisUntilFinished / 1000) + "s");
            }
            if (null != tvTime2) {
                tvTime2.setText("倒计时" + (millisUntilFinished / 1000) + "s");
            }
            CountDownTime = false;
            tvPass.setEnabled(false);
            tvNg.setEnabled(false);
            tvPinglun.setEnabled(false);
            llTime.setVisibility(View.VISIBLE);
            imVideo.setVisibility(View.VISIBLE);
            llComment.setVisibility(View.VISIBLE);
            imComment.setVisibility(View.VISIBLE);
            tvTime.setVisibility(View.VISIBLE);
            tvTime2.setVisibility(View.VISIBLE);

            llRecycle.setVisibility(View.GONE);
        }

        @Override
        public void onFinish() {
            tvTime.setVisibility(View.GONE);
            tvTime2.setVisibility(View.GONE);
            jzVideo.startVideo();
            CountDownTime = true;
        }
    };

    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        switch (v.getId()) {
            case R.id.riIcon://用户主页
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
                bundle.putString("cid", id);
                bundle.putString("entered", entered);
                ActivitySwitcher.startFragment(getActivity(), LookDetailFra.class, bundle);
                break;
            case R.id.navi_right_txt://报名
                bundle.putString("cid", id);
                ActivitySwitcher.startFragment(getActivity(), ApplyFra.class, bundle);
                break;
            case R.id.imCollected://收藏
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
                position = position - 1;
                setData(dataListBeans.get(position));
                timer.start();
                break;
            case R.id.imadvance://下一个
                if (position == dataListBeans.size()) {
                    ToastUtil.show("已是最后一个");
                    return;
                }
                position = position + 1;
                setData(dataListBeans.get(position));
                timer.start();
                break;
            case R.id.tvGuanzhu://关注
                if (focused.equals("1")) {
                    focused = "0";
                    focusMember("0");
                } else {
                    focused = "1";
                    focusMember("1");
                }
                break;
            case R.id.tvPinglun://评论
                if (StringUtil.isEmpty(etPinglun.getText().toString())) {
                    ToastUtil.show("请输入评论内容");
                    return;
                }
                pubWorksComment(etPinglun.getText().toString());
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
                PNWorks("1", StringUtil.formatTurnSecond(jzVideo.currentTimeTextView.getText().toString()) + "");
                break;
            case R.id.tvNg://NG
                PNWorks("2", StringUtil.formatTurnSecond(jzVideo.currentTimeTextView.getText().toString()) + "");
                break;
            case R.id.imfenxiang:
                shareWorks(id);
                break;

        }
    }
    /**
     * 作品分享
     */
    private void shareWorks(String wid) {
        Map<String, Object> params = new HashMap<>();
        params.put("mid", userId);
        params.put("wid",wid);
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
                new ShareFra().show(act.getSupportFragmentManager(), "Menu");
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
                if (entered.equals("1")) {
                    naviRightTxt.setText("已参赛");
                    naviRightTxt.setEnabled(true);
                } else {
                    naviRightTxt.setText("报名");
                    naviRightTxt.setEnabled(true);
                }
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
                listBeans.clear();
                listBeans.addAll(resultBean.dataList);
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
                        .load(resultBean.advertising.get(1).image)
                        .into(imComment);

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
                etPinglun.setText("");
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
                if (position > dataListBeans.size()) {
                    ToastUtil.show("已是最后一个");
                    return;
                }
                position = position + 1;
                setData(dataListBeans.get(position));
                timer.start();
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
                adapter.notifyDataChanged();
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
                adapter.notifyDataChanged();
            }

            @Override
            public void onError(Response response, int code, Exception e) {
            }
        });
    }

    public void setData(DataListBean data) {
        tvtitle.setText(data.title);
        tvCollectCount.setText(data.collectCount);
        tvshareCount.setText(data.shareCount);
        tvnickname.setText(data.member.nickname);
        toMid = data.member.id;
        tvCommentName.setText(data.member.nickname);
        Glide.with(getContext()).applyDefaultRequestOptions(new RequestOptions()
                .error(R.mipmap.touxiang)
                .placeholder(R.mipmap.touxiang))
                .load(data.member.avatar)
                .into(riIcon);
        Glide.with(getContext()).applyDefaultRequestOptions(new RequestOptions()
                .error(R.mipmap.touxiang)
                .placeholder(R.mipmap.touxiang))
                .load(data.member.avatar)
                .into(riCommentIcon);
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
                .load(data.video+ AppConsts.ViDEOEND)
                .into(jzVideo.thumbImageView);

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
        jzVideo.fullscreenButton.setVisibility(View.GONE);
        jzVideo.currentTimeTextView.setVisibility(View.GONE);
        jzVideo.thumbImageView.setVisibility(View.VISIBLE);

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

        tvcommentCount.setText("共计" + data.commentCount + "条评论");

        worksCommentList();
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
        timer.start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
