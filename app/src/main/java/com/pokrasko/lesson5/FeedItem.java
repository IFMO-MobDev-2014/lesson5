package com.pokrasko.lesson5;

/**
 * Created by pokrasko on 21.10.14.
 */
public class FeedItem {
    private boolean isVisible;
    private String title;
    private String description;
    private String link;

    public FeedItem(String title, String description, String link) {
        this.title = title;
        this.description = description;
        this.link = link;
        isVisible = false;
    }

    public void changeVisibility() {
        isVisible = !isVisible ;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getLink() {
        return link;
    }
}
