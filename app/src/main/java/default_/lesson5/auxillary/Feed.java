package default_.lesson5.auxillary;

import java.util.ArrayList;

/**
 * Created by default on 21.10.14.
 */
public class Feed {

    ArrayList<Entry> entries;
    String title;

    public Feed() {
        this.title = "BBC Feed";
    }

    public void setContent(ArrayList<Entry> entries) {
        this.entries = entries;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Entry getEntry(int pos) {
        if (pos >= 0 && pos < entries.size())
            return entries.get(pos);
        else
            return null;
    }

    public int countEntries() {
        return entries.size();
    }

    public String getTitle() {
        return title;
    }

}
