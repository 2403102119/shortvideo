package com.lxkj.shortvideo.ui.activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.gyf.immersionbar.ImmersionBar;
import com.lxkj.shortvideo.GlobalBeans;
import com.lxkj.shortvideo.R;
import com.lxkj.shortvideo.biz.ActivityWatcher;
import com.lxkj.shortvideo.biz.EventCenter;


public class BaseFragAct extends AppCompatActivity {
    public GlobalBeans beans;
    public EventCenter eventCenter;
    protected ImmersionBar mImmersionBar;
    @Override
    protected void onCreate(Bundle arg0) {
        if (null == GlobalBeans.getSelf()) {
            GlobalBeans.initForMainUI(getApplication());
        }
        beans = GlobalBeans.getSelf();
        eventCenter = beans.getEventCenter();
        super.onCreate(arg0);
        initImmersionBar();
    }


    protected void initImmersionBar() {
        //在BaseActivity里初始化
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.transparentStatusBar();
        mImmersionBar.statusBarDarkFont(false);
        mImmersionBar.statusBarColor(R.color.main_color);
        mImmersionBar.autoDarkModeEnable(true);
        mImmersionBar.init();

    }



    @Override
    protected void onResume() {
        super.onResume();
        ActivityWatcher.setCurAct(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        ActivityWatcher.setCurAct(null);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
