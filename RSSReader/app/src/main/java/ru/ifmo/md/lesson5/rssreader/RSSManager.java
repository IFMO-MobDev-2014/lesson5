package ru.ifmo.md.lesson5.rssreader;

import android.content.Context;
import android.database.Cursor;

import java.util.List;

import ru.ifmo.md.lesson5.rssreader.utils.RSSChannel;
import ru.ifmo.md.lesson5.rssreader.utils.RSSItem;

/**
 * Created by Nikita Yaschenko on 18.10.14.
 */
public class RSSManager {
    private static RSSManager sRssManager;
    private Context mAppContext;
    private RSSDatabaseHelper mHelper;

    private RSSManager(Context context) {
        mAppContext = context;
        mHelper = new RSSDatabaseHelper(context);
    }

    public static RSSManager get(Context context) {
        if (sRssManager == null) {
            sRssManager = new RSSManager(context.getApplicationContext());
        }
        return sRssManager;
    }

    public long addChannel(RSSChannel channel) {
        long id = mHelper.insertChannel(channel);
        channel.setId(id);
        return id;
    }

    public void deleteChannel(long id) {
        mHelper.deleteChannel(id);
    }

    public RSSChannel getChannel(long id) {
        return mHelper.getChannel(id).getChannel();
    }

    public long addItem(RSSItem item) {
        long id = mHelper.insertItem(item);
        item.setId(id);
        return id;
    }

    public void addItems(List<RSSItem> items) {
        for (RSSItem item : items) {
            addItem(item);
        }
    }

    public Cursor getAllChannels() {
        return mHelper.getAllChannels();
    }

    public Cursor getAllItems(long rssId) {
        return mHelper.getAllItems(rssId);
    }

    public RSSItem getItem(long itemId) {
        return mHelper.getItem(itemId).getItem();
    }

}
