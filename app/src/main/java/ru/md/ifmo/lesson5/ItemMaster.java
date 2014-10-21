package ru.md.ifmo.lesson5;


public class ItemMaster {
    String title = null;
    String description = null;
    String link = null;
    String pubDate = null;

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String number) {
        this.title = number;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return this.link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getPubDate() {
        return this.pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }
}
