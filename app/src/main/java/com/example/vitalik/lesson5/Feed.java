package com.example.vitalik.lesson5;

import java.util.Date;

/**
 * Created by vitalik on 20.10.14.
 */
public class Feed {
    private String title;
    private String issue;
    private String link;
    private Date date;

    public Feed(String title, String issue, String link, Date date) {
        this.title = title;
        this.issue = issue;
        this.link = link;
        this.date = date;
    }

    public String getLink() {
        return link;
    }

    @Override
    public String toString() {
        return "" + title;
    }
}
