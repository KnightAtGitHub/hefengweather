package com.knight.hefengweather.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.knight.hefengweather.util.L;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by knight on 17-6-8.
 */

public class HefengWeatherDB {
    /*
    * 数据库名
    * */
    public static final String DB_NAME = "hefeng_weather";
    /*
    * 数据库版本
    * */
    public static final int VERSION = 1;

    private static HefengWeatherDB hefengWeatherDB;

    private SQLiteDatabase sqLiteDatabase;

    /*
    * 将构造方法私有化
    * */
    private HefengWeatherDB(Context context){
        HefengWeatherSQLiteOpenHelper dbHelper = new HefengWeatherSQLiteOpenHelper(context,DB_NAME,null,VERSION);
        sqLiteDatabase = dbHelper.getWritableDatabase();
    }
    /*
    * 获取CoolWeatherDB的实例
    * */
    public synchronized static HefengWeatherDB getInstance(Context context){
        if (hefengWeatherDB == null){
            hefengWeatherDB = new HefengWeatherDB(context);
        }
        return hefengWeatherDB;
    }

    /*
    * 将City实例存储到数据库
    * */
    public void saveCity(City city){
        if (city != null){
            ContentValues values = new ContentValues();
            values.put(HefengWeatherSQLiteOpenHelper.cityId,city.getCityId());
            values.put(HefengWeatherSQLiteOpenHelper.cityEn,city.getCityEn());
            values.put(HefengWeatherSQLiteOpenHelper.cityZh,city.getCityZh());
            values.put(HefengWeatherSQLiteOpenHelper.countryCode,city.getCountryCode());
            values.put(HefengWeatherSQLiteOpenHelper.countryEn,city.getCountryEn());
            values.put(HefengWeatherSQLiteOpenHelper.countryZh,city.getCountryZh());
            values.put(HefengWeatherSQLiteOpenHelper.provinceEn,city.getProvinceEn());
            values.put(HefengWeatherSQLiteOpenHelper.provinceZh,city.getProvinceZh());
            values.put(HefengWeatherSQLiteOpenHelper.leaderEn,city.getLeaderEn());
            values.put(HefengWeatherSQLiteOpenHelper.leaderZh,city.getLeaderZh());
            values.put(HefengWeatherSQLiteOpenHelper.lat,city.getLat());
            values.put(HefengWeatherSQLiteOpenHelper.lon,city.getLon());
            sqLiteDatabase.insert(HefengWeatherSQLiteOpenHelper.City,null,values);
        }
    }
    /*
    * 从数据库读取某省份下所有城市信息
    * */
    public List<City> loadCities(String string){
        L.i("loadCities");
        List<City> cityList = new ArrayList<City>();
        Cursor cursor = sqLiteDatabase.query(HefengWeatherSQLiteOpenHelper.City,null,
                HefengWeatherSQLiteOpenHelper.provinceZh + " = ?",
                new String[]{string},null,null,null);
        if (cursor.moveToFirst()){
            do {
                City city = new City();
                city.setCityId(cursor.getString(cursor.getColumnIndex(HefengWeatherSQLiteOpenHelper.cityId)));
                city.setCityEn(cursor.getString(cursor.getColumnIndex(HefengWeatherSQLiteOpenHelper.cityEn)));
                city.setCityZh(cursor.getString(cursor.getColumnIndex(HefengWeatherSQLiteOpenHelper.cityZh)));
                city.setCountryCode(cursor.getString(cursor.getColumnIndex(HefengWeatherSQLiteOpenHelper.countryCode)));
                city.setCountryEn(cursor.getString(cursor.getColumnIndex(HefengWeatherSQLiteOpenHelper.countryEn)));
                city.setCountryZh(cursor.getString(cursor.getColumnIndex(HefengWeatherSQLiteOpenHelper.countryZh)));
                city.setProvinceEn(cursor.getString(cursor.getColumnIndex(HefengWeatherSQLiteOpenHelper.provinceEn)));
                city.setProvinceZh(cursor.getString(cursor.getColumnIndex(HefengWeatherSQLiteOpenHelper.provinceZh)));
                city.setLeaderEn(cursor.getString(cursor.getColumnIndex(HefengWeatherSQLiteOpenHelper.leaderEn)));
                city.setLeaderZh(cursor.getString(cursor.getColumnIndex(HefengWeatherSQLiteOpenHelper.leaderZh)));
                city.setLat(cursor.getString(cursor.getColumnIndex(HefengWeatherSQLiteOpenHelper.lat)));
                city.setLon(cursor.getString(cursor.getColumnIndex(HefengWeatherSQLiteOpenHelper.lon)));
                cityList.add(city);
            }while (cursor.moveToNext());
        }
        return cityList;
    }

