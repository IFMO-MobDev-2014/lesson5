package com.example.kirill.rssreader;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Kirill on 19.10.2014.
 */
public class FeedItem {
    private final String link;
    private final String title;
    private final String description;
    private final String pubDate;
    private Date realDate;

    public FeedItem(String link, String title, String description, String pubDate) {
        this.title = title;
        this.link = link;
        this.description = description;
        this.pubDate = pubDate;
        try {
            this.realDate = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z").parse(pubDate);
        } catch (ParseException e) {
            this.realDate = null;
            Log.i("error parsing date", this.pubDate);
        }
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return new SimpleDateFormat("EEE, H:mm").format(realDate);
    }

    public String getDate(String format) {
        return new SimpleDateFormat(format).format(realDate);
    }

    @Override
    public String toString() {
        return "{" + title + "\n" + link + "\n" + description + "\n" + pubDate + "}";
    }

    public static FeedItem fromString(String data) throws IllegalArgumentException{
        String temp = data.substring(1, data.length()-1);
        String[] res = temp.split("\n");
        if (res.length != 4)
            throw new IllegalArgumentException("Can't convert data to FeedItem");
        FeedItem rv = new FeedItem(res[0], res[1], res[2], res[3]);
        return rv;
    }
}
