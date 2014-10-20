package com.example.vitalik.lesson5;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by vitalik on 20.10.14.
 */
public class Feed {
    private String title;
    private String issue;
    private String link;
    private Date date;

    public String getTitle() {
        return title;
    }

    public String getIssue() {
        return issue;
    }

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
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyyy hh:mm");
        String dateString = sdf.format(date);
        return dateString + " " + title;
    }
}
