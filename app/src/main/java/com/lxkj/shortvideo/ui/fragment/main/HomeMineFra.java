package com.lxkj.shortvideo.ui.fragment.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lxkj.shortvideo.AppConsts;
import com.lxkj.shortvideo.R;
import com.lxkj.shortvideo.biz.ActivitySwitcher;
import com.lxkj.shortvideo.biz.EventCenter;
import com.lxkj.shortvideo.ui.fragment.CachableFrg;
import com.lxkj.shortvideo.ui.fragment.homemine.AttentionFra;
import com.lxkj.shortvideo.ui.fragment.homemine.FansFra;
import com.lxkj.shortvideo.ui.fragment.homemine.HomepageFra;
import com.lxkj.shortvideo.ui.fragment.homemine.IssueFra;
import com.lxkj.shortvideo.ui.fragment.homemine.SetFra;
import com.lxkj.shortvideo.utils.SharePrefUtil;
import com.lxkj.shortvideo.utils.ToastUtil;
import com.zhy.m.permission.MPermissions;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Time:2020/10/28
 * <p>
 * Author:李迪迦
 * <p>
 * Description:我的
 */
public class HomeMineFra extends CachableFrg implements View.OnClickListener {

    Unbinder unbinder;
    @BindView(R.id.tvHomepage)
    TextView tvHomepage;
    @BindView(R.id.llAttention)
    LinearLayout llAttention;
    @BindView(R.id.llFans)
    LinearLayout llFans;
    @BindView(R.id.llIssue)
    LinearLayout llIssue;
    @BindView(R.id.llSet)
    LinearLayout llSet;


    @Override
    protected int rootLayout() {
        return R.layout.fra_mine;
    }

    @Override
    protected void initView() {
        eventCenter.registEvent(this, EventCenter.EventType.EVT_EDITINFO);

        tvHomepage.setOnClickListener(this);
        llAttention.setOnClickListener(this);
        llFans.setOnClickListener(this);
        llIssue.setOnClickListener(this);
        llSet.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvHomepage://个人主页
                ActivitySwitcher.startFragment(getActivity(), HomepageFra.class);
                break;
            case R.id.llAttention://关注
                ActivitySwitcher.startFragment(getActivity(), AttentionFra.class);
                break;
            case R.id.llFans://粉丝
                ActivitySwitcher.startFragment(getActivity(), FansFra.class);
                break;
            case R.id.llIssue://意见反馈
                ActivitySwitcher.startFragment(getActivity(), IssueFra.class);
                break;
            case R.id.llSet://设置
                ActivitySwitcher.startFragment(getActivity(), SetFra.class);
                break;
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        eventCenter.unregistEvent(this, EventCenter.EventType.EVT_EDITINFO);
        eventCenter.unregistEvent(this, EventCenter.EventType.EVT_RZSUCCESS);
    }


    @PermissionGrant(AppConsts.PMS_LOCATION)
    public void pmsLocationSuccess() {
        //权限授权成功
//        platformPhone();

    }

    @PermissionDenied(AppConsts.PMS_LOCATION)
    public void pmsLocationError() {
        ToastUtil.show("请设置电话权限");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        MPermissions.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    public void onEvent(EventCenter.HcbEvent e) {
        super.onEvent(e);
        switch (e.type) {
            case EVT_EDITINFO:
                userId = SharePrefUtil.getString(getContext(), AppConsts.UID, "");
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
