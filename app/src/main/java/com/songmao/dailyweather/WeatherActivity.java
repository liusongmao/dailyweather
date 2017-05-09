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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.songmao.dailyweather.Adapter.FragmentAdapter;
import com.songmao.dailyweather.Adapter.ViewFragment;
import com.songmao.dailyweather.db.CityCode;
import com.songmao.dailyweather.service.AutoUpdateService;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;


/**
 * Created by Acer on 2017/4/21.
 */

public class WeatherActivity extends AppCompatActivity {

    private static final String TAG = "WeatherActivity";

    private CircleIndicator indicator;
    private DrawerLayout mDrawerLayout;
    private NavigationView navigationView;
    private ViewPager viewPager;
    private FragmentAdapter adapter;
    private List<ViewFragment> viewFragments = new ArrayList<>();
    private boolean isAddCity = false;

    private LocalBroadcastManager localManager;
    private CityManagerBroadCastReceiver broadCastReceiver;

    @Override
    protected void onRestart() {
        super.onRestart();
        boolean isRepeat = true;
        if ( isAddCity ){
            Log.d(TAG, "onRestart: "+getIntent().getBooleanExtra("result",false));
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

        setContentView(R.layout.activity_weather);
        initBroadCast();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);   //实例化 Navigation控件；
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.select_city:
                         // 跳转到选择城市页面
                        Intent intent1 = new Intent(WeatherActivity.this,ChooseFragment.class);
                        startActivity(intent1);
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.city:
                        Intent intent = new Intent(WeatherActivity.this,CityManagerActivity.class);
                        startActivityForResult(intent,1);
                        mDrawerLayout.closeDrawers();
                }
                return true;
            }
        });
        viewPager = (ViewPager) findViewById(R.id.weather_pager);
        adapter = new FragmentAdapter(getSupportFragmentManager(),viewFragments);
        indicator = (CircleIndicator) findViewById(R.id.indicator);
        viewPager.setAdapter(adapter);
        indicator.setViewPager(viewPager);
        adapter.registerDataSetObserver(indicator.getDataSetObserver());

        List<CityCode> cityCodes = DataSupport.findAll(CityCode.class);
        if (cityCodes.size() > 0){
            //有缓存时直接解析天气
            Intent intent = new Intent(this, AutoUpdateService.class);
            startService(intent);
            for (CityCode code : cityCodes){
                showWeatherInfo(code.getCityCode());
            }
        }else {
            //无缓存时去服务器查询数据
            Intent intent = getIntent();
            String weatherId = intent.getStringExtra("weather_id");
            String  cityName = getIntent().getStringExtra("county_name");
            showWeatherInfo(weatherId);
            CityCode cityCode = new CityCode();
            cityCode.setCityCode(weatherId);
            cityCode.setCityName(cityName);
            cityCode.save();
        }
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

    public DrawerLayout getDrawerLayout(){
        return this.mDrawerLayout;
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


    private class CityManagerBroadCastReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "onReceive: ");
            List<String> deleteCityIds = intent.getStringArrayListExtra("delete_city");
            if (deleteCityIds.size() > 0 ){
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
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
            }
        }
    }
}
