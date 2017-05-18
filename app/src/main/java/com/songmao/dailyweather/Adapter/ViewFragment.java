package com.songmao.dailyweather.Adapter;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.songmao.dailyweather.R;
import com.songmao.dailyweather.View.HourlyForecast;
import com.songmao.dailyweather.WeatherActivity;
import com.songmao.dailyweather.gson.Forecast;
import com.songmao.dailyweather.gson.Hourly;
import com.songmao.dailyweather.gson.Weather;
import com.songmao.dailyweather.util.HttpUtil;
import com.songmao.dailyweather.util.MyApplication;
import com.songmao.dailyweather.util.Utility;
import com.songmao.dailyweather.util.WeatherImageUtil;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Acer on 2017/5/2.
 */

public class ViewFragment extends Fragment {
    private static final String TAG = "ViewFragment";
    private Weather weather;
    private Activity mActivity;
    private View mView;
    private String newWeatherId;
    private SwipeRefreshLayout swipeRefresh;
    private Boolean isRefresh = false;
    HourlyForecast hourlyForecast;

    private WeatherImageUtil imageUtil;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21){
            View decorView = getActivity().getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getActivity().getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.weather_viewpager,container,false);
        this.mView = view;
        mActivity = getActivity();
        swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        Bundle bundle = getArguments();
        String weatherId = bundle.getString("weather_id",null);
        SharedPreferences prfs = PreferenceManager.getDefaultSharedPreferences(mActivity);
        String keepResponse = prfs.getString(""+weatherId,null);
        Log.d(TAG, "onCreateView: " + weatherId);
        if (imageUtil == null){
            imageUtil = new WeatherImageUtil();
        }

        if (keepResponse != null){
//            Log.d(TAG, "onCreateView: "+keepResponse);
            weather = Utility.handleWeatherResponse(keepResponse);
            newWeatherId = weather.basic.weatherId;
            showWeatherInfo(mView);
        }else {
            requestWeatherData(weatherId);
        }
        return view;
    }



    /*
    * 根据 weather_id 请求天气
    */
    public void requestWeatherData(final String weatherId) {
        newWeatherId = weatherId;
        String address = "https://free-api.heweather.com/v5/weather?city="+weatherId+"&key=21bdab34c00c4a90b946dda59235841f";
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mActivity,"获取天气信息失败",Toast.LENGTH_SHORT).show();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final Weather weather = Utility.handleWeatherResponse(responseText);
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weather != null && "ok".equals(weather.status)) {
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getActivity()).edit();
                            editor.putString(""+weatherId,responseText);
                            editor.apply();
                            ViewFragment.this.weather = weather;
                            isRefresh = true;
                            showWeatherInfo(mView);
                        }else {
                            Toast.makeText(mActivity,"获取天气信息失败",Toast.LENGTH_SHORT).show();
                        }
                        swipeRefresh.setRefreshing(false);
                    }
                });

            }
        });
    }

    private void showWeatherInfo(View view){
        ImageView bingPicImg = (ImageView) view.findViewById(R.id.bing_pic_img);
        ScrollView weatherView = (ScrollView) view.findViewById(R.id.weather_layout);
        TextView titleText = (TextView) view.findViewById(R.id.title_city);
        TextView updateTime = (TextView) view.findViewById(R.id.title_update_time);
        LinearLayout forecastView = (LinearLayout) view.findViewById(R.id.forecast_layout);
        TextView nowTemperature = (TextView) view.findViewById(R.id.now_temperature);
        TextView nowInfo = (TextView) view.findViewById(R.id.now_weather_info);
        TextView aqiText = (TextView) view.findViewById(R.id.aqi_text);
        TextView pm25Text = (TextView) view.findViewById(R.id.pm25_text);
        TextView comfortText = (TextView) view.findViewById(R.id.comfort_text);
        TextView carWashText = (TextView) view.findViewById(R.id.car_wash_text);
        TextView sportText = (TextView) view.findViewById(R.id.sport_text);
        Button navButton = (Button) view.findViewById(R.id.nav_button);
        navButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() instanceof WeatherActivity) {
                    WeatherActivity weatherActivity = (WeatherActivity) getActivity();
                    weatherActivity.getDrawerLayout().openDrawer(GravityCompat.START);
                }
            }
        });
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestWeatherData(newWeatherId);
            }
        });
 //       Glide.with(getActivity()).load(bingPic).into(bingPicImg);
        String cityName = weather.basic.cityName;
        String update = weather.basic.update.updateTime.split(" ")[1];
        String degree = weather.now.temperature + "°C";
        String nowInfotext = weather.now.more.info;
        titleText.setText(cityName);
        updateTime.setText(update);
        nowTemperature.setText(degree);
        nowInfo.setText(nowInfotext);
        forecastView.removeAllViews();
        for (Forecast forecast : weather.forecastList) {
            View view1 = LayoutInflater.from(mActivity).inflate(R.layout.forecast_item, forecastView, false);
            TextView dateText = (TextView) view1.findViewById(R.id.forecast_date);
            TextView infoText = (TextView) view1.findViewById(R.id.forecast_info);
            TextView maxText = (TextView) view1.findViewById(R.id.forecast_tem_max);
            TextView minText = (TextView) view1.findViewById(R.id.forecast_tem_min);
            dateText.setText(forecast.date);
            infoText.setText(forecast.more.info);
            maxText.setText(forecast.temperature.max);
            minText.setText(forecast.temperature.min);
            forecastView.addView(view1);
        }

        if (hourlyForecast == null) {
            hourlyForecast = (HourlyForecast) view.findViewById(R.id.hourly_forecast);
            hourlyForecast.setHourlyData(weather.hourForecastList);
        }else{
            hourlyForecast.setHourlyData(weather.hourForecastList);
        }

        if (weather.aqi != null) {
            aqiText.setText(weather.aqi.city.aqi);
            pm25Text.setText(weather.aqi.city.pm25);
        }

        String comfort = "舒适度：" + weather.suggestion.comfort.info;
        String carWash = "洗车指数：" + weather.suggestion.carWash.info;
        String sport = "运动建议：" + weather.suggestion.sport.info;
        comfortText.setText(comfort);
        carWashText.setText(carWash);
        sportText.setText(sport);
        Glide.with(getActivity()).load(imageUtil.setImageUrl(nowInfotext)).into(bingPicImg);
        weatherView.setVisibility(View.VISIBLE);
        WeatherActivity weatherActivity = (WeatherActivity) getActivity();
        MyRecyclerAdapter adapter = weatherActivity.setNowTmp(degree,cityName);
        if (isRefresh){
            adapter.notifyDataSetChanged();
            isRefresh = false;
        }
//        adapter.notifyDataSetChanged();
    }

}
