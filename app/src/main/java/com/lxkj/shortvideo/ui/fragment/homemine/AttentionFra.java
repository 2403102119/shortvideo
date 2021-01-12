package com.lxkj.shortvideo.ui.fragment.homemine;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.flyco.tablayout.SlidingTabLayout;
import com.lxkj.shortvideo.R;
import com.lxkj.shortvideo.adapter.MFragmentStatePagerAdapter;
import com.lxkj.shortvideo.ui.fragment.TitleFragment;
import com.lxkj.shortvideo.ui.fragment.classical.ClassicalFra;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Time:2021/1/4
 * <p>
 * Author:李迪迦
 * <p>
 * Interface:关注
 */
public class AttentionFra extends TitleFragment {
    Unbinder unbinder;
    @BindView(R.id.tabLayout)
    SlidingTabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    TagAdapter<String> adapter;
    List<String> hot_list = new ArrayList<>();
    private List<Fragment> fragments = new ArrayList<>();
    @Override
    public String getTitleName() {
        return "关注";
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        rootView = inflater.inflate(R.layout.fra_attention, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        initView();
        return rootView;
    }

    public void initView() {



        String[] titles = new String[3];
        titles[0] = "互相关注";
        titles[1] = "我的关注";
        titles[2] = "附近";
        AttentionListFra allOrderListFra = new AttentionListFra();
        Bundle all = new Bundle();
        all.putString("type","1");
        allOrderListFra.setArguments(all);

        AttentionListFra dfkOrderListFra = new AttentionListFra();
        Bundle dfk = new Bundle();
        dfk.putString("type","2");
        dfkOrderListFra.setArguments(dfk);

        AttentionListFra dfhOrderListFra = new AttentionListFra();
        Bundle dfh = new Bundle();
        dfh.putString("type","3");
        dfhOrderListFra.setArguments(dfh);

        fragments.add(allOrderListFra);
        fragments.add(dfkOrderListFra);
        fragments.add(dfhOrderListFra);

        viewPager.setAdapter(new MFragmentStatePagerAdapter(getChildFragmentManager(), fragments, titles));
        tabLayout.setViewPager(viewPager);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
