package com.songmao.dailyweather;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.songmao.dailyweather.db.CityCode;

import org.litepal.crud.DataSupport;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        List<CityCode> cityCodes = DataSupport.findAll(CityCode.class);
        if(cityCodes.size() > 0){
            Intent intent = new Intent(this,WeatherActivity.class);
            startActivity(intent);
            finish();
        }
//        SharedPreferences prfs = PreferenceManager.getDefaultSharedPreferences(this);
//        String weatherIds = prfs.getString("weather",null);
//        if (weatherIds != null){
//            Intent intent = new Intent(this,WeatherActivity.class);
//            startActivity(intent);
//            finish();
//        }
    }
}
