package ru.ifmo.ctddev.soloveva.rssreader.rss;

/**
 * Created by maria on 21.10.14.
 */
public class Entry {
    private final String title;
    private final String html;

    public Entry(String title, String html) {
        this.title = title;
        this.html = html;
    }

    public String getTitle() {
        return title;
    }

    public String getHtml() {
        return html;
    }

    @Override
    public String toString() {
        return title;
    }
}
