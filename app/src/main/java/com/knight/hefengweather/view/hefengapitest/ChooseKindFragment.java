package com.knight.hefengweather.view.hefengapitest;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.knight.hefengweather.R;
import com.knight.hefengweather.hefengweatherapi.HefengWeatherApi;
import com.knight.hefengweather.util.L;

/**
 * Created by knight on 17-6-13.
 */

public class ChooseKindFragment extends Fragment implements AdapterView.OnItemClickListener {
    private String cityCode;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cityCode = getArguments().getString("cityCode");

    }
    private ListView weatherKindLv;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("选择类型");
        View view = inflater.inflate(R.layout.fragment_choose_kind,container,false);

        weatherKindLv = (ListView) view.findViewById(R.id.weather_kind_list);
        weatherKindLv.setAdapter(new FragmentArrayAdapter(getActivity(), weatherKindList));
        weatherKindLv.setOnItemClickListener(this);
        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        WeatherKind weatherKind = (WeatherKind) weatherKindLv.getAdapter().getItem(position);
        Bundle bundle = new Bundle();
        bundle.putString("cityCode",cityCode);
        bundle.putString("weatherKind",weatherKind.weatherKind);
        WeatherDataFragment weatherDataFragment = new WeatherDataFragment();
        weatherDataFragment.setArguments(bundle);
        ((ApiTestActivity)getActivity()).replaceFragment(weatherDataFragment);
    }

    private static class WeatherKind {
        private String title;
        private String weatherKind;
        public WeatherKind(String title, String weatherKind){
            this.title = title;
            this.weatherKind = weatherKind;
        }
    }
    private static WeatherKind[] weatherKindList = {
            new WeatherKind("weather", HefengWeatherApi.WEATHER),
            new WeatherKind("forecast",HefengWeatherApi.FORECAST),
            new WeatherKind("now",HefengWeatherApi.NOW),
            new WeatherKind("hourly_forecast",HefengWeatherApi.HOURLY),
            new WeatherKind("suggstion",HefengWeatherApi.SUGGESTION),
    };
    private static class FragmentArrayAdapter extends ArrayAdapter<WeatherKind>{

        public FragmentArrayAdapter(@NonNull Context context,WeatherKind[] weatherKindList) {
            super(context, R.layout.kind_item, weatherKindList);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            convertView = layoutInflater.inflate(R.layout.kind_item,null);
            WeatherKind weatherKind = getItem(position);
            TextView title = (TextView) convertView.findViewById(R.id.text1);
            title.setText(weatherKind.title);
            return convertView;
        }
    }
}
