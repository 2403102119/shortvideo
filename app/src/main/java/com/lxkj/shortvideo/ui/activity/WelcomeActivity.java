package com.lxkj.shortvideo.ui.activity;

import android.Manifest;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.gyf.immersionbar.ImmersionBar;
import com.lxkj.shortvideo.AppConsts;
import com.lxkj.shortvideo.GlobalBeans;
import com.lxkj.shortvideo.R;
import com.lxkj.shortvideo.bean.ResultBean;
import com.lxkj.shortvideo.biz.ActivitySwitcher;
import com.lxkj.shortvideo.http.BaseCallback;
import com.lxkj.shortvideo.http.OkHttpHelper;
import com.lxkj.shortvideo.http.Url;
import com.lxkj.shortvideo.ui.fragment.login.LoginFra;
import com.lxkj.shortvideo.utils.SharePrefUtil;
import com.lxkj.shortvideo.utils.StringUtil;
import com.lxkj.shortvideo.utils.ToastUtil;
import com.lxkj.shortvideo.view.AgreementDialog;
import com.zhy.m.permission.MPermissions;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Request;
import okhttp3.Response;

public class WelcomeActivity extends BaseFragAct {
    private final static int SECOND = 1000;
    private int downCount = 1;
    private GlobalBeans beans;
    private Handler uiHandler;
    private Context context;
    AgreementDialog agreementDialog;
    private boolean isAgree = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        context = this;
        GlobalBeans.initForMainUI(getApplication());
        beans = GlobalBeans.getSelf();
        uiHandler = beans.getHandler();

        isAgree = SharePrefUtil.getBoolean(context, AppConsts.ISAGREE, false);

        agreementDialog = new AgreementDialog(context, new AgreementDialog.onRightClickListener() {
            @Override
            public void onRightClickListener() {
                uiHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        new Timer().scheduleAtFixedRate(timerTask, 0, SECOND);
                    }
                }, 400);
                SharePrefUtil.saveBoolean(context, AppConsts.ISAGREE, true);
            }

            @Override
            public void onLeftClickListener() {
                onExit();
            }
        });

        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.transparentStatusBar();
        mImmersionBar.init();

        mLocationClient = new AMapLocationClient(this);
        mLocationClient.setLocationListener(mLocationListener);
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Battery_Saving，低功耗模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
        //获取一次定位结果：
        //该方法默认为false。
        mLocationOption.setOnceLocation(false);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            MPermissions.requestPermissions(this, AppConsts.PMS_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
            );
        } else {
            pmsLocationSuccess();
        }


        AppConsts.city = SharePrefUtil.getString(context, AppConsts.CITY, "");
//        if (!StringUtil.isEmpty(SharePrefUtil.getString(context, AppConsts.UID, "")))
//            getUserInfo();
    }





    /**
     * 定位监听
     */
    //声明定位回调监听器
    AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            if (amapLocation != null) {
                if (amapLocation.getErrorCode() == 0) {
                    Log.e("onLocationChanged", amapLocation.getCity());
                    AppConsts.city = amapLocation.getCity();
                    SharePrefUtil.saveString(context, AppConsts.LAT, amapLocation.getLatitude() + "");
                    SharePrefUtil.saveString(context, AppConsts.LNG, amapLocation.getLongitude() + "");
                    SharePrefUtil.saveString(context, AppConsts.CITY, amapLocation.getCity());
                    SharePrefUtil.saveString(context, AppConsts.ADDRESS, amapLocation.getAddress());
                }
            }
        }
    };


    AMapLocationClient mLocationClient = null;
    AMapLocationClientOption mLocationOption = null;


    @PermissionGrant(AppConsts.PMS_LOCATION)
    public void pmsLocationSuccess() {
        //权限授权成功
        mLocationClient.startLocation();
        if (!isAgree)
            agreementDialog.show();
        else {
            uiHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    new Timer().scheduleAtFixedRate(timerTask, 0, SECOND);
                }
            }, 400);
        }
    }


    @PermissionDenied(AppConsts.PMS_LOCATION)
    public void pmsLocationError() {
        if (!isAgree)
            agreementDialog.show();
        else {
            uiHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    new Timer().scheduleAtFixedRate(timerTask, 0, SECOND);
                }
            }, 400);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        MPermissions.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    private final TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            if (downCount > 0) {
                downCount--;
            } else {
                enterApp();
                this.cancel();
            }
        }
    };


    private void enterApp() {
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
//                if (!StringUtil.isEmpty(SharePrefUtil.getString(context, AppConsts.UID, "")))
                    ActivitySwitcher.start(WelcomeActivity.this, MainActivity.class);
//                else
//                    ActivitySwitcher.startFragment(WelcomeActivity.this, LoginFra.class);
                finish();
            }
        });
    }


    private long backPressTime = 0;

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        mLocationClient.stop();
    }

    @Override
    public void onBackPressed() {
        final long uptimeMillis = SystemClock.uptimeMillis();
        if (uptimeMillis - backPressTime > 2 * SECOND) {
            backPressTime = uptimeMillis;
            ToastUtil.show(getString(R.string.press_again_to_leave));
        } else {
            onExit();
        }
    }

    private void onExit() {
        timerTask.cancel();
        finish();
        if (null != beans) {
            beans.onTerminate();
        }
    }


}