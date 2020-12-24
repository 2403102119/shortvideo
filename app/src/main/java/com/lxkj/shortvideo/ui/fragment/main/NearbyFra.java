package com.lxkj.shortvideo.ui.fragment.main;

import android.view.View;

import com.lxkj.shortvideo.R;
import com.lxkj.shortvideo.ui.fragment.CachableFrg;

/**
 * Time:2020/8/24
 * <p>
 * Author:李迪迦
 * <p>
 * Description:附近
 */
public class NearbyFra extends CachableFrg implements View.OnClickListener {



    @Override
    protected int rootLayout() {
        return R.layout.fra_nearby;
    }

    @Override
    protected void initView() {

    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {

        }
    }

}
