package com.example.vlad107.rssreader;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by vlad107 on 21.10.14.
 */
public class FeedAdapter extends BaseAdapter {
    private List<Feed> feed;

    public FeedAdapter(List<Feed> feed) {
        this.feed = feed;
    }

    @Override
    public int getCount() {
        return feed.size();
    }

    @Override
    public Feed getItem(int i) {
        return feed.get(i);
    }

    @Override
    public long getItemId(int i) {
        return feed.get(i).hashCode();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View curView;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        if (view == null) {
            curView = inflater.inflate(R.layout.view_feed, viewGroup, false);
        } else {
            curView = view;
        }
        TextView authorText = (TextView) curView.findViewById(R.id.authorView);
        authorText.setText(feed.get(i).getAuthor());
        TextView titleText = (TextView) curView.findViewById(R.id.titleView);
        titleText.setText(feed.get(i).getTitle());
        TextView dateText = (TextView) curView.findViewById(R.id.dateView);
        dateText.setText(feed.get(i).getDate());
        if (i % 2 == 0) {
            curView.setBackgroundColor(Color.WHITE);
        } else {
            curView.setBackgroundColor(Color.LTGRAY);
        }
        return curView;
    }
}
