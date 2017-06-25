package com.knight.hefengweather.hefengweatherapi;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.knight.hefengweather.hefengweatherapi.model.weather.City;
import com.knight.hefengweather.hefengweatherapi.model.weather.Daily_Forecast;
import com.knight.hefengweather.hefengweatherapi.model.weather.Hourly_Forecast;
import com.knight.hefengweather.hefengweatherapi.model.weather.NOW;

import com.knight.hefengweather.hefengweatherapi.model.weather.SUGGESTION;
import com.knight.hefengweather.hefengweatherapi.model.weather.Weather;
import com.knight.hefengweather.util.FileUtil;
import com.knight.hefengweather.util.HttpCallbackListener;
import com.knight.hefengweather.util.HttpUtil;
import com.knight.hefengweather.util.L;
import com.knight.hefengweather.util.Psfs;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by knight on 17-6-9.
 */

public class HefengWeatherApi {
    public static final String KEY = "4662a05c54b64ad5b2c0ebc1999e7670";

    public static final String ADDRESS_V5 = "https://free-api.heweather.com/v5/";
    public static final String WEATHER = "weather";
    public static final String FORECAST = "forecast";
    public static final String NOW = "now";
    public static final String HOURLY = "hourly";
    public static final String SUGGESTION = "suggestion";
    public static final String SCENIC = "scenic";
    public static final String ALARM = "alarm";

    public static final String SEARCH = "search";

    /*
    * handler empty message code
    * */
    public static final int hemcFailure = 0;
    public static final int hemcWeather = 1;
    public static final int hemcForecast = 2;
    public static final int hemcNow = 3;
    public static final int hemcHourly = 4;
    public static final int hemcSuggestion = 5;
    public static final int hemcScenic = 6;
    public static final int hemcAlarm = 7;
    public static final int hemcSearch = 8;

    /*
    * 所有请求格式一样，都是 地址+请求类型+city+key
    * 其中City可通过城市中英文名称、ID、IP和经纬度进行查询，经纬度查询格式为：经度,纬度，
    * 例如city=北京，city=beijing，city=CN101010100，city= 60.194.130.1，city=120.343,36.088。
    * */
    public static final String getCommonUrl(String kind,String yourcity){
        StringBuilder url = new StringBuilder();
        return url.append(ADDRESS_V5)
                .append(kind)
                .append("?city=")
                .append(yourcity)
                .append("&key=")
                .append(KEY)
                .toString();
    }

    public static String getWeatherUrl(String yourcity){
        return getCommonUrl(WEATHER,yourcity);
    }
    public static String getForecastUrl(String yourcity){
        return getCommonUrl(FORECAST,yourcity);
    }
    public static String getNowUrl(String yourcity){
        return getCommonUrl(NOW,yourcity);
    }
    public static String getHourlyUrl(String yourcity){
        return getCommonUrl(HOURLY,yourcity);
    }
    public static String getSuggestionUrl(String yourcity){
        return getCommonUrl(SUGGESTION,yourcity);
    }
    public static String getScenicUrl(String yourcity){
        return getCommonUrl(SCENIC,yourcity);
    }
    public static String getAlarmUrl(String yourcity){
        return getCommonUrl(ALARM,yourcity);
    }

