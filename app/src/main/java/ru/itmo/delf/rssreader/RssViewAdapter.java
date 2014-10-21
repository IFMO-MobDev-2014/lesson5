package ru.itmo.delf.rssreader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;

/**
 * Created by delf on 21.10.14.
 */
public class RssViewAdapter<T extends  RssItem> extends BaseAdapter {

    private final android.os.Handler mt;
    private ArrayList<T> data;

    public RssViewAdapter(android.os.Handler mt){
        this.data = new ArrayList<T>();
        this.mt = mt;
    }



    public void add(T item){
        data.add(item);
        mt.post(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        });
    }

    public void clear(){
        data.clear();

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
        title.setTag(data.get(position).getLink());
        TextView content = (TextView) v.findViewById(R.id.contentTextView);
        content.setText(data.get(position).getDescription());
        TextView date_ = (TextView) v.findViewById(R.id.dateTextView);
        date_.setText(data.get(position).getPublicateDateTime().toString());


        return v;
    }
}
