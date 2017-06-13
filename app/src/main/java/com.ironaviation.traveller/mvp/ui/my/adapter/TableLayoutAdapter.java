package com.ironaviation.traveller.mvp.ui.my.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Dennis on 2017/6/12.
 */

public class TableLayoutAdapter extends FragmentPagerAdapter {

    private String[] mTitles;
    private List<Fragment> fragmentList;
    public TableLayoutAdapter(FragmentManager fm, String[] mTitles, List<Fragment> fragmentList) {
        super(fm);
        this.mTitles = mTitles;
        this.fragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList != null ? fragmentList.size() : 0 ;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }
}
