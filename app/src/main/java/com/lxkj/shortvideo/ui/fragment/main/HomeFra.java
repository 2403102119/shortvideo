package com.lxkj.shortvideo.ui.fragment.main;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lxkj.shortvideo.R;
import com.lxkj.shortvideo.bean.ResultBean;
import com.lxkj.shortvideo.http.BaseCallback;
import com.lxkj.shortvideo.http.Url;
import com.lxkj.shortvideo.ui.fragment.CachableFrg;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import okhttp3.Request;
import okhttp3.Response;

/**

 *Time:2020/10/28

 *Author:李迪迦

 *Description:首页

 */
public class HomeFra extends CachableFrg implements View.OnClickListener {

    @BindView(R.id.imCommodity)
    ImageView imCommodity;
    @BindView(R.id.imkaiguan)
    ImageView imkaiguan;
    @BindView(R.id.im_classify)
    ImageView imClassify;
    @BindView(R.id.llwallet)
    LinearLayout llwallet;
    @BindView(R.id.imEvaluate)
    ImageView imEvaluate;
    @BindView(R.id.llDiscount)
    LinearLayout llDiscount;
    @BindView(R.id.imorder)
    ImageView imorder;
    @BindView(R.id.llbill)
    LinearLayout llbill;
    @BindView(R.id.llCommodity)
    LinearLayout llCommodity;
    @BindView(R.id.llFeight)
    LinearLayout llFeight;
    @BindView(R.id.tvtoday)
    TextView tvtoday;
    @BindView(R.id.tvtotalEarnings)
    TextView tvtotalEarnings;
    @BindView(R.id.tvname)
    TextView tvname;
    @BindView(R.id.tvState)
    TextView tvState;
    @BindView(R.id.smart)
    SmartRefreshLayout smart;

    private boolean Shopswitch = false;
    private String opening = "0", carriage;
    private int page = 1, totalPage = 1;
    @Override
    protected int rootLayout() {
        return R.layout.fra_home;
    }

    @Override
    protected void initView() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

        }
    }


    /**
     * 首页信息
     */
    private void home() {
        Map<String, Object> params = new HashMap<>();
        params.put("sid", userId);
        mOkHttpHelper.post_json(getContext(), Url.home, params, new BaseCallback<ResultBean>() {
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

            }

            @Override
            public void onError(Response response, int code, Exception e) {
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
//        if (!StringUtil.isEmpty(userId))
//            home();
    }


}
