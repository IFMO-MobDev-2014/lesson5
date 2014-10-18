package com.pinguinson.reader.models;

/**
 * Created by pinguinson on 17.10.2014.
 */

public class Article {
    public String title;
    public String url;
    public String author;

    public Article(String title, String url, String author) {
        this.title = title;
        this.url = url;
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public String getAuthor() {
        return author;
    }
}
