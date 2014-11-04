package ru.eugene.listviewpractice2;

/**
 * Created by eugene on 11/4/14.
 */
public class ItemObject {
    private String link;
    private String description;
    private String title;
    private String pubDate;

    String getDescription() {
        return description;
    }

    String getLink() {
        return link;
    }

    String getTitle() {
        return title;
    }

    String getPubDate() {
        return pubDate;
    }

    void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    void setDescription(String description) {
        this.description = description;
    }

    void setLink(String link) {
        this.link = link;
    }

    void setTitle(String title) {
        this.title = title;
    }


}
