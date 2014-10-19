package ru.ifmo.md.lesson5.rssreader;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Nikita Yaschenko on 18.10.14.
 */
public class RssDatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "rss.db";
    private static final int VERSION = 1;

    private static final String TABLE_RSS = "rss";
    private static final String COLUMN_RSS_ID = "_id";
    public static final String COLUMN_RSS_NAME = "name";
    public static final String COLUMN_RSS_URL = "url";
    private static final String COLUMN_RSS_FAVOURITE = "favourite";

    private static String[] predefinedRss = new String[]{
        "http://stackoverflow.com/feeds/tag/android",
        "http://feeds.bbci.co.uk/news/rss.xml",
        "http://echo.msk.ru/interview/rss-fulltext.xml",
        "http://bash.im/rss/"
    };

    public RssDatabaseHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE " + TABLE_RSS + "(" +
                        COLUMN_RSS_ID + " integer primary key autoincrement, " +
                        COLUMN_RSS_NAME + " text, " +
                        COLUMN_RSS_URL + " text, " +
                        COLUMN_RSS_FAVOURITE + " int" +
                        ");"
        );
        for (String url : predefinedRss) {
            ContentValues cv = new ContentValues();
            cv.put(COLUMN_RSS_NAME, url);
            cv.put(COLUMN_RSS_URL, url);
            cv.put(COLUMN_RSS_FAVOURITE, 0);
            db.insert(TABLE_RSS, null, cv);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public long insertRss(Rss rss) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_RSS_NAME, rss.getName());
        cv.put(COLUMN_RSS_URL, rss.getUrl());
        cv.put(COLUMN_RSS_FAVOURITE, rss.getFavourite());
        return getWritableDatabase().insert(TABLE_RSS, null, cv);
    }

    public void deleteRss(long id) {
        getWritableDatabase().delete(
                TABLE_RSS,
                COLUMN_RSS_ID + " = " + id,
                null
        );
    }

    public Cursor getAllRss() {
        return getReadableDatabase().query(
                TABLE_RSS,
                null,                              // all columns
                null,                              // select *
                null,                              // selectionArgs
                null,                              // group by
                null,                              // order by
                null,
                null
        );
    }

    public Cursor getRss(long id) {
        return getReadableDatabase().query(
                TABLE_RSS,
                null,                              // all columns
                COLUMN_RSS_ID + " = ?",            // specify id
                new String[]{ Long.toString(id) }, // exactly 'id'
                null,                              // group by
                null,                              // order by
                null,                              // having
                "1"                                // limit by 1
        );
    }

/*
    public static class RssCursor extends CursorWrapper {

        public RssCursor(Cursor cursor) {
            super(cursor);
        }

        public Rss getRss() {
            if (isBeforeFirst() || isAfterLast()) {
                return null;
            }
            Rss rss = new Rss();
            rss.setId(getLong(getColumnIndex(COLUMN_RSS_ID)));
            rss.setName(getString(getColumnIndex(COLUMN_RSS_NAME)));
            rss.setUrl(getString(getColumnIndex(COLUMN_RSS_URL)));
            rss.setFavourite(getInt(getColumnIndex(COLUMN_RSS_FAVOURITE)));
            return rss;
        }
    }
    */
}
