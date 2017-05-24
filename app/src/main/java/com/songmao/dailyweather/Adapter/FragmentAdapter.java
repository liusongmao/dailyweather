package com.songmao.dailyweather.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;

import com.songmao.dailyweather.ViewFragment;

import java.util.List;

/**
 * Created by Acer on 2017/5/3.
 */

public class FragmentAdapter extends FragmentStatePagerAdapter {

    private List<ViewFragment> fragmentList;

    public FragmentAdapter(FragmentManager fm, List<ViewFragment> fragments) {
        super(fm);
        this.fragmentList = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }
}
