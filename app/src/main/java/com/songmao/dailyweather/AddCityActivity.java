package com.songmao.dailyweather;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.songmao.dailyweather.gson.Weather;
import com.songmao.dailyweather.util.HttpUtil;
import com.songmao.dailyweather.util.Utility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Acer on 2017/4/25.
 */

public class AddCityActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView currentLocation;
    private Button locationBt;
    private View fragmentView;
    public LocationClient mClient;

    public Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    if (currentLocation != null) {
                        currentLocation.setVisibility(View.VISIBLE);
                        currentLocation.setText((String) msg.obj);
                    }
            }
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_area_fragment);
        ChooseAreaFragment fragment = (ChooseAreaFragment) getSupportFragmentManager().findFragmentById(R.id.choose_fragment);
        fragmentView = fragment.getCurrentView();
        currentLocation = (TextView) fragmentView.findViewById(R.id.current_location);
        locationBt = (Button) fragmentView.findViewById(R.id.location_bt);
        currentLocation.setOnClickListener(this);
        locationBt.setOnClickListener(this);
        mClient = new LocationClient(getApplicationContext());
        mClient.registerLocationListener(new MyLocationListener());
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(AddCityActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(AddCityActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(AddCityActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if(!permissionList.isEmpty()){
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(AddCityActivity.this,permissions,1);
        }else{
            requestLocation();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.current_location:
                String location = (String) currentLocation.getText();
                requestReturn(location);
                break;
            case R.id.location_bt:
                mClient.start();
                Toast.makeText(this, "定位中...", Toast.LENGTH_SHORT).show();
        }
    }
    public void requestReturn(String location){
        String url = "https://api.heweather.com/v5/search?city="+location+"&key=21bdab34c00c4a90b946dda59235841f";
        HttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseString = response.body().string();
                if (responseString != null){
                    Weather weather =  Utility.handleWeatherResponse(responseString);
                    if ("ok".equals(weather.status)){
                        Intent intent = new Intent(AddCityActivity.this,MainActivity.class);
                        intent.putExtra("result",true);
                        intent.putExtra("weather_id",weather.basic.weatherId);
                        intent.putExtra("county_name",weather.basic.cityName);
                        Log.d("tag", "onResponse: ");
                        startActivity(intent);
                    }
                }
            }
        });

    }

    private void requestLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setIsNeedAddress(true);
        mClient.setLocOption(option);
        mClient.start();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if (grantResults.length > 0){
                    for (int result : grantResults){
                        if (result != PackageManager.PERMISSION_GRANTED){
                            Toast.makeText(this, "必须同意所有权限", Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                    requestLocation();
                }
                else {}
                break;
            default:
        }
    }

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            String district = bdLocation.getDistrict();
            if (district != null) {
                Message message = new Message();
                message.what = 1;
                message.obj = district;
                handler.sendMessage(message);
                mClient.stop();
            }else {
                currentLocation.setVisibility(View.GONE);
                Toast.makeText(AddCityActivity.this, "定位失败！", Toast.LENGTH_SHORT).show();
                mClient.stop();
            }
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    }
}
