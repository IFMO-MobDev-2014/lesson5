package volhovm.com.rssreader;

import android.content.AsyncTaskLoader;
import android.content.Context;

/**
 * @author volhovm
 *         Created on 11/9/14
 */

public class FeedUpdateLoader extends AsyncTaskLoader<Feed> {
    private final FeedDAO feedDAO;
    private final Feed feed;

    public FeedUpdateLoader(Context context, Feed feed, FeedDAO feedDAO) {
        super(context);
        this.feedDAO = feedDAO;
        this.feed = feed;
    }

    @Override
    public Feed loadInBackground() {
        Feed feed = feedDAO.fillFeed(this.feed);
        return feed;
    }
}
