package ru.ifmo.md.lesson5.rssreader;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;

/**
 * Created by Nikita Yaschenko on 19.10.14.
 */
public class RssLoader extends CursorLoader {

    private RssManager manager;

    public RssLoader(Context context, RssManager manager) {
        super(context);
        this.manager = manager;
    }

    @Override
    public Cursor loadInBackground() {
        return manager.getAllRss();
    }
}