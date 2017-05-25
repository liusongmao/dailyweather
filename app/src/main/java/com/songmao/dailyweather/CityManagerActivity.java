package com.songmao.dailyweather;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.songmao.dailyweather.Adapter.MyListAdapter;
import com.songmao.dailyweather.db.CityCode;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Acer on 2017/5/7.
 */

public class CityManagerActivity extends AppCompatActivity{

    private ListView cityManagerView;
    private List<String> cityList = new ArrayList<>();
    private MyListAdapter adapter ;
    private Boolean isEditState = false;
    private Menu menu;

    private static final String TAG = "CityManagerActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        if (Build.VERSION.SDK_INT >= 21){
//            View decorView = getWindow().getDecorView();
//            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//            getWindow().setStatusBarColor(Color.TRANSPARENT);
//        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.city_manager_acticity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.city_manager_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        cityManagerView = (ListView) findViewById(R.id.city_manager_list);
        List<CityCode> cityCodes = DataSupport.findAll(CityCode.class);
        if (cityCodes.size() > 0 ){
            for (CityCode cityCode : cityCodes){
                cityList.add(cityCode.getCityName());
            }
        }
        adapter = new MyListAdapter(cityList);
        cityManagerView.setAdapter(adapter);

        cityManagerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("position",position);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.city_manager_toolbar,menu);
        this.menu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.city_manager_toolbar_edit:
                isEditState = !isEditState;
                if (isEditState){
                    item.setTitle("确认");
                    item.setIcon(R.drawable.ic_done_white_48dp);
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                }else {
                    item.setTitle("编辑");
                    item.setIcon(R.drawable.ic_create_white_48dp);
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    if (MyListAdapter.CHANGE_TAG == 1){
                        adapter.changeData();
                        adapter.CHANGE_TAG = 0;
                    }
                }
                adapter.setIsEditState(isEditState);
                adapter.notifyDataSetChanged();
                break;
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (isEditState){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            MenuItem item = menu.getItem(0);
            item.setTitle("编辑");
            item.setIcon(R.drawable.ic_create_white_48dp);
            if (MyListAdapter.CHANGE_TAG == 1){
                adapter.changeData();
                adapter.CHANGE_TAG = 0;
            }
            isEditState = !isEditState;
            adapter.setIsEditState(isEditState);
            adapter.notifyDataSetChanged();
        }else {
            super.onBackPressed();
        }
    }
}
