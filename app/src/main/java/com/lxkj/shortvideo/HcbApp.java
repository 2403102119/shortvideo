package com.lxkj.shortvideo;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.StrictMode;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.danikula.cachevideo.HttpProxyCacheServer;
import com.lxkj.shortvideo.biz.CrashHandler;
import com.lxkj.shortvideo.imageloader.PicassoImageLoader;
import com.lxkj.shortvideo.utils.SharePrefUtil;
import com.lzy.ninegrid.NineGridView;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import cn.jpush.android.api.JPushInterface;


public class HcbApp extends MultiDexApplication {

    public static Application self;
    private static final String TAG = "HcbApp";


    @Override
    public void onCreate() {
        super.onCreate();
        self = this;
        MultiDex.install(this);

        //极光
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);

        //设置LOG开关，默认为false
        UMConfigure.setLogEnabled(true);
        //初始化组件化基础库, 统计SDK/推送SDK/分享SDK都必须调用此初始化接口
        UMConfigure.init(this, "5f59eb6ca4ae0a7f7d02c4ef", "Umeng", UMConfigure.DEVICE_TYPE_PHONE,
                "");

        PlatformConfig.setWeixin(AppConsts.WXAPPID, AppConsts.WXAPPSECRET);
        PlatformConfig.setQQZone(AppConsts.QQAPPID, AppConsts.QQAPPKEY);

        AppConsts.userId = SharePrefUtil.getString(this, AppConsts.UID, "");

//        //百度地图初始化
//        SDKInitializer.initialize(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }

        CrashHandler.getInstance().init(this);

        //九宫格图片初始化
        NineGridView.setImageLoader(new PicassoImageLoader());

        closeAndroidPDialog();




    }





    public static Application getContext() {
        return self;
    }


    /**
     * 获得当前进程的名字
     *
     * @param context
     * @return 进程号
     */
    public static String getCurProcessName(Context context) {

        int pid = android.os.Process.myPid();

        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
                .getRunningAppProcesses()) {

            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

    private void closeAndroidPDialog() {
        try {
            Class aClass = Class.forName("android.content.pm.PackageParser$Package");
            Constructor declaredConstructor = aClass.getDeclaredConstructor(String.class);
            declaredConstructor.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Class cls = Class.forName("android.app.ActivityThread");
            Method declaredMethod = cls.getDeclaredMethod("currentActivityThread");
            declaredMethod.setAccessible(true);
            Object activityThread = declaredMethod.invoke(null);
            Field mHiddenApiWarningShown = cls.getDeclaredField("mHiddenApiWarningShown");
            mHiddenApiWarningShown.setAccessible(true);
            mHiddenApiWarningShown.setBoolean(activityThread, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private HttpProxyCacheServer proxy;

    public static HttpProxyCacheServer getProxy(Context context) {
        HcbApp app = (HcbApp) context.getApplicationContext();
        return app.proxy == null ? (app.proxy = app.newProxy()) : app.proxy;
    }

    private HttpProxyCacheServer newProxy() {
        return new HttpProxyCacheServer(this);
    }
}
