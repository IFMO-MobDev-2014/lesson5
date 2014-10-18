package ru.ifmo.md.lesson5.rssreader;

import android.content.Context;

/**
 * Created by Nikita Yaschenko on 18.10.14.
 */
public class RssManager {
    private static RssManager sRssManager;
    private Context mAppContext;

    private RssManager(Context context) {
        mAppContext = context;
    }

    public static RssManager get(Context context) {
        if (sRssManager == null) {
            sRssManager = new RssManager(context.getApplicationContext());
        }
        return sRssManager;
    }
}
