package ru.ifmo.md.patrikeev.rssreader;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by sergey on 02.11.14.
 */
public class RSSListAdapter extends BaseAdapter {

    private ArrayList<RSSItem> rssItems;
    private MainActivity context;

    RSSListAdapter(MainActivity context) {
        rssItems = new ArrayList<>();
        this.context = context;
    }

    public void addRssItems(ArrayList<RSSItem> newItems) {
        rssItems.addAll(0, newItems);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return rssItems.size();
    }

    @Override
    public RSSItem getItem(int i) {
        return rssItems.get(i);
    }

    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(context);
        FieldsHolder fieldsHolder;
        if (view == null) {
            view = inflater.inflate(android.R.layout.simple_list_item_2, null);
            fieldsHolder = new FieldsHolder();
            fieldsHolder.title = (TextView) view.findViewById(android.R.id.text1);
            fieldsHolder.description = (TextView) view.findViewById(android.R.id.text2);
            view.setTag(fieldsHolder);
        } else {
            fieldsHolder = (FieldsHolder) view.getTag();
        }
        TextView title = fieldsHolder.title;
        TextView description = fieldsHolder.description;
        final RSSItem rssItem = getItem(i);
        title.setText(rssItem.getTitle());
        description.setText(rssItem.getDescription());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.onRSSClicked(rssItem.getLink());
            }
        });
        return view;
    }

    class FieldsHolder {
        TextView title;
        TextView description;
    }
}