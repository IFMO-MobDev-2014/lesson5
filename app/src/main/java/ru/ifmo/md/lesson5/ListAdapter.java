package ru.ifmo.md.lesson5;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Svet on 14.10.2014.
 */
public class ListAdapter extends BaseAdapter {
    Context context;
    ArrayList<Message> values;
    DataStorage ds;
    ListAdapter(Context cont, DataStorage data) {
        values = new ArrayList<Message>();
        context = cont;
        ds = data;
        values = ds.readRssData();
    }
    @Override
    public int getCount() {
        return values.size();
    }

    @Override
    public void notifyDataSetChanged() {
        values = ds.readRssData();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.item, null);
        TextView name = (TextView) v.findViewById(R.id.name);
        TextView url = (TextView) v.findViewById(R.id.url);
        name.setText(values.get(i).title);
        url.setText(values.get(i).description);
        return v;
    }
}
