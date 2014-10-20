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
    final String rssTable = "RSS_TABLE";
    final String urlTable = "URL_TABLE";

    //colums rssTable
    final String TITLE_COLUM = "TITLE";
    final String DESCRIPTION_COLUM = "DESCRIPTION";
    final String SOURCE_NAME_COLUM = "SOURCE_NAME";
    final String URL_COLUM = "URL";

    //colums urlTable
    final String NAME_COLUM = "NAME";
    final String URL_SOURCE_COLUM = "URL_SOURCE";

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String scriptData = "create table "
                + rssTable + " (" + BaseColumns._ID + " integer primary key autoincrement, " +
                TITLE_COLUM + " text not null, " +
                DESCRIPTION_COLUM + " text not null, " +
                SOURCE_NAME_COLUM + " text not null, " +
                URL_COLUM + " text not null);";
        sqLiteDatabase.execSQL(scriptData);

        String srriptUrl = "create table " +
                urlTable + " (" + BaseColumns._ID + " integer primary key autoincrement, " +
                NAME_COLUM + " text not null, " +
                URL_SOURCE_COLUM + " text not null);";
        sqLiteDatabase.execSQL(srriptUrl);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

    }
}
