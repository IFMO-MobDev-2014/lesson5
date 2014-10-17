package com.wibk.rss;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class MyMegaCoolSuperPuperAdapter2000<T> extends BaseAdapter {

    private List<T> data;

    public MyMegaCoolSuperPuperAdapter2000(List<T> data) {
        if (data == null) {
            throw new IllegalArgumentException("Null list is illegal");
        }
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public T getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.explayout, viewGroup, false);
        ((TextView) v).setText(getItemId(i) + " " + getItem(i));
        if (i % 2 == 0) {
            v.setBackgroundColor(Color.BLACK);
        }
        return v;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
}
