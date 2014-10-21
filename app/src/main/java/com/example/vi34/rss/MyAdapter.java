package com.example.vi34.rss;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by vi34 on 21.10.14.
 */
public class MyAdapter extends BaseAdapter {
    private List<Entry> data;

    public MyAdapter(List<Entry> data) {

        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_list, parent, false);


       // TextView txt = (TextView)convertView.findViewById(android.R.id.text1);
        //txt.setText(String.valueOf(data.get(position).title));
        ((TextView)((LinearLayout)v).getChildAt(0)).setText(String.valueOf(data.get(position).title));
        ((TextView)((LinearLayout)v).getChildAt(1)).setText(String.valueOf(data.get(position).name));
        getItem(position);
        return v;
    }
}
