package com.example.timur.rssreader;

/**
 * Created by timur on 21.10.14.
 */
public class RSSitem {
    private String link;
    private String title;
    private String mainInfo;

    public String getTitle() {
        return title;
    }

    public String getMainInfo() {
        return mainInfo;
    }

    public String getLink() {
        return link;
    }

    public RSSitem(String link, String title, String mainInfo) {
        this.link = link;
        this.title = title;
        this.mainInfo = mainInfo;
    }
}
