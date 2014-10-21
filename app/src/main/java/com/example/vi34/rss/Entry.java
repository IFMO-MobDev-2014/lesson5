package com.example.vi34.rss;

/**
 * Created by vi34 on 21.10.14.
 */
public class Entry {
    public String title;
    public String name;
    public String summary;
    public String link;

    public Entry(String title, String name, String summary, String link) {
        this.title = title;
        this.link = link;
        this.name = name;
        this.summary = summary;
    }

}
