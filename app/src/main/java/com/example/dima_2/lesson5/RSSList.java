package com.example.dima_2.lesson5;

import android.widget.ArrayAdapter;

import java.lang.reflect.Array;
import java.util.*;

/**
 * Created by Dima_2 on 08.01.2015.
 */
public class RSSList {
    public ArrayList<RSSElement> list;
    public ArrayAdapter<RSSElement> adapter;

    public RSSList() {
        list = new ArrayList<RSSElement>();
    }

    public RSSList(ArrayList<RSSElement> list) {
        this.list = list;
    }

    public void addElement(RSSElement element) {
        list.add(element);
        adapter.notifyDataSetChanged();
    }

    public void clearAll() {
        list.clear();
        adapter.notifyDataSetChanged();
    }

    public ArrayList<RSSElement> getList() {
        return list;
    }

    public RSSElement getElement(int idx) {
        return list.get(idx);
    }

    public String getUrl(int idx) {
        return list.get(idx).url;
    }
}
