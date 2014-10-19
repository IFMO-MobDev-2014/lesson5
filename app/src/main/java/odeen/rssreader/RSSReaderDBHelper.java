package odeen.rssreader;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Женя on 19.10.2014.
 */


public class RSSReaderDBHelper extends SQLiteOpenHelper {
    public static final String TABLE_NAME = "news";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_UUID = "NEWS_UUID";
    public static final String COLUMN_TITLE = "NEWS_TITLE";
    public static final String COLUMN_URL = "NEWS_URL";
    public static final String COLUMN_PUBDATE = "NEWS_PUB_DATE";
    public static final String COLUMN_DESCRIPTION = "NEWS_DESCRIPTION";
    public static final String COLUMN_IMAGELINK = "NEWS_IMAGE_LINK";

    private static final String DATABASE_CREATE = "create table "
            + TABLE_NAME + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_UUID + " text not null,"
            + COLUMN_TITLE + " text not null,"
            + COLUMN_URL + " text not null,"
            + COLUMN_PUBDATE + " text not null,"
            + COLUMN_DESCRIPTION + " text not null,"
            + COLUMN_IMAGELINK + " text);";

    public static final String DATABASE_NAME = TABLE_NAME + ".db";
    public static final int DATABASE_VERSION = 1;

    public RSSReaderDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        onCreate(sqLiteDatabase);
    }
}
