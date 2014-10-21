package ru.ifmo.md.lesson5.rssreader.utils;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.os.Bundle;
import android.util.Pair;

import java.net.URL;
import java.util.List;

/**
 * Created by Nikita Yaschenko on 21.10.14.
 */
public class RSSLoader extends DataLoader<RSSChannel> {
    private static final String ARGS_URL = "URL";

    private String mUrl = null;

    public RSSLoader(Context context, Bundle args) {
        super(context);
        if (args != null) {
            mUrl = args.getString(ARGS_URL);
        }
    }

    @Override
    public RSSChannel loadInBackground() {
        if (mUrl == null) {
            return null;
        }
        try {
            RSSChannel rss = new RSSReader().parse(new URL(mUrl));
            return rss;
        } catch (Exception e) {
            return null;
        }
    }

}
