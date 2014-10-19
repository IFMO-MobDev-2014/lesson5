package ru.ifmo.md.lesson5.rssreader;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Nikita Yaschenko on 18.10.14.
 */
public class RssManager {
    private static RssManager sRssManager;
    private Context mAppContext;
    private RssDatabaseHelper mHelper;

    private RssManager(Context context) {
        mAppContext = context;
        mHelper = new RssDatabaseHelper(context);
    }

    public static RssManager get(Context context) {
        if (sRssManager == null) {
            sRssManager = new RssManager(context.getApplicationContext());
        }
        return sRssManager;
    }

    public void deleteRss(long id) {
        mHelper.deleteRss(id);
    }

    public Cursor getAllRss() {
        return mHelper.getAllRss();
    }

    public Cursor getRss(long id) {
        return mHelper.getRss(id);
    }

    public long insertRss(Rss rss) {
        long id = mHelper.insertRss(rss);
        rss.setId(id);
        return id;
    }

}
