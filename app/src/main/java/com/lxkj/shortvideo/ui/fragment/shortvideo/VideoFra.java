package com.lxkj.shortvideo.ui.fragment.shortvideo;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.gyf.immersionbar.ImmersionBar;
import com.lxkj.shortvideo.AppConsts;
import com.lxkj.shortvideo.HcbApp;
import com.lxkj.shortvideo.R;
import com.lxkj.shortvideo.ui.fragment.TitleFragment;

import java.util.LinkedHashMap;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.jzvd.JZDataSource;
import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;

/**
 * Time:2021/1/20
 * <p>
 * Author:李迪迦
 * <p>
 * Interface:全屏播放
 */
public class VideoFra extends TitleFragment {
    Unbinder unbinder;
    @BindView(R.id.videoplayer)
    JzvdStd videoplayer;
    @BindView(R.id.ivBack)
    ImageView ivBack;
    private ImmersionBar mImmersionBar;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        rootView = inflater.inflate(R.layout.fra_video, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        act.hindNaviBar();
        initView();
        return rootView;
    }

    public void initView() {
        String video = getArguments().getString("video");


//        LinkedHashMap map = new LinkedHashMap();
//        JZDataSource jzDataSource = new JZDataSource(video, "");
//        jzDataSource.looping = true;
//        videoplayer.setUp(jzDataSource
//                , JzvdStd.SCREEN_TINY);
//



        LinkedHashMap map = new LinkedHashMap();
        String proxyUrl = HcbApp.getProxy(mContext).getProxyUrl(video);
        map.put("高清", proxyUrl);
        JZDataSource jzDataSource = new JZDataSource(map, "");
        jzDataSource.looping = true;
        videoplayer.setUp(jzDataSource
                , JzvdStd.SCREEN_NORMAL);
        Glide.with(this).load(video+ AppConsts.ViDEOEND).into(videoplayer.thumbImageView);
        videoplayer.startVideo();

        //全屏
//        videoplayer.setVideoImageDisplayType(JzvdStd.VIDEO_IMAGE_DISPLAY_TYPE_FILL_PARENT);
        //设置全屏播放
        videoplayer.FULLSCREEN_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;  //横向
        videoplayer.NORMAL_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT;  //纵向


        videoplayer.titleTextView.setVisibility(View.GONE);
        videoplayer.replayTextView.setVisibility(View.GONE);
        videoplayer.backButton.setVisibility(View.GONE);
        videoplayer.batteryTimeLayout.setVisibility(View.GONE);
        videoplayer.tinyBackImageView.setVisibility(View.GONE);
        videoplayer.videoCurrentTime.setVisibility(View.GONE);
        videoplayer.clarity.setVisibility(View.GONE);
        videoplayer.mRetryLayout.setVisibility(View.GONE);
        videoplayer.mRetryBtn.setVisibility(View.GONE);
        videoplayer.clarity.setVisibility(View.GONE);
        videoplayer.fullscreenButton.setVisibility(View.GONE);
        videoplayer.currentTimeTextView.setVisibility(View.GONE);
        videoplayer.bottomProgressBar.setVisibility(View.GONE);
        videoplayer.totalTimeTextView.setVisibility(View.GONE);
        videoplayer.progressBar.setVisibility(View.GONE);

        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.statusBarColor(R.color.main_color);
        mImmersionBar.transparentStatusBar();
        mImmersionBar.statusBarDarkFont(false,0.2f);
        mImmersionBar.init();

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               act.finishSelf();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Jzvd.releaseAllVideos();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
