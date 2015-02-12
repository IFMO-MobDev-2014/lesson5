package com.example.rssreader;

/**
 * Created by Яна on 03.02.2015.
 */
public class Item {
    String title;
    String link;

    public Item(String title, String link) {
        this.title = title;
        this.link = link;
    }

    public Item() {
        this.title = "";
        this.link = "";
    }
}
