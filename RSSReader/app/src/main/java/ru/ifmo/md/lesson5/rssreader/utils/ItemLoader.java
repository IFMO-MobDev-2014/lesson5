package ru.ifmo.md.lesson5.rssreader.utils;

import android.content.Context;
import android.os.Bundle;

import ru.ifmo.md.lesson5.rssreader.RSSManager;

/**
 * Created by Nikita Yaschenko on 21.10.14.
 */
public class ItemLoader extends DataLoader<RSSItem> {
    private static final String ARGS_ITEM_ID = "ITEM_ID";

    private long mItemId;
    private RSSManager mRSSManager;

    public ItemLoader(Context context, Bundle args, RSSManager rssManager) {
        super(context);
        mRSSManager = rssManager;
        if (args != null) {
            mItemId = args.getLong(ARGS_ITEM_ID);
        }
    }

    @Override
    public RSSItem loadInBackground() {
        return mRSSManager.getItem(mItemId);
    }
}
