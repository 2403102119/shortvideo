package com.lxkj.shortvideo.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTabHost;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.allenliu.versionchecklib.v2.AllenVersionChecker;
import com.allenliu.versionchecklib.v2.builder.NotificationBuilder;
import com.allenliu.versionchecklib.v2.builder.UIData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.gyf.immersionbar.ImmersionBar;
import com.lxkj.shortvideo.AppConsts;
import com.lxkj.shortvideo.GlobalBeans;
import com.lxkj.shortvideo.R;
import com.lxkj.shortvideo.bean.ResultBean;
import com.lxkj.shortvideo.bean.SendmessageBean;
import com.lxkj.shortvideo.biz.ActivitySwitcher;
import com.lxkj.shortvideo.biz.EventCenter;
import com.lxkj.shortvideo.http.BaseCallback;
import com.lxkj.shortvideo.http.OkHttpHelper;
import com.lxkj.shortvideo.http.SpotsCallBack;
import com.lxkj.shortvideo.http.Url;
import com.lxkj.shortvideo.socket.WsManager;
import com.lxkj.shortvideo.ui.fragment.login.LoginFra;
import com.lxkj.shortvideo.ui.fragment.main.HomeClassicalFra;
import com.lxkj.shortvideo.ui.fragment.main.HomeFra;
import com.lxkj.shortvideo.ui.fragment.main.HomeMineFra;
import com.lxkj.shortvideo.ui.fragment.main.MessageListFra;
import com.lxkj.shortvideo.ui.fragment.main.HomeShortVideoFra;
import com.lxkj.shortvideo.utils.APKVersionCodeUtils;
import com.lxkj.shortvideo.utils.PicassoUtil;
import com.lxkj.shortvideo.utils.SharePrefUtil;
import com.lxkj.shortvideo.utils.ToastUtil;
import com.lxkj.shortvideo.view.NormalDialog;
import com.lzy.ninegrid.ImageInfo;
import com.makeramen.roundedimageview.RoundedImageView;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMFriendshipManager;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMMessageListener;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMMessage;
import com.tencent.imsdk.v2.V2TIMMessageManager;
import com.tencent.imsdk.v2.V2TIMSDKListener;
import com.tencent.imsdk.v2.V2TIMSimpleMsgListener;
import com.tencent.qcloud.tim.uikit.TUIKit;
import com.tencent.qcloud.tim.uikit.TUIKitImpl;
import com.tencent.qcloud.tim.uikit.base.IMEventListener;
import com.tencent.qcloud.tim.uikit.base.IUIKitCallBack;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ymex.widget.banner.callback.BindViewCallBack;
import cn.ymex.widget.banner.callback.CreateViewCallBack;
import cn.ymex.widget.banner.callback.OnClickBannerListener;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends BaseFragAct
        implements TabHost.OnTabChangeListener, EventCenter.EventListener {
    @BindView(R.id.tabhost)
    public FragmentTabHost mTabHost;
    private int curTab = 0, tabIdx = 0;
    private WsManager wsManager;
    public  TextView tvUnreadCount;
    private int verCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (GlobalBeans.getSelf() == null) {
            GlobalBeans.initForMainUI(getApplication());
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        AppConsts.userId = SharePrefUtil.getString(this, AppConsts.UID, "");
        ButterKnife.bind(this);
        initTabHost();
        setTabSelected(curTab, true);
        eventCenter.registEvent(this, EventCenter.EventType.EVT_TOHOME);
        eventCenter.registEvent(this, EventCenter.EventType.EVT_LOGIN);
        eventCenter.registEvent(this, EventCenter.EventType.EVT_LOGOUT);
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.transparentStatusBar();
        mImmersionBar.init();

        if (!EventBus.getDefault().isRegistered(this)) {//判断是否已经注册了（避免崩溃）
            EventBus.getDefault().register(this); //向EventBus注册该对象，使之成为订阅者
        }
        verCode = APKVersionCodeUtils.getVersionCode(this);
        getUserSig();
        versionUpdate();




    }



    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void senmessage(SendmessageBean mMessageEvent) {
        if (mMessageEvent.type.equals("load")){
            Map<String,String> map = new HashMap<>();
            map.put("cmd","19");
            map.put("fromUserId","");
            map.put("userId",SharePrefUtil.getString(this, AppConsts.UID, ""));
            map.put("pageNo",mMessageEvent.pageNo);
            map.put("pageSize","10");
            map.put("type","1");
            Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
            String requestData = gson.toJson(map);
            wsManager.sendMessage(requestData);
        }
    }

    /**
     * 获取UserSig
     */
    private void getUserSig() {
        Map<String, Object> params = new HashMap<>();
        params.put("mid",  SharePrefUtil.getString(this,AppConsts.UID,null));
        OkHttpHelper.getInstance().post_json(this, Url.getUserSig, params, new BaseCallback<ResultBean>() {
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
                TUIKit.login(SharePrefUtil.getString(MainActivity.this, AppConsts.UID, ""),resultBean.userSig, new IUIKitCallBack() {
                    @Override
                    public void onSuccess(Object data) {
                        // 登录成功
                        TIMManager.getInstance().getConversationList();

                        HashMap<String,Object> map = new HashMap<>();
                        map.put(TIMUserProfile.TIM_PROFILE_TYPE_KEY_NICK,SharePrefUtil.getString(MainActivity.this, AppConsts.username, ""));
                        map.put(TIMUserProfile.TIM_PROFILE_TYPE_KEY_FACEURL,SharePrefUtil.getString(MainActivity.this, AppConsts.user_icon, ""));
                        TIMFriendshipManager.getInstance().modifySelfProfile(map, new TIMCallBack() {
                            @Override
                            public void onError(int code, String desc) {
                            }

                            @Override
                            public void onSuccess() {
                            }
                        });

                        TUIKit.addIMEventListener(new IMEventListener() {
                            @Override
                            public void onForceOffline() {
                                NormalDialog dialog = new NormalDialog(MainActivity.this, "您的账号已在其它设备登录", "", "确定", true);
                                dialog.show();
                                dialog.setOnButtonClickListener(new NormalDialog.OnButtonClick() {
                                    @Override
                                    public void OnRightClick() {
                                        SharePrefUtil.saveString(MainActivity.this, AppConsts.UID, "");
//                    eventCenter.sendType(EventCenter.EventType.EVT_LOGOUT);
                                        Bundle bundle = new Bundle();
                                        bundle.putString("phone",SharePrefUtil.getString(MainActivity.this, AppConsts.PHONE,null));
                                        ActivitySwitcher.startFragment(MainActivity.this, LoginFra.class,bundle);
                                        finish();
                                    }

                                    @Override
                                    public void OnLeftClick() {
                                        SharePrefUtil.saveString(MainActivity.this, AppConsts.UID, "");
//                    eventCenter.sendType(EventCenter.EventType.EVT_LOGOUT);
                                        Bundle bundle = new Bundle();
                                        bundle.putString("phone",SharePrefUtil.getString(MainActivity.this, AppConsts.PHONE,null));
                                        ActivitySwitcher.startFragment(MainActivity.this, LoginFra.class,bundle);
                                        finish();
                                    }
                                });
                            }

                            @Override
                            public void onUserSigExpired() {
                                NormalDialog dialog = new NormalDialog(MainActivity.this, "账号已过期，请重新登录", "", "确定", true);
                                dialog.show();
                                dialog.setOnButtonClickListener(new NormalDialog.OnButtonClick() {
                                    @Override
                                    public void OnRightClick() {
                                        SharePrefUtil.saveString(MainActivity.this, AppConsts.UID, "");
//                    eventCenter.sendType(EventCenter.EventType.EVT_LOGOUT);
                                        Bundle bundle = new Bundle();
                                        bundle.putString("phone",SharePrefUtil.getString(MainActivity.this, AppConsts.PHONE,null));
                                        ActivitySwitcher.startFragment(MainActivity.this, LoginFra.class,bundle);
                                        finish();
                                    }

                                    @Override
                                    public void OnLeftClick() {
                                        SharePrefUtil.saveString(MainActivity.this, AppConsts.UID, "");
//                    eventCenter.sendType(EventCenter.EventType.EVT_LOGOUT);
                                        Bundle bundle = new Bundle();
                                        bundle.putString("phone",SharePrefUtil.getString(MainActivity.this, AppConsts.PHONE,null));
                                        ActivitySwitcher.startFragment(MainActivity.this, LoginFra.class,bundle);
                                        finish();
                                    }
                                });
                            }

                            @Override
                            public void onNewMessage(V2TIMMessage v2TIMMessage) {
                                super.onNewMessage(v2TIMMessage);
                            }
                        });

                    }

                    @Override
                    public void onError(String module, final int code, final String desc) {
                        // 登录失败
                        Log.e("TAG", "onError: "+module+"---"+code+"---"+desc );
                    }
                });

            }

            @Override
            public void onError(Response response, int code, Exception e) {
            }
        });
    }

    /**
     * 获取版本更新
     */
    private void versionUpdate() {
        Map<String, Object> params = new HashMap<>();
        params.put("type","1");
        OkHttpHelper.getInstance().post_json(this, Url.versionUpdate, params, new SpotsCallBack<ResultBean>(MainActivity.this) {
            @Override
            public void onSuccess(Response response, ResultBean resultBean) {
                if (verCode<Integer.parseInt(resultBean.number)){
                    AllenVersionChecker
                            .getInstance()
                            .downloadOnly(
                                    UIData.create().setDownloadUrl(resultBean.androidFile).setTitle("提示").setContent(resultBean.remarks)
                            ).setNotificationBuilder(
                            NotificationBuilder.create()
                                    .setRingtone(true)
                                    .setIcon(R.mipmap.logo)
                                    .setTicker("版本更新")
                                    .setContentTitle("版本更新")
                                    .setContentText("正在下载....")
                    ).setShowNotification(true).setShowDownloadingDialog(true).executeMission(MainActivity.this);
                } else{

                }
//                    ToastUtil.show("当前已是最新版本！");
            }

            @Override
            public void onError(Response response, int code, Exception e) {
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        mTabHost.setCurrentTab(tabIdx);
    }

    private void initTabHost() {
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        for (int i = 0; i < tabs.size(); i++) {
            final TabDesc td = tabs.get(i);
            final View vTab = makeTabView();
            TextView tab = ((TextView) vTab.findViewById(R.id.tab_label));
//            if (i==1)
//                tvUnreadCount = ((TextView) vTab.findViewById(R.id.tvUnreadCount));
//            if (i==2)
//                tab.setVisibility(View.GONE);

            tab.setText(td.name);
            refreshTab(vTab, td, false);
            mTabHost.addTab(mTabHost.newTabSpec(td.tag).setIndicator(vTab), td.frgClass, null);
        }
        mTabHost.getTabWidget().setDividerDrawable(null);
        mTabHost.setOnTabChangedListener(this);
    }

    private void setTabSelected(int tabIdx, boolean selected) {
        refreshTab(mTabHost.getTabWidget().getChildAt(tabIdx),
                tabs.get(tabIdx), selected);
    }

    private View makeTabView() {
        return this.getLayoutInflater().inflate(
                R.layout.maintab, mTabHost.getTabWidget(), false);
    }

    private void refreshTab(View vTab, TabDesc td, boolean selected) {
        final ImageView iv = (ImageView) vTab.findViewById(R.id.tab_image);
        iv.setImageResource(selected ? td.icSelect : td.icNormal);
    }
    private final List<TabDesc> tabs = new ArrayList<TabDesc>() {
        {
            add(TabDesc.make("home", R.string.home,//赛事
                    R.drawable.saishi, R.drawable.saishi_xuan, HomeFra.class));
            add(TabDesc.make("shop", R.string.shop,//短视频
                    R.drawable.duanshipin, R.drawable.duanshipin_se, HomeShortVideoFra.class));
            add(TabDesc.make("add", R.string.add,//经典
                    R.drawable.jingdian, R.drawable.jingdian_se, HomeClassicalFra.class));
            add(TabDesc.make("tg", R.string.car,//消息
                    R.drawable.xiaoxi, R.drawable.xiaoxi_xuan, MessageListFra.class));
            add(TabDesc.make("mine", R.string.mine,//我的
                    R.drawable.wode, R.drawable.wode_se, HomeMineFra.class));
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getSupportFragmentManager().findFragmentByTag("home").onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // 在onStop时释放掉播放器
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        eventCenter.unregistEvent(this, EventCenter.EventType.EVT_TOHOME);
        eventCenter.unregistEvent(this, EventCenter.EventType.EVT_LOGIN);
    }

    @Override
    public void onEvent(EventCenter.HcbEvent e) {
        switch (e.type) {
            case EVT_TOHOME:
                tabIdx = 0;
                break;
            case EVT_LOGOUT:
                onExit();
                break;
        }
    }


    private static class TabDesc {
        String tag;
        int name;
        int icNormal;
        int icSelect;
        Class<? extends Fragment> frgClass;

        static TabDesc make(String tag, int name, int icNormal, int icSelect,
                            Class<? extends Fragment> frgClass) {
            TabDesc td = new TabDesc();
            td.tag = tag;
            td.name = name;
            td.icNormal = icNormal;
            td.icSelect = icSelect;
            td.frgClass = frgClass;
            return td;
        }

    }


    @Override
    public void onTabChanged(String s) {
        tabIdx = mTabHost.getCurrentTab();
        if (tabIdx == curTab) {
            return;
        }
        setTabSelected(curTab, false);
        curTab = tabIdx;
        setTabSelected(curTab, true);
    }


    private long backPressTime = 0;
    private static final int SECOND = 1000;

    @Override
    public void onBackPressed() {
        // 在全屏或者小窗口时按返回键要先退出全屏或小窗口，
        final long uptimeMillis = SystemClock.uptimeMillis();
        if (uptimeMillis - backPressTime > 2 * SECOND) {
            backPressTime = uptimeMillis;
            ToastUtil.show(getString(R.string.press_again_to_leave));
        } else {
            ToastUtil.cancel();
            onExit();
        }
    }

    private void onExit() {

        finish();

        EventBus.getDefault().unregister(this);
        if (wsManager != null) {
            wsManager.stopConnect();
            wsManager = null;
        }

        if (null != beans) {
            beans.onTerminate();
        }
    }

}