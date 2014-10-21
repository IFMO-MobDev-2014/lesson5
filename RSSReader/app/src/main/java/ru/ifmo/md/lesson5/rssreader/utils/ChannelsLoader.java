package ru.ifmo.md.lesson5.rssreader.utils;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;

import ru.ifmo.md.lesson5.rssreader.RSSManager;

/**
 * Created by Nikita Yaschenko on 19.10.14.
 */
public class ChannelsLoader extends CursorLoader {

    private RSSManager mRSSManager;

    public ChannelsLoader(Context context, RSSManager mRSSManager) {
        super(context);
        this.mRSSManager = mRSSManager;
    }

    @Override
    public Cursor loadInBackground() {
        return mRSSManager.getAllChannels();
    }
}