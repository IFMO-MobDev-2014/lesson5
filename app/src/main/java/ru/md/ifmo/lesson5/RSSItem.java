package ru.md.ifmo.lesson5;

/**
 * Created by Илья on 21.10.2014.
 */
public class RSSItem {
    String title = null;
    String description = null;
    String link = null;
    String datetime = null;

    public RSSItem (String title, String description, String link, String datetime) {
        this.title=title;
        this.description=description;
        this.link=link;
        this.datetime=datetime;
    }
}
