package com.songmao.dailyweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Acer on 2017/4/20.
 */

public class Forecast {

    public String date;

    @SerializedName("cond")
    public More more;

    @SerializedName("tmp")
    public Temperature temperature;

    public class More{

        @SerializedName("txt_d")
        public String info;
    }

    public class Temperature{
         public String max;

        public String min;
    }
}
