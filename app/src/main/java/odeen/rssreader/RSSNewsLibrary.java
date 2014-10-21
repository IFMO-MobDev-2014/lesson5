package odeen.rssreader;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.UUID;

/**
 * Created by Женя on 17.10.2014.
 */
public class RSSNewsLibrary {
    public static final int NEWS_MAX_COUNT = 100;
    static RSSNewsLibrary sLibrary;
    private ArrayList< ArrayList<News> > mNews;
    private Context mContext;
    private ArrayList<String> mChannels;
    private NewsFetcherService mNewsFetcherService;

    private RSSReaderDBSource db;

    private RSSNewsLibrary(Context c) {
        db = new RSSReaderDBSource(c);
        mChannels = new ArrayList<String>();
        mChannels.add("http://lenta.ru/rss");
        mChannels.add("http://feeds.bbci.co.uk/news/rss.xml");
        mChannels.add("http://echo.msk.ru/interview/rss-fulltext.xml");

        mNews = new ArrayList<ArrayList<News>>();
        mPipeNews = new ArrayList<ArrayList<News>>();
        for (int i = 0; i < mChannels.size(); i++) {
            mNews.add(new ArrayList<News>());
            mPipeNews.add(new ArrayList<News>());
        }
        mNewsFetcherService = new NewsFetcherService();
        for (int i = 0; i < mPipeNews.size(); i++)
            mPipeNews.set(i, new ArrayList<News>());
        mContext = c;
    }

    public static RSSNewsLibrary get(Context c) {
        if (sLibrary == null) {
            sLibrary = new RSSNewsLibrary(c.getApplicationContext());
        }
        return sLibrary;
    }

    public void addChannel(String channel) {
        mChannels.add(channel);
    }

    public void refresh(int table_id) {
        //mNews.get(table_id).clear();
            Intent i = new Intent(mContext, NewsFetcherService.class);
            i.putExtra(Constants.URL_DATA, mChannels.get(table_id));
            i.putExtra(NewsListActivity.CHANNEL_ID, table_id);
            mContext.startService(i);

    }
    public ArrayList<News> getNews(int table_id) {
        if (mNews.get(table_id).size() == 0)
           getFromDB(table_id);
        return mNews.get(table_id);
    }

    private void getFromDB(int table_id) {
        db.open();
        mNews.set(table_id, db.getAllNews("table_" + table_id));
        Collections.sort(mNews.get(table_id), new Comparator<News>() {
            @Override
            public int compare(News news, News news2) {
                return news2.getPubDate().compareTo(news.getPubDate());
            }
        });
        db.close();
    }

    public News get(UUID id, int table_id) {
        for (News news : mNews.get(table_id)) {
            if (news.getId().equals(id))
                return news;
        }
        return null;
    }

    volatile private ArrayList< ArrayList<News> > mPipeNews;
    public void addToPipe(News news, int table_id) {
        synchronized (mPipeNews) {
            mPipeNews.get(table_id).add(news);
        }
    }
    public void clearPipe(int table_id) {
        synchronized (mPipeNews) {
            mPipeNews.get(table_id).clear();
        }
    }
    public void takeFromPipe(int table_id) {
        synchronized (mPipeNews) {
            for (int i = 0; i < mPipeNews.get(table_id).size(); i++) {
                boolean added = false;
                for (int j = 0; j < mNews.get(table_id).size(); j++) {
                    if (mNews.get(table_id).get(j).equals(mPipeNews.get(table_id).get(i))) {
                        added = true;
                        break;
                    }
                    if (mNews.get(table_id).get(j).getPubDate().compareTo(mPipeNews.get(table_id).get(i).getPubDate()) < 0) {
                        mNews.get(table_id).add(j, mPipeNews.get(table_id).get(i));
                        added = true;
                        break;
                    }
                }
                if (!added)
                    mNews.get(table_id).add(mPipeNews.get(table_id).get(i));
            }
            mPipeNews.get(table_id).clear();
            while (mNews.get(table_id).size() > NEWS_MAX_COUNT)
                mNews.get(table_id).remove(mNews.get(table_id).size() - 1);

        }
    }

    public boolean alreadyHave(News news, int table_id) {
        for (int i = 0; i < mNews.get(table_id).size(); i++)
            if (mNews.get(table_id).get(i).equals(news))
                return true;
        return false;
    }

    public void dump(int table_id) {
        RSSReaderDBSource db = new RSSReaderDBSource(mContext);
        db.open();
        db.dump(mNews.get(table_id), "table_" + table_id);
        db.close();
    }

    public ArrayList<String> getChannels() {
        return mChannels;
    }
}
