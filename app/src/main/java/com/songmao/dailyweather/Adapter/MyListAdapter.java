package com.songmao.dailyweather.Adapter;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.songmao.dailyweather.R;
import com.songmao.dailyweather.db.CityCode;
import com.songmao.dailyweather.util.MyApplication;

import org.litepal.crud.DataSupport;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Acer on 2017/5/8.
 */

public class MyListAdapter extends BaseAdapter {

    private List<String> cityList= new ArrayList<>();
    private Boolean isEditState = false ;
    private int deleteCount ;
    private ArrayList<String> alreadyDe = new ArrayList<>();

    public static int CHANGE_TAG = 0;

    public MyListAdapter(List<String> cityList){
        this.cityList = cityList;
        Log.d("MyListAdapter", "MyListAdapter: "+deleteCount);
    }

    @Override
    public int getCount() {
        return cityList.size();
    }

    @Override
    public Object getItem(int position) {
        return cityList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null){
            viewHolder = new ViewHolder();
            convertView = View.inflate(MyApplication.getContext(), R.layout.city_manager_item,null);
            viewHolder.dragButton = (ImageView) convertView.findViewById(R.id.item_image);
            viewHolder.deleteButton = (ImageView) convertView.findViewById(R.id.item_button);
            viewHolder.cityText = (TextView) convertView.findViewById(R.id.item_city_text);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final int finalPosition = position;
        viewHolder.cityText.setText(cityList.get(position));
        viewHolder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cityName = cityList.get(finalPosition);
                cityList.remove(finalPosition);
                notifyDataSetChanged();
                deleteKeepCity(cityName);
                CHANGE_TAG = 1;
            }
        });
        if (isEditState){
            viewHolder.deleteButton.setVisibility(View.VISIBLE);
            viewHolder.dragButton.setVisibility(View.VISIBLE);
        }else {
            viewHolder.deleteButton.setVisibility(View.GONE);
            viewHolder.dragButton.setVisibility(View.GONE);
        }
        return convertView;
    }
    class ViewHolder{
        ImageView dragButton;
        ImageView deleteButton;
        TextView cityText;
    }

    public void setIsEditState(Boolean isEditState){
        this.isEditState = isEditState;
    }

    private void deleteKeepCity(String deleteCity){
        List<CityCode> cityCodes = DataSupport.where("cityName = ?",deleteCity).find(CityCode.class);
        deleteCount = DataSupport.deleteAll(CityCode.class,"cityName = ?",deleteCity);
        if (deleteCount > 0){
            for (CityCode city : cityCodes){
                alreadyDe.add(city.getCityCode());
                Log.d("db", "deleteKeepCity: "+city.getCityCode());
            }
        }
    }
    public void changeData(){
        LocalBroadcastManager broadCastManager = LocalBroadcastManager.getInstance(MyApplication.getContext());
        Intent intent = new Intent("com.songmao.dailyweather.CITY_MANAGER");
        intent.putStringArrayListExtra("delete_city",alreadyDe);
        broadCastManager.sendBroadcast(intent);
    }
}
