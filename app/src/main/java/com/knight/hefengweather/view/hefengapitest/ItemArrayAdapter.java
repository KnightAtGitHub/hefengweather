package com.knight.hefengweather.view.hefengapitest;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.knight.hefengweather.R;

import java.util.List;

/**
 * Created by knight on 17-6-14.
 */

public class ItemArrayAdapter extends ArrayAdapter<Item> {
    private int resourceId;
    public ItemArrayAdapter(@NonNull Context context, List<Item> itemList) {
        super(context, R.layout.list_item,itemList);
        this.resourceId = R.layout.list_item;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        convertView = layoutInflater.inflate(R.layout.list_item,null);
        TextView title = (TextView) convertView.findViewById(R.id.item_title);
        TextView content = (TextView) convertView.findViewById(R.id.item_content);
        Item item = getItem(position);
        title.setText(item.title);
        content.setText(item.content);
        return convertView;

    }
}