    public static String getSearchUrl(String yourcity){
        return getCommonUrl(SEARCH,yourcity);
    }
    public static List<Weather> parseWeatherFromString(String string){
        List<Weather> weatherList = new ArrayList<Weather>();
        try {
            JSONObject HeWeather5JjsonObject = new JSONObject(string);
            Gson gson = new Gson();
            weatherList = gson.fromJson(HeWeather5JjsonObject.getString("HeWeather5"),new TypeToken<List<Weather>>(){}.getType());
            return weatherList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }
    public static List<Daily_Forecast> parseForecastFromString(String string){
        List<Daily_Forecast> dailyForecastList = new ArrayList<Daily_Forecast>();
        try {
            JSONObject HeWeather5JsonObject = new JSONObject(string);
            Gson gson = new Gson();
            dailyForecastList = gson.fromJson(HeWeather5JsonObject.getString("HeWeather5"),new TypeToken<List<Daily_Forecast>>(){}.getType());
            return dailyForecastList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static List<Hourly_Forecast> parseHourlyForecastFromString(String string){
        List<Hourly_Forecast> hourly_ForecastList = new ArrayList<Hourly_Forecast>();
        try {
            JSONObject jsonObject = new JSONObject(string);
            Gson gson = new Gson();
            hourly_ForecastList = gson.fromJson(jsonObject.getString("HeWeather5"),new TypeToken<List<Hourly_Forecast>>(){}.getType());
            return hourly_ForecastList;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static List<com.knight.hefengweather.hefengweatherapi.model.weather.SUGGESTION> parseSuggestionFromString(String string){
        List<SUGGESTION> suggestionList = new ArrayList<SUGGESTION>();
        try {
            JSONObject jsonObject = new JSONObject(string);
            Gson gson = new Gson();
            suggestionList = gson.fromJson(jsonObject.getString("HeWeather5"),new TypeToken<List<SUGGESTION>>(){}.getType());
            return suggestionList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    /*public static List<Weather> parseWeatherFromString(String string){
        List<Weather> weatherList = new ArrayList<Weather>();
        try {
                *//*
                * 开始解析json数据
                * *//*
                *//*
                * 获取第一层jsonObject：HeWeather5
                * *//*

            JSONObject rootJsonObject = new JSONObject(string);
                *//*
                * 获取HeWeather5下的数组JsonObject对象,即cityweather数组
                * *//*
            JSONArray cityWeatherJsonArray = rootJsonObject.getJSONArray("HeWeather5");


            int length = cityWeatherJsonArray.length();
            L.i("length:" + length);

            for (int i = 0; i < cityWeatherJsonArray.length() ; i++ ){
                    *//*
                    * 解析Weather，即各城市的全部天气
                    * *//*
                Weather weather = new Weather();
                JSONObject cityWeatherJsonObject = cityWeatherJsonArray.getJSONObject(i);
                    *//*
                    * get alarms array
                    * *//*
                List<Weather.Alarms> alarmsList = new ArrayList<Weather.Alarms>();
                try{
                    JSONArray alarmsJsonArray = cityWeatherJsonObject.getJSONArray("alarms");
                    for (int j = 0; j < alarmsJsonArray.length() ; j++ ){
                        Weather.Alarms alarms = new Weather.Alarms();
                        try{
                            JSONObject alarmsJsonObject = alarmsJsonArray.getJSONObject(j);
                            alarms.setLevel(alarmsJsonObject.getString("level"));
                            alarms.setStat(alarmsJsonObject.getString("stat"));
                            alarms.setTitle(alarmsJsonObject.getString("title"));
                            alarms.setTxt(alarmsJsonObject.getString("txt"));
                            alarms.setType(alarmsJsonObject.getString("type"));

                        }catch (Exception e){
                            L.i(e.toString());
                        }
                        alarmsList.add(alarms);
                    }


                }catch (Exception e){
                    L.i(e.toString());
                }
                weather.setAlarmsList(alarmsList);
                    *//*
                    * get aqi
                    * *//*
                Weather.Aqi aqi = new Weather.Aqi();
                try{
                    JSONObject aqiJsonObject = cityWeatherJsonObject.getJSONObject("aqi");
                    L.i("aqiJsonObject:" + aqiJsonObject.toString());
                    JSONObject cityJsonObject = aqiJsonObject.getJSONObject("city");
                    L.i("cityJsonObject:" + cityJsonObject);
                    Weather.Aqi.City city = new Weather.Aqi.City();
                    city.setAqi(cityJsonObject.getString("aqi"));
                    city.setPm10(cityJsonObject.getString("pm10"));
                    city.setPm25(cityJsonObject.getString("pm25"));
                    city.setQlty(cityJsonObject.getString("qlty"));
                    aqi.setCity(city);

                }catch (Exception e){
                    L.i(e.toString());
                }
                weather.setAqi(aqi);
                    *//*
                    * get basic
                    * *//*
                Weather.Basic basic = new Weather.Basic();
                try{
                    JSONObject basicJsonObject = cityWeatherJsonObject.getJSONObject("basic");
                    basic.setCity(basicJsonObject.getString("city"));
                    basic.setCnty(basicJsonObject.getString("cnty"));
                    basic.setId(basicJsonObject.getString("id"));
                    basic.setLat(basicJsonObject.getString("lat"));
                    basic.setLon(basicJsonObject.getString("lon"));
                    JSONObject updateJsonObject = basicJsonObject.getJSONObject("update");
                    Weather.Basic.Update update = new Weather.Basic.Update();
                    update.setLoc(updateJsonObject.getString("loc"));
                    update.setUtc(updateJsonObject.getString("utc"));
                    basic.setUpdate(update);
                }catch (Exception e){
                    L.i(e.toString());
                }
                weather.setBasic(basic);
                    *//*
                    * get daily_forecast array
                    * *//*
                List<Weather.Daily_forecast> daily_forecastList = new ArrayList<Weather.Daily_forecast>();
                try{
                    JSONArray daily_forecast_JSONArray = cityWeatherJsonObject.getJSONArray("daily_forecast");
                    for (int j = 0 ; j < daily_forecast_JSONArray.length() ; j++ ){
                            *//*
                            * get daily_forecast
                            * *//*
                        Weather.Daily_forecast daily_forecast = new Weather.Daily_forecast();
                        try{
                            JSONObject daily_forecastJsonObject = daily_forecast_JSONArray.getJSONObject(j);
                                *//*
                                * get astro
                                * *//*
                            Weather.Daily_forecast.Astro astro = new Weather.Daily_forecast.Astro();
                            JSONObject astroJsonObject = daily_forecastJsonObject.getJSONObject("astro");
                            astro.setMr(astroJsonObject.getString("mr"));
                            astro.setMs(astroJsonObject.getString("ms"));
                            astro.setSr(astroJsonObject.getString("sr"));
                            astro.setSs(astroJsonObject.getString("ss"));
                            daily_forecast.setAstro(astro);
                                *//*
                                * get cond
                                * *//*
                            Weather.Daily_forecast.Cond cond = new Weather.Daily_forecast.Cond();
                            JSONObject condJsonObject = daily_forecastJsonObject.getJSONObject("cond");
                            cond.setCode_d(condJsonObject.getString("code_d"));
                            cond.setCode_n(condJsonObject.getString("code_n"));
                            cond.setTxt_d(condJsonObject.getString("txt_d"));
                            cond.setTxt_n(condJsonObject.getString("txt_n"));
                            daily_forecast.setCond(cond);
                            daily_forecast.setDate(daily_forecastJsonObject.getString("date"));
                            daily_forecast.setHum(daily_forecastJsonObject.getString("hum"));
                            daily_forecast.setPcpn(daily_forecastJsonObject.getString("pcpn"));
                            daily_forecast.setPop(daily_forecastJsonObject.getString("pop"));
                            daily_forecast.setPres(daily_forecastJsonObject.getString("pres"));
                                *//*
                                * get tmp
                                * *//*
                            Weather.Daily_forecast.Tmp tmp = new Weather.Daily_forecast.Tmp();
                            JSONObject tmpJsonObject = daily_forecastJsonObject.getJSONObject("tmp");
                            tmp.setMax(tmpJsonObject.getString("max"));
                            tmp.setMin(tmpJsonObject.getString("min"));
                            daily_forecast.setTmp(tmp);

                            daily_forecast.setUv(daily_forecastJsonObject.getString("uv"));
                            daily_forecast.setVis(daily_forecastJsonObject.getString("vis"));
                                *//*
                                * get wind
                                * *//*
                            Weather.Wind wind = new Weather.Wind();
                            JSONObject windJsonObject = daily_forecastJsonObject.getJSONObject("wind");
                            wind.setDeg(windJsonObject.getString("deg"));
                            wind.setDir(windJsonObject.getString("dir"));
                            wind.setSc(windJsonObject.getString("sc"));
                            wind.setSpd(windJsonObject.getString("spd"));
                            daily_forecast.setWind(wind);

                        }catch (Exception e){
                            L.i(e.toString());
                        }
                        daily_forecastList.add(daily_forecast);
                    }

                }catch (Exception e){
                    L.i(e.toString());
                }
                weather.setDaily_forecastList(daily_forecastList);
                    *//*
                    * get hourly_forecast array
                    * *//*
                List<Weather.Hourly_forecast> hourly_forecastList = new ArrayList<Weather.Hourly_forecast>();
                JSONArray hourly_forecast_jsonArray = cityWeatherJsonObject.getJSONArray("hourly_forecast");
                for (int j = 0 ; j < hourly_forecast_jsonArray.length() ; j++ ){
                        *//*
                        * 解析hourly_forecast
                        * *//*
                    Weather.Hourly_forecast hourly_forecast = new Weather.Hourly_forecast();
                    try{
                        JSONObject hourly_forecast_jsonObject = hourly_forecast_jsonArray.getJSONObject(j);
                            *//*
                            * get cond
                            * *//*
                        Weather.Hourly_forecast.Cond cond = new Weather.Hourly_forecast.Cond();
                        JSONObject condJsonObject = hourly_forecast_jsonObject.getJSONObject("cond");
                        cond.setCode(condJsonObject.getString("code"));
                        cond.setTxt(condJsonObject.getString("txt"));
                        hourly_forecast.setCond(cond);
                        hourly_forecast.setDate(hourly_forecast_jsonObject.getString("date"));
                        hourly_forecast.setHum(hourly_forecast_jsonObject.getString("hum"));
                        hourly_forecast.setPop(hourly_forecast_jsonObject.getString("pop"));
                        hourly_forecast.setPres(hourly_forecast_jsonObject.getString("pres"));
                        hourly_forecast.setTmp(hourly_forecast_jsonObject.getString("tmp"));
                        Weather.Wind wind = new Weather.Wind();
                        JSONObject windJsonObject = hourly_forecast_jsonObject.getJSONObject("wind");
                        wind.setDeg(windJsonObject.getString("deg"));
                        wind.setDir(windJsonObject.getString("dir"));
                        wind.setSc(windJsonObject.getString("sc"));
                        wind.setSpd(windJsonObject.getString("spd"));
                        hourly_forecast.setWind(wind);


                    }catch (Exception e){
                        L.i(e.toString());
                    }
                    hourly_forecastList.add(hourly_forecast);
                }
                    *//*
                    * get now
                    * *//*
                Weather.Now now = new Weather.Now();
                try{
                    JSONObject nowJsonObject = cityWeatherJsonObject.getJSONObject("now");
                    Weather.Now.Cond cond = new Weather.Now.Cond();
                    JSONObject condJsonObject = nowJsonObject.getJSONObject("cond");
                    cond.setCode(condJsonObject.getString("code"));
                    cond.setTxt(condJsonObject.getString("txt"));
                    now.setCond(cond);
                    now.setFl(nowJsonObject.getString("fl"));
                    now.setHum(nowJsonObject.getString("hum"));
                    now.setPcpn(nowJsonObject.getString("pcpn"));
                    now.setPres(nowJsonObject.getString("pres"));
                    now.setTmp(nowJsonObject.getString("tmp"));
                    now.setVis(nowJsonObject.getString("vis"));
                    Weather.Wind wind = new Weather.Wind();
                    JSONObject windJsonObject = nowJsonObject.getJSONObject("wind");
                    wind.setDeg(windJsonObject.getString("deg"));
                    wind.setDir(windJsonObject.getString("dir"));
                    wind.setSc(windJsonObject.getString("sc"));
                    wind.setSpd(windJsonObject.getString("spd"));
                    now.setWind(wind);
                }catch (Exception e){
                    L.i(e.toString());
                }
                weather.setNow(now);

                weather.setStatus(cityWeatherJsonObject.getString("status"));

                    *//*
                    * get suggestion
                    * *//*
                Weather.Suggestion suggestion = new Weather.Suggestion();
                try {
                    JSONObject suggestionJsonObject = cityWeatherJsonObject.getJSONObject("suggestion");
                        *//*
                        * get comf,cw,drsg...
                        * *//*
                    Weather.Suggestion.CommonSuggestion comf,cw,drsg,flu,sport,traw,uv;

                    comf = new Weather.Suggestion.CommonSuggestion();
                    JSONObject comfJsonObject = suggestionJsonObject.getJSONObject("comf");
                    comf.setBrf(comfJsonObject.getString("brf"));
                    comf.setTxt(comfJsonObject.getString("txt"));
                    suggestion.setComf(comf);

                    cw = new Weather.Suggestion.CommonSuggestion();
                    JSONObject cwJsonObject = suggestionJsonObject.getJSONObject("cw");
                    cw.setBrf(cwJsonObject.getString("brf"));
                    cw.setTxt(cwJsonObject.getString("txt"));
                    suggestion.setCw(cw);

                    drsg = new Weather.Suggestion.CommonSuggestion();
                    JSONObject drsgJsonObject = suggestionJsonObject.getJSONObject("drsg");
                    drsg.setBrf(drsgJsonObject.getString("brf"));
                    drsg.setTxt(drsgJsonObject.getString("txt"));
                    suggestion.setDrsg(drsg);

                    flu = new Weather.Suggestion.CommonSuggestion();
                    JSONObject fluJsonObject = suggestionJsonObject.getJSONObject("flu");
                    flu.setBrf(fluJsonObject.getString("brf"));
                    flu.setTxt(fluJsonObject.getString("txt"));
                    suggestion.setFlu(flu);

                    sport = new Weather.Suggestion.CommonSuggestion();
                    JSONObject sportJsonObject = suggestionJsonObject.getJSONObject("sport");
                    sport.setBrf(sportJsonObject.getString("brf"));
                    sport.setTxt(sportJsonObject.getString("txt"));
                    suggestion.setSport(sport);

                    traw = new Weather.Suggestion.CommonSuggestion();
                    JSONObject trawJsonObject = suggestionJsonObject.getJSONObject("traw");
                    traw.setBrf(trawJsonObject.getString("brf"));
                    traw.setTxt(trawJsonObject.getString("txt"));
                    suggestion.setTraw(traw);

                    uv = new Weather.Suggestion.CommonSuggestion();
                    JSONObject uvJsonObject = suggestionJsonObject.getJSONObject("uv");
                    uv.setBrf(uvJsonObject.getString("brf"));
                    uv.setTxt(uvJsonObject.getString("txt"));
                    suggestion.setUv(uv);

                }catch (Exception e){
                    L.i(e.toString());
                }
                weather.setSuggestion(suggestion);
                weatherList.add(weather);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return weatherList;

    }*/

    public static List<com.knight.hefengweather.hefengweatherapi.model.weather.NOW> parseNowFromString(String string){
        List<NOW> nowList = new ArrayList<NOW>();
        try {
            JSONObject HeWeather5jsonObject = new JSONObject(string);
            Gson gson = new Gson();
            nowList = gson.fromJson(HeWeather5jsonObject.getString("HeWeather5"),new TypeToken<List<NOW>>(){}.getType());
            L.i("test:" + nowList.get(0).status);
            return nowList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
    public static List<Weather> getWeather(Context context,String yourCityId){
        List<Weather> weatherList = new ArrayList<Weather>();
        String response = FileUtil.getStringFromFile(context,Psfs.weatherFname + "-" + yourCityId);
        weatherList = parseWeatherFromString(response);
        return weatherList;
    }
    public static List<Daily_Forecast> getForecast(Context context,String yourCityId){
        List<Daily_Forecast> dailyForecastList = new ArrayList<Daily_Forecast>();
        String response = FileUtil.getStringFromFile(context,Psfs.forecastFname + "-" + yourCityId);
        dailyForecastList = parseForecastFromString(response);
        return dailyForecastList;
    }
    public static List<NOW> getNow(Context context,String yourCityId){
        List<NOW> nowList = new ArrayList<NOW>();
        String response = FileUtil.getStringFromFile(context,Psfs.nowFname + "-" + yourCityId);
        nowList = parseNowFromString(response);
        return nowList;
    }
    public static List<Hourly_Forecast> getHourly(Context context,String yourCityId){
        List<Hourly_Forecast> hourlyForecastList = new ArrayList<Hourly_Forecast>();
        String response = FileUtil.getStringFromFile(context,Psfs.hourlyFname + "-" + yourCityId);
        hourlyForecastList = parseHourlyForecastFromString(response);
        return hourlyForecastList;
    }
    public static List<SUGGESTION> getSuggestion(Context context,String yourCityId){
        List<SUGGESTION> suggestionList = new ArrayList<SUGGESTION>();
        String response = FileUtil.getStringFromFile(context,Psfs.suggestionFname+ "-" + yourCityId );
        suggestionList = parseSuggestionFromString(response);
        return suggestionList;
    }
    public static List<City> getCity(Context context){
        List<City> cityList = new ArrayList<City>();
        String response = FileUtil.getStringFromFile(context,Psfs.searchFname);
        try {
            JSONObject Heweather5jsonObject = new JSONObject(response);
            Gson gson = new Gson();
            cityList = gson.fromJson(Heweather5jsonObject.getString("HeWeather5"),new TypeToken<List<City>>(){}.getType());

            return cityList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static void updateWeather(final String yourCityId, final Handler handler, final Context context){

        HttpUtil.sendHttpRequest(getWeatherUrl(yourCityId), new HttpCallbackListener() {

            @Override
            public void onFinish(String response) {
                if (FileUtil.saveStringToFile(context,response,Psfs.weatherFname + "-" + yourCityId)){
                    handler.sendEmptyMessage(hemcWeather);
                }else {
                    handler.sendEmptyMessage(hemcFailure);
                }
            }

            @Override
            public void onError(Exception e) {
                handler.sendEmptyMessage(hemcFailure);
            }
        });
    }
    public static void updateForecast(final String yourCityId, final Handler handler, final Context context){
        HttpUtil.sendHttpRequest(getForecastUrl(yourCityId), new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                if (FileUtil.saveStringToFile(context,response,Psfs.forecastFname + "-" + yourCityId)){
                    handler.sendEmptyMessage(hemcForecast);
                }else {
                    handler.sendEmptyMessage(hemcFailure);
                }
            }

            @Override
            public void onError(Exception e) {
                handler.sendEmptyMessage(hemcFailure);

            }
        });
    }
    public static void updateNow(final String yourCityId, final Handler handler, final Context context){
        HttpUtil.sendHttpRequest(getNowUrl(yourCityId), new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                if (FileUtil.saveStringToFile(context,response,Psfs.nowFname + "-" + yourCityId)){
                    handler.sendEmptyMessage(hemcNow);
                }else {
                    handler.sendEmptyMessage(hemcFailure);
                }
            }

            @Override
            public void onError(Exception e) {
                    handler.sendEmptyMessage(hemcFailure);
            }
        });
    }
    public static void updateHourly(final String yourCityId, final Handler handler, final Context context){
        HttpUtil.sendHttpRequest(getHourlyUrl(yourCityId), new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                if (FileUtil.saveStringToFile(context,response,Psfs.hourlyFname + "-" + yourCityId)){
                    handler.sendEmptyMessage(hemcHourly);
                }else {
                    handler.sendEmptyMessage(hemcFailure);
                }
            }

            @Override
            public void onError(Exception e) {
                handler.sendEmptyMessage(hemcFailure);
            }
        });
    }
    public static void updateSuggstion(final String yourCityId, final Handler handler, final Context context){
        HttpUtil.sendHttpRequest(getSuggestionUrl(yourCityId), new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                if (FileUtil.saveStringToFile(context,response,Psfs.suggestionFname + "-" + yourCityId)){
                    handler.sendEmptyMessage(hemcSuggestion);
                }else {
                    handler.sendEmptyMessage(hemcFailure);
                }
            }

            @Override
            public void onError(Exception e) {
                handler.sendEmptyMessage(hemcFailure);
            }
        });
    }

    public static void updateCity(String yourCity,final Handler handler,final Context context){

        HttpUtil.sendHttpRequest(getSearchUrl(yourCity), new HttpCallbackListener() {

            @Override
            public void onFinish(String response) {
                if (FileUtil.saveStringToFile(context,response,Psfs.searchFname)){
                    handler.sendEmptyMessage(hemcSearch);
                }else {
                    handler.sendEmptyMessage(hemcFailure);
                }
            }

            @Override
            public void onError(Exception e) {
                handler.sendEmptyMessage(hemcFailure);
            }
        });
    }

    public static void getHefengData(String hefengUrl,final Handler handler){
        HttpUtil.sendHttpRequest(hefengUrl, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {

                Message message = handler.obtainMessage();
                message.obj = response;
                handler.sendMessage(message);
            }

            @Override
            public void onError(Exception e) {
                Message message = handler.obtainMessage();
                message.obj = e.toString();
                handler.sendMessage(message);
            }
        });
    }





}
