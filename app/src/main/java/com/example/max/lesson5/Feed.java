package com.example.max.lesson5;

public class Feed {

    public final String link;
    public final String title;
    public final String author;

    public Feed(String link, String title, String author) {
        this.link = link;
        this.title = title;
        this.author = author;
    }

    public String getLink() {
        return link;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

}
