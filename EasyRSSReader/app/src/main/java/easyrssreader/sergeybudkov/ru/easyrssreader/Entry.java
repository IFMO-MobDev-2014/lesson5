package easyrssreader.sergeybudkov.ru.easyrssreader;

public class Entry {
    private String title = null;
    private String description = null;
    private String link = null;
    private String publishedDate = null;

    public Entry(String title, String description, String link, String publishedDate) {
        this.title = title;
        this.description = description;
        this.link = link;
        this.publishedDate = publishedDate;
    }
    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }

    public String getLink() {
        return this.link;
    }

    @Override
    public String toString() {
        return getTitle() + "(" + publishedDate + ")";
    }
}
