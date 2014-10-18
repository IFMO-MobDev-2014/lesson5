package ru.ifmo.mobdev.rss;

import android.os.AsyncTask;

import java.util.ArrayList;
import ru.ifmo.mobdev.rss.utils.RSSHandler;
import ru.ifmo.mobdev.rss.utils.RssArticle;

/**
 * @author sugakandrey
 */
public class RssReaderTask extends AsyncTask<String, Void, ArrayList<RssArticle>>{
    private final LoaderActivity ctx;

    public RssReaderTask(LoaderActivity ctx) {
        this.ctx = ctx;
    }

    @Override
    protected ArrayList<RssArticle> doInBackground(String... strings) {
        RSSHandler handler = new RSSHandler();
        return handler.getArticles(strings[0]);
    }

    @Override
    protected void onPostExecute(ArrayList<RssArticle> rssArticles) {
       ctx.onDataLoaded(rssArticles);
    }
}
