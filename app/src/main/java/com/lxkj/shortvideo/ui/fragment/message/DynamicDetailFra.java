package com.lxkj.shortvideo.ui.fragment.message;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.lxkj.shortvideo.HcbApp;
import com.lxkj.shortvideo.R;
import com.lxkj.shortvideo.adapter.CommentAdapter;
import com.lxkj.shortvideo.adapter.Recycle_one_itemAdapter;
import com.lxkj.shortvideo.bean.DataListBean;
import com.lxkj.shortvideo.bean.ResultBean;
import com.lxkj.shortvideo.http.BaseCallback;
import com.lxkj.shortvideo.http.Url;
import com.lxkj.shortvideo.ui.fragment.TitleFragment;
import com.lxkj.shortvideo.utils.StringUtil;
import com.lxkj.shortvideo.utils.ToastUtil;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.jzvd.JZDataSource;
import cn.jzvd.JzvdStd;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Time:2021/1/12
 * <p>
 * Author:李迪迦
 * <p>
 * Interface:动态评论
 */
public class DynamicDetailFra extends TitleFragment implements View.OnClickListener {
    Unbinder unbinder;
    @BindView(R.id.riIcon)
    RoundedImageView riIcon;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvTime)
    TextView tvTime;
    @BindView(R.id.tvGuanzhu)
    TextView tvGuanzhu;
    @BindView(R.id.tvConten)
    TextView tvConten;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.tvPinglun)
    TextView tvPinglun;
    @BindView(R.id.rycomment)
    RecyclerView rycomment;
    @BindView(R.id.riCommentIcon)
    RoundedImageView riCommentIcon;
    @BindView(R.id.tvCommentName)
    TextView tvCommentName;
    @BindView(R.id.etComment)
    EditText etComment;
    @BindView(R.id.tvSent)
    TextView tvSent;
    private ArrayList<DataListBean> listBeans;
    private ArrayList<String> images = new ArrayList<>();
    private int page = 1, totalPage = 1;
    private CommentAdapter commentAdapter;
    private String wid, pcid, focused, toMid;
    private int position = 0;
    private TagAdapter<String> adapter;
    private String fmid;
    private Recycle_one_itemAdapter recycletwoItemAdapter;

    @Override
    public String getTitleName() {
        return "动态评论";
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        rootView = inflater.inflate(R.layout.fra_dynamicdetail, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        initView();
        return rootView;
    }

    public void initView() {
        fmid = getArguments().getString("fmid");

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



        StaggeredGridLayoutManager layoutManager1 = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager1);
        recycletwoItemAdapter=new Recycle_one_itemAdapter(getContext(),images);
        recyclerView.setAdapter(recycletwoItemAdapter);
        recycletwoItemAdapter.setOnItemClickListener(new Recycle_one_itemAdapter.OnItemClickListener() {
            @Override
            public void OnItemClickListener(int firstPosition) {
//                onItemClickListener.Onchakandatu(firstPosition,position);
            }
        });


        friendMomentsDetail();

        tvSent.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvSent://发表评论
                if (StringUtil.isEmpty(etComment.getText().toString())) {
                    ToastUtil.show("请输入评论内容");
                    return;
                }
                pubWorksComment(etComment.getText().toString());
                break;
        }
    }


    /**
     * 动态详情
     */
    private void friendMomentsDetail() {
        Map<String, Object> params = new HashMap<>();
        params.put("mid", userId);
        params.put("fmid", fmid);
        mOkHttpHelper.post_json(getContext(), Url.friendMomentsDetail, params, new BaseCallback<ResultBean>() {
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

                Glide.with(getActivity()).applyDefaultRequestOptions(new RequestOptions()
                        .error(R.mipmap.imageerror)
                        .placeholder(R.mipmap.imageerror))
                        .load(resultBean.member.avatar)
                        .into(riIcon);
                toMid = resultBean.member.id;
                wid = resultBean.id;
                tvCommentName.setText(resultBean.member.nickname);
                Glide.with(getContext()).applyDefaultRequestOptions(new RequestOptions()
                        .error(R.mipmap.touxiang)
                        .placeholder(R.mipmap.touxiang))
                        .load(resultBean.member.avatar)
                        .into(riCommentIcon);
                tvTitle.setText(resultBean.member.nickname);
                tvTime.setText(resultBean.createDate);
                tvConten.setText(resultBean.content);

                if (resultBean.focused.equals("1")){
                    if (resultBean.beFocused.equals("1")){
                        tvGuanzhu.setText("互相关注");
                    }else {
                        tvGuanzhu.setText("已关注");
                    }
                }else {
                    tvGuanzhu.setText("+关注");
                }


                tvPinglun.setText("共计" + resultBean.commentCount + "条评论");

                images.clear();
                images.addAll(resultBean.images);
                recycletwoItemAdapter.notifyDataSetChanged();

                friendMomentsCommentList();

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
        params.put("wid", toMid);
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
                friendMomentsCommentList();
            }

            @Override
            public void onError(Response response, int code, Exception e) {
            }
        });
    }

    /**
     * 作品评论列表
     */
    private void friendMomentsCommentList() {
        Map<String, Object> params = new HashMap<>();
        params.put("mid", userId);
        params.put("wid", wid);
        mOkHttpHelper.post_json(getContext(), Url.friendMomentsCommentList, params, new BaseCallback<ResultBean>() {
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
     * 评论点赞/取消点赞
     */
    private void likeWorksComment(String cid, String type, int position) {
        Map<String, Object> params = new HashMap<>();
        params.put("mid", userId);
        params.put("cid", cid);
        params.put("type", type);
        mOkHttpHelper.post_json(getContext(), Url.likeMomentsComment, params, new BaseCallback<ResultBean>() {
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
        mOkHttpHelper.post_json(getContext(), Url.likeMomentsComment, params, new BaseCallback<ResultBean>() {
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
