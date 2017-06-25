package com.knight.hefengweather.view.hefengapitest;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.knight.hefengweather.R;
import com.knight.hefengweather.hefengweatherapi.HefengWeatherApi;
import com.knight.hefengweather.hefengweatherapi.model.weather.City;
import com.knight.hefengweather.util.L;
import com.knight.hefengweather.util.Psfs;
import com.knight.hefengweather.view.WeatherActivity2;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by knight on 17-6-13.
 */

public class SearchCityFragment extends Fragment {
    private View view;
    private EditText cityInputEt;
    private Button searchCityBtn;
    private ListView cityLv;
    List<String> cityNameList = new ArrayList<String>();
    ArrayAdapter<String> cityNameAdapter;

    private List<City> cityList = new ArrayList<City>();
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case HefengWeatherApi.hemcSearch:
                    L.i("hemcSearch");
                    cityList = HefengWeatherApi.getCity(getActivity());
                    cityNameList.clear();
                    for (City city : cityList){
                        if (city.status.equals("ok")){
                            cityNameList.add(city.basic.prov + "," + city.basic.city);
                        }else {
                            cityNameList.add("没有这个城市，请重新搜索");
                        }


                    }
                    cityNameAdapter.notifyDataSetChanged();
                    break;
                case HefengWeatherApi.hemcFailure:

                    break;
                    default:
                        break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("搜索城市");
        view = inflater.inflate(R.layout.fragment_choose_city,container,false);
        initView();
        cityNameAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_dropdown_item_1line,cityNameList);
        cityLv.setAdapter(cityNameAdapter);
        cityLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String cityId = cityList.get(position).basic.id;
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(Psfs.currentCityId,cityId);
                L.i("cityId:" + cityId);
                editor.apply();
                WeatherActivity2 weatherActivity2 = (WeatherActivity2) getActivity();
                weatherActivity2.drawerLayout.closeDrawers();
                weatherActivity2.swipeRefresh.setRefreshing(true);
                weatherActivity2.updateWeather();

              /*  Fragment fragment = new ChooseKindFragment();
                Bundle bundle = new Bundle();
                bundle.putString("cityCode",cityList.get(position).basic.id);
                fragment.setArguments(bundle);
                ((ApiTestActivity)getActivity()).replaceFragment(fragment);*/
            }
        });
        return view;
    }

    private void initView() {
        cityInputEt = (EditText) view.findViewById(R.id.city_input);
        searchCityBtn = (Button) view.findViewById(R.id.search_city_button);
        cityLv = (ListView) view.findViewById(R.id.city_list_view);
        searchCityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    HefengWeatherApi.updateCity(URLEncoder.encode(cityInputEt.getText().toString(),"utf-8"),handler,getActivity());
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
