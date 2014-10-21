package com.lyamkin.rss;

public class Article {
    private final String title;
    private final String link;

    public Article(String title, String link) {
        this.title = title;
        this.link = link;
    }

    @Override
    public String toString() {
        return title;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

}