    public void deleteCityById(String cityId){
        sqLiteDatabase.delete(HefengWeatherSQLiteOpenHelper.City,HefengWeatherSQLiteOpenHelper.cityId + " = ?",new String[]{cityId});
    }
    public String getCityNameById(String cityId){
        Cursor cursor = sqLiteDatabase.query(HefengWeatherSQLiteOpenHelper.City,null,
                HefengWeatherSQLiteOpenHelper.cityId + " = ?",
                new String[]{cityId},null,null,null);
        String cityName = null;
        if (cursor.moveToFirst()){
            do {
                cityName = cursor.getString(cursor.getColumnIndex(HefengWeatherSQLiteOpenHelper.cityZh));
            }while (cursor.moveToNext());
        }
        return cityName;

    }
    public String getCityIdByCityName(String cityName){
        Cursor cursor = sqLiteDatabase.query(HefengWeatherSQLiteOpenHelper.City,null,
                HefengWeatherSQLiteOpenHelper.cityZh + " = ?",
                new String[]{cityName},null,null,null);
        String cityId = null;
        if (cursor.moveToFirst()){
            do {
                cityId = cursor.getString(cursor.getColumnIndex(HefengWeatherSQLiteOpenHelper.cityId));
            }while (cursor.moveToNext());
        }
        return cityId;

    }
    public City getCityById(String cityId){
        Cursor cursor = sqLiteDatabase.query(HefengWeatherSQLiteOpenHelper.City,null,
                HefengWeatherSQLiteOpenHelper.cityId + " = ?",
                new String[]{cityId},null,null,null);
        City city = new City();
        if (cursor.moveToFirst()){
            do {
                city.setCityId(cursor.getString(cursor.getColumnIndex(HefengWeatherSQLiteOpenHelper.cityId)));
                city.setCityEn(cursor.getString(cursor.getColumnIndex(HefengWeatherSQLiteOpenHelper.cityEn)));
                city.setCityZh(cursor.getString(cursor.getColumnIndex(HefengWeatherSQLiteOpenHelper.cityZh)));
                city.setCountryCode(cursor.getString(cursor.getColumnIndex(HefengWeatherSQLiteOpenHelper.countryCode)));
                city.setCountryEn(cursor.getString(cursor.getColumnIndex(HefengWeatherSQLiteOpenHelper.countryEn)));
                city.setCountryZh(cursor.getString(cursor.getColumnIndex(HefengWeatherSQLiteOpenHelper.countryZh)));
                city.setProvinceEn(cursor.getString(cursor.getColumnIndex(HefengWeatherSQLiteOpenHelper.provinceEn)));
                city.setProvinceZh(cursor.getString(cursor.getColumnIndex(HefengWeatherSQLiteOpenHelper.provinceZh)));
                city.setLeaderEn(cursor.getString(cursor.getColumnIndex(HefengWeatherSQLiteOpenHelper.leaderEn)));
                city.setLeaderZh(cursor.getString(cursor.getColumnIndex(HefengWeatherSQLiteOpenHelper.leaderZh)));
                city.setLat(cursor.getString(cursor.getColumnIndex(HefengWeatherSQLiteOpenHelper.lat)));
                city.setLon(cursor.getString(cursor.getColumnIndex(HefengWeatherSQLiteOpenHelper.lon)));
            }while (cursor.moveToNext());
        }
        return city;

    }
    public List<String> getProvinces(){
        Cursor cursor = sqLiteDatabase.query(HefengWeatherSQLiteOpenHelper.City,null,null,null,null,null,null);
        List<String> provinceList = new ArrayList<String>();
        if (cursor.moveToFirst()){
            do {
                provinceList.add(cursor.getString(cursor.getColumnIndex(HefengWeatherSQLiteOpenHelper.provinceZh)));
            }while (cursor.moveToNext());
        }
        return removeDuplicate(provinceList);
    }
    public List<String> getLeadersByProvinceZh(String provinceZh){
        Cursor cursor = sqLiteDatabase.query(HefengWeatherSQLiteOpenHelper.City,null,
                HefengWeatherSQLiteOpenHelper.provinceZh + " = ? ",
                new String[]{provinceZh},null,null,null);
        List<String> cityList = new ArrayList<String>();
        if (cursor.moveToFirst()){
            do {
                cityList.add(cursor.getString(cursor.getColumnIndex(HefengWeatherSQLiteOpenHelper.leaderZh)));
            }while (cursor.moveToNext());
        }
        return removeDuplicate(cityList);
    }
    public List<String> getCitiesByLeaderZh(String leaderZh){
        Cursor cursor = sqLiteDatabase.query(HefengWeatherSQLiteOpenHelper.City,null,
                HefengWeatherSQLiteOpenHelper.leaderZh + " = ? ",
                new String[]{leaderZh},null,null,null);
        List<String> leaderList = new ArrayList<String>();
        if (cursor.moveToFirst()){
            do {
                leaderList.add(cursor.getString(cursor.getColumnIndex(HefengWeatherSQLiteOpenHelper.cityZh)));
            }while (cursor.moveToNext());
        }
        return removeDuplicate(leaderList);
    }

     public static List<String> removeDuplicate(List<String> list) {
         Set set = new LinkedHashSet<String>();
         set.addAll(list);
         list.clear();
         list.addAll(set);
         return list;
     }

}
