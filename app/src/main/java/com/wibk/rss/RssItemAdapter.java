package com.wibk.rss;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class RssItemAdapter extends BaseAdapter implements View.OnClickListener {
    private List<RssItem> rssItemList;
    private LayoutInflater inflater;
    private MainActivity mainActivity;
    private final static DateFormat dateFormat = new SimpleDateFormat("d MMM yyyy HH:mm:ss");

    public RssItemAdapter(Context context, MainActivity mainActivity) {
        rssItemList = new ArrayList<RssItem>();
        inflater = LayoutInflater.from(context);
        this.mainActivity = mainActivity;
    }

    public void add(RssItem item) {
        rssItemList.add(item);
    }

    @Override
    public int getCount() {
        return rssItemList.size();
    }

    @Override
    public RssItem getItem(int i) {
        return rssItemList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            View v = inflater.inflate(R.layout.rss_item_layout, viewGroup, false);
            TextView dateTextView = (TextView) v.findViewById(R.id.dateTextView);
            TextView titleTextView = (TextView) v.findViewById(R.id.titleTextView);
            TextView descriptionTextView = (TextView) v.findViewById(R.id.descriptionTextView);
            if (rssItemList.get(i) != null) {
                if (rssItemList.get(i).getDate() != null) {
                    dateTextView.setText(dateFormat.format(rssItemList.get(i).getDate()));
                }
                titleTextView.setText(rssItemList.get(i).getTitle());
                String s = String.valueOf(Html.fromHtml(rssItemList.get(i).getDescription()));
                if (s.length() <= 100) {
                    descriptionTextView.setText(s);
                } else {
                    descriptionTextView.setText(s.substring(0, 100) + "...");
                }
            }
            v.setOnClickListener(new OnItemClickListener(i));
            return v;
        } else {
            return view;
        }
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {

    }

    private class OnItemClickListener implements View.OnClickListener {
        private int number;

        private OnItemClickListener(int number) {
            this.number = number;
        }

        @Override
        public void onClick(View view) {
            mainActivity.onItemClick(number);
        }
    }
}