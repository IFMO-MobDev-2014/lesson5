package ru.ifmo.md.rssreader;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

class RssEntryAdapter extends BaseAdapter {
    private final ArrayList<RssEntry> data;

    public RssEntryAdapter(ArrayList<RssEntry> data) {
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public RssEntry getItem(int ind) {
        return data.get(ind);
    }

    @Override
    public long getItemId(int ind) {
        return ind;
    }

    @Override
    public View getView(final int ind, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rss_list_item, viewGroup, false);
        }
        TextView text = (TextView) view.findViewById(R.id.text);
        text.setText(getItem(ind).title);

        return view;
    }
}
