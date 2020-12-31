package com.lxkj.shortvideo.ui.fragment.main;

import android.os.Bundle;
import android.view.View;

import com.flyco.tablayout.SlidingTabLayout;
import com.lxkj.shortvideo.R;
import com.lxkj.shortvideo.adapter.MFragmentStatePagerAdapter;
import com.lxkj.shortvideo.ui.fragment.CachableFrg;
import com.lxkj.shortvideo.ui.fragment.classical.ClassicalFra;
import com.lxkj.shortvideo.ui.fragment.message.DynamicFra;
import com.lxkj.shortvideo.ui.fragment.message.LeaterFra;
import com.lxkj.shortvideo.ui.fragment.message.SystemMessageFra;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;

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
