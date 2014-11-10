package volhovm.com.rssreader;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

/**
 * @author volhovm
 *         Created on 11/6/14
 */

public class Feed {
    String feedName;
    String feedUrl;
    private ArrayList<Item> items = new ArrayList<Item>();

    public Feed(Feed feed) {
        this.feedName = feed.feedName;
        this.feedUrl = feed.feedUrl;
        for (int i = 0; i < feed.size(); i++) {
            this.items.add(new Item(feed.get(i)));
        }
    }

    public Feed(String feedName, String feedUrl) {
        this.feedName = feedName;
        this.feedUrl = feedUrl;
        items = new ArrayList<Item>();
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public Item get(int position) {
        if (position < items.size()) return items.get(position);
        else return null;
    }

    public int size() {
        return items.size();
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public String toString() {
        return feedName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Feed feed = (Feed) o;

        if (!feedName.equals(feed.feedName)) return false;
        if (!feedUrl.equals(feed.feedUrl)) return false;
        if (!items.equals(feed.items)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = feedName.hashCode();
        result = 31 * result + feedUrl.hashCode();
        result = 31 * result + items.hashCode();
        return result;
    }

    public static class Item {
        String title;
        String description;
        URL link;
        URL pictureLink;
        Date date;

        public Item(String title, String description, URL link, URL pictureLink, Date pubDate) {
            this.title = title;
            this.link = link;
            this.description = description;
            this.pictureLink = pictureLink;
            this.date = pubDate;
        }

        public Item(Item item) {
            this.title = item.title;
            this.description = item.description;
            this.link = item.link;
            this.pictureLink = item.pictureLink;
            this.date = item.date;
        }
    }
}
