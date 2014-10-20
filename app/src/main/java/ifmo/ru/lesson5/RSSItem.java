package ifmo.ru.lesson5;

/**
 * Created by mariashka on 10/19/14.
 */
public class RSSItem {
    private String title, text, url;

    RSSItem(String newTitle, String newText, String newURL) {
        title = newTitle;
        text = newText;
        url = newURL;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public String getUrl() {
        return url;
    }
}
