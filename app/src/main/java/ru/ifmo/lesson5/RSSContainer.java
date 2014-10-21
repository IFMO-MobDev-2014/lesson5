package ru.ifmo.lesson5;

import android.widget.ArrayAdapter;

import java.util.ArrayList;

/**
 * Created by creed on 21.10.14.
 */
public class RSSContainer {
    private ArrayList<RSSItem> container;
    public ArrayAdapter<RSSItem> aa;
    RSSContainer() {
        container = new ArrayList<RSSItem>();
    }
    RSSContainer(ArrayList<RSSItem> container) {
        this.container = container;
    }
    public void add(RSSItem item) {
        container.add(item);
        aa.notifyDataSetChanged();
    }
    public void clear() {
        container.clear();
        aa.notifyDataSetChanged();
    }
    public ArrayList<RSSItem> getContainer() {
        return container;
    }
    public RSSItem getItem(int index) {
        return container.get(index);
    }
}
