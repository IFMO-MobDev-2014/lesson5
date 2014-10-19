package ru.ifmo.md.rssdef;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by Svet on 19.10.2014.
 */
public class DataStorage {

    DatabaseHelper db;
    SQLiteDatabase sql;
    DataStorage(Context c) {
        db = new DatabaseHelper(c, "database.db", null, 1);
    }

    public void put(String name, String url) {
        sql = db.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(db.NAME_COLUM, name);
        values.put(db.URL_COLUM, url);
        sql.insert(db.tableName, null, values);

        db.close();
    }

    public ArrayList<String> read() {
        sql = db.getWritableDatabase();

        Cursor cursor = sql.query(db.tableName, new String[] {db.NAME_COLUM, db.URL_COLUM}, null, null, null, null, null);
        cursor.moveToFirst();
        ArrayList<String> result = new ArrayList<String>();
        while(!cursor.isAfterLast()) {
            String name = cursor.getString(cursor.getColumnIndex(db.NAME_COLUM));
            String url = cursor.getString(cursor.getColumnIndex(db.URL_COLUM));
            result.add(name);
            result.add(url);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return result;
    }
}
