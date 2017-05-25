package com.songmao.dailyweather.Adapter;

import android.support.v4.view.ViewPager;

import com.songmao.dailyweather.util.NowTmp;

import java.util.List;

/**
 * Created by Acer on 2017/5/10.
 */

public interface HeaderTmpListener {
    List<NowTmp> headerTmp();
    ViewPager getViewPager();
}
