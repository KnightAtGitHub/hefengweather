package com.knight.hefengweather.hefengweatherapi.model.weather;

/**
 * Created by knight on 17-6-14.
 */

public class Hourly_forecast {
    public Cond cond;
    public String date;
    public String hum;
    public String pop;
    public String pres;
    public String tmp;
    public Wind wind;
    public static class Cond{
        public String code;
        public String txt;
    }
}
