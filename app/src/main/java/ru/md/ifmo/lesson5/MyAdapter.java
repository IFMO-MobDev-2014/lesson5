package ru.md.ifmo.lesson5;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MyAdapter extends ArrayAdapter<ItemMaster> {
    Context context;
    ArrayList<ItemMaster> items;

    public MyAdapter(Context context, ArrayList<ItemMaster> items) {
        super(context, R.layout.list_element, items);

        this.context = context;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View elementView = inflater.inflate(R.layout.list_element, parent, false);
        TextView title = (TextView) elementView.findViewById(R.id.title);
        TextView date = (TextView) elementView.findViewById(R.id.date);
        TextView description = (TextView) elementView.findViewById(R.id.description);
        TextView linkHelper = (TextView) elementView.findViewById(R.id.link_helper);

        linkHelper.setText(items.get(position).getLink());
        title.setText(items.get(position).getTitle());
        date.setText(items.get(position).getPubDate());
        description.setText(items.get(position).getDescription());

        return elementView;

    }





}
