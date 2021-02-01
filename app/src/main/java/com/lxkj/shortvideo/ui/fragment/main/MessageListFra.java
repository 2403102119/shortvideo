package com.lxkj.shortvideo.ui.fragment.main;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.flyco.tablayout.SlidingTabLayout;
import com.lxkj.shortvideo.AppConsts;
import com.lxkj.shortvideo.R;
import com.lxkj.shortvideo.adapter.MFragmentStatePagerAdapter;
import com.lxkj.shortvideo.bean.ResultBean;
import com.lxkj.shortvideo.biz.ActivitySwitcher;
import com.lxkj.shortvideo.http.BaseCallback;
import com.lxkj.shortvideo.http.Url;
import com.lxkj.shortvideo.ui.fragment.CachableFrg;
import com.lxkj.shortvideo.ui.fragment.classical.ClassicalFra;
import com.lxkj.shortvideo.ui.fragment.login.LoginFra;
import com.lxkj.shortvideo.ui.fragment.message.DynamicFra;
import com.lxkj.shortvideo.ui.fragment.message.LeaterFra;
import com.lxkj.shortvideo.ui.fragment.message.SystemMessageFra;
import com.lxkj.shortvideo.utils.SharePrefUtil;
import com.lxkj.shortvideo.utils.StringUtil;
import com.lxkj.shortvideo.view.NormalDialog;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMMessageListener;
import com.tencent.qcloud.tim.uikit.modules.conversation.ConversationManagerKit;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Time:2020/10/28
 * <p>
 * Author:李迪迦
 * <p>
 * Description:消息
 */
public class MessageListFra extends CachableFrg implements View.OnClickListener {


    @BindView(R.id.tabLayout)
    SlidingTabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    private List<Fragment> fragments = new ArrayList<>();
    @Override
    protected int rootLayout() {
        return R.layout.fra_messagelist;
    }

    @Override
    protected void initView() {
        String[] titles = new String[3];
        titles[0] = "动态";
        titles[1] = "私信";
        titles[2] = "系统消息";
        DynamicFra allOrderListFra = new DynamicFra();
        Bundle all = new Bundle();
        all.putString("state", "0");
        allOrderListFra.setArguments(all);

        LeaterFra dfkOrderListFra = new LeaterFra();
        Bundle dfk = new Bundle();
        dfk.putString("state", "1");
        dfkOrderListFra.setArguments(dfk);

        SystemMessageFra dfhOrderListFra = new SystemMessageFra();
        Bundle dfh = new Bundle();
        dfh.putString("state", "2");
        dfhOrderListFra.setArguments(dfh);

        fragments.add(allOrderListFra);
        fragments.add(dfkOrderListFra);
        fragments.add(dfhOrderListFra);

        viewPager.setAdapter(new MFragmentStatePagerAdapter(getChildFragmentManager(), fragments, titles));
        tabLayout.setViewPager(viewPager);








    }

    /**
     * 未读系统消息数量
     */
    private void unreadSysMessageCount() {
        Map<String, Object> params = new HashMap<>();
        params.put("mid", userId);

        mOkHttpHelper.post_json(getContext(), Url.unreadSysMessageCount, params, new BaseCallback<ResultBean>() {
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
                if (StringUtil.isEmpty(resultBean.count)||resultBean.count.equals("0")){
                    tabLayout.hideMsg(2);
                }else {
                    tabLayout.showMsg(2,Integer.parseInt(resultBean.count));
                }

            }

            @Override
            public void onError(Response response, int code, Exception e) {
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();


        if (StringUtil.isEmpty(SharePrefUtil.getString(getContext(), AppConsts.UID, ""))){
            if (AppConsts.login.equals("0")){
                NormalDialog dialog = new NormalDialog(getContext(), "未登录,请登录", "取消", "确定", true);
                dialog.show();
                dialog.setOnButtonClickListener(new NormalDialog.OnButtonClick() {
                    @Override
                    public void OnRightClick() {
                        ActivitySwitcher.startFragment(getActivity(), LoginFra.class);
                    }

                    @Override
                    public void OnLeftClick() {
                        AppConsts.login = "1";
                    }
                });
            }
        }else {
            ConversationManagerKit.getInstance().addUnreadWatcher(new ConversationManagerKit.MessageUnreadWatcher() {
                @Override
                public void updateUnread(int count) {
                    if (count == 0){
                        tabLayout.hideMsg(1);
                    }else {
                        tabLayout.showMsg(1,count);
                    }
                }
            });
            unreadSysMessageCount();
        }

    }
}
