package com.example.alexey.lesson5;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "tabl.db";
    private static final int DATABASE_VERSION = 1;
    public static String TABLE_NAME = "contact_table";

    public static String TABLE_NAME2 = "main";
    public static final String UID = "_id";
    public static final String POSTNAME = "name";
    public static final String EMAIL = "email";

    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE "
            + TABLE_NAME + " (" + UID + " INTEGER PRIMARY KEY AUTOINCREMENT,"

   + EMAIL + " VARCHAR(255), " + POSTNAME + " text);";


    private static final String SQL_CREATE_ENTRIES2 = "CREATE TABLE "
            + TABLE_NAME2 + " (" + UID + " INTEGER PRIMARY KEY AUTOINCREMENT,"

            + EMAIL + " VARCHAR(255), " + POSTNAME + " text);";



    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS "
            + TABLE_NAME;

    public DataBase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static void delet(SQLiteDatabase db){
        db.execSQL(SQL_DELETE_ENTRIES);
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
        db.execSQL(SQL_CREATE_ENTRIES2);

    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

}
