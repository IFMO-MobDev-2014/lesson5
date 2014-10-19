package odeen.rssreader;

import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Женя on 17.10.2014.
 */
public class RSSNewsLibrary {
    static RSSNewsLibrary sLibrary;
    private ArrayList<News> mNews;
    private Context mContext;
    private ArrayList<String> mChannels;
    private NewsFetcherService mNewsFetcherService;
    private RSSReaderDBSource db;
    private RSSNewsLibrary(Context c) {
        db = new RSSReaderDBSource(c);
        takeFromDB();
        mChannels = new ArrayList<String>();
        mNewsFetcherService = new NewsFetcherService();
        //mChannels.add("http://feeds.bbci.co.uk/news/rss.xml");
        mChannels.add("http://lenta.ru/rss");
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
    public void addNews(News news) {
        mNews.add(news);
    }
    public void refresh() {
        mNews.clear();
        db.delete();
        for (String s : mChannels) {
            Intent i = new Intent(mContext, NewsFetcherService.class);
            i.putExtra(Constants.URL_DATA, s);
            mContext.startService(i);
        }
    }
    public ArrayList<News> getNews() {
        return mNews;
    }
    public void takeFromDB() {
        db.open();
        mNews = db.getAllNews();
        db.close();
    }

    public News get(UUID id) {
        for (News news : mNews) {
            if (news.getId().equals(id))
                return news;
        }
        return null;
    }

    private News mPipeNews;
    public void addToPipe(News news) {
        mPipeNews = news;
    }
    public void takeFromPipe() {
        if (mPipeNews != null) {
            mNews.add(mPipeNews);
            mPipeNews = null;
        }
    }


}
