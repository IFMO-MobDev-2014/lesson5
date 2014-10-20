package ru.ifmo.md.lesson5.rssreader;

/**
 * Created by Nikita Yaschenko on 18.10.14.
 */
public class RssItem {
    private long mId;
    private String mName;
    private String mUrl;
    private int mFavourite;

    public RssItem() {
        mId = -1;
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public int getFavourite() {
        return mFavourite;
    }

    public void setFavourite(int favourite) {
        mFavourite = favourite;
    }
}
