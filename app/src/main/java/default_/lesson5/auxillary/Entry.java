package default_.lesson5.auxillary;

/**
 * Created by default on 21.10.14.
 */
public class Entry {

    public String link;
    public String title;
    public String summary;

    public String getLink() {
        return link;
    }

    public String getTitle() {
        return title;
    }

    public String getSummary() {
        return summary;
    }

    public Entry(String link, String title, String summary) {

        this.link = link;
        this.title = title;
        this.summary = summary;
    }
}
