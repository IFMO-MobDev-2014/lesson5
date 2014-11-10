package volhovm.com.rssreader;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

/**
 * @author volhovm
 *         Created on 11/2/14
 */

public class DatabaseHelper extends SQLiteOpenHelper implements BaseColumns {
    private static int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "rss_feeds.db";
    public static final String DATABASE_TABLE = "feeds";

    public static final String POST_FEED_NAME = "post_feed_name";
    public static final String POST_FEED_URL = "post_feed_url";



    public static final String POST_TITLE = "post_title";
    public static final String POST_DESCRIPTION = "post_description";
    public static final String POST_LINK = "post_link";
    public static final String POST_IMAGE_LINK = "post_image";
    public static final String POST_DATE = "post_date";

    public static final String CREATE_DB_QUERY = "create table "
            + DATABASE_TABLE + " (" +
            BaseColumns._ID + " integer primary key autoincrement, "
            + POST_FEED_NAME + " text not null, "
            + POST_FEED_URL + " text not null, "
            + POST_TITLE + " text not null, "
            + POST_DESCRIPTION + " text not null, "
            + POST_LINK + " text not null, "
            + POST_IMAGE_LINK + " text, "
            + POST_DATE + " text);";

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DB_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w("SQLite", "Updating from version " + oldVersion + " to version " + newVersion);
        db.execSQL("DROP TABLE IF IT EXIST " + DATABASE_TABLE);
        onCreate(db);
    }
}
