package net.dimatomp.lesson5;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;

/**
 * Created by dimatomp on 21.10.14.
 */
public class FeedLoaderWithUpdate extends CursorLoader {
    private final int feedId;

    public FeedLoaderWithUpdate(Context context, int feedId, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        super(context, Uri.parse("content://net.dimatomp.feeds.provider/entries?feedId=" + feedId), projection, selection, selectionArgs, sortOrder);
        this.feedId = feedId;
    }

    @Override
    public Cursor loadInBackground() {
        getContext().getContentResolver().update(
                Uri.parse("content://net.dimatomp.feeds.provider/refresh?feedId=" + feedId), null, null, null);
        return super.loadInBackground();
    }
}
