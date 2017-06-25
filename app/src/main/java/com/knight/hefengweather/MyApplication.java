package com.knight.hefengweather;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.knight.hefengweather.db.City;
import com.knight.hefengweather.db.HefengWeatherDB;
import com.knight.hefengweather.util.L;
import com.knight.hefengweather.util.Psfs;
import com.knight.hefengweather.view.hefengapitest.DBTestActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by knight on 17-6-16.
 */

public class MyApplication extends Application {
    private static Context appContext;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    public static Context getContext(){
        return appContext;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        appContext = getApplicationContext();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(appContext);
        if (!sharedPreferences.getBoolean(Psfs.isCityListParsed,false)){
            parseCityList();
        }

    }

    private void parseCityList() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    InputStream inputStream = getResources().getAssets().open("china-city-list.txt");
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    StringBuffer stringBuffer = new StringBuffer();
                    String line;
                    while ((line = bufferedReader.readLine()) != null){
                        stringBuffer.append(line);
              //          L.i("line:" + line);
                        String[] words = line.split("\\s+");
                        if (words.length > 2){
                            City city = new City();
                            city.setAll(words);
                            HefengWeatherDB.getInstance(appContext).saveCity(city);
                        }
/*
                        for (String string : words){
                            L.i(string);
                        }*/
                    }
                    HefengWeatherDB.getInstance(appContext).deleteCityById("城市/地区编码");
                    L.i("finished");
                    editor = sharedPreferences.edit();
                    editor.putBoolean(Psfs.isCityListParsed,true);
                    editor.commit();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
