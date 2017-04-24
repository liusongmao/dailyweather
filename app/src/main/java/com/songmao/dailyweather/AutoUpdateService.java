package com.songmao.dailyweather;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.Preference;
import android.preference.PreferenceManager;

import com.songmao.dailyweather.gson.Weather;
import com.songmao.dailyweather.util.HttpUtil;
import com.songmao.dailyweather.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AutoUpdateService extends Service {
    public AutoUpdateService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        updateWeather();
        updateBingPic();
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int hour = 8 * 60 * 60 * 1000; //8小时的毫秒数；
        long triggerAtTime = SystemClock.elapsedRealtime() + hour;
        Intent i = new Intent(this,AutoUpdateService.class);
        PendingIntent pi = PendingIntent.getService(this,0,i,0);
        manager.set(AlarmManager.ELAPSED_REALTIME,triggerAtTime,pi);
        return super.onStartCommand(intent, flags, startId);
    }

    private void updateWeather(){
        SharedPreferences prfs = PreferenceManager.getDefaultSharedPreferences(this);
        String  weatherString = prfs.getString("weather",null);
        if (weatherString != null){
            //已经有缓存了，直接解析天气数据；
            Weather weather = Utility.handleWeatherResponse(weatherString);
            String weatherId = weather.basic.weatherId;
            String weatherUrl =  "https://free-api.heweather.com/v5/weather?city="+weatherId+"&key=21bdab34c00c4a90b946dda59235841f";
            HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String weatherString = response.body().string();
                    Weather  newWeather = Utility.handleWeatherResponse(weatherString);
                    if (newWeather != null && "ok".equals(newWeather.status)) {
                        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(AutoUpdateService.this).edit();
                        editor.putString("weather",weatherString);
                        editor.apply();
                    }
                }
            });
        }

    }

    private void updateBingPic(){
        String bingPicUrl = "http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpRequest(bingPicUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String bingPicString = response.body().string();
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(AutoUpdateService.this).edit();
                editor.putString("bing_pic",bingPicString);
                editor.apply();
            }
        });
    }
}


