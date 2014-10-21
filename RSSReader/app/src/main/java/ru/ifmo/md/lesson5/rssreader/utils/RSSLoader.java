package ru.ifmo.md.lesson5.rssreader.utils;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.os.Bundle;

import java.net.URL;

/**
 * Created by Nikita Yaschenko on 21.10.14.
 */
public class RSSLoader extends AsyncTaskLoader<RSSChannel> {
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
            String url = new String(mUrl);
            mUrl = null;
            RSSChannel rss = new RSSParser().parse(new URL(url));
            return rss;
        } catch (Exception e) {
            return null;
        }
    }

}
