package ru.ifmo.md.rssdef;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Svet on 14.10.2014.
 */
public class ListAdapter extends BaseAdapter {
    Context context;
    ArrayList<String> values;
    ListAdapter(Context cont) {
        values = new ArrayList<String>();
        context = cont;
    }
    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.item, null);
        TextView cur = (TextView) v.findViewById(R.id.item);
        cur.setText(values.get(i));
        return v;
    }
}
