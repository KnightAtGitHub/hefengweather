package com.knight.hefengweather.view;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.knight.hefengweather.R;
import com.knight.hefengweather.hefengweatherapi.HefengWeatherApi;
import com.knight.hefengweather.hefengweatherapi.model.weather.Daily_Forecast;
import com.knight.hefengweather.hefengweatherapi.model.weather.Daily_forecast;
import com.knight.hefengweather.hefengweatherapi.model.weather.Weather;
import com.knight.hefengweather.util.Psfs;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by knight on 17-6-11.
 */

public class WeatherActivity extends Activity implements View.OnClickListener {
    private Button switchCityBtn, refreshBtn;
    private TextView cityNameTv,todayWeatherTmpTv,tomorrowWeatherTmpTv,theDayAfterTomorrowTmpTv,
            todayWeatherCondTv,tomorrowWeatherCondTv,theDayAfterTomorrowCondTv;
    private ImageView todayWeatherImg,tomorrowWeatherImg,theDayAfterTomorrowImg;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private Weather weather;
    private String currentCityName;
    private String currentCityId;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case HefengWeatherApi.hemcFailure:
                    break;
                case HefengWeatherApi.hemcWeather:
                    refreshViews();
                    break;
                default:
                    break;
            }

        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPreferences.edit();
        currentCityName = sharedPreferences.getString(Psfs.currentCityName,"请选择您所在的城市");
        currentCityId = sharedPreferences.getString(Psfs.currentCityId,null);

        switchCityBtn = (Button) findViewById(R.id.switch_city);
        refreshBtn = (Button) findViewById(R.id.update_weather);
        cityNameTv = (TextView) findViewById(R.id.city_name);
        cityNameTv.setText(currentCityName);

        switchCityBtn.setOnClickListener(this);
        refreshBtn.setOnClickListener(this);
        initViews();
        updateWeatherData();
    }
    private void updateWeatherData(){
        HefengWeatherApi.updateWeather(currentCityId,handler,WeatherActivity.this);
    }
    private void refreshViews() {
        List<Weather> weatherList = HefengWeatherApi.getWeather(WeatherActivity.this,currentCityId);
        weather = weatherList.get(0);
        Daily_forecast[] daily_forecastList = weather.daily_forecast;

        Daily_forecast today = daily_forecastList[0];
        Daily_forecast tomorrow = daily_forecastList[1];
        Daily_forecast dayAfterTommorrow = daily_forecastList[2];

        todayWeatherTmpTv.setText(today.tmp.min + "/" + today.tmp.max);
        todayWeatherCondTv.setText(today.cond.txt_d);

        tomorrowWeatherTmpTv.setText(tomorrow.tmp.min + "/" + tomorrow.tmp.max);
        tomorrowWeatherCondTv.setText(tomorrow.cond.txt_d);


        theDayAfterTomorrowTmpTv.setText(dayAfterTommorrow.tmp.min + "/" + dayAfterTommorrow.tmp.max);
        theDayAfterTomorrowCondTv.setText(dayAfterTommorrow.cond.txt_d);

        int identifier;
        identifier = getResources().getIdentifier("p" + today.cond.code_d, "drawable", "com.knight.hefengweather");
        todayWeatherImg.setImageResource(identifier);
        identifier = getResources().getIdentifier("p" + tomorrow.cond.code_d,"drawable","com.knight.hefengweather");
        tomorrowWeatherImg.setImageResource(identifier);
        identifier = getResources().getIdentifier("p" + dayAfterTommorrow.cond.code_d,"drawable","com.knight.hefengweather");
        theDayAfterTomorrowImg.setImageResource(identifier);

    }

    private void initViews() {
        todayWeatherImg = (ImageView) findViewById(R.id.today_weather_img);
        todayWeatherTmpTv = (TextView) findViewById(R.id.today_weather_tmp);
        todayWeatherCondTv = (TextView) findViewById(R.id.today_weather_cond);

        tomorrowWeatherImg = (ImageView) findViewById(R.id.tomorrow_weather_img);
        tomorrowWeatherTmpTv = (TextView) findViewById(R.id.tomorrow_weather_tmp);
        tomorrowWeatherCondTv = (TextView) findViewById(R.id.tomorrow_weather_comd);

        theDayAfterTomorrowImg = (ImageView) findViewById(R.id.the_day_after_tomorrow_weather_img);
        theDayAfterTomorrowTmpTv = (TextView) findViewById(R.id.the_day_after_tomorrow_weather_tmp);
        theDayAfterTomorrowCondTv = (TextView) findViewById(R.id.the_day_after_tomorrow_weather_cond);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.switch_city:
                Intent intent = new Intent(WeatherActivity.this,ChooseAreaActivity.class);
                intent.putExtra(Psfs.isFromWeatherActivity,true);
                startActivity(intent);
                break;
            case R.id.update_weather:
                updateWeatherData();
                break;
                default:
                    break;
        }

    }
}
