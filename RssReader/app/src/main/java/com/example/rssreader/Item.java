package com.example.rssreader;

/**
 * Created by Яна on 03.02.2015.
 */
public class Item {
    String title;
    String link;
    String description;
    String pubDate;

    public Item(String title, String link, String description, String pubDate) {
        this.title = title;
        this.link = link;
        this.description = description;
        this.pubDate = pubDate;
    }

    public Item() {
        this.title = "";
        this.link = "";
        this.description = "";
        this.pubDate = "";
    }
}
