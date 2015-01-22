package ru.ifmo.md.lesson5;

public class Item {
    private String title;
    private String description;
    private String date;
    private String url;

    public Item(String title, String description, String date, String url) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    public String getUrl() {
        return url;
    }
}
