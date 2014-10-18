package ru.ifmo.md.lesson5.rssreader;

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

}
