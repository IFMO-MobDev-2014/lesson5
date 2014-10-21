package ru.ifmo.md.lesson5;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Svet on 21.10.2014.
 */
public class EditListAdapter extends BaseAdapter {

    ArrayList<Resource> resources;
    Context context;
    EditListAdapter(Context context, DataStorage dataStorage) {
        this.context = context;
        resources = dataStorage.readResourceData();
    }

    @Override
    public int getCount() {
        return resources.size();
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
        TextView name = (TextView) v.findViewById(R.id.name);
        TextView url = (TextView) v.findViewById(R.id.url);
        name.setText(resources.get(i).name);
        url.setText(resources.get(i).url);
        return v;
    }
}
