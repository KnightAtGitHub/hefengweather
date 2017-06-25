package com.knight.hefengweather.hefengweatherapi.model.weather;

/**
 * Created by knight on 17-6-14.
 */

public class Aqi {
    public City city;
    public static class City{
        public String aqi = "空";
        public String co = "空";
        public String no2 = "空";
        public String o3 = "空";
        public String pm10 = "空";
        public String pm25 = "空";
        public String qlty = "空";
        public String so2 = "空";
    }
}
