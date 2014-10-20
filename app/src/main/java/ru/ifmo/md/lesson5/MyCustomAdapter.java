package ru.ifmo.md.lesson5;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Snopi on 20.10.2014.
 */
public class MyCustomAdapter extends BaseAdapter {

    private LinearLayout someGoodLayout;
    private Context context;
    private List<FeedItem> data;

    public void add(FeedItem token) {
        data.add(token);
        notifyDataSetChanged();
    }

    public void remove(int index) {
        data.remove(index);
//        notifyDataSetChanged();
    }

//    @Override
//    public void notifyDataSetChanged() {
//        super.notifyDataSetChanged();
//    }

    public MyCustomAdapter(Context context, List<FeedItem> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public FeedItem getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View res;
        if (view != null) {
            res = view;
        } else {
            res = LayoutInflater.from(context).inflate(R.layout.feeditem, viewGroup, false);
        }
        FeedItem tmp = getItem(i);
        ((TextView) res.findViewById(R.id.title)).setText(tmp.getTitle());
        ((TextView) res.findViewById(R.id.description)).setText(tmp.getDescription());
//        res.setMinimumWidth(ActionBar.LayoutParams.WRAP_CONTENT);
//        res.setMinimumHeight(ActionBar.LayoutParams.WRAP_CONTENT);
        return res;
    }
}
