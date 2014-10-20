package ru.ifmo.md.lesson5.rssreader;

/**
 * Created by Nikita Yaschenko on 20.10.14.
 */
public class RSSChannel {
    private long mId;
    private String mUrl;
    private String mTitle;
    private String mDescription;
    private int mFavourite;
    private RSSItem[] mRssItems;

    public RSSChannel() {
        mId = -1;
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        this.mId = id;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
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
