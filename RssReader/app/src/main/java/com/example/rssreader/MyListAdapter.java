package com.example.rssreader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Яна on 04.02.2015.
 */
public class MyListAdapter extends BaseAdapter {
    ArrayList<Item> items;
    Context context;

    MyListAdapter(Context context, ArrayList<Item> items) {
        this.items = items;
        this.context = context;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

    public ArrayList<Item> getItems() {
        return this.items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View result = inflater.inflate(R.layout.item, parent, false);

        TextView text1 = (TextView) result.findViewById(R.id.title);
        text1.setText(items.get(position).title);

        TextView text2 = (TextView) result.findViewById(R.id.link);
        text2.setText(items.get(position).link);

        TextView text3 = (TextView) result.findViewById(R.id.description);
        text3.setText(items.get(position).description);

        TextView text4 = (TextView) result.findViewById(R.id.pubDate);
        text4.setText(items.get(position).pubDate);

        return result;
    }
}
