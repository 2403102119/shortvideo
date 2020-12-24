package com.lxkj.shortvideo.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kxn on 2018/7/7 0007.
 */

public class MyPagerAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> mFragmentList;

    public void setFragments(ArrayList<Fragment> fragments) {
        mFragmentList = fragments;
    }

    public MyPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        Fragment fragment = mFragmentList.get(position);

        return fragment;
    }

    @Override
    public int getCount() {

        return mFragmentList.size();
    }
    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }
}
