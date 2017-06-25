package com.knight.hefengweather.view;

import android.Manifest;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.bumptech.glide.Glide;
import com.knight.hefengweather.MyApplication;
import com.knight.hefengweather.R;
import com.knight.hefengweather.db.HefengWeatherDB;
import com.knight.hefengweather.db.HefengWeatherSQLiteOpenHelper;
import com.knight.hefengweather.hefengweatherapi.HefengWeatherApi;
import com.knight.hefengweather.hefengweatherapi.model.weather.City;
import com.knight.hefengweather.hefengweatherapi.model.weather.Daily_Forecast;
import com.knight.hefengweather.hefengweatherapi.model.weather.Daily_forecast;
import com.knight.hefengweather.hefengweatherapi.model.weather.Weather;
import com.knight.hefengweather.util.FileUtil;
import com.knight.hefengweather.util.HttpCallbackListener;
import com.knight.hefengweather.util.HttpUtil;
import com.knight.hefengweather.util.L;
import com.knight.hefengweather.util.Psfs;
import com.knight.hefengweather.view.hefengapitest.SearchCityFragment;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by knight on 17-6-16.
 */

public class WeatherActivity2 extends AppCompatActivity {
    private final static int PermissionsRequestCode = 1;
    public DrawerLayout drawerLayout;

    public SwipeRefreshLayout swipeRefresh;

    private ScrollView weatherLayout;

    private Button navButton;

    private TextView titleCity;

    private TextView titleUpdateTime;

    private TextView degreeText;

    private TextView weatherInfoText;

    private LinearLayout forecastLayout;

    private TextView aqiText;

    private TextView pm25Text;

    private TextView comfortText;

    private TextView carWashText;

    private TextView sportText;

    private ImageView bingPicImg;

    private String currentCityName;
    private String currentCityId;

    public LocationClient locationClient = null;
    public BDLocationListener myListener = new MyLocationListener();
    SharedPreferences sharedPreferences;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case HefengWeatherApi.hemcFailure:

                    break;
                case HefengWeatherApi.hemcWeather:
                    L.i("hemcWeather");
                    refreshView();
                    break;
                case HefengWeatherApi.hemcSearch:
                    L.i("hemcSearch");
                    List<City> cityList = HefengWeatherApi.getCity(WeatherActivity2.this);
                    City city = cityList.get(0);
                    L.i("city status:" + city.status);
                    if (city.status.equals("ok")){
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(Psfs.currentCityId,city.basic.id);
                        editor.putString(Psfs.currentCityName,city.basic.city);
                        editor.apply();
                        updateWeather();

                    }else {

                    }


