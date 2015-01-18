package com.example.dima_2.lesson5;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

/**
 * Created by Dima_2 on 16.01.2015.
 */
public class DatabaseContentProvider extends ContentProvider {
    private DatabaseHelper databaseHelper;
    private static final int CHANNELS_COUNT = 10;
    private static final int CHANNEL_INDEX = 20;
    private static final int ELEMENTS = 30;
    private static final int ELEMENT_ID = 40;

    private static final String AUTHORITY = "com.example.dima_2.lesson5.contentprovider";
    private static final String BASE_PATH_CHANNELS = "channels";
    private static final String BASE_PATH_ITEMS = "items";

    public static final Uri CONTENT_URI_CHANNELS = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH_CHANNELS);
    public static final Uri CONTENT_URI_ITEMS = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH_ITEMS);

    private static final UriMatcher matcher;
    static {
        matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(AUTHORITY, BASE_PATH_CHANNELS, CHANNELS_COUNT);
        matcher.addURI(AUTHORITY, BASE_PATH_CHANNELS + "/#", CHANNEL_INDEX);
        matcher.addURI(AUTHORITY, BASE_PATH_ITEMS, ELEMENTS);
        matcher.addURI(AUTHORITY, BASE_PATH_ITEMS + "/#", ELEMENT_ID);
    }

    @Override
    public boolean onCreate() {
        databaseHelper = new DatabaseHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qBuilder = new SQLiteQueryBuilder();
        int uriType = matcher.match(uri);
        if (uriType == CHANNELS_COUNT || uriType == CHANNEL_INDEX)
               qBuilder.setTables(Table.TABLE_NAME);
        else
               qBuilder.setTables(ItemsTable.TABLE_NAME);
        switch (uriType) {
           case CHANNELS_COUNT:
                   break;
           case CHANNEL_INDEX:
                   qBuilder.appendWhere(Table.COLUMN_ID + "=" + uri.getLastPathSegment());
                   break;
           case ELEMENTS:
                   break;
           case ELEMENT_ID:
                   qBuilder.appendWhere(ItemsTable.COLUMN_ID + "=" + uri.getLastPathSegment());
                   break;
           default:
                   throw new IllegalArgumentException("Wrong URI: " + uri);
       }
       SQLiteDatabase database = databaseHelper.getWritableDatabase();
       Cursor cursor = qBuilder.query(database, projection, selection, selectionArgs, null, null, sortOrder);
       cursor.setNotificationUri(getContext().getContentResolver(), uri);
       return cursor;
   }

   public String getType(Uri uri) {
       return null;
   }

   public Uri insert(Uri uri, ContentValues values) {
       int uriType = matcher.match(uri);
       SQLiteDatabase database = databaseHelper.getWritableDatabase();
       long index = 0;
       switch (uriType) {
           case CHANNELS_COUNT:
               index = database.insert(Table.TABLE_NAME, null, values);
               break;
           case ELEMENTS:
               index = database.insert(ItemsTable.TABLE_NAME, null, values);
               break;
           default:
               throw new IllegalArgumentException("Wrong URI - " + uri);
       }
       getContext().getContentResolver().notifyChange(uri, null);
       return Uri.withAppendedPath(uri, String.valueOf(index));
   }

   public int delete(Uri uri, String selection, String[] selectionArgs) {
       int uriType = matcher.match(uri);
       SQLiteDatabase database = databaseHelper.getWritableDatabase();
       int deletedRows = 0;
       switch (uriType) {
           case CHANNELS_COUNT:
               deletedRows = database.delete(Table.TABLE_NAME, selection, selectionArgs);
               break;
           case CHANNEL_INDEX:
               String index = uri.getLastPathSegment();
               if (TextUtils.isEmpty(selection)) {
                   deletedRows = database.delete(Table.TABLE_NAME, Table.COLUMN_ID + "=" + index, null);
               } else {
                   deletedRows = database.delete(Table.TABLE_NAME, Table.COLUMN_ID + "=" + index + " and " + selection, selectionArgs);
               }
               break;
           case ELEMENTS:
               deletedRows = database.delete(ItemsTable.TABLE_NAME, selection, selectionArgs);
               break;
           case ELEMENT_ID:
               String indexTmp = uri.getLastPathSegment();
               if (TextUtils.isEmpty(selection)) {
                   deletedRows = database.delete(ItemsTable.TABLE_NAME, ItemsTable.COLUMN_ID + "=" + indexTmp, null);
               } else {
                   deletedRows = database.delete(ItemsTable.TABLE_NAME, ItemsTable.COLUMN_ID + "=" + indexTmp + " and " + selection, selectionArgs);
               }
               break;
           default:
               throw new IllegalArgumentException("Wrong URI: " + uri);
       }
       getContext().getContentResolver().notifyChange(uri, null);
       return deletedRows;
   }

   public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
       int uriType = matcher.match(uri);
       SQLiteDatabase database = databaseHelper.getWritableDatabase();
       int updatedRows = 0;
       switch (uriType) {
           case CHANNELS_COUNT:
               updatedRows = database.update(Table.TABLE_NAME, values, selection, selectionArgs);
               break;
           case CHANNEL_INDEX:
               String id = uri.getLastPathSegment();
               if (TextUtils.isEmpty(selection)) {
                   updatedRows = database.update(Table.TABLE_NAME, values, Table.COLUMN_ID + "=" + id, null);
               } else {
                   updatedRows = database.update(Table.TABLE_NAME, values, Table.COLUMN_ID + "=" + id + " and " + selection, selectionArgs);
               }
               break;
           case ELEMENTS:
               updatedRows = database.update(ItemsTable.TABLE_NAME, values, selection, selectionArgs);
               break;
           case ELEMENT_ID:
               String id2 = uri.getLastPathSegment();
               if (TextUtils.isEmpty(selection)) {
                   updatedRows = database.update(ItemsTable.TABLE_NAME, values, ItemsTable.COLUMN_ID + "=" + id2, null);
               } else {
                   updatedRows = database.update(ItemsTable.TABLE_NAME, values, ItemsTable.COLUMN_ID + "=" + id2 + " and " + selection, selectionArgs);
               }
               break;
           default:
               throw new IllegalArgumentException("Wrong URI - " + uri);
       }
       getContext().getContentResolver().notifyChange(uri, null);
       return updatedRows;
   }
}