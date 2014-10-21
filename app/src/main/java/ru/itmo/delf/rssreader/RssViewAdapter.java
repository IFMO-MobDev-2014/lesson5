package ru.itmo.delf.rssreader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by delf on 21.10.14.
 */
public class RssViewAdapter<T extends  RssItem> extends BaseAdapter {

    private ArrayList<T> data;

    public RssViewAdapter(ArrayList<T> data){
        this.data = data;
    }

    public void add(T item){
        data.add(item);
        notifyDataSetChanged();
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
        View v = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.rss_item_displayer,parent,false);
        TextView title = (TextView) v.findViewById(R.id.titleTextView);
        title.setText(data.get(position).getTitle());
        TextView content = (TextView) v.findViewById(R.id.contentTextView);
        content.setText(data.get(position).getDescription());

        return v;
    }
}
