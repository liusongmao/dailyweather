package com.songmao.dailyweather.util;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Acer on 2017/5/14.
 */

public class WeatherImageUtil {

    private Map<String,String> map_day = new HashMap<String,String>();
    private Map<String,String> map_night = new HashMap<>();

    public WeatherImageUtil(){
        map_day.put("晴","http://static.etouch.cn/imgs/upload/1464156616.6833.jpg");
        map_day.put("多云","http://static.etouch.cn/imgs/upload/1464156680.4088.jpg");
        map_day.put("少云","http://static.etouch.cn/imgs/upload/1464156680.4088.jpg");
        map_day.put("晴转多云","http://static.etouch.cn/imgs/upload/1464156680.4088.jpg");
        map_day.put("阴","http://static.etouch.cn/imgs/upload/1464156697.6173.jpg");
        map_day.put("有风","http://static.etouch.cn/imgs/upload/1464156697.6173.jpg");
        map_day.put("平静","http://static.etouch.cn/imgs/upload/1464156697.6173.jpg");
        map_day.put("微风","http://static.etouch.cn/imgs/upload/1464156697.6173.jpg");
        map_day.put("和风","http://static.etouch.cn/imgs/upload/1464156697.6173.jpg");
        map_day.put("清风","http://static.etouch.cn/imgs/upload/1464156697.6173.jpg");
        map_day.put("强风","http://static.etouch.cn/imgs/upload/1464156697.6173.jpg");
        map_day.put("疾风","http://static.etouch.cn/imgs/upload/1464156697.6173.jpg");
        map_day.put("大风","http://static.etouch.cn/imgs/upload/1464156697.6173.jpg");
        map_day.put("烈风","http://static.etouch.cn/imgs/upload/1464156697.6173.jpg");
        map_day.put("风暴","http://static.etouch.cn/imgs/upload/1464156697.6173.jpg");
        map_day.put("狂爆风","http://static.etouch.cn/imgs/upload/1464156697.6173.jpg");
        map_day.put("飓风","http://static.etouch.cn/imgs/upload/1464156697.6173.jpg");
        map_day.put("龙卷风","http://static.etouch.cn/imgs/upload/1464156697.6173.jpg");
        map_day.put("热带风暴","http://static.etouch.cn/imgs/upload/1464156893.7236.jpg");
        map_day.put("阵雨","http://static.etouch.cn/imgs/upload/1464156721.5622.jpg");
        map_day.put("强阵雨","http://static.etouch.cn/imgs/upload/1464156721.5622.jpg");
        map_day.put("雷阵雨","http://static.etouch.cn/imgs/upload/1464156742.4928.jpg");
        map_day.put("强雷阵雨","http://static.etouch.cn/imgs/upload/1464156742.4928.jpg");
        map_day.put("雷阵雨伴有冰雹","http://static.etouch.cn/imgs/upload/1464156806.3523.jpg");
        map_day.put("小雨","http://static.etouch.cn/imgs/upload/1464156806.3523.jpg");
        map_day.put("毛毛雨","http://static.etouch.cn/imgs/upload/1464156806.3523.jpg");
        map_day.put("细雨","http://static.etouch.cn/imgs/upload/1464156806.3523.jpg");
        map_day.put("中雨","http://static.etouch.cn/imgs/upload/1464156835.9677.jpg");
        map_day.put("大雨","http://static.etouch.cn/imgs/upload/1464156872.1353.jpg");
        map_day.put("极端降雨","http://static.etouch.cn/imgs/upload/1464156893.7236.jpg");
        map_day.put("暴雨","http://static.etouch.cn/imgs/upload/1464156893.7236.jpg");
        map_day.put("大暴雨","http://static.etouch.cn/imgs/upload/1464156893.7236.jpg");
        map_day.put("特大暴雨","http://static.etouch.cn/imgs/upload/1464156893.7236.jpg");
        map_day.put("冻雨","http://static.etouch.cn/imgs/upload/1464156872.1353.jpg");
        map_day.put("小雪","http://static.etouch.cn/imgs/upload/1454119940.3162.jpg");
        map_day.put("中雪","http://static.etouch.cn/imgs/upload/1454119940.3162.jpg");
        map_day.put("大雪","http://static.etouch.cn/imgs/upload/1454119940.3162.jpg");
        map_day.put("暴雪","http://static.etouch.cn/imgs/upload/1454119940.3162.jpg");
        map_day.put("雨加雪","http://static.etouch.cn/imgs/upload/1446801371.5782.jpg");
        map_day.put("阵雨夹雪","http://static.etouch.cn/imgs/upload/1446801371.5782.jpg");
        map_day.put("阵雪","http://static.etouch.cn/imgs/upload/1446801371.5782.jpg");
        map_day.put("雨雪天气","http://static.etouch.cn/imgs/upload/1446801371.5782.jpg");
        map_day.put("薄雾","http://static.etouch.cn/imgs/upload/1464157261.3416.jpg");
        map_day.put("雾","http://static.etouch.cn/imgs/upload/1464157261.3416.jpg");
        map_day.put("霾","http://static.etouch.cn/imgs/upload/1464157104.4627.jpg");
        map_day.put("浮尘","http://static.etouch.cn/imgs/upload/1442471142.3815.jpg");
        map_day.put("扬沙","http://static.etouch.cn/imgs/upload/1442471142.3815.jpg");
        map_day.put("沙尘暴","http://static.etouch.cn/imgs/upload/1442471142.3815.jpg");
        map_day.put("强沙尘暴","http://static.etouch.cn/imgs/upload/1442471142.3815.jpg");
        map_day.put("热","http://static.etouch.cn/imgs/upload/1464156616.6833.jpg");
        map_day.put("冷","http://static.etouch.cn/imgs/upload/1464156697.6173.jpg");
        map_day.put("其他","http://static.etouch.cn/imgs/upload/1464156680.4088.jpg");


        map_night.put("晴","http://static.etouch.cn/imgs/upload/1464156624.5772.jpg");
        map_night.put("多云","http://static.etouch.cn/imgs/upload/1464156685.511.jpg");
        map_night.put("少云","http://static.etouch.cn/imgs/upload/1464156685.511.jpg");
        map_night.put("晴转多云","http://static.etouch.cn/imgs/upload/1464156685.511.jpg");
        map_night.put("阴","http://static.etouch.cn/imgs/upload/1464156697.6173.jpg");
        map_night.put("有风","http://static.etouch.cn/imgs/upload/1464156697.6173.jpg");
        map_night.put("平静","http://static.etouch.cn/imgs/upload/1464156697.6173.jpg");
        map_night.put("微风","http://static.etouch.cn/imgs/upload/1464156697.6173.jpg");
        map_night.put("和风","http://static.etouch.cn/imgs/upload/1464156697.6173.jpg");
        map_night.put("清风","http://static.etouch.cn/imgs/upload/1464156697.6173.jpg");
        map_night.put("强风","http://static.etouch.cn/imgs/upload/1464156697.6173.jpg");
        map_night.put("疾风","http://static.etouch.cn/imgs/upload/1464156697.6173.jpg");
        map_night.put("大风","http://static.etouch.cn/imgs/upload/1464156697.6173.jpg");
        map_night.put("烈风","http://static.etouch.cn/imgs/upload/1464156697.6173.jpg");
        map_night.put("风暴","http://static.etouch.cn/imgs/upload/1464156697.6173.jpg");
        map_night.put("狂爆风","http://static.etouch.cn/imgs/upload/1464156697.6173.jpg");
        map_night.put("飓风","http://static.etouch.cn/imgs/upload/1464156697.6173.jpg");
        map_night.put("龙卷风","http://static.etouch.cn/imgs/upload/1464156697.6173.jpg");
        map_night.put("热带风暴","http://static.etouch.cn/imgs/upload/1464156893.7236.jpg");
        map_night.put("阵雨","http://static.etouch.cn/imgs/upload/1464156721.5622.jpg");
        map_night.put("强阵雨","http://static.etouch.cn/imgs/upload/1464156721.5622.jpg");
        map_night.put("雷阵雨","http://static.etouch.cn/imgs/upload/1464156742.4928.jpg");
        map_night.put("强雷阵雨","http://static.etouch.cn/imgs/upload/1464156742.4928.jpg");
        map_night.put("雷阵雨伴有冰雹","http://static.etouch.cn/imgs/upload/1464156806.3523.jpg");
        map_night.put("小雨","http://static.etouch.cn/imgs/upload/1464156806.3523.jpg");
        map_night.put("中雨","http://static.etouch.cn/imgs/upload/1464156835.9677.jpg");
        map_night.put("大雨","http://static.etouch.cn/imgs/upload/1464156872.1353.jpg");
        map_night.put("极端降雨","http://static.etouch.cn/imgs/upload/1464156872.1353.jpg");
        map_night.put("暴雨","http://static.etouch.cn/imgs/upload/1464156893.7236.jpg");
        map_night.put("大暴雨","http://static.etouch.cn/imgs/upload/1464156893.7236.jpg");
        map_night.put("特大暴雨","http://static.etouch.cn/imgs/upload/1464156893.7236.jpg");
        map_night.put("冻雨","http://static.etouch.cn/imgs/upload/1464156872.1353.jpg");
        map_night.put("小雪","http://static.etouch.cn/suishen/weather/nighticyrain.jpg");
        map_night.put("中雪","http://static.etouch.cn/suishen/weather/nighticyrain.jpg");
        map_night.put("大雪","http://static.etouch.cn/suishen/weather/nighticyrain.jpg");
        map_night.put("暴雪","http://static.etouch.cn/suishen/weather/nighticyrain.jpg");
        map_night.put("雨加雪","http://static.etouch.cn/imgs/upload/1446801371.5782.jpg");
        map_night.put("雨雪天气","http://static.etouch.cn/imgs/upload/1446801371.5782.jpg");
        map_night.put("阵雨夹雪","http://static.etouch.cn/imgs/upload/1446801371.5782.jpg");
        map_night.put("阵雪","http://static.etouch.cn/suishen/weather/nighticyrain.jpg");
        map_night.put("薄雾","http://static.etouch.cn/imgs/upload/1464157261.3416.jpg");
        map_night.put("雾","http://static.etouch.cn/imgs/upload/1464157261.3416.jpg");
        map_night.put("霾","http://static.etouch.cn/imgs/upload/1464157104.4627.jpg");
        map_night.put("浮尘","http://static.etouch.cn/imgs/upload/1442472108.7831.jpg");
        map_night.put("扬沙","http://static.etouch.cn/imgs/upload/1442472108.7831.jpg");
        map_night.put("沙尘暴","http://static.etouch.cn/imgs/upload/1442472108.7831.jpg");
        map_night.put("强沙尘暴","http://static.etouch.cn/imgs/upload/1442472108.7831.jpg");
        map_night.put("热","http://static.etouch.cn/imgs/upload/1464156624.5772.jpg");
        map_night.put("冷","http://static.etouch.cn/imgs/upload/1464156697.6173.jpg");
        map_night.put("其他","http://static.etouch.cn/imgs/upload/1464156685.511.jpg");
    }

    public String setImageUrl(String key){
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        if (hour > 6 && hour < 19){
            if (map_day.containsKey(key)){
                return map_day.get(key);
            }
            else return map_day.get("其他");
        }else {
            if (map_night.containsKey(key)) {
                return map_night.get(key);
            }else {
                return map_night.get("其他");
            }
        }
    }
}
