package ru.ifmo.md.rssdef;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by Svet on 19.10.2014.
 */
public class DatabaseHelper extends SQLiteOpenHelper implements BaseColumns {

    final String databaseName = "RSS_DATA";
    final String tableName = "RSS_TABLE";

    //colums
    final String NAME_COLUM = "NAME";
    final String URL_COLUM = "URL";

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String script = "create table "
                + tableName + " (" + BaseColumns._ID + " integer primary key autoincrement, " +
                NAME_COLUM + " text not null, " +
                URL_COLUM + " text not null);";
        sqLiteDatabase.execSQL(script);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

    }
}
