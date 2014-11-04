package com.example.rssreader;

/**
 * Created by Амир on 04.11.2014.
 */
public class ContentItem {
    public String link;
    public String title;
    public String pubDate;
    public String description;

    public ContentItem(String link, String title, String pubDate, String description) {
        this.link = link;
        this.title = title;
        this.pubDate = pubDate;
        this.description = description;
    }
}
