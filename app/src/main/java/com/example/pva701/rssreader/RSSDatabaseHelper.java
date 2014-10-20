package com.example.pva701.rssreader;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;

/**
 * Created by pva701 on 17.10.14.
 */
public class RSSDatabaseHelper extends SQLiteOpenHelper {
    public static class SourceCursor extends CursorWrapper {
        public SourceCursor(Cursor cursor) {
            super(cursor);
        }

        public SourcesManager.Source getSource() {
            if (isBeforeFirst() || isAfterLast())
                return null;
            return new SourcesManager.Source(getLong(getColumnIndexOrThrow(COLUMN_SOURCES_ID)),
                    getString(getColumnIndexOrThrow(COLUMN_SOURCES_NAME)),
                    getString(getColumnIndexOrThrow(COLUMN_SOURCES_URL)),
                    new Date(getLong(getColumnIndexOrThrow(COLUMN_SOURCES_LAST_UPDATE))));
        }
    }

    public static class NewsCursor extends CursorWrapper {
        public NewsCursor(Cursor cursor) {
            super(cursor);
        }


        public NewsManager.News getNews() {
            NewsManager.News ret = new NewsManager.News();
            ret.setId(getInt(getColumnIndexOrThrow(COLUMN_NEWS_ID)));
            ret.setLink(getString(getColumnIndexOrThrow(COLUMN_NEWS_LINK)));
            ret.setTitle(getString(getColumnIndexOrThrow(COLUMN_NEWS_TITLE)));
            ret.setDescription(getString(getColumnIndexOrThrow(COLUMN_NEWS_DESCRIPTION)));
            ret.setPubDate(new Date(getInt(getColumnIndexOrThrow(COLUMN_NEWS_PUB_DATE)) * 1000L));
            ret.setSourceId(getInt(getColumnIndexOrThrow(COLUMN_NEWS_SOURCE_ID)));
            ret.setRead(getInt(getColumnIndexOrThrow(COLUMN_NEWS_READ)) == 1);
            String category = getString(getColumnIndexOrThrow(COLUMN_NEWS_CATEGORY));
            StringTokenizer tokenizer = new StringTokenizer(category, ",");
            while (tokenizer.hasMoreTokens())
                ret.addCategory(tokenizer.nextToken());
            return ret;
        }
    }

    private static final int VERSION = 1;
    private static final String DB_NAME = "RSS";

    private static final String TABLE_SOURCES = "sources";
    private static final String COLUMN_SOURCES_ID = "_id";
    private static final String COLUMN_SOURCES_NAME = "name";
    private static final String COLUMN_SOURCES_URL = "url";
    private static final String COLUMN_SOURCES_LAST_UPDATE = "last_update";

    private static final String TABLE_NEWS = "news";
    private static final String COLUMN_NEWS_ID = "_id";
    private static final String COLUMN_NEWS_TITLE = "title";
    private static final String COLUMN_NEWS_LINK = "link";
    private static final String COLUMN_NEWS_DESCRIPTION = "description";
    private static final String COLUMN_NEWS_PUB_DATE = "pub_date";
    private static final String COLUMN_NEWS_CATEGORY = "category";
    private static final String COLUMN_NEWS_READ = "read";
    private static final String COLUMN_NEWS_SOURCE_ID = "source_id";

    public RSSDatabaseHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table sources (_id integer primary key autoincrement, " +
                                                  "name varchar(64), " +
                                                  "url varchar(100), " +
                                                  "last_update integer)");

        db.execSQL("create table news (_id integer primary key autoincrement, " +
                                "title varchar(100), " +
                                "link varchar(100), " +
                                "description varchar(1000), " +
                                "pub_date integer, " +
                                "category varchar(100), " +
                                "`read` integer, " +
                                "source_id integer)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        //none
    }

    public long insertSource(SourcesManager.Source source) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_SOURCES_NAME, source.getName());
        cv.put(COLUMN_SOURCES_URL, source.getUrl());
        cv.put(COLUMN_SOURCES_LAST_UPDATE, source.getLastUpdate().getTime());
        return getWritableDatabase().insert(TABLE_SOURCES, null, cv);
    }

    public long insertNews(NewsManager.News cur) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NEWS_TITLE, cur.getTitle());
        cv.put(COLUMN_NEWS_LINK, cur.getLink());
        cv.put(COLUMN_NEWS_DESCRIPTION, cur.getDescription());
        cv.put(COLUMN_NEWS_PUB_DATE, cur.getPubDate().getTime() / 1000);
        cv.put(COLUMN_NEWS_CATEGORY, cur.getCategory().toString());
        cv.put(COLUMN_NEWS_READ, cur.isRead());
        if (cur.getSourceId() == 0)
            throw new RuntimeException("source id incorrect!");
        cv.put(COLUMN_NEWS_SOURCE_ID, cur.getSourceId());
        return getWritableDatabase().insert(TABLE_NEWS, null, cv);
        /*SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO " + TABLE_NEWS + " (title, link, description, pub_date, category, read, source_id) " +
                "VALUE(?, ?, ?, ?, ?, ?, ?)", new Object[]{cur.getTitle(), cur.getLink(), cur.getDescription(), cur.getPubDate().getTime(),
                cur.toStringCategory(), cur.isRead(), cur.getSourceId()});*/

    }

    public SourceCursor querySources() {
        Cursor wrapper = getReadableDatabase().query(TABLE_SOURCES,
                null, null, null, null, null, null);
        return new SourceCursor(wrapper);
    }

    public NewsCursor queryNews(int sourceId) {
        Cursor wrapper = getReadableDatabase().query(TABLE_NEWS, null, "source_id=" + sourceId, null, null, null, COLUMN_SOURCES_LAST_UPDATE + " asc");
        return new NewsCursor(wrapper);
    }

    public NewsCursor queryNewses() {
        Cursor wrapper = getReadableDatabase().query(TABLE_NEWS, null, null, null, null, null, null);
        return new NewsCursor(wrapper);
    }


    public void deleteNews(int id) {
        getWritableDatabase().delete(TABLE_NEWS, COLUMN_NEWS_ID + "=" + id, null);
    }

    public int getUnreadMessages() {
        Cursor cur = getReadableDatabase().query(TABLE_NEWS, null, "read=0", null, null, null, null);
        cur.moveToNext();
        return cur.getInt(0);
    }

    public void deleteSources() {//Bad
        getWritableDatabase().execSQL("DELETE FROM " + TABLE_SOURCES);
    }

    public void updateNews(NewsManager.News news) {
        getWritableDatabase().execSQL("UPDATE " + TABLE_NEWS + " SET title=?, link=?, description=?," +
                "pub_date=?, category=?, `read`=?, source_id=? WHERE _id=?",
                new Object[] {news.getTitle(), news.getLink(), news.getDescription(), news.getPubDate().getTime() / 1000, news.toStringCategory(), news.isRead(), news.getSourceId(), news.getId()});
    }

    public void updateSource(SourcesManager.Source source) {
        getWritableDatabase().execSQL("UPDATE " + TABLE_SOURCES + " SET name=?, url=?, last_update=? WHERE _id=?",
                new Object[] {source.getName(), source.getUrl(), source.getLastUpdate(), source.getId()});
    }
}
