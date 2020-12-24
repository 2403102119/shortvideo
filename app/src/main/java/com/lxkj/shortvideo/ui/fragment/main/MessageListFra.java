package com.lxkj.shortvideo.ui.fragment.main;

import android.view.View;

import com.lxkj.shortvideo.R;
import com.lxkj.shortvideo.ui.fragment.CachableFrg;

import org.greenrobot.eventbus.EventBus;

/**

 *Time:2020/10/28

 *Author:李迪迦

 *Description:消息

 */
public class MessageListFra extends CachableFrg implements View.OnClickListener {


    @Override
    protected int rootLayout() {
        return R.layout.fra_messagelist;
    }

    @Override
    protected void initView() {

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

    }
}
