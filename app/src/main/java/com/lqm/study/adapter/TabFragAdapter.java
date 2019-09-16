package com.lqm.study.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * @user lqm
 * @desc TabLayout+ViewPager 的适配器
 */

public class TabFragAdapter extends FragmentPagerAdapter {
    private List<String> mTabData;
    private List<Fragment> mFragments;

    public TabFragAdapter(FragmentManager fm, List<Fragment> fragments, List<String> tabData) {
        super(fm);
        this.mFragments = fragments;
        this.mTabData = tabData;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mTabData.size();
    }

    //此方法用来显示tab上的名字  (不添加将不显示文字)
    @Override
    public CharSequence getPageTitle(int position) {
        return mTabData.get(position);
    }
}

