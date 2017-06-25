package com.knight.hefengweather.hefengweatherapi.model.weather;

/**
 * Created by knight on 17-6-14.
 */

public class Daily_forecast {
    public Astro astro;
    public Cond cond;
    public String date;
    public String hum;
    public String pcpn;
    public String pop;
    public String pres;
    public Tmp tmp;
    public String uv;
    public String vis;
    public Wind wind;


    public static class Cond{
        public String code_d;
        public String code_n;
        public String txt_d;
        public String txt_n;
    }


}
