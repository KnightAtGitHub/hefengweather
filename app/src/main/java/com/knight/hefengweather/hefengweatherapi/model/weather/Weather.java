package com.knight.hefengweather.hefengweatherapi.model.weather;

/**
 * Created by knight on 17-6-14.
 */

public class Weather {
    public Aqi aqi;
    public Basic basic;
    public Daily_forecast[] daily_forecast;
    public Hourly_forecast[] hourly_forecast;
    public Now now;
    public String status;
    public Suggestion suggestion;
}
