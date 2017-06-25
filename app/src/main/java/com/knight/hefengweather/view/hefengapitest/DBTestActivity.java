package com.knight.hefengweather.view.hefengapitest;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.knight.hefengweather.R;
import com.knight.hefengweather.db.City;
import com.knight.hefengweather.db.HefengWeatherDB;
import com.knight.hefengweather.util.L;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Created by knight on 17-6-15.
 */

public class DBTestActivity extends AppCompatActivity {
    EditText provinceEnInputEt;
    Button queryBtn;
    TextView showTv;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dbtest);
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
                        L.i("line:" + line);
                        String[] words = line.split("\\s+");
                        if (words.length > 2){
                            City city = new City();
                            city.setAll(words);
                            HefengWeatherDB.getInstance(DBTestActivity.this).saveCity(city);
                        }

                        for (String string : words){
                            L.i(string);
                        }
                    }
                    L.i("finished");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        provinceEnInputEt = (EditText) findViewById(R.id.province_input);
        queryBtn = (Button) findViewById(R.id.query);
        showTv = (TextView) findViewById(R.id.show);

        queryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                L.i("OnClickListener");
                HefengWeatherDB hefengWeatherDB = HefengWeatherDB.getInstance(DBTestActivity.this);
                List<City> cityList = hefengWeatherDB.loadCities(provinceEnInputEt.getText().toString());
                StringBuilder stringBuilder = new StringBuilder();
                for (City city : cityList){
                    stringBuilder.append(city.getCityZh())
                            .append("\n");
                }
                showTv.setText(stringBuilder.toString());
            }
        });
    }
}
