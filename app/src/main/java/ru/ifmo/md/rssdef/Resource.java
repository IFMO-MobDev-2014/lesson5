package ru.ifmo.md.rssdef;

/**
 * Created by Svet on 20.10.2014.
 */
public class Resource {
    String name;
    String url;
    Resource() {
        name = "";
        url = "";
    }

    Resource(String name, String url) {
        this.name = name;
        this.url = url;
    }
}
