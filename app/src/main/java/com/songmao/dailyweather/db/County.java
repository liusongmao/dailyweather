package com.songmao.dailyweather.db;

import org.litepal.crud.DataSupport;

/**
 * Created by Acer on 2017/4/19.
 */

public class County extends DataSupport {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public int getCountyId() {
        return countyId;
    }

    public void setCountyId(int countyId) {
        this.countyId = countyId;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    private int id;
    private String countyName;
    private int countyId;
    private int cityId;

}
