package volhovm.com.rssreader;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;

import static volhovm.com.rssreader.DatabaseHelper.*;

/**
 * @author volhovm
 *         Created on 11/8/14
 */

public class FeedDAO {
    private SQLiteDatabase database;
    private DatabaseHelper databaseHelper;
    private String[] dataColumns = new String[]{
            _ID,
            POST_FEED_NAME,
            POST_FEED_URL,
            POST_TITLE,
            POST_DESCRIPTION,
            POST_LINK,
            POST_IMAGE_LINK,
            POST_DATE
    };
    private String[] feedColumns = new String[]{
            POST_FEED_NAME,
            POST_FEED_URL
    };

    public FeedDAO(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    public void open() {
        database = databaseHelper.getWritableDatabase();
    }

    public void close() {
        database.close();
    }

    public void putFeed(Feed feed) {
        for (int i = 0; i < feed.size(); i++) {
            ContentValues values = new ContentValues();
            values.put(POST_FEED_NAME, feed.feedName);
            values.put(POST_FEED_URL, feed.feedUrl);
            Feed.Item item = feed.get(i);
            values.put(POST_TITLE, item.title);
            values.put(POST_DESCRIPTION, item.description);
            values.put(POST_LINK, item.link.toString());
            values.put(POST_IMAGE_LINK, item.pictureLink == null ? "" : item.pictureLink.toString());
            values.put(POST_DATE, item.date == null ? "" : new SimpleDateFormat("HH:mm dd.MM.yyyy").format(item.date));
            database.insert(DATABASE_TABLE, null, values);
        }
    }

    public void deleteFeed(Feed feed) {
        database.delete(DATABASE_TABLE, POST_FEED_URL + "='" + feed.feedUrl + "';", null);
    }

    public ArrayList<Feed> getEmptyFeeds() {
        Cursor cursor = database.query(DATABASE_TABLE, feedColumns, null, null, null, null, feedColumns[0] + " asc");
        cursor.moveToFirst();
        HashSet<Feed> emptyFeeds = new HashSet<Feed>();
        while (!cursor.isAfterLast()) {
            emptyFeeds.add(new Feed(cursor.getString(0), cursor.getString(1)));
            cursor.moveToNext();
        }
        cursor.close();
        return new ArrayList<Feed>(emptyFeeds);
    }

    public ArrayList<Feed> getAllFeeds() {
        ArrayList<Feed> feeds = getEmptyFeeds();
        for (Feed feed : feeds) {
            fillFeed(feed);
        }
        return feeds;
    }

    public Feed getFeed(String feedName) {
        // TODO CHECK IF EXISTS
//        if (!getFeedNames().contains(feedName)) return null;
        Cursor cursor = database.query(DATABASE_TABLE, dataColumns, POST_FEED_NAME + "=" + feedName, null, null, null, _ID + " asc");
        try {
            return (cursorToFeed(cursor));
        } catch (MalformedURLException e) {
            e.printStackTrace();
            cursor.close();
            return null;
        } catch (ParseException e) {
            cursor.close();
            e.printStackTrace();
            return null;
        } finally {
            cursor.close();
        }
    }

    public Feed fillFeed(Feed feed) {
        Cursor current = database.query(DATABASE_TABLE, dataColumns, POST_FEED_NAME + "='" + feed.feedName + "'", null, null, null, _ID + " asc");
        try {
            return(cursorReplaceFeed(current, feed));
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        } finally {
            current.close();
        }
    }

    public Feed cursorToFeed(Cursor cursor) throws MalformedURLException, ParseException {
        cursor.moveToFirst();
        Feed feed = new Feed(cursor.getString(1), cursor.getString(2));
        return cursorReplaceFeed(cursor, feed);
    }

    public Feed cursorReplaceFeed(Cursor cursor, Feed feed) throws MalformedURLException, ParseException {
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Feed.Item item = new Feed.Item(
                    cursor.getString(3),
                    cursor.getString(4),
                    new URL(cursor.getString(5)),
                    cursor.getString(6).equals("") ? null : new URL(cursor.getString(6)),
                    cursor.getString(7).equals("") ? null : new SimpleDateFormat("HH:mm dd.MM.yyyy").parse(cursor.getString(7)));
            feed.addItem(item);
            cursor.moveToNext();
        }
        return feed;
    }
}
