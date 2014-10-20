package ru.ifmo.md.lesson5.rssreader;

/**
 * Created by Nikita Yaschenko on 18.10.14.
 */
public class RSSItem {
    private long mId;
    private String mTitle;
    private String mUrl;
    //TODO: use Date instead of String
    private String mDate;
    private String mDescription;
    private int mFavourite;

    public RSSItem() {
        mId = -1;
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public int getFavourite() {
        return mFavourite;
    }

    public void setFavourite(int favourite) {
        mFavourite = favourite;
    }
}
