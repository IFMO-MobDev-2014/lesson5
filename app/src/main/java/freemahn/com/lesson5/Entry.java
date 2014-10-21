package freemahn.com.lesson5;

/**
 * Created by Freemahn on 17.10.2014.
 */
public class Entry {
    public final String title;
    public final String summary;
    public final String link;
    //   public final String updated;

    public Entry(String title, String summary, String link) {
        this.title = title;
        this.summary = summary;
        this.link = link;
        //this.updated = updated;

    }

    @Override
    public String toString() {
        return title + " " + "\n" + link + "\n" + summary;
    }
}