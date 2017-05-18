package com.songmao.dailyweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Acer on 2017/5/15.
 */

public class Hourly {

    public Cond cond;

    public String date;

    public String pop;

    public String tmp;

    public class Cond{
        @SerializedName("txt")
        public String info;
    }
}
