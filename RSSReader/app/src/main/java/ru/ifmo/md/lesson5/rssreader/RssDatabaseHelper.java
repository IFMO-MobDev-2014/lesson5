package ru.ifmo.md.lesson5.rssreader;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Nikita Yaschenko on 18.10.14.
 */
public class RssDatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "rss.db";
    private static final int VERSION = 1;

    private static final String TABLE_RSS = "rss";
    private static final String COLUMN_RSS_NAME = "NAME";
    private static final String COLUMN_RSS_URL = "URL";
    private static final String COLUMN_RSS_FAVOURITE = "FAVOURITE";


    public RssDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE " + TABLE_RSS + " (" +
                "ID        INT  PRIMARY KEY AUTOINCREMENT NOT NULL," +
                "NAME      TEXT                           NOT NULL," +
                "URL       TEXT                           NOT NULL," +
                "FAVOURITE INT" +
                ");"
        );
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

}
