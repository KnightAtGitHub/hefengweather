package com.knight.hefengweather.view.hefengapitest;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.knight.hefengweather.R;
import com.knight.hefengweather.hefengweatherapi.HefengWeatherApi;
import com.knight.hefengweather.hefengweatherapi.model.weather.Daily_Forecast;
import com.knight.hefengweather.hefengweatherapi.model.weather.Daily_forecast;
import com.knight.hefengweather.hefengweatherapi.model.weather.Hourly_Forecast;
import com.knight.hefengweather.hefengweatherapi.model.weather.Hourly_forecast;
import com.knight.hefengweather.hefengweatherapi.model.weather.NOW;
import com.knight.hefengweather.hefengweatherapi.model.weather.SUGGESTION;
import com.knight.hefengweather.hefengweatherapi.model.weather.Weather;
import com.knight.hefengweather.util.L;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by knight on 17-6-14.
 */

public class WeatherDataFragment extends Fragment {
    private String yourCityId;
    private String weatherKind;
    ItemArrayAdapter itemArrayAdapter;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case HefengWeatherApi.hemcFailure:
                    break;
                case HefengWeatherApi.hemcWeather:
                    refreshWeatherData();
                    break;
                case HefengWeatherApi.hemcForecast:
                    refreshForecastData();
                    break;
                case HefengWeatherApi.hemcNow:
                    refreshNOWData();
                    break;
                case HefengWeatherApi.hemcHourly:
                    refreshHourlyData();
                    break;
                case HefengWeatherApi.hemcSuggestion:
                    refreshSuggestion();
                default:
                    break;
            }
            getActivity().setTitle(weatherKind);
        }
    };

    private void refreshSuggestion() {
        List<SUGGESTION> suggestionList = HefengWeatherApi.getSuggestion(getActivity(),yourCityId);
        SUGGESTION suggestion = suggestionList.get(0);
        itemList.clear();
        Item[] items;
        items = new Item[]{
                new Item("status",suggestion.status),
                new Item("basic",""),
                new Item("city",suggestion.basic.city),
                new Item("cnty",suggestion.basic.cnty),
                new Item("id",suggestion.basic.id),
                new Item("lat",suggestion.basic.lat),
                new Item("lon",suggestion.basic.lon),
                new Item("prov",suggestion.basic.prov),
                new Item("update.loc",suggestion.basic.update.loc),
                new Item("update.utc",suggestion.basic.update.utc),
                new Item("comf.brf",suggestion.suggestion.comf.brf),
                new Item("comf.txt",suggestion.suggestion.comf.txt),
                new Item("cw.brf",suggestion.suggestion.cw.brf),
                new Item("cw.txt",suggestion.suggestion.cw.txt),
                new Item("drsg.brf",suggestion.suggestion.drsg.brf),
                new Item("drsg.txt",suggestion.suggestion.drsg.txt),
                new Item("flu.brf",suggestion.suggestion.flu.brf),
                new Item("flu.txt",suggestion.suggestion.flu.txt),
                new Item("sport.brf",suggestion.suggestion.sport.brf),
                new Item("sport.txt",suggestion.suggestion.sport.txt),
                new Item("trav.brf",suggestion.suggestion.trav.brf),
                new Item("trav.txt",suggestion.suggestion.trav.txt),
                new Item("uv.brf",suggestion.suggestion.uv.brf),
                new Item("uv.txt",suggestion.suggestion.uv.txt),
        };
        for (Item item : items){
            itemList.add(item);
        }
        itemArrayAdapter.notifyDataSetChanged();
    }

    private void refreshHourlyData() {
        List<Hourly_Forecast> hourly_ForecastList = HefengWeatherApi.getHourly(getActivity(),yourCityId);
        Hourly_Forecast hourly_Forecast = hourly_ForecastList.get(0);
        itemList.clear();
        Item[] items;
        items = new Item[]{
                new Item("status",hourly_Forecast.status),
                new Item("basic",""),
                new Item("city",hourly_Forecast.basic.city),
                new Item("cnty",hourly_Forecast.basic.cnty),
                new Item("id",hourly_Forecast.basic.id),
                new Item("lat",hourly_Forecast.basic.lat),
                new Item("lon",hourly_Forecast.basic.lon),
                new Item("prov",hourly_Forecast.basic.prov),
                new Item("update.loc",hourly_Forecast.basic.update.loc),
                new Item("update.utc",hourly_Forecast.basic.update.utc),
        };
        for (Item item : items){
            itemList.add(item);
        }
        for (Hourly_forecast hourly_forecast : hourly_Forecast.hourly_forecast){
            items = new Item[]{
                    new Item("hourly_forecast",hourly_forecast.date),
                    new Item("cond.code",hourly_forecast.cond.code),
                    new Item("cond.txt",hourly_forecast.cond.txt),
                    new Item("hum",hourly_forecast.hum),
                    new Item("pop",hourly_forecast.pop),
                    new Item("pres",hourly_forecast.pres),
                    new Item("tmp",hourly_forecast.tmp),
                    new Item("wind.deg",hourly_forecast.wind.deg),
                    new Item("wind.dir",hourly_forecast.wind.dir),
                    new Item("wind.sc",hourly_forecast.wind.sc),
                    new Item("wind.spd",hourly_forecast.wind.spd),

            };
            for (Item item : items){
                itemList.add(item);
            }
        }
        itemArrayAdapter.notifyDataSetChanged();

    }

    private void refreshForecastData() {
        List<Daily_Forecast> dailyForecastList = HefengWeatherApi.getForecast(getActivity(),yourCityId);
        Daily_Forecast daily_Forecast = dailyForecastList.get(0);
        itemList.clear();
        Item[] items = new Item[]{
                new Item("status", daily_Forecast.status),

                new Item("basic",""),
                new Item("city", daily_Forecast.basic.city),
                new Item("cnty", daily_Forecast.basic.cnty),
                new Item("id", daily_Forecast.basic.id),
                new Item("lat", daily_Forecast.basic.lat),
                new Item("lon", daily_Forecast.basic.lon),
                new Item("prov", daily_Forecast.basic.prov),
                new Item("update.loc", daily_Forecast.basic.update.loc),
                new Item("update.utc", daily_Forecast.basic.update.utc),

        };
        for (Item item : items){
            itemList.add(item);
        }
        for (Daily_forecast daily_forecast : daily_Forecast.daily_forecast){
            items = new Item[]{
                    new Item("daily_forecast",daily_forecast.date),
                    new Item("astro.mr",daily_forecast.astro.mr),
                    new Item("astro.ms",daily_forecast.astro.ms),
                    new Item("astro.sr",daily_forecast.astro.sr),
                    new Item("astro.ss",daily_forecast.astro.ss),
                    new Item("cond.code_d",daily_forecast.cond.code_d),
                    new Item("cond.code_n",daily_forecast.cond.code_n),
                    new Item("cond.txt_d",daily_forecast.cond.txt_d),
                    new Item("cond.txt_n",daily_forecast.cond.txt_n),


                    new Item("hum",daily_forecast.hum),
                    new Item("pcpn",daily_forecast.pcpn),
                    new Item("pop",daily_forecast.pop),
                    new Item("pres",daily_forecast.pres),
                    new Item("tmp.max",daily_forecast.tmp.max),
                    new Item("tmp.min",daily_forecast.tmp.min),
                    new Item("vis",daily_forecast.vis),
                    new Item("wind.deg",daily_forecast.wind.deg),
                    new Item("wind.dir",daily_forecast.wind.dir),
                    new Item("wind.sc",daily_forecast.wind.sc),
                    new Item("wind.spd",daily_forecast.wind.spd),
            };
            for (Item item : items){
                itemList.add(item);
            }
        }

        itemArrayAdapter.notifyDataSetChanged();
    }

    private void refreshNOWData() {
        L.i("refreshNOWData");
        List<NOW> nowList = HefengWeatherApi.getNow(getActivity(),yourCityId);
        NOW now = nowList.get(0);
        itemList.clear();
        Item item;

        item = new Item("status",now.status);
        itemList.add(item);

        item = new Item("basic","");
        itemList.add(item);

        item = new Item("city",now.basic.city);
        itemList.add(item);

        item = new Item("cnty",now.basic.cnty);
        itemList.add(item);

        item = new Item("id",now.basic.id);
        itemList.add(item);

        item = new Item("lat",now.basic.lat);
        itemList.add(item);

        item = new Item("lon",now.basic.lon);
        itemList.add(item);

        item = new Item("update.loc",now.basic.update.loc);
        itemList.add(item);

        item = new Item("update.utc",now.basic.update.utc);
        itemList.add(item);

        item = new Item("now","");
        itemList.add(item);

        item = new Item("cond.code",now.now.cond.code);
        itemList.add(item);

        item = new Item("cond.txt",now.now.cond.txt);
        itemList.add(item);

        item = new Item("fl",now.now.fl);
        itemList.add(item);

        item = new Item("hum",now.now.hum);
        itemList.add(item);

        item = new Item("pcpn",now.now.pcpn);
        itemList.add(item);

        item = new Item("pres",now.now.pres);
        itemList.add(item);

        item = new Item("tmp",now.now.tmp);
        itemList.add(item);

        item = new Item("vis",now.now.vis);
        itemList.add(item);

        item = new Item("wind.deg",now.now.wind.deg);
        itemList.add(item);

        item = new Item("wind.dir",now.now.wind.dir);
        itemList.add(item);

        item = new Item("wind.sc",now.now.wind.sc);
        itemList.add(item);

        item = new Item("wind.spd",now.now.wind.spd);
        itemList.add(item);




        itemArrayAdapter.notifyDataSetChanged();
        getActivity().setTitle(now.basic.city);
    }

    private void refreshWeatherData() {
        List<Weather> weatherList = HefengWeatherApi.getWeather(getActivity(),yourCityId);
        Weather weather = weatherList.get(0);
        itemList.clear();
        Item item;

        item = new Item("status",weather.status);
        itemList.add(item);

        item = new Item("basic","");
        itemList.add(item);

        item = new Item("city",weather.basic.city);
        itemList.add(item);

        item = new Item("cnty",weather.basic.cnty);
        itemList.add(item);

        item = new Item("id",weather.basic.id);
        itemList.add(item);

        item = new Item("lat",weather.basic.lat);
        itemList.add(item);

        item = new Item("lon",weather.basic.lon);
        itemList.add(item);

        item = new Item("update.loc",weather.basic.update.loc);
        itemList.add(item);

        item = new Item("update.utc",weather.basic.update.utc);
        itemList.add(item);

        item = new Item("aqi","");
        itemList.add(item);

        if (weather.aqi != null){
            L.i("aqi 存在");
            item = new Item("aqi",weather.aqi.city.aqi);
            itemList.add(item);

            item = new Item("co",weather.aqi.city.co);
            itemList.add(item);

            item = new Item("no2",weather.aqi.city.no2);
            itemList.add(item);

            item = new Item("o3",weather.aqi.city.o3);
            itemList.add(item);

            item = new Item("pm10",weather.aqi.city.pm10);
            itemList.add(item);

            item = new Item("pm25",weather.aqi.city.pm25);
            itemList.add(item);

            item = new Item("qlty",weather.aqi.city.qlty);
            itemList.add(item);

            item = new Item("so2",weather.aqi.city.so2);
            itemList.add(item);

        }else {
            L.i("aqi null");
        }



        for (Daily_forecast daily_forecast : weather.daily_forecast){
            item = new Item("daily_forecast",daily_forecast.date);
            itemList.add(item);

            item = new Item("astro.mr",daily_forecast.astro.mr);
            itemList.add(item);

            item = new Item("astro.ms",daily_forecast.astro.ms);
            itemList.add(item);

            item = new Item("astro.sr",daily_forecast.astro.sr);
            itemList.add(item);

            item = new Item("astro.ss",daily_forecast.astro.ss);
            itemList.add(item);

            item = new Item("hum",daily_forecast.hum);
            itemList.add(item);

            item = new Item("pcpn",daily_forecast.pcpn);
            itemList.add(item);

            item = new Item("pop",daily_forecast.pop);
            itemList.add(item);

            item = new Item("pres",daily_forecast.pres);
            itemList.add(item);

            item = new Item("tmp.max",daily_forecast.tmp.max);
            itemList.add(item);

            item = new Item("tmp.min",daily_forecast.tmp.min);
            itemList.add(item);

            item = new Item("uv",daily_forecast.uv);
            itemList.add(item);

            item = new Item("vis",daily_forecast.vis);
            itemList.add(item);

            item = new Item("wind.deg",daily_forecast.wind.deg);
            itemList.add(item);

            item = new Item("wind.dir",daily_forecast.wind.dir);
            itemList.add(item);

            item = new Item("wind.sc",daily_forecast.wind.sc);
            itemList.add(item);

            item = new Item("wind.spd",daily_forecast.wind.spd);
            itemList.add(item);

        }

        for (Hourly_forecast hourly_forecast : weather.hourly_forecast){
            item = new Item("hourly_forecast",hourly_forecast.date);
            itemList.add(item);

            item = new Item("cond.code",hourly_forecast.cond.code);
            itemList.add(item);

            item = new Item("cond.txt",hourly_forecast.cond.txt);
            itemList.add(item);

            item = new Item("hum",hourly_forecast.hum);
            itemList.add(item);

            item = new Item("pop",hourly_forecast.pop);
            itemList.add(item);

            item = new Item("pres",hourly_forecast.pres);
            itemList.add(item);

            item = new Item("tmp",hourly_forecast.tmp);
            itemList.add(item);

            item = new Item("wind.deg",hourly_forecast.wind.deg);
            itemList.add(item);

            item = new Item("wind.dir",hourly_forecast.wind.dir);
            itemList.add(item);

            item = new Item("wind.sc",hourly_forecast.wind.sc);
            itemList.add(item);

            item = new Item("wind.spd",hourly_forecast.wind.spd);
            itemList.add(item);
        }

        item = new Item("now","");
        itemList.add(item);

        item = new Item("cond.code",weather.now.cond.code);
        itemList.add(item);

        item = new Item("cond.txt",weather.now.cond.txt);
        itemList.add(item);

        item = new Item("fl",weather.now.fl);
        itemList.add(item);

        item = new Item("hum",weather.now.hum);
        itemList.add(item);

        item = new Item("pcpn",weather.now.pcpn);
        itemList.add(item);

        item = new Item("pres",weather.now.pres);
        itemList.add(item);

        item = new Item("tmp",weather.now.tmp);
        itemList.add(item);

        item = new Item("vis",weather.now.vis);
        itemList.add(item);

        item = new Item("wind.deg",weather.now.wind.deg);
        itemList.add(item);

        item = new Item("wind.dir",weather.now.wind.dir);
        itemList.add(item);

        item = new Item("wind.sc",weather.now.wind.sc);
        itemList.add(item);

        item = new Item("wind.spd",weather.now.wind.spd);
        itemList.add(item);

        item = new Item("suggestion","");
        itemList.add(item);

        item = new Item("comf.brf",weather.suggestion.comf.brf);
        itemList.add(item);

        item = new Item("comf.txt",weather.suggestion.comf.txt);
        itemList.add(item);

        item = new Item("cw.brf",weather.suggestion.cw.brf);
        itemList.add(item);

        item = new Item("cw.txt",weather.suggestion.cw.txt);
        itemList.add(item);

        item = new Item("drsg.brf",weather.suggestion.drsg.brf);
        itemList.add(item);

        item = new Item("drsg.txt",weather.suggestion.drsg.txt);
        itemList.add(item);

        item = new Item("flu.brf",weather.suggestion.flu.brf);
        itemList.add(item);

        item = new Item("flu.txt",weather.suggestion.flu.txt);
        itemList.add(item);

        item = new Item("sport.brf",weather.suggestion.sport.brf);
        itemList.add(item);

        item = new Item("sport.txt",weather.suggestion.sport.txt);
        itemList.add(item);

        item = new Item("trav.brf",weather.suggestion.trav.brf);
        itemList.add(item);

        item = new Item("trav.txt",weather.suggestion.trav.txt);
        itemList.add(item);

        item = new Item("uv.brf",weather.suggestion.uv.brf);
        itemList.add(item);

        item = new Item("uv.txt",weather.suggestion.uv.txt);
        itemList.add(item);



        itemArrayAdapter.notifyDataSetChanged();
        getActivity().setTitle(weather.basic.city);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        yourCityId = getArguments().getString("yourCityId");
        weatherKind = getArguments().getString("weatherKind");
    }
    Button updateDataBtn;
    ListView listView;
    List<Item> itemList = new ArrayList<Item>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather_data,container,false);
        updateDataBtn = (Button) view.findViewById(R.id.update_data_button);
        updateDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDate();
            }
        });
        listView = (ListView) view.findViewById(R.id.list_view);
        itemArrayAdapter = new ItemArrayAdapter(getActivity(),itemList);
        listView.setAdapter(itemArrayAdapter);

        updateDate();
        return view;
    }

    private void updateDate() {
        switch (weatherKind){
            case HefengWeatherApi.WEATHER:
                HefengWeatherApi.updateWeather(yourCityId,handler,getActivity());
                break;
            case HefengWeatherApi.FORECAST:
                HefengWeatherApi.updateForecast(yourCityId,handler,getActivity());
                break;
            case HefengWeatherApi.NOW:
                HefengWeatherApi.updateNow(yourCityId,handler,getActivity());
                break;
            case HefengWeatherApi.HOURLY:
                HefengWeatherApi.updateHourly(yourCityId,handler,getActivity());
                break;
            case HefengWeatherApi.SUGGESTION:
                HefengWeatherApi.updateSuggstion(yourCityId,handler,getActivity());
                break;
            default:
                break;
        }

    }
}
