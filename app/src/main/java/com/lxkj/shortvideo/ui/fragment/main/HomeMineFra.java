package com.lxkj.shortvideo.ui.fragment.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.lxkj.shortvideo.AppConsts;
import com.lxkj.shortvideo.R;
import com.lxkj.shortvideo.bean.ResultBean;
import com.lxkj.shortvideo.biz.ActivitySwitcher;
import com.lxkj.shortvideo.biz.EventCenter;
import com.lxkj.shortvideo.http.BaseCallback;
import com.lxkj.shortvideo.http.OkHttpHelper;
import com.lxkj.shortvideo.http.Url;
import com.lxkj.shortvideo.ui.fragment.CachableFrg;
import com.lxkj.shortvideo.ui.fragment.homemine.AttentionFra;
import com.lxkj.shortvideo.ui.fragment.homemine.FansFra;
import com.lxkj.shortvideo.ui.fragment.homemine.HomepageFra;
import com.lxkj.shortvideo.ui.fragment.homemine.IssueFra;
import com.lxkj.shortvideo.ui.fragment.homemine.SetFra;
import com.lxkj.shortvideo.ui.fragment.system.WebFra;
import com.lxkj.shortvideo.utils.SharePrefUtil;
import com.lxkj.shortvideo.utils.ToastUtil;
import com.makeramen.roundedimageview.RoundedImageView;
import com.zhy.m.permission.MPermissions;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Request;
import okhttp3.Response;

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
    @BindView(R.id.riIcon)
    RoundedImageView riIcon;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvMotto)
    TextView tvMotto;
    @BindView(R.id.tvtoFocusedCount)
    TextView tvtoFocusedCount;
    @BindView(R.id.tvfocusedCount)
    TextView tvfocusedCount;
    @BindView(R.id.llAbout)
    LinearLayout llAbout;


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
        llAbout.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        memberHome();

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
            case R.id.llAbout://关于我们
                Bundle bundle = new Bundle();
                bundle.putString("title", "关于我们");
                bundle.putString("url","http://122.114.49.242:8081/apiService/common/protocol/3");
                ActivitySwitcher.startFragment(getActivity(), WebFra.class, bundle);
                break;
        }
    }


    /**
     * 我的主页
     */
    private void memberHome() {
        Map<String, Object> params = new HashMap<>();
        params.put("mid", SharePrefUtil.getString(getContext(), AppConsts.UID, ""));
        OkHttpHelper.getInstance().post_json(getContext(), Url.memberHome, params, new BaseCallback<ResultBean>() {
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
                Glide.with(getContext()).applyDefaultRequestOptions(new RequestOptions()
                        .error(R.mipmap.touxiang)
                        .placeholder(R.mipmap.touxiang))
                        .load(resultBean.avatar)
                        .into(riIcon);

                tvName.setText(resultBean.nickname);
                tvMotto.setText(resultBean.motto);
                tvtoFocusedCount.setText(resultBean.toFocusedCount);
                tvfocusedCount.setText(resultBean.focusedCount);

            }

            @Override
            public void onError(Response response, int code, Exception e) {
            }
        });
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
