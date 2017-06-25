package com.knight.hefengweather.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by knight on 17-6-8.
 */

public class HefengWeatherSQLiteOpenHelper extends SQLiteOpenHelper {
    /*
    * common table word
    * */
    public static final String create = "create";
    public static final String table = "table";
    public static final String id_integer_primary_key_autoincrement = "id integer primary key autoincrement";

    /*
    * city 建表语句
    * */
    public static final String City = "City";
    public static final String cityId = "cityId";
    public static final String cityEn = "cityEn";
    public static final String cityZh = "cityZh";
    public static final String countryCode = "countryCode";
    public static final String countryEn = "countryEn";
    public static final String countryZh = "countryZh";
    public static final String provinceEn = "provinceEn";
    public static final String provinceZh = "provinceZh";
    public static final String leaderEn = "leaderEn";
    public static final String leaderZh = "leaderZh";
    public static final String lat = "lat";
    public static final String lon = "lon";

    public static final String CREATE_CITY = create + " " + table + " " + City + " ("
         //   + id_integer_primary_key_autoincrement + ","
        //    + cityId + " text,"
            + "cityId text primary key,"
            + cityEn + " text,"
            + cityZh + " text,"
            + countryCode + " text,"
            + countryEn + " text,"
            + countryZh + " text,"
            + provinceEn + " text,"
            + provinceZh + " text,"
            + leaderEn + " text,"
            + leaderZh + " text,"
            + lat + " text,"
            + lon + " text)";


    public HefengWeatherSQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CITY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
