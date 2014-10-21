package ru.ifmo.lesson5;

/**
 * Created by creed on 21.10.14.
 */
public class RSSItem {
    public String title;
    public String url;
    RSSItem(String title, String url) {
        this.title = title;
        this.url = url;
    }

    @Override
    public String toString() {
        return title;

    }
}
