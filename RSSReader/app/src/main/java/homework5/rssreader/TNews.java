package homework5.rssreader;

/**
 * Created by Anstanasia on 21.10.2014.
 */
public class TNews {
    private String title;
    private String description;
    private String link;

    public TNews () {
        title = "null";
        description = "null";
        link = "null";
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLink(String link) {
        this.link = link;
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
