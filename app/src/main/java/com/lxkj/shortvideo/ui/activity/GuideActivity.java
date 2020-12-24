package com.lxkj.shortvideo.ui.activity;

import android.content.Context;
import android.os.Bundle;
import androidx.viewpager.widget.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gyf.immersionbar.ImmersionBar;
import com.lxkj.shortvideo.R;
import com.lxkj.shortvideo.biz.ActivitySwitcher;
import com.lxkj.shortvideo.utils.PicassoUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bingoogolapple.bgabanner.BGABanner;

public class GuideActivity extends BaseFragAct {

    @BindView(R.id.banner_guide_content)
    BGABanner bannerGuideContent;
    Context context;
    @BindView(R.id.tvNext)
    TextView tvNext;
    List<Integer> guides = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        ButterKnife.bind(this);
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.transparentStatusBar();
        mImmersionBar.init();
        context = this;
        bannerGuideContent.setAdapter(bannerAdapter);
        bannerGuideContent.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                if (i == guides.size()-1) {
                    tvNext.setVisibility(View.VISIBLE);
                }else
                    tvNext.setVisibility(View.GONE);
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });

        guides.add(R.mipmap.guide1);
        guides.add(R.mipmap.guide2);
        guides.add(R.mipmap.guide3);
        guides.add(R.mipmap.guide4);
        bannerGuideContent.setData(guides, null);

        tvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (!StringUtil.isEmpty(SharePrefUtil.getString(context, AppConsts.UID, "")))
                    ActivitySwitcher.start(GuideActivity.this, MainActivity.class);
//                else
//                    ActivitySwitcher.startFragment(GuideActivity.this, LoginFra.class);
                finish();
            }
        });
    }

    private BGABanner.Adapter bannerAdapter = new BGABanner.Adapter() {
        @Override
        public void fillBannerItem(BGABanner banner, View view, Object model, int position) {
            PicassoUtil.setImag(context, (int) model, (ImageView) view);
        }
    };
}
