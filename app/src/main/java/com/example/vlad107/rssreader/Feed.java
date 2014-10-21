package com.example.vlad107.rssreader;

/**
 * Created by vlad107 on 21.10.14
 */
public class Feed {
    private String author;
    private String title;
    private String link;
    private String date;

    public Feed(String author, String title, String date, String link) {
        this.author = author;
        this.title = title;
        this.date = date;
        this.link = link;
    }

    public String getDate() {
        return date;
    }

    public String getLink() {

        return link;
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

}
