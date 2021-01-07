package com.lxkj.shortvideo.ui.fragment.competition;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lxkj.shortvideo.R;
import com.lxkj.shortvideo.adapter.CommentAdapter;
import com.lxkj.shortvideo.bean.DataListBean;
import com.lxkj.shortvideo.biz.ActivitySwitcher;
import com.lxkj.shortvideo.ui.fragment.TitleFragment;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

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
    private ArrayList<DataListBean> listBeans;
    private int page = 1, totalPage = 1;
    private CommentAdapter commentAdapter;
    private boolean CountDownTime = false;


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
        listBeans = new ArrayList<DataListBean>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rycomment.setLayoutManager(layoutManager);
        commentAdapter = new CommentAdapter(getContext(), listBeans);
        rycomment.setAdapter(commentAdapter);
        commentAdapter.setOnItemClickListener(new CommentAdapter.OnItemClickListener() {
            @Override
            public void OnItemClickListener(int firstPosition) {

            }
        });


        riIcon.setOnClickListener(this);
        llRank.setOnClickListener(this);
        naviLeft.setOnClickListener(this);
        tvLookDetail.setOnClickListener(this);
        naviRightTxt.setOnClickListener(this);

        ns.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (CountDownTime) {
                    llTime.setVisibility(View.GONE);
                    imVideo.setVisibility(View.GONE);
                    llComment.setVisibility(View.GONE);
                    imComment.setVisibility(View.GONE);
                    llRecycle.setVisibility(View.VISIBLE);
                }

            }
        });

        timer.start();

    }

    public CountDownTimer timer = new CountDownTimer(10000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            tvTime.setText("倒计时" + (millisUntilFinished / 1000) + "s");
            tvTime2.setText("倒计时" + (millisUntilFinished / 1000) + "s");
            CountDownTime = false;
        }

        @Override
        public void onFinish() {
            CountDownTime = true;
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.riIcon://用户主页
                ActivitySwitcher.startFragment(getActivity(), UserHomeFra.class);
                break;
            case R.id.llRank://排行榜
                ActivitySwitcher.startFragment(getActivity(), RankFra.class);
                break;
            case R.id.navi_left:
                act.finishSelf();
                break;
            case R.id.tvLookDetail://查看详情
                ActivitySwitcher.startFragment(getActivity(), LookDetailFra.class);
                break;
            case R.id.navi_right_txt://报名
                ActivitySwitcher.startFragment(getActivity(), ApplyFra.class);
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
