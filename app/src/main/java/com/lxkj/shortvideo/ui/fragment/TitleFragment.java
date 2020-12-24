package com.lxkj.shortvideo.ui.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.gyf.immersionbar.ImmersionBar;
import com.gyf.immersionbar.components.ImmersionFragment;
import com.lxkj.shortvideo.AppConsts;
import com.lxkj.shortvideo.GlobalBeans;
import com.lxkj.shortvideo.R;
import com.lxkj.shortvideo.biz.EventCenter;
import com.lxkj.shortvideo.http.OkHttpHelper;
import com.lxkj.shortvideo.ui.activity.NaviActivity;
import com.lxkj.shortvideo.utils.KeyboardUtil;
import com.lxkj.shortvideo.utils.ScreenUtil;
import com.lxkj.shortvideo.utils.SharePrefUtil;


public abstract class TitleFragment extends ImmersionFragment implements EventCenter.EventListener {
    protected final GlobalBeans beans;
    protected NaviActivity act;
    protected int screenWidth;//屏幕宽度

    protected View rootView;
    public OkHttpHelper mOkHttpHelper;
    public Context mContext;
    public String userId,cityId,lat,lng,userPhone;
    public EventCenter eventCenter;
    public TitleFragment() {
        beans = GlobalBeans.getSelf();
        screenWidth = ScreenUtil.getScreenWidth(getContext());
        mOkHttpHelper = OkHttpHelper.getInstance();
        eventCenter = beans.getEventCenter();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            mContext = act;
            eventCenter.registEvent(this, EventCenter.EventType.EVT_LOGIN);
            eventCenter.registEvent(this, EventCenter.EventType.EVT_LOGOUT);
            act.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }catch (Exception e){

        }
    }

    @Override
    public void initImmersionBar() {
        ImmersionBar.with(this).transparentStatusBar().statusBarDarkFont(false).statusBarColor(R.color.white).autoDarkModeEnable(true).init();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
//        if (null == act) {
//            act = (NaviActivity) activity;
//        }
        mContext = act;
        userPhone = SharePrefUtil.getString(mContext, AppConsts.PHONE,null);
        userId = SharePrefUtil.getString(mContext,AppConsts.UID,null);
        cityId = SharePrefUtil.getString(getContext(),AppConsts.CURRENTCITYID,AppConsts.DEFAULTCITYID);
        lat = SharePrefUtil.getString(getContext(), AppConsts.LAT,AppConsts.DEFAULTLAT);
        lng = SharePrefUtil.getString(getContext(), AppConsts.LNG,AppConsts.DEFAULTLNG);

    }

    @Override
    public void onDetach() {
        act = null;
        super.onDetach();
    }

    public void setActivity(NaviActivity act) {
        this.act = act;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("onActivityResult","onActivityResult");
    }

    public int getTitleId() {
        return 0;
    }

    public String getTitleName() {
        return null;
    }

    public boolean hideLeftArrow() {
        return false;
    }

    protected boolean isAlive() {
        return null != act && !this.isDetached() && !act.isFinishing();
    }

    protected void hideKeyboard() {
        KeyboardUtil.hideKeyboard(act);
    }

    ProgressDialog dialog;

    protected void showProgressDialog(final String title, final String msg) {
        if (null == dialog) {
            dialog = ProgressDialog.show(act, title, msg);
        } else {
            dialog.setTitle(title);
            dialog.setMessage(msg);
            dialog.show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        eventCenter.unregistEvent(this, EventCenter.EventType.EVT_LOGIN);
        eventCenter.unregistEvent(this, EventCenter.EventType.EVT_LOGOUT);
    }

    @Override
    public void onEvent(EventCenter.HcbEvent e) {
        switch (e.type){
            case EVT_LOGIN:
                userId = SharePrefUtil.getString(mContext,AppConsts.UID,null);
                break;
            case EVT_LOGOUT:
                act.finish();
                userId = SharePrefUtil.getString(mContext,AppConsts.UID,null);
                break;
        }
    }

}
