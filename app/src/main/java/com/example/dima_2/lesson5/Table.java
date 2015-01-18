package com.example.dima_2.lesson5;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Dima_2 on 16.01.2015.
 */
public class Table {
    public static final String TABLE_NAME = "CHANNELS_TABLE";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_LINK = "link";
    
    private static final String DATABASE_CREATE = "create table if not exists " + TABLE_NAME
            + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_NAME + " text, "
            + COLUMN_LINK + " text" + ");";
    
    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }
    
    public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(database);
    }
}