package com.lxkj.shortvideo.ui.fragment.competition;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
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
    @BindView(R.id.riIcon)
    RoundedImageView riIcon;
    @BindView(R.id.tvGuanzhu)
    TextView tvGuanzhu;
    @BindView(R.id.rycomment)
    RecyclerView rycomment;
    @BindView(R.id.jzVideo)
    JzvdStd jzVideo;
    @BindView(R.id.imShoucang)
    ImageView imShoucang;
    @BindView(R.id.tvShoucang)
    TextView tvShoucang;
    @BindView(R.id.tvFenxiang)
    TextView tvFenxiang;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tagFlow)
    TagFlowLayout tagFlow;
    @BindView(R.id.tvPinglun)
    TextView tvPinglun;
    @BindView(R.id.riCommentIcon)
    RoundedImageView riCommentIcon;
    @BindView(R.id.tvCommentName)
    TextView tvCommentName;
    @BindView(R.id.etComment)
    EditText etComment;
    @BindView(R.id.tvSent)
    TextView tvSent;
    @BindView(R.id.llDetail)
    LinearLayout llDetail;
    @BindView(R.id.tvVideoTitle)
    TextView tvVideoTitle;
    @BindView(R.id.smart)
    SmartRefreshLayout smart;
    @BindView(R.id.imfenxiang)
    ImageView imfenxiang;

    private ArrayList<DataListBean> listBeans;
    private int page = 1, totalPage = 1;
    private CommentAdapter commentAdapter;
    private String wid, pcid, focused, toMid;
    private int position = 0;
    private TagAdapter<String> adapter;
    private String collected,commentCount;

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


        tvSent.setOnClickListener(this);
        llDetail.setOnClickListener(this);
        imShoucang.setOnClickListener(this);
        tvGuanzhu.setOnClickListener(this);
        tvGuanzhu.setOnClickListener(this);
        imfenxiang.setOnClickListener(this);



        competitionWorksDetail();
        worksCommentList();
    }
    private ObjectAnimator objectAnimatorX;

    // 属性动画-平移
    private void startPopsAnimTrans(){
        if(objectAnimatorX == null){
            float [] x= {0f,60f,120f,180f};
            objectAnimatorX = ObjectAnimator.ofFloat(llDetail,"translationX", x);
            objectAnimatorX.setDuration(2000);
        }
        objectAnimatorX.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvSent:
                if (StringUtil.isEmpty(etComment.getText().toString())) {
                    ToastUtil.show("请输入评论内容");
                    return;
                }
                pubWorksComment(etComment.getText().toString());
                break;
            case R.id.llDetail:
                Bundle bundle = new Bundle();
                bundle.putString("toMid", toMid);
                ActivitySwitcher.startFragment(getActivity(), UserHomeFra.class, bundle);
                break;
            case R.id.imShoucang://收藏
                if (collected.equals("1")) {
                    collected = "0";
                    collectWorks("0");
                } else {
                    collected = "1";
                    collectWorks("1");
                }
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
            case R.id.imfenxiang:
                shareWorks(wid);
                break;
        }
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
                tvVideoTitle.setText(resultBean.title);
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
                Glide.with(getContext()).applyDefaultRequestOptions(new RequestOptions()
                        .error(R.mipmap.touxiang)
                        .placeholder(R.mipmap.touxiang))
                        .load(resultBean.member.avatar)
                        .into(riCommentIcon);
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
                        .load(resultBean.coverImage)
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

                tvPinglun.setText("共计" + resultBean.commentCount + "条评论");
                commentCount =  resultBean.commentCount;
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
                etComment.setText("");
                tvPinglun.setText("共计" +(Integer.parseInt(commentCount)+1)+"条评论");
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
//                if (listBeans.size() == 0) {
//                    llNoData.setVisibility(View.VISIBLE);
//                    recyclerView.setVisibility(View.GONE);
//                } else {
//                    recyclerView.setVisibility(View.VISIBLE);
//                    llNoData.setVisibility(View.GONE);
//                }
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
