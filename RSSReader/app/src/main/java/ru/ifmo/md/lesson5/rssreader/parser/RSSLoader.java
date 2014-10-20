package ru.ifmo.md.lesson5.rssreader.parser;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.os.Bundle;
import android.util.Pair;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.List;

import ru.ifmo.md.lesson5.rssreader.RSSChannel;
import ru.ifmo.md.lesson5.rssreader.RSSItem;

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
        try {
            Pair<RSSChannel, List<RSSItem>> rss = new RSSReader().parse(new URL(mUrl));
            if (rss != null) return rss.first;
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    protected void onStartLoading() {
        if (takeContentChanged()) {
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }
}
