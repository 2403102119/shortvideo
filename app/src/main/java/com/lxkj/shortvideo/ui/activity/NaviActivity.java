package com.lxkj.shortvideo.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.lxkj.shortvideo.R;
import com.lxkj.shortvideo.actlink.ActDecorator;
import com.lxkj.shortvideo.actlink.NaviLeftListener;
import com.lxkj.shortvideo.actlink.NaviLeftText;
import com.lxkj.shortvideo.actlink.NaviRightDecorator;
import com.lxkj.shortvideo.actlink.NaviRightListener;
import com.lxkj.shortvideo.actlink.NaviTitleDecorator;
import com.lxkj.shortvideo.biz.ActivitySwitcher;
import com.lxkj.shortvideo.ui.fragment.TitleFragment;
import com.lxkj.shortvideo.utils.ReflectUtil;
import com.lxkj.shortvideo.utils.StringUtil;
import com.lxkj.shortvideo.utils.TypeSafer;


public class NaviActivity extends BaseFragAct {

    public static final String EXT_FRAGMENT = "fragment_name";

    protected TitleFragment fragment;

    protected boolean isDestroyed;
    public TextView titleTv;
    public TextView right;

    public interface NaviRigthImageListener {

        public int rightImg();

        public void onRightClicked(View v);
    }

    public void setRightImgVisibility(boolean isVisibility) {
        if (isVisibility) {
            img.setVisibility(View.VISIBLE);
        } else {
            img.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        fragment = createInstance(getFragmentName(getIntent()));
        if (null == fragment) {
            finish();
            return;
        }
        fragment.setArguments(getIntent().getExtras());
        fragment.setActivity(this);
        if (fragment instanceof ActDecorator) {
            ((ActDecorator) fragment).beforeContentView();
        }
        setContentView(R.layout.act_common);
        initNaviBar();
        if (fragment instanceof ActDecorator) {
            ((ActDecorator) fragment).afterContentView();
        }
        addFragment();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // 在onStop时释放掉播放器
    }

    public void hindNaviBar() {
        findViewById(R.id.act_navi).setVisibility(View.GONE);
    }

    public void setNaviTitle(final String title) {
        TypeSafer.text((TextView) findViewById(R.id.navi_title), title);
    }

    public void setRightTextVisible(Boolean isVisible) {
        if (isVisible)
            findViewById(R.id.navi_right_txt).setVisibility(View.VISIBLE);
        else
            findViewById(R.id.navi_right_txt).setVisibility(View.GONE);
    }

    public void setNaviColor(int color) {
        final int rgb = getResources().getColor(color);
        findViewById(R.id.act_navi).setBackgroundColor(rgb);
//        setStatuBarColor(rgb);
    }

    @Deprecated
    public void hideNavi() {
        findViewById(R.id.act_navi).setVisibility(View.GONE);
    }

    public boolean isDisable() {
        return isDestroyed || isFinishing();
    }

    private String getFragmentName(final Intent intent) {
        return (null != intent) ? intent.getStringExtra(EXT_FRAGMENT) : null;
    }

    public TitleFragment createInstance(final String fragName) {
        final Object obj = ReflectUtil.createInstance(fragName);
        if (obj instanceof TitleFragment) {
            return (TitleFragment) obj;
        }
        return null;
    }

    public void initNaviBar() {
        initTitle();
        initLeft();
        initRight();

    }

    public ImageView arrowLeft;

    public void initLeft() {
        final OnClickListener cl = new OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        };

        arrowLeft = findViewById(R.id.navi_left);
        if (!fragment.hideLeftArrow()) {
            arrowLeft.setOnClickListener(cl);
        }
        arrowLeft.setVisibility(View.GONE);

        final TextView tvLeft = (TextView) findViewById(R.id.navi_left_txt);
        if (fragment instanceof NaviLeftText) {
            tvLeft.setVisibility(View.VISIBLE);
            tvLeft.setText(((NaviLeftText) fragment).leftText());
            tvLeft.setOnClickListener(cl);
            arrowLeft.setPadding(
                    arrowLeft.getPaddingLeft(), arrowLeft.getPaddingTop(),
                    0, arrowLeft.getPaddingBottom());
        }
        if (isHindLeft) {
            arrowLeft.setVisibility(View.INVISIBLE);
        }
    }

    private boolean isHindLeft = false;

    public void hindLeft() {
        isHindLeft = true;
        if (null != arrowLeft) {
            arrowLeft.setVisibility(View.INVISIBLE);
        }
    }

    public void initTitle() {
        titleTv = (TextView) findViewById(R.id.navi_title);
        final int title = fragment.getTitleId();
        final String titleName = fragment.getTitleName();
        if (title > 0) {
            titleTv.setText(title);
        } else if (!StringUtil.isEmpty(titleName)) {
            titleTv.setText(titleName);
        }
        if (fragment instanceof NaviTitleDecorator) {
            ((NaviTitleDecorator) fragment).decorTitle(titleTv);
        }
    }

    public void setTitle(String title) {
        if (!StringUtil.isEmpty(title)) {
            titleTv.setText(title);
        }
    }


    public ImageView img;

    public void initRight() {
        right = (TextView) findViewById(R.id.navi_right_txt);
        img = (ImageView) findViewById(R.id.navi_right_img);
        if (fragment instanceof NaviRightListener) {
            final NaviRightListener l = (NaviRightListener) fragment;
            right.setText(l.rightText());
            right.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (null != l) {
                        l.onRightClicked(v);
                    }
                }
            });
        } else if (fragment instanceof NaviRightDecorator) {
            ((NaviRightDecorator) fragment).decorRightBtn(right);
        } else {
            right.setVisibility(View.INVISIBLE);
        }

        if (fragment instanceof NaviRigthImageListener) {
            final NaviRigthImageListener imgl = (NaviRigthImageListener) fragment;
//            if (imgl.rightImg() != -1) {
            img.setImageResource(imgl.rightImg());
            img.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != imgl) {
                        imgl.onRightClicked(v);
                    }
                }
            });
//            }else {
//                img.setVisibility(View.INVISIBLE);
//            }
        } else {
            img.setVisibility(View.INVISIBLE);
        }
    }

    public void addFragment() {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.act_content, fragment)
                .commit();
    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        fragment.onActivityResult(arg0, arg1, arg2);
//        UMShareAPI.get(this).onActivityResult(arg0, arg1, arg2);
    }

    public void finishSelf() {
        arrowLeft.setVisibility(View.GONE);
        ActivitySwitcher.finish(this);
    }

    public void finishDown() {
        ActivitySwitcher.finishDown(this);
    }

    @Override
    public void onBackPressed() {


        if (fragment instanceof NaviLeftListener
                && ((NaviLeftListener) fragment).onLeftClicked()) {
            return;
        }
        finishSelf();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!fragment.hideLeftArrow()) {
            arrowLeft.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public String simpleName() {
        return fragment.getClass().getSimpleName();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isDestroyed = true;
    }

}
