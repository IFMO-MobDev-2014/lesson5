package ru.ifmo.md.lesson5;

/**
 * Created by Snopi on 19.10.2014.
 */
public class FeedItem {
    private String title;
    private String description;
    private String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private String url;

    public FeedItem() {
        title = "";
        description = "";
        url = "http://google.com";
        date = "";
    }

    public FeedItem(String title, String description, String url) {
        this.title = title;
        this.description = description;
        this.url = url;
    }
}
