package com.example.dima_2.lesson5;

/**
 * Created by Dima_2 on 08.01.2015.
 */
public class RSSElement {
    public String name, url;

    public RSSElement(String title, String url) {
        this.name = title;
        this.url = url;
    }

    public String toString() {
        return name;
    }
}
