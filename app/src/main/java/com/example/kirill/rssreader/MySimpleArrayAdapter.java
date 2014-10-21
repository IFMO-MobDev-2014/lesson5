package com.example.kirill.rssreader;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Kirill on 20.10.2014.
 */
public class MySimpleArrayAdapter extends ArrayAdapter<FeedItem> {
    private final Activity context;
    private final ArrayList<FeedItem> values;

    static class ViewHolder {
        public TextView title;
        public TextView date;
        public WebView description;
    }

    public MySimpleArrayAdapter(Activity context, ArrayList<FeedItem> values) {
        super(context, R.layout.list_element, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        if (rowView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            rowView = inflater.inflate(R.layout.list_element, null);
            // configure view holder
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.title = (TextView) rowView.findViewById(R.id.title);
            viewHolder.date = (TextView) rowView.findViewById(R.id.date);
            viewHolder.description = (WebView) rowView.findViewById(R.id.description);
            rowView.setTag(viewHolder);
        }

        ViewHolder holder = (ViewHolder) rowView.getTag();
        FeedItem item = values.get(position);
        holder.title.setText(item.getTitle());
        holder.date.setText(item.getDate());
        holder.description.loadData(item.getDescription(), "text/html; charset=UTF-8", null);
        return rowView;
    }
}