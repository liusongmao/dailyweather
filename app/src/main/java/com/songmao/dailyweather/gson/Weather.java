package com.songmao.dailyweather.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Acer on 2017/4/20.
 */

public class Weather {

    public String status;

    public Basic basic;

    public Now now;

    public  AQI aqi;

    public Suggestion suggestion;

    @SerializedName("daily_forecast")
    public List<Forecast> forecastList;
}
