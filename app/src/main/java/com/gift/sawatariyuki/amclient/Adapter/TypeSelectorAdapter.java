package com.gift.sawatariyuki.amclient.Adapter;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;


import com.gift.sawatariyuki.amclient.Bean.Type;
import com.gift.sawatariyuki.amclient.R;

import java.util.List;

public class TypeSelectorAdapter extends BaseAdapter implements SpinnerAdapter {
    private List<Type> types;
    private Context context;

    public TypeSelectorAdapter(List<Type> types, Context context) {
        this.types = types;
        this.context = context;
    }

    @Override
    public int getCount() {
        return types.size();
    }

    @Override
    public Object getItem(int position) {
        return types.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.type_selector_item, null);
        TextView type_selector_item_type = convertView.findViewById(R.id.type_selector_item_type);
        type_selector_item_type.setText(types.get(position).getFields().getName());
        return convertView;
    }

}
