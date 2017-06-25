package com.knight.hefengweather.view;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.knight.hefengweather.R;
import com.knight.hefengweather.db.HefengWeatherDB;
import com.knight.hefengweather.util.L;
import com.knight.hefengweather.util.Psfs;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by knight on 17-6-21.
 */

public class ChooseDistrictFragment extends Fragment {
    View view;
    TextView fillBlankTv,titleTv;
    Button backBtn;
    ListView listView;
    private List<String> dataList = new ArrayList<String>();
    ArrayAdapter<String> arrayAdapter;

    public static final int LEVEL_PROVINCE = 0;

    public static final int LEVEL_LEADER = 1;

    public static final int LEVEL_CITY = 2;

    private String selectedProvince;

    private String selectedLeader;

    private String selectedCity;

    private int currentLevel;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_choose_district,container,false);
        initView();
        return view;
    }

    private void initView() {
        L.i("initView");
        fillBlankTv = (TextView) view.findViewById(R.id.fill_blank_tv);
        titleTv = (TextView) view.findViewById(R.id.title_tv);
        backBtn = (Button) view.findViewById(R.id.back_button);
        listView = (ListView) view.findViewById(R.id.list_view);

        arrayAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,dataList);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (currentLevel){
                    case LEVEL_PROVINCE:
                        selectedProvince = dataList.get(position);
                        L.i("provinceZh:" + selectedProvince);
                        queryLeaders();
                        break;
                    case LEVEL_LEADER:
                        selectedLeader = dataList.get(position);
                        L.i("selectedLeader:" + selectedLeader);
                        queryCityies();
                        break;
                    case LEVEL_CITY:
                        selectedCity = dataList.get(position);
                        L.i("selectedCity:" + selectedCity);
                        String cityId = HefengWeatherDB.getInstance(getActivity()).getCityIdByCityName(selectedCity);
                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(Psfs.currentCityId,cityId);
                        L.i("cityId:" + cityId);
                        editor.apply();
                        WeatherActivity2 weatherActivity2 = (WeatherActivity2) getActivity();
                        weatherActivity2.drawerLayout.closeDrawers();
                        weatherActivity2.swipeRefresh.setRefreshing(true);
                        weatherActivity2.updateWeather();
                        break;

                }

            }
        });
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (currentLevel){
                    case LEVEL_PROVINCE:
                        break;
                    case LEVEL_LEADER:
                        queryProvinces();
                        break;
                    case LEVEL_CITY:
                        queryLeaders();
                        break;
                }
            }
        });

        queryProvinces();
    }



    private void queryProvinces(){
        L.i("queryProvinces");
        currentLevel = LEVEL_PROVINCE;
        titleTv.setText("中国");
        backBtn.setVisibility(View.GONE);
        dataList.clear();
        dataList.addAll(HefengWeatherDB.getInstance(getActivity()).getProvinces()) ;
        StringBuilder stringBuilder = new StringBuilder();
        for (String string : dataList){
            stringBuilder.append(string)
                    .append("\n");
        }
        L.i("provinces:" + stringBuilder.toString());
        arrayAdapter.notifyDataSetChanged();
        listView.setSelection(0);

    }
    private void queryLeaders(){
        L.i("queryLeaders");
        backBtn.setVisibility(View.VISIBLE);
        currentLevel = LEVEL_LEADER;
        titleTv.setText(selectedProvince);
        dataList.clear();
        dataList.addAll(HefengWeatherDB.getInstance(getActivity()).getLeadersByProvinceZh(selectedProvince));
        arrayAdapter.notifyDataSetChanged();


    }
    private void queryCityies(){
        L.i("queryCityies");
        backBtn.setVisibility(View.VISIBLE);
        currentLevel = LEVEL_CITY;
        titleTv.setText(selectedCity);
        dataList.clear();
        dataList.addAll(HefengWeatherDB.getInstance(getActivity()).getCitiesByLeaderZh(selectedLeader));
        arrayAdapter.notifyDataSetChanged();
    }


}
