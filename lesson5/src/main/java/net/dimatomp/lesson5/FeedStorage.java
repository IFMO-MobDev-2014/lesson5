package net.dimatomp.lesson5;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import mf.javax.xml.parsers.SAXParser;
import mf.javax.xml.parsers.SAXParserFactory;
import mf.org.apache.xerces.jaxp.SAXParserFactoryImpl;

import static net.dimatomp.lesson5.FeedColumns.ENTRIES;
import static net.dimatomp.lesson5.FeedColumns.ENTRY_DATE;
import static net.dimatomp.lesson5.FeedColumns.ENTRY_DESCRIPTION;
import static net.dimatomp.lesson5.FeedColumns.ENTRY_FEED;
import static net.dimatomp.lesson5.FeedColumns.ENTRY_TITLE;
import static net.dimatomp.lesson5.FeedColumns.ENTRY_URL;
import static net.dimatomp.lesson5.FeedColumns.FEEDS;
import static net.dimatomp.lesson5.FeedColumns.FEED_DESCRIPTION;
import static net.dimatomp.lesson5.FeedColumns.FEED_TITLE;
import static net.dimatomp.lesson5.FeedColumns.FEED_WEBSITE;
import static net.dimatomp.lesson5.FeedColumns.FEED_XML;
import static net.dimatomp.lesson5.FeedColumns._ID;

public class FeedStorage extends ContentProvider {
    private FeedDatabase database;

    private boolean updateWithID(final int id) {
        // TODO Optimize this by passing the cursor
        final String whereID = _ID + " = '" + id + "'";
        Cursor query = database.getReadableDatabase()
                .query(FEEDS, new String[]{FEED_XML}, whereID, null, null, null, null);
        query.moveToFirst();
        String xml = query.getString(query.getColumnIndex(FEED_XML));
        try {
            SAXParserFactory factory = new SAXParserFactoryImpl();
            //factory.setFeature("http://xml.org/sax/features/use-locator2", true);
            /*URL url = new URL(xml);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            InputSource source = new InputSource(urlConnection.getInputStream()); */
            SAXParser parser = factory.newSAXParser();
            parser.parse(xml, new FeedParser(new FeedParser.ParserCallbacks() {
                SQLiteDatabase db;

                private void holdDB() {
                    if (db == null)
                        db = database.getWritableDatabase();
                }

                @Override
                public void updateFeedInfo(ContentValues values) {
                    holdDB();
                    db.update(FEEDS, values, whereID, null);
                }

                @Override
                public boolean insertEntryIfNew(ContentValues values) {
                    holdDB();
                    Cursor cursor = db.query(ENTRIES, new String[]{_ID},
                            ENTRY_FEED + " = '" + id + "' AND " +
                                    ENTRY_TITLE + " = '" + values.getAsString(ENTRY_TITLE) + "' AND " +
                                    ENTRY_DATE + " = '" + values.getAsString(ENTRY_DATE) + "'", null, null, null, null);
                    if (cursor.getCount() == 0) {
                        values.put(ENTRY_FEED, id);
                        db.insert(ENTRIES, null, values);
                        return true;
                    }
                    cursor.moveToFirst();
                    db.update(ENTRIES, values,
                            _ID + " = '" + cursor.getInt(cursor.getColumnIndex(_ID)) + "'", null);
                    return false;
                }
            }));
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        throw new IllegalArgumentException("Bad delete URI");
    }

    @Override
    public String getType(Uri uri) {
        // Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        throw new IllegalArgumentException("Bad insert URI");
    }

    @Override
    public boolean onCreate() {
        database = new FeedDatabase(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = database.getReadableDatabase();
        if (uri.getPathSegments().size() == 1) {
            switch (uri.getLastPathSegment()) {
                case "feeds":
                    return db.query(FEEDS, projection, selection, selectionArgs, null, null, null);
                case "entries":
                    if (!uri.getQueryParameterNames().contains("feedId"))
                        break;
                    selection = addToSelection(selection, ENTRY_FEED, uri.getQueryParameter("feedId"));
                    return db.query(ENTRIES, projection, selection, selectionArgs, null, null, null);
            }
        }
        throw new IllegalArgumentException("Bad query URI");
    }

    private String addToSelection(String selection, String key, String value) {
        if (selection != null && !selection.isEmpty())
            selection = " AND (" + selection + ")";
        else
            selection = "";
        return key + " = '" + value + "'" + selection;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        if (uri.getPathSegments().size() == 1)
            switch (uri.getLastPathSegment()) {
                case "refresh":
                    if (uri.getQueryParameterNames().contains("feedId"))
                        selection = addToSelection(selection, _ID, uri.getQueryParameter("feedId"));
                    Cursor ids = database.getReadableDatabase().query(FEEDS, new String[]{_ID}, selection, selectionArgs, null, null, null);
                    for (ids.moveToFirst(); !ids.isAfterLast(); ids.moveToNext())
                        updateWithID(ids.getInt(ids.getColumnIndex(_ID)));
                    return ids.getCount();
            }
        throw new IllegalArgumentException("Bad update URI");
    }

    private class FeedDatabase extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = "feeds.db";

        FeedDatabase(Context context) {
            super(context, DATABASE_NAME, null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + FEEDS + " (" +
                    _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    FEED_TITLE + " TEXT, " +
                    FEED_XML + " TEXT NOT NULL, " +
                    FEED_WEBSITE + " TEXT, " +
                    FEED_DESCRIPTION + " TEXT);");
            db.execSQL("CREATE TABLE " + ENTRIES + " (" +
                    _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    ENTRY_TITLE + " TEXT, " +
                    ENTRY_FEED + " INTEGER NOT NULL, " +
                    ENTRY_DESCRIPTION + " TEXT, " +
                    ENTRY_URL + " TEXT, " +
                    ENTRY_DATE + " INTEGER);");

            ContentValues values = new ContentValues();
            values.put(FEED_XML, "http://bash.im/rss/");
            db.insert("FEEDS", null, values);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + FEEDS + ";");
            db.execSQL("DROP TABLE IF EXISTS " + ENTRIES + ";");
            onCreate(db);
        }
    }
}
