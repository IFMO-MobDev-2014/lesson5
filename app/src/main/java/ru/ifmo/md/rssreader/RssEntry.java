package ru.ifmo.md.rssreader;

import java.lang.String;
import java.util.Date;

public class RssEntry {
    public final String title;
    public final String link;
    public final String description;
    public final Date pubDate;

    public RssEntry(String title, String description, String link, Date pubDate) {
        this.title = title;
        this.description = description;
        this.link = link;
        this.pubDate = pubDate;
    }
}
