package com.lxkj.shortvideo.ui.fragment.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flyco.tablayout.SlidingTabLayout;
import com.lxkj.shortvideo.R;
import com.lxkj.shortvideo.adapter.MFragmentStatePagerAdapter;
import com.lxkj.shortvideo.ui.fragment.CachableFrg;
import com.lxkj.shortvideo.ui.fragment.classical.ClassicalFra;
import com.lxkj.shortvideo.ui.fragment.shortvideo.ShortVideoFra;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

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
 * Interface:经典
 */
public class HomeClassicalFra extends CachableFrg implements View.OnClickListener {


    @BindView(R.id.tabLayout)
    SlidingTabLayout tabLayout;
    @BindView(R.id.imClassify)
    ImageView imClassify;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.imGuanbi)
    ImageView imGuanbi;
    @BindView(R.id.taglay)
    TagFlowLayout taglay;
    @BindView(R.id.llClassify)
    LinearLayout llClassify;
    TagAdapter<String> adapter;
    List<String> hot_list = new ArrayList<>();
    private List<Fragment> fragments = new ArrayList<>();

    @Override
    protected int rootLayout() {
        return R.layout.fra_issue;
    }

    @Override
    protected void initView() {
        hot_list.clear();
        hot_list.add("分类1");
        hot_list.add("分类2");
        hot_list.add("分类3");
        hot_list.add("分类4");
        hot_list.add("分类5");
        hot_list.add("分类6");

        imClassify.setOnClickListener(this);
        imGuanbi.setOnClickListener(this);

        adapter = new TagAdapter<String>(hot_list) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView view = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.item_choose, parent, false);
                view.setText(s);
                return view;
            }
        };
        taglay.setAdapter(adapter);

        String[] titles = new String[4];
        titles[0] = "全部";
        titles[1] = "舞蹈";
        titles[2] = "音乐";
        titles[3] = "分类";
        ClassicalFra allOrderListFra = new ClassicalFra();
        Bundle all = new Bundle();
        all.putString("state", "0");
        allOrderListFra.setArguments(all);

        ClassicalFra dfkOrderListFra = new ClassicalFra();
        Bundle dfk = new Bundle();
        dfk.putString("state", "1");
        dfkOrderListFra.setArguments(dfk);

        ClassicalFra dfhOrderListFra = new ClassicalFra();
        Bundle dfh = new Bundle();
        dfh.putString("state", "2");
        dfhOrderListFra.setArguments(dfh);

        ClassicalFra dshOrderListFra = new ClassicalFra();
        Bundle dsh = new Bundle();
        dsh.putString("state", "3");
        dshOrderListFra.setArguments(dsh);

        fragments.add(allOrderListFra);
        fragments.add(dfkOrderListFra);
        fragments.add(dfhOrderListFra);
        fragments.add(dshOrderListFra);

        viewPager.setAdapter(new MFragmentStatePagerAdapter(getChildFragmentManager(), fragments, titles));
        tabLayout.setViewPager(viewPager);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imClassify://更多分类
                llClassify.setVisibility(View.VISIBLE);
                break;
            case R.id.imGuanbi:
                llClassify.setVisibility(View.GONE);
                break;
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

