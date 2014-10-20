package com.dreamteam.app.exercise1;

import java.util.ArrayList;

/**
 * Created by flyingleafe on 21.10.14.
 */
public class Feed {
    private ArrayList<FeedItem> items = new ArrayList<FeedItem>();
    private String title, description;

    public Feed() {}

    public Feed(String title, String description) {
        this.description = description;
        this.title = title;
    }

    public ArrayList<FeedItem> getItems() {
        return items;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public void setItems(ArrayList<FeedItem> items) {
        this.items = items;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void addItem(FeedItem item) {
        items.add(item);
    }
}
