package ru.ifmo.md.lesson5.rssreader;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import ru.ifmo.md.lesson5.rssreader.utils.RSSChannel;
import ru.ifmo.md.lesson5.rssreader.utils.RSSItem;

/**
 * Created by Nikita Yaschenko on 18.10.14.
 */
public class RSSDatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "rss.db";
    private static final int VERSION = 1;

    private static final String TABLE_CHANNEL = "channel";
    private static final String COLUMN_CHANNEL_ID = "_id";
    // public for SimpleCursorAdapter
    public static final String COLUMN_CHANNEL_TITLE = "title";
    public static final String COLUMN_CHANNEL_URL = "url";
    private static final String COLUMN_CHANNEL_DESCRIPTION = "description";
    private static final String COLUMN_CHANNEL_FAVOURITE = "favourite";

    private static final String TABLE_ITEM = "item";
    private static final String COLUMN_ITEM_ID = "_id";
    private static final String COLUMN_ITEM_TITLE = "title";
    private static final String COLUMN_ITEM_URL = "url";
    private static final String COLUMN_ITEM_DESCRIPTION = "description";
    private static final String COLUMN_ITEM_DATE = "date";
    private static final String COLUMN_ITEM_FAVOURITE = "favourite";
    private static final String COLUMN_ITEM_CHANNEL_ID = "_id";

    private static final String INIT_CHANNEL_DB =
            "CREATE TABLE " + TABLE_CHANNEL + " (" +
                    COLUMN_CHANNEL_ID          + " INTEGER " + "PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_CHANNEL_TITLE       + " TEXT, "   +
                    COLUMN_CHANNEL_URL         + " TEXT, "   +
                    COLUMN_CHANNEL_DESCRIPTION + " TEXT, "   +
                    COLUMN_CHANNEL_FAVOURITE   + " INT"      +
            ");";

    private static final String INIT_ITEM_DB =
            "CREATE TABLE " + TABLE_ITEM + " (" +
                    COLUMN_ITEM_ID          + " INTEGER "  + "PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_ITEM_TITLE       + " TEXT, "    +
                    COLUMN_ITEM_URL         + " TEXT, "    +
                    COLUMN_ITEM_DESCRIPTION + " TEXT, "    +
                    COLUMN_ITEM_DATE        + " TEXT, "    +
                    COLUMN_ITEM_FAVOURITE   + " TEXT, "    +
                    "FOREIGN KEY(" + COLUMN_ITEM_CHANNEL_ID + ") REFERENCES " +
                            TABLE_CHANNEL + "(" + COLUMN_CHANNEL_ID + ")"     +
            ");";


    private static final String[] predefinedRss = new String[]{
        "http://stackoverflow.com/feeds/tag/android",
        "http://feeds.bbci.co.uk/news/rss.xml",
        "http://echo.msk.ru/interview/rss-fulltext.xml",
        "http://bash.im/rss/"
    };

    private SQLiteDatabase mReadableDB;
    private SQLiteDatabase mWritableDB;

    public RSSDatabaseHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
        mReadableDB = getReadableDatabase();
        mWritableDB = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(INIT_CHANNEL_DB);
        db.execSQL(INIT_ITEM_DB);

        for (String url : predefinedRss) {
            ContentValues cv = new ContentValues();
            cv.put(COLUMN_CHANNEL_TITLE, "Title: " + url);
            cv.put(COLUMN_CHANNEL_URL, url);
            cv.put(COLUMN_CHANNEL_DESCRIPTION, "desc");
            cv.put(COLUMN_CHANNEL_FAVOURITE, 0);
            db.insert(TABLE_CHANNEL, null, cv);
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public long insertChannel(RSSChannel channel) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_CHANNEL_TITLE, channel.getTitle());
        cv.put(COLUMN_CHANNEL_URL, channel.getUrl());
        cv.put(COLUMN_CHANNEL_DESCRIPTION, channel.getDescription());
        cv.put(COLUMN_CHANNEL_FAVOURITE, channel.getFavourite());
        return mWritableDB.insert(TABLE_CHANNEL, null, cv);
    }

    public long insertItem(RSSItem item) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_ITEM_TITLE, item.getTitle());
        cv.put(COLUMN_ITEM_URL, item.getUrl());
        cv.put(COLUMN_ITEM_DESCRIPTION, item.getDescription());
        cv.put(COLUMN_ITEM_DATE, item.getDate());
        cv.put(COLUMN_ITEM_FAVOURITE, item.getFavourite());
        return mWritableDB.insert(TABLE_ITEM, null, cv);
    }

    public void deleteChannel(long id) {
        mWritableDB.delete(TABLE_CHANNEL, COLUMN_CHANNEL_ID + " = " + id, null);
    }

    public ChannelCursor getChannel(long id) {
        Cursor cursor = mReadableDB.query(
                TABLE_CHANNEL,
                null,                              // all columns
                COLUMN_CHANNEL_ID + " = ?",        // specify id
                new String[]{String.valueOf(id)},  // exactly 'id'
                null,                              // group by
                null,                              // order by
                null,                              // having
                "1"                                // limit by 1
        );
        return new ChannelCursor(cursor);
    }

    public Cursor getAllChannels() {
        return mReadableDB.query(
                TABLE_CHANNEL,
                null,                              // all columns
                null,                              // select *
                null,                              // selectionArgs
                null,                              // group by
                null,                              // order by
                null,
                null
        );
    }

    public static class ChannelCursor extends CursorWrapper {

        public ChannelCursor(Cursor cursor) {
            super(cursor);
        }

        public RSSChannel getChannel() {
            moveToFirst();
            if (isBeforeFirst() || isAfterLast()) {
                return null;
            }
            RSSChannel channel = new RSSChannel();
            channel.setId(getLong(getColumnIndex(COLUMN_CHANNEL_ID)));
            channel.setTitle(getString(getColumnIndex(COLUMN_CHANNEL_TITLE)));
            channel.setUrl(getString(getColumnIndex(COLUMN_CHANNEL_URL)));
            channel.setDescription(getString(getColumnIndex(COLUMN_CHANNEL_DESCRIPTION)));
            channel.setFavourite(getInt(getColumnIndex(COLUMN_CHANNEL_FAVOURITE)));
            return channel;
        }

        //TODO: implement
        //public RSSChannel[] getAllChannels() {}
    }

}
