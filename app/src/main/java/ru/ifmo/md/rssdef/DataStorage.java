package ru.ifmo.md.rssdef;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by Svet on 19.10.2014.
 */
public class DataStorage {

    DatabaseHelper db;
    SQLiteDatabase sql;
    DataStorage(Context c) {
        db = new DatabaseHelper(c, "database.db", null, 1);
        getInformationFromInterner();
    }

    public void getInformationFromInterner() {
        ArrayList<Resource> resources = readResourceData();
        for(Resource resource : resources) {
            try {
                String data = new LoadInfoTask().execute(resource.url).get();
                ArrayList<Message> messages = XMLParser.parse(resource.name, data);
                for (Message m : messages) {
                    putRssRada(m);
                }
            }catch(ExecutionException e) {
                Log.i("ERROR", "ExecutionException exception");
            } catch(InterruptedException e) {
                Log.i("ERROR", "Interrupted exception");
            }
        }
    }

    public void putRssRada(Message m) {
        sql = db.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(db.TITLE_COLUM, m.title);
        values.put(db.DESCRIPTION_COLUM, m.description);
        values.put(db.SOURCE_NAME_COLUM, m.source);
        values.put(db.URL_COLUM, m.link);

        sql.insert(db.rssTable, null, values);

        db.close();
    }

    public void putSourceData(String name, String url) {
        sql = db.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(db.NAME_COLUM, name);
        values.put(db.URL_SOURCE_COLUM, url);

        sql.insert(db.urlTable, null, values);

        db.close();
    }

    public ArrayList<Message> readRssData() {
        sql = db.getWritableDatabase();

        Cursor cursor = sql.query(db.rssTable, new String[] {db.TITLE_COLUM, db.DESCRIPTION_COLUM, db.SOURCE_NAME_COLUM, db.URL_COLUM}, null, null, null, null, null);
        cursor.moveToFirst();
        ArrayList<Message> result = new ArrayList<Message>();
        while(!cursor.isAfterLast()) {
            String title = cursor.getString(cursor.getColumnIndex(db.TITLE_COLUM));
            String description = cursor.getString(cursor.getColumnIndex(db.DESCRIPTION_COLUM));
            String source = cursor.getString(cursor.getColumnIndex(db.SOURCE_NAME_COLUM));
            String url = cursor.getString(cursor.getColumnIndex(db.URL_COLUM));
            Message cur = new Message(title, description, url, source);
            result.add(cur);
            cursor.moveToNext();
        }
        cursor.close();

        db.close();
        return result;
    }

    public ArrayList<Resource> readResourceData() {
        sql = db.getWritableDatabase();

        Cursor cursor = sql.query(db.urlTable, new String[] {db.NAME_COLUM, db.URL_SOURCE_COLUM}, null, null, null, null, null);
        cursor.moveToFirst();
        ArrayList<Resource> res = new ArrayList<Resource>();
        while(!cursor.isAfterLast()) {
            String name = cursor.getString(cursor.getColumnIndex(db.NAME_COLUM));
            String url = cursor.getString(cursor.getColumnIndex(db.URL_SOURCE_COLUM));
            Resource r = new Resource(name, url);
            res.add(r);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return res;
    }
}
