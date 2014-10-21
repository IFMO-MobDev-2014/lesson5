package odeen.rssreader;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Женя on 19.10.2014.
 */
public class RSSReaderDBSource {
    private String[] allColumns = {
            RSSReaderDBHelper.COLUMN_ID,
            RSSReaderDBHelper.COLUMN_UUID,
            RSSReaderDBHelper.COLUMN_TITLE,
            RSSReaderDBHelper.COLUMN_URL,
            RSSReaderDBHelper.COLUMN_PUBDATE,
            RSSReaderDBHelper.COLUMN_DESCRIPTION,
            RSSReaderDBHelper.COLUMN_IMAGELINK };
    private SQLiteDatabase mDatabase;
    private RSSReaderDBHelper mDBHelper;
    private static final SimpleDateFormat mDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    public RSSReaderDBSource(Context context) {
        mDBHelper = new RSSReaderDBHelper(context);
    }
    public void open() throws SQLException {
        mDatabase = mDBHelper.getWritableDatabase();
    }
    private void clear(String table) {
        Cursor cur = mDatabase.query(table, allColumns, null, null, null, null, null);
        cur.moveToFirst();
        for (; !cur.isAfterLast(); cur.moveToNext()) {
            String selection = "_id=" + cur.getInt(0);
            mDatabase.delete(table, selection, null);
        }
    }
    public void close() {
        mDBHelper.close();
    }
    private void insertNews(News news, String table) {
        ContentValues values = new ContentValues();
        values.put(RSSReaderDBHelper.COLUMN_UUID, news.getId().toString());
        values.put(RSSReaderDBHelper.COLUMN_TITLE, news.getTitle());
        values.put(RSSReaderDBHelper.COLUMN_URL, news.getURL());
        values.put(RSSReaderDBHelper.COLUMN_PUBDATE, mDateFormat.format(news.getPubDate()));
        values.put(RSSReaderDBHelper.COLUMN_DESCRIPTION, news.getDescription());
        values.put(RSSReaderDBHelper.COLUMN_IMAGELINK, news.getImageLink());//TODO:NULL LINK
        mDatabase.insertOrThrow(table, null, values);
    }
    public void dump(ArrayList<News> news, String table) {
        try {
            mDatabase.query(table, allColumns, null, null, null, null, null);
        } catch (Exception e) {
            mDatabase.execSQL(createName(table));
        }
        clear(table);
        for (int i = 0; i < news.size(); i++)
            insertNews(news.get(i), table);
    }
    public ArrayList<News> getAllNews(String table) {
        ArrayList<News> res = new ArrayList<News>();
        Cursor cursor;
        try {
            cursor = mDatabase.query(table, allColumns, null, null, null, null, null);
        } catch (Exception e) {
            mDatabase.execSQL(createName(table));
            return getAllNews(table);
        }
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            News word = cursorToComment(cursor);
            res.add(word);
            cursor.moveToNext();
        }
        cursor.close();
        return res;
    }
    private News cursorToComment(Cursor cursor) {
        News res = new News();
        res.setId(UUID.fromString(cursor.getString(1)));
        res.setTitle(cursor.getString(2));
        res.setURL(cursor.getString(3));
        try {
            res.setPubDate(mDateFormat.parse(cursor.getString(4)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        res.setDescription(cursor.getString(5));
        res.setImageLink(cursor.getString(6));
        return  res;
    }
    private String createName(String table) {
        return "create table "
                + table + "("
                + RSSReaderDBHelper.COLUMN_ID + " integer primary key autoincrement, "
                + RSSReaderDBHelper.COLUMN_UUID + " text not null,"
                + RSSReaderDBHelper.COLUMN_TITLE + " text not null,"
                + RSSReaderDBHelper.COLUMN_URL + " text not null,"
                + RSSReaderDBHelper.COLUMN_PUBDATE + " text not null,"
                + RSSReaderDBHelper.COLUMN_DESCRIPTION + " text not null,"
                + RSSReaderDBHelper.COLUMN_IMAGELINK + " text);";
    }
}
