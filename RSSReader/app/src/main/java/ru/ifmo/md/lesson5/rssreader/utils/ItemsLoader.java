package ru.ifmo.md.lesson5.rssreader.utils;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

import ru.ifmo.md.lesson5.rssreader.RSSManager;

/**
 * Created by Nikita Yaschenko on 21.10.14.
 */
public class ItemsLoader extends CursorLoader {
    private static final String EXTRA_RSS_ID = "RSS_ID";

    private RSSManager mRSSManager;
    private long mRssId;

    public ItemsLoader(Context context, RSSManager manager, Bundle args) {
        super(context);
        mRSSManager = manager;

        mRssId = args.getLong(EXTRA_RSS_ID, -1);
        Log.d("TAG", "Loader loads id: " + mRssId);
    }

    @Override
    public Cursor loadInBackground() {
        return mRSSManager.getAllItems(mRssId);
    }
}
