package com.pokrasko.lesson5;

import java.util.ArrayList;

/**
 * Created by pokrasko on 21.10.14.
 */
public class Feed {
    private String title;
    private String description;
    private ArrayList<FeedItem> items;

    public Feed(String title, String description) {
        this.title = title;
        this.description = description;
        items = new ArrayList<FeedItem>();
    }

    public String getTitle() {
        return title;
    }

    public FeedItem getItem(int i) {
        return items.get(i);
    }

    public void addItem(FeedItem item) {
        items.add(item);
    }

    public int getSize() {
        return items.size();
    }
}
