package com.knight.hefengweather.view;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.knight.hefengweather.R;
import com.knight.hefengweather.hefengweatherapi.HefengWeatherApi;
import com.knight.hefengweather.hefengweatherapi.model.weather.CityInfo;
import com.knight.hefengweather.util.HttpCallbackListener;
import com.knight.hefengweather.util.HttpUtil;
import com.knight.hefengweather.util.L;
import com.knight.hefengweather.util.Psfs;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by knight on 17-6-11.
 */

public class ChooseAreaActivity extends Activity {
    private static final int MY_PERMISSION_REQUEST_INTERNET = 0;
    private boolean isFromWeatherActivity;
    private EditText cityEt;
    private Button searchCityBtn;
    private ListView cityLv;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private List<CityInfo> cityInfoList = new ArrayList<CityInfo>();
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            cityInfoList = (List<CityInfo>) msg.obj;

            List<String> cityNameList = new ArrayList<String>();
            for (int i = 0 ; i < cityInfoList.size() ; i++ ){
                cityNameList.add(cityInfoList.get(i).getBasic().getCity());
            }
            ArrayAdapter<String> cityNameArrayAdapter = new ArrayAdapter<String>(ChooseAreaActivity.this,
                    android.R.layout.simple_expandable_list_item_1,cityNameList);
            cityLv.setAdapter(cityNameArrayAdapter);
            cityLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    CityInfo cityInfo = cityInfoList.get(position);
                    editor.putBoolean(Psfs.city_selected,true);
                    editor.putString(Psfs.currentCityId,cityInfo.getBasic().getId());
                    editor.putString(Psfs.currentCityName,cityInfo.getBasic().getCity());

                    editor.commit();
                    Intent intent = new Intent(ChooseAreaActivity.this,WeatherActivity2.class);
                    startActivity(intent);

                }
            });

        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.INTERNET},MY_PERMISSION_REQUEST_INTERNET);
        }
        isFromWeatherActivity = getIntent().getBooleanExtra(Psfs.isFromWeatherActivity,false);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPreferences.edit();
        if (sharedPreferences.getBoolean(Psfs.city_selected,false) && !isFromWeatherActivity){
            L.i("if");
            Intent intent = new Intent(ChooseAreaActivity.this,WeatherActivity2.class);
            startActivity(intent);
            finish();
            return;
        }
        setContentView(R.layout.activity_choose_area);
        cityEt = (EditText) findViewById(R.id.city_edit_text);
        searchCityBtn = (Button) findViewById(R.id.search_city_button);
        cityLv = (ListView) findViewById(R.id.city_list_view);
        searchCityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HttpUtil.sendHttpRequest(HefengWeatherApi.getSearchUrl(cityEt.getText().toString()), new HttpCallbackListener() {
                    @Override
                    public void onFinish(String response) {
                        List<CityInfo> cityInfoList = new ArrayList<CityInfo>();
                        try{
                            JSONObject heWeather5JsonObject = new JSONObject(response);
                            JSONArray heWeather5JsonArray = heWeather5JsonObject.getJSONArray("HeWeather5");
                            for (int i = 0 ; i < heWeather5JsonArray.length() ; i++ ){
                                CityInfo cityInfo = new CityInfo();
                                try{
                                    JSONObject cityInfoJsonObject = heWeather5JsonArray.getJSONObject(i);
                                    if (cityInfoJsonObject.getString("status").equals("ok")){
                                        cityInfo.setStatus("ok");
                                        CityInfo.Basic basic = new CityInfo.Basic();
                                        JSONObject basicJsonObject = cityInfoJsonObject.getJSONObject("basic");
                                        basic.setCity(basicJsonObject.getString("city"));
                                        basic.setCnty(basicJsonObject.getString("cnty"));
                                        basic.setId(basicJsonObject.getString("id"));
                                        basic.setLat(basicJsonObject.getString("lat"));
                                        basic.setLon(basicJsonObject.getString("lon"));
                                        basic.setProv(basicJsonObject.getString("prov"));
                                        cityInfo.setBasic(basic);
                                    }

                                }catch (Exception e){
                                    L.e(e.toString());
                                }
                                cityInfoList.add(cityInfo);
                            }
                        }catch (Exception e){
                            L.i(e.toString());
                        }
                        Message message = handler.obtainMessage();
                        message.obj = cityInfoList;
                        handler.sendMessage(message);

                    }

                    @Override
                    public void onError(Exception e) {
                        L.e(e.toString());
                    }
                });
            }
        });
    }
}
