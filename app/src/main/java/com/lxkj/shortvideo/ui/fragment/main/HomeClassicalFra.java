package com.lxkj.shortvideo.ui.fragment.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flyco.tablayout.SlidingTabLayout;
import com.lxkj.shortvideo.AppConsts;
import com.lxkj.shortvideo.R;
import com.lxkj.shortvideo.adapter.MFragmentStatePagerAdapter;
import com.lxkj.shortvideo.bean.DataListBean;
import com.lxkj.shortvideo.bean.ResultBean;
import com.lxkj.shortvideo.biz.ActivitySwitcher;
import com.lxkj.shortvideo.http.BaseCallback;
import com.lxkj.shortvideo.http.OkHttpHelper;
import com.lxkj.shortvideo.http.Url;
import com.lxkj.shortvideo.ui.fragment.CachableFrg;
import com.lxkj.shortvideo.ui.fragment.classical.ClassicalFra;
import com.lxkj.shortvideo.ui.fragment.competition.SeachFra;
import com.lxkj.shortvideo.utils.SharePrefUtil;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

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
    @BindView(R.id.llSeach)
    LinearLayout llSeach;
    @BindView(R.id.llShoucang)
    LinearLayout llShoucang;
    @BindView(R.id.llCanping)
    LinearLayout llCanping;
    @BindView(R.id.tvShoucang)
    TextView tvShoucang;
    @BindView(R.id.viewShoucang)
    View viewShoucang;
    @BindView(R.id.tvCanping)
    TextView tvCanping;
    @BindView(R.id.viewCanping)
    View viewCanping;
    private List<Fragment> fragments = new ArrayList<>();
    private List<DataListBean> list = new ArrayList<>();
    private String type = "1";

    @Override
    protected int rootLayout() {
        return R.layout.fra_issue;
    }

    @Override
    protected void initView() {
        imClassify.setOnClickListener(this);
        imGuanbi.setOnClickListener(this);
        llSeach.setOnClickListener(this);
        llShoucang.setOnClickListener(this);
        llCanping.setOnClickListener(this);

        adapter = new TagAdapter<String>(hot_list) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView view = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.item_choose, parent, false);
                view.setText(s);
                return view;
            }
        };
        taglay.setAdapter(adapter);

        taglay.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                llClassify.setVisibility(View.GONE);
                viewPager.setCurrentItem(position + 1);
                return true;
            }
        });

        competitionCategoryList();

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
            case R.id.llSeach://搜索
                ActivitySwitcher.startFragment(getActivity(), SeachFra.class);
                break;
            case R.id.llShoucang://收藏
                type = "1";
                tvShoucang.setTextColor(getResources().getColor(R.color.colorBlack));
                viewShoucang.setBackgroundColor(getResources().getColor(R.color.colorBlack));
                viewCanping.setBackgroundColor(getResources().getColor(R.color.white));
                tvCanping.setTextColor(getResources().getColor(R.color.txt_66));
                setData(list);
                break;
            case R.id.llCanping://参评
                type = "2";
                tvShoucang.setTextColor(getResources().getColor(R.color.txt_66));
                viewShoucang.setBackgroundColor(getResources().getColor(R.color.white));
                viewCanping.setBackgroundColor(getResources().getColor(R.color.colorBlack));
                tvCanping.setTextColor(getResources().getColor(R.color.colorBlack));
                setData(list);
                break;
        }
    }


    public void setData(List<DataListBean> listBeans) {
        fragments.clear();
        String[] titles = new String[listBeans.size() + 1];
        titles[0] = "全部";
        ClassicalFra allOrderListFra1 = new ClassicalFra();
        Bundle all1 = new Bundle();
        all1.putString("id", "");
        all1.putString("type", type);
        allOrderListFra1.setArguments(all1);
        fragments.add(allOrderListFra1);
        for (int i = 0; i < listBeans.size(); i++) {
            titles[i + 1] = listBeans.get(i).name;

            ClassicalFra allOrderListFra = new ClassicalFra();
            Bundle all = new Bundle();
            all.putString("id", listBeans.get(i).id);
            all.putString("type", type);
            allOrderListFra.setArguments(all);
            fragments.add(allOrderListFra);
        }

        viewPager.setAdapter(new MFragmentStatePagerAdapter(getChildFragmentManager(), fragments, titles));
        tabLayout.setViewPager(viewPager);

    }

    /**
     * 赛事分类
     */
    private void competitionCategoryList() {
        Map<String, Object> params = new HashMap<>();
        params.put("sid", SharePrefUtil.getString(getContext(), AppConsts.UID, ""));
        OkHttpHelper.getInstance().post_json(getContext(), Url.competitionCategoryList, params, new BaseCallback<ResultBean>() {
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
                hot_list.clear();
                for (int i = 0; i < resultBean.dataList.size(); i++) {
                    hot_list.add(resultBean.dataList.get(i).name);
                }
                adapter.notifyDataChanged();

                list.clear();
                list.addAll(resultBean.dataList);

                setData(resultBean.dataList);

            }

            @Override
            public void onError(Response response, int code, Exception e) {
            }
        });
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

