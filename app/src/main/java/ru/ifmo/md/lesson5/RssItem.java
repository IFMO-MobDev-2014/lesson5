package ru.ifmo.md.lesson5;

/**
 * Created by lightning95 on 10/21/14.
 */

public class RssItem {
    private final String link;
    private final String title;
    private final String description;

    public RssItem(String link, String title, String description) {
        this.link = link;
        this.title = title;
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "RssItem{link='" + link + '\'' + ", " +
                        "title='" + title + '\'' + ", " +
                        "description='" + description + "\'}";
    }
}