                    break;

            }
            swipeRefresh.setRefreshing(false);

        }
    };

    private void refreshView() {
        L.i("refreshView");

        List<Weather> weatherList = HefengWeatherApi.getWeather(WeatherActivity2.this,currentCityId);
        Weather weather = weatherList.get(0);
        L.i("weather status:" + weather.status);
        if (weather.status.equals("ok")){
            L.i("titleCity:" + weather.basic.city);
            titleCity.setText(weather.basic.city);
            titleUpdateTime.setText(weather.basic.update.loc);

            degreeText.setText(weather.now.tmp + "℃");
            weatherInfoText.setText(weather.now.cond.txt);
            forecastLayout.removeAllViews();
            for (Daily_forecast daily_forecast : weather.daily_forecast) {
                View view = LayoutInflater.from(this).inflate(R.layout.forecast_item, forecastLayout, false);
                TextView dateText = (TextView) view.findViewById(R.id.date_text);
                TextView infoText = (TextView) view.findViewById(R.id.info_text);
                TextView maxText = (TextView) view.findViewById(R.id.max_text);
                TextView minText = (TextView) view.findViewById(R.id.min_text);
                dateText.setText(daily_forecast.date);
                infoText.setText(daily_forecast.cond.txt_d);
                maxText.setText(daily_forecast.tmp.max);
                minText.setText(daily_forecast.tmp.min);
                forecastLayout.addView(view);

            }

            if (weather.aqi != null){
                aqiText.setText(weather.aqi.city.aqi);
                pm25Text.setText(weather.aqi.city.pm25);
            }else {
                L.i("aqi is null");
                aqiText.setText("null");
                pm25Text.setText("null");
            }

            String comfort = "舒适度：" + weather.suggestion.comf.txt;
            String carWash = "洗车指数：" + weather.suggestion.cw.txt;
            String sport = "运行建议：" + weather.suggestion.sport.txt;
            comfortText.setText(comfort);
            carWashText.setText(carWash);
            sportText.setText(sport);

        }else {

        }



    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);

        }
        setContentView(R.layout.activity_weather2);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(WeatherActivity2.this);
        initView();

        //定位
        locationClient = new LocationClient(MyApplication.getContext());
        locationClient.registerLocationListener(myListener);

        L.i("Build.VERSION.SDK_INT :" + Build.VERSION.SDK_INT );
        if (Build.VERSION.SDK_INT >=23){
            myRequestPermissions();
        }else {
            requestLocation();
        }




        currentCityId = sharedPreferences.getString(Psfs.currentCityId,null);
        currentCityName = sharedPreferences.getString(Psfs.currentCityName,null);
        if (currentCityId != null){
            L.i("currentCityId:" + currentCityId);
            if (FileUtil.getStringFromFile(WeatherActivity2.this,Psfs.weatherFname + "-" + currentCityId) != null){
                L.i("isFileExists:true" );
                L.i("file:" + FileUtil.getStringFromFile(WeatherActivity2.this,Psfs.weatherFname + "-" + currentCityId) );
                refreshView();

            }else {
                L.i("isFileExists:false" );
            }

        }



    }

    private void myRequestPermissions() {
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(WeatherActivity2.this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(WeatherActivity2.this,
                Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(WeatherActivity2.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()) {
            String [] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(WeatherActivity2.this, permissions, PermissionsRequestCode);

        } else {
            requestLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PermissionsRequestCode:
                if (grantResults.length == 3){
                    requestLocation();
                }else {
                    myRequestPermissions();
                }

                break;
        }
    }

    private void requestLocation() {
        initLocation();
        locationClient.start();
    }
     private void initLocation() {
        L.i("initLocation");
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setCoorType("gcj02");
        option.setScanSpan(2000);
        option.setIsNeedAddress(true);
        option.setOpenGps(true);
        option.setLocationNotify(true);
        option.setIsNeedLocationDescribe(true);
        option.setIgnoreKillProcess(true);
        locationClient.setLocOption(option);
    }

    private void initView() {
        // 初始化各控件
        bingPicImg = (ImageView) findViewById(R.id.bing_pic_img);
     //   bingPicImg.setImageResource(R.drawable.chobechick);
        weatherLayout = (ScrollView) findViewById(R.id.weather_layout);
        titleCity = (TextView) findViewById(R.id.title_city);
        titleUpdateTime = (TextView) findViewById(R.id.title_update_time);
        degreeText = (TextView) findViewById(R.id.degree_text);
        weatherInfoText = (TextView) findViewById(R.id.weather_info_text);
        forecastLayout = (LinearLayout) findViewById(R.id.forecast_layout);
        aqiText = (TextView) findViewById(R.id.aqi_text);
        pm25Text = (TextView) findViewById(R.id.pm25_text);
        comfortText = (TextView) findViewById(R.id.comfort_text);
        carWashText = (TextView) findViewById(R.id.car_wash_text);
        sportText = (TextView) findViewById(R.id.sport_text);
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateWeather();
            }
        });
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navButton = (Button) findViewById(R.id.nav_button);
        navButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        loadBingPic();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (sharedPreferences.getBoolean(Psfs.isCityListParsed,false)){
            transaction.replace(R.id.fragment_container,new ChooseDistrictFragment());
        }else {
            transaction.replace(R.id.fragment_container,new SearchCityFragment());
        }
        transaction.commit();
    }

    public void updateWeather() {
        L.i("updateWeather");
        currentCityId = sharedPreferences.getString(Psfs.currentCityId,null);
        L.i("updateWeather currentCityId:" + currentCityId);
        HefengWeatherApi.updateWeather(currentCityId,handler,WeatherActivity2.this);

    }

    private void loadBingPic() {
        String bingPic = sharedPreferences.getString(Psfs.bingPic,"android.resource://com.knight.hefengweather/drawable/chobechick");
        if ( bingPic !=null){
            Glide.with(WeatherActivity2.this).load(bingPic).into(bingPicImg);
        }else {
            HttpUtil.sendHttpRequest("http://guolin.tech/api/bing_pic", new HttpCallbackListener() {
                @Override
                public void onFinish(final String response) {
                    L.i("bingPic" + response);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(Psfs.bingPic,response);
                    editor.apply();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Glide.with(WeatherActivity2.this).load(response).into(bingPicImg);
                        }
                    });


                }

                @Override
                public void onError(Exception e) {

                }
            });

        }
    }

    public class MyLocationListener implements BDLocationListener{

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            L.i("erro code:" + bdLocation.getLocType());
            StringBuilder sb = new StringBuilder();
            sb.append(bdLocation.getAddress())
                    .append(bdLocation.getCity())
                    .append(bdLocation.getCityCode())
                    .append(bdLocation.getDistrict());
            L.i(sb.toString());
            if (bdLocation.getDistrict() != null){
                L.i("getDistrict:" + bdLocation.getDistrict());

                try {
                    HefengWeatherApi.updateCity(URLEncoder.encode(bdLocation.getDistrict(),"utf-8"),handler,WeatherActivity2.this);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                locationClient.stop();
            }else {
                locationClient.start();
                L.i("暂未定位");

            }

        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    }

}
