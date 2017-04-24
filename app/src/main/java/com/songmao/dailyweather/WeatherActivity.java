package com.songmao.dailyweather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.songmao.dailyweather.gson.Forecast;
import com.songmao.dailyweather.gson.Weather;
import com.songmao.dailyweather.util.HttpUtil;
import com.songmao.dailyweather.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


/**
 * Created by Acer on 2017/4/21.
 */

public class WeatherActivity extends AppCompatActivity {

    private ScrollView weatherView;

    private TextView titleText;

    private TextView updateTime;

    private TextView nowTemperature;

    private TextView nowInfo;

    private LinearLayout forecastView;

    private TextView aqiText;

    private TextView pm25Text;

    private TextView comfortText;

    private TextView carWashText;

    private TextView sportText;

    private ImageView bingPicImg;

    public SwipeRefreshLayout refreshLayout;

    public DrawerLayout mDrawerLayout;

    private Button navButton;

    private String newWeatherId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

//        if (Build.VERSION.SDK_INT >= 21){
//            View decorView = getWindow().getDecorView();
//            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//            getWindow().setStatusBarColor(Color.TRANSPARENT);
//        }

        bingPicImg = (ImageView) findViewById(R.id.bing_pic_img);
        weatherView = (ScrollView) findViewById(R.id.weather_layout);
        titleText = (TextView) findViewById(R.id.title_city);
        updateTime = (TextView) findViewById(R.id.title_update_time);
        forecastView = (LinearLayout) findViewById(R.id.forecast_layout);
        nowTemperature = (TextView) findViewById(R.id.now_temperature);
        nowInfo = (TextView) findViewById(R.id.now_weather_info);
        aqiText = (TextView) findViewById(R.id.aqi_text);
        pm25Text = (TextView) findViewById(R.id.pm25_text);
        comfortText = (TextView) findViewById(R.id.comfort_text);
        carWashText = (TextView) findViewById(R.id.car_wash_text);
        sportText = (TextView) findViewById(R.id.sport_text);

        /*
        */

        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        refreshLayout.setColorSchemeResources(R.color.colorPrimary);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestWeatherData(newWeatherId);
                loadImage();
            }
        });

        navButton = (Button) findViewById(R.id.nav_button);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });


        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString = prefs.getString("weather",null);
        String bingPicString = prefs.getString("bing_pic",null);
        if (bingPicString != null){
            Glide.with(this).load(bingPicString).into(bingPicImg);
        }
        else {
            loadImage();
        }
       if (weatherString != null){
            //有缓存时直接解析天气数据
            Weather  weather = Utility.handleWeatherResponse(weatherString);
            newWeatherId= weather.basic.weatherId;
            showWeatherInfo(weather);
       }else {
            //无缓存时去服务器查询数据
            Intent intent = getIntent();
            String weatherId = intent.getStringExtra("weather_id");
            weatherView.setVisibility(View.INVISIBLE);
            requestWeatherData(weatherId);
       }

    }

    /*
    * 根据 weather_id 请求天气
    */
    public void requestWeatherData(String weatherId) {
        String address = "https://free-api.heweather.com/v5/weather?city="+weatherId+"&key=21bdab34c00c4a90b946dda59235841f";
        newWeatherId =weatherId;
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this,"获取天气信息失败",Toast.LENGTH_SHORT).show();
                        refreshLayout.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final Weather weather = Utility.handleWeatherResponse(responseText);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weather != null && "ok".equals(weather.status)) {
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                            editor.putString("weather", responseText);
                            editor.apply();
                            showWeatherInfo(weather);
                            loadImage();
                        }else {
                            Toast.makeText(WeatherActivity.this,"获取天气信息失败",Toast.LENGTH_SHORT).show();
                        }
                        refreshLayout.setRefreshing(false);
                    }
                });
            }
        });
    }

    /*
    *  将天气信息显示在屏幕上
    */
    private void showWeatherInfo(Weather weather) {

        String cityName = weather.basic.cityName;
        String update = weather.basic.update.updateTime.split(" ")[1];
        String degree = weather.now.temperature +  "°C";
        String nowInfotext = weather.now.more.info;

        titleText.setText(cityName);
        updateTime.setText(update);
        nowTemperature.setText(degree);
        nowInfo.setText(nowInfotext);

        forecastView.removeAllViews();
        for (Forecast forecast : weather.forecastList) {
            View view = LayoutInflater.from(this).inflate(R.layout.forecast_item, forecastView, false);
            TextView dateText = (TextView) view.findViewById(R.id.forecast_date);
            TextView infoText = (TextView) view.findViewById(R.id.forecast_info);
            TextView maxText = (TextView) view.findViewById(R.id.forecast_tem_max);
            TextView minText = (TextView) view.findViewById(R.id.forecast_tem_min);
            dateText.setText(forecast.date);
            infoText.setText(forecast.more.info);
            maxText.setText(forecast.temperature.max);
            minText.setText(forecast.temperature.min);
            forecastView.addView(view);
        }

        if (weather.aqi!= null){
            aqiText.setText(weather.aqi.city.aqi);
            pm25Text.setText(weather.aqi.city.pm25);
        }

        String comfort = "舒适度："+weather.suggestion.comfort.info;
        String carWash = "洗车指数："+weather.suggestion.carWash.info;
        String sport = "运动建议：" +weather.suggestion.sport.info;

        comfortText.setText(comfort);
        carWashText.setText(carWash);
        sportText.setText(sport);
        weatherView.setVisibility(View.VISIBLE);
    }


    private void loadImage(){
        String requestBingPic = "http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingPic = response.body().string();
                SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                edit.putString("bing_pic",bingPic);
                edit.apply();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(WeatherActivity.this).load(bingPic).into(bingPicImg);
                        refreshLayout.setRefreshing(false);
                    }
                });
            }
        });
    }
}
