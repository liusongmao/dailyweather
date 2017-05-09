package com.songmao.dailyweather.db;

import org.litepal.crud.DataSupport;

/**
 * Created by Acer on 2017/5/5.
 */

public class CityCode extends DataSupport {
    private int id;
    private String cityCode;
    private String cityName;
    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }


    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }
    public String getCityCode(){
        return this.cityCode;
    }
}
