package ru.ifmo.md.lesson5;

/**
 * Created by lightning95 on 10/21/14.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class RssItemAdapter extends BaseAdapter {
    private List<RssItem> rssItemList;
    private LayoutInflater inflater;

    public RssItemAdapter(List<RssItem> rssItemList, Context context) {
        this.rssItemList = rssItemList;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return rssItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return rssItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = inflater.inflate(R.layout.rss_item_layout, null);
        }

        TextView title = (TextView) view.findViewById(R.id.title);
        TextView description = (TextView) view.findViewById(R.id.description);

        RssItem rssItem = rssItemList.get(position);
        title.setText(rssItem.getTitle());
        description.setText(rssItem.getDescription());

        return view;
    }
}
