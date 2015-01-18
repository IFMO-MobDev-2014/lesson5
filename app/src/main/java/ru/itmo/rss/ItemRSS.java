package ru.itmo.rss;

public class ItemRSS {
    private final String link;
    private final String title;
    private final String description;

    public ItemRSS(String link, String title, String description) {
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
        return String.format("FeedItem{link='%s', title='%s', description='%s'}", link, title, description);
    }
}