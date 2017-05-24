package com.songmao.dailyweather;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.songmao.dailyweather.Adapter.FragmentAdapter;
import com.songmao.dailyweather.Adapter.HeaderTmpListener;
import com.songmao.dailyweather.Adapter.MyRecyclerAdapter;
import com.songmao.dailyweather.View.MyDrawerLayout;
import com.songmao.dailyweather.db.CityCode;
import com.songmao.dailyweather.service.AutoUpdateService;
import com.songmao.dailyweather.util.NowTmp;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;


/**
 * Created by Acer on 2017/4/21.
 */

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "WeatherActivity";

    private TextView notCity;
    private CircleIndicator indicator;
    private MyDrawerLayout mDrawerLayout;
    private NavigationView navigationView;
    private RecyclerView recyclerView;
    private View headerView;
    private ViewPager viewPager;
    private FragmentAdapter adapter;
    private MyRecyclerAdapter recyclerAdapter;
    private List<ViewFragment> viewFragments = new ArrayList<>();
    private boolean isAddCity = false;
    private List<NowTmp> nowTmps = new ArrayList<>();

    private LocalBroadcastManager localManager;
    private CityManagerBroadCastReceiver broadCastReceiver;

    @Override
    protected void onRestart() {
        super.onRestart();
        boolean isRepeat = true;
        if ( isAddCity ){
            String  addWeatherId = getIntent().getStringExtra("weather_id");
            List<CityCode> cityCides = DataSupport.findAll(CityCode.class);
            if (cityCides.size() > 0){
                for (int i = 0 ; i < cityCides.size() ; i ++){
                    CityCode code = cityCides.get(i);
                    if (code.getCityCode().equals(addWeatherId)){
                        isRepeat = false;
                        viewPager.setCurrentItem(i);
                    }
                }
            }
            if (isRepeat){
                String  cityName = getIntent().getStringExtra("county_name");
                NowTmp nowTmp = new NowTmp();
                nowTmp.setCity(cityName);
                nowTmps.add(nowTmp);
                showWeatherInfo(addWeatherId);
                viewPager.setCurrentItem(cityCides.size());
                CityCode cityCode = new CityCode();
                cityCode.setCityCode(addWeatherId);
                cityCode.setCityName(cityName);
                cityCode.save();
            }
            isAddCity = false;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21){
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_main);
        initView();
        initBroadCast();
        List<CityCode> cityCodes = DataSupport.findAll(CityCode.class);
        if (cityCodes.size() > 0){
            //有缓存时直接解析天气
            Intent intent = new Intent(this, AutoUpdateService.class);
            startService(intent);
            for (CityCode code : cityCodes){
                NowTmp nowTmp = new NowTmp();
                nowTmp.setCity(code.getCityName());
                nowTmps.add(nowTmp);
                showWeatherInfo(code.getCityCode());

            }
        }else {
            //无缓存时去服务器查询数据
            Intent intent = new Intent(this,AddCityActivity.class);
            startActivity(intent);
        }
    }

    public void initView(){
        notCity = (TextView) findViewById(R.id.not_city);
        mDrawerLayout = (MyDrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);   //实例化 Navigation控件；
        navigationView.setNavigationItemSelectedListener(this);
        headerView =navigationView.getHeaderView(0);
        recyclerView = (RecyclerView) headerView.findViewById(R.id.header_recycler);
        LinearLayoutManager lManager = new LinearLayoutManager(this);
        lManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(lManager);
        recyclerAdapter = new MyRecyclerAdapter(new HeaderTmpListener() {
            @Override
            public List<NowTmp> headerTmp() {
                return nowTmps;
            }
        });
        recyclerView.setAdapter(recyclerAdapter);
        viewPager = (ViewPager) findViewById(R.id.weather_pager);
        adapter = new FragmentAdapter(getSupportFragmentManager(),viewFragments);
        indicator = (CircleIndicator) findViewById(R.id.indicator);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(10);
        indicator.setViewPager(viewPager);
        adapter.registerDataSetObserver( indicator.getDataSetObserver() );
    }

    /*
    *  将天气信息显示在屏幕上
    */
    private void showWeatherInfo(String weatherId) {
        ViewFragment viewFragment = new ViewFragment();
        Bundle bundle = new Bundle();
        bundle.putString("weather_id",weatherId);
        viewFragment.setArguments(bundle);
        viewFragments.add(viewFragment);
        adapter.notifyDataSetChanged();
        recyclerAdapter.notifyDataSetChanged();
    }

    public DrawerLayout getDrawerLayout(){
        return this.mDrawerLayout;
    }
    public MyRecyclerAdapter setNowTmp(final String tmp, final String city){
        for (int i = 0 ; i < nowTmps.size() ; i ++ ){
            String mCity = nowTmps.get(i).getCity();
            if (mCity.equals(city)){
                nowTmps.get(i).setTmp(tmp);
            }
        }
        return this.recyclerAdapter;
    }


    @Override
    protected void onNewIntent(Intent intent) {
        this.isAddCity = intent.getBooleanExtra("result",false);
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:
                if (resultCode == RESULT_OK){
                    viewPager.setCurrentItem(data.getIntExtra("position",0));
                }
                break;
            default:
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        localManager.unregisterReceiver(broadCastReceiver);
    }

    private void initBroadCast(){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.songmao.dailyweather.CITY_MANAGER");
        broadCastReceiver = new CityManagerBroadCastReceiver();
        localManager = LocalBroadcastManager.getInstance(this);
        localManager.registerReceiver(broadCastReceiver,intentFilter);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.select_city:
                // 跳转到选择城市页面
                Intent intent1 = new Intent(MainActivity.this,AddCityActivity.class);
                startActivity(intent1);
                mDrawerLayout.closeDrawers();
                break;
            case R.id.city:
                Intent intent = new Intent(MainActivity.this,CityManagerActivity.class);
                startActivityForResult(intent,1);
                mDrawerLayout.closeDrawers();
        }
        return true;
    }


    private class CityManagerBroadCastReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "onReceive: ");
            List<String> deleteCityIds = intent.getStringArrayListExtra("delete_city_id");
            if (deleteCityIds.size() > 0 ){
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit();
                for (String cityId : deleteCityIds){
                    Log.d(TAG, "onActivityResult: "+deleteCityIds.size() );
                    for (int i = 0 ; i < viewFragments.size() ; i ++){
                        ViewFragment viewFragment = viewFragments.get(i);
                        Bundle bundle = viewFragment.getArguments();
                        if (bundle.getString("weather_id").equals(cityId)){
                            viewFragments.remove(i);
                            editor.remove(""+cityId);
                            editor.commit();
                            Log.d(TAG, "onActivityResult: fragment "+viewFragments.size() );
                            break;
                        }
                    }
                }
                adapter.notifyDataSetChanged();
                List<String> deNames = intent.getStringArrayListExtra("delete_city");
                for (String name : deNames){
                    for (int i = 0 ; i < nowTmps.size() ; i++){
                        if (name.equals( nowTmps.get(i).getCity() ));
                        nowTmps.remove(i);
                        break;
                    }
                }
                recyclerAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        /*
        *
        * 获得抽屉中RecyclerView的位置
        * 处理滑动冲突
        *
        */

        if (hasFocus){
            int left = recyclerView.getLeft();
            int top = recyclerView.getTop();
            int right = recyclerView.getRight();
            int bottom = recyclerView.getBottom();
            mDrawerLayout.setRecyclerView(left,top,right,bottom);
        }
    }
}
