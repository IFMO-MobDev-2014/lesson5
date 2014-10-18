package ru.ifmo.mobdev.rss;

import android.os.AsyncTask;
import android.util.Pair;

import java.util.ArrayList;
import ru.ifmo.mobdev.rss.utils.RSSHandler;
import ru.ifmo.mobdev.rss.utils.RssArticle;

/**
 * @author sugakandrey
 */
public class RssReaderTask extends AsyncTask<String, Void, Pair<String, ArrayList<RssArticle>>>{
    private final LoaderActivity ctx;

    public RssReaderTask(LoaderActivity ctx) {
        this.ctx = ctx;
    }

    @Override
    protected Pair<String, ArrayList<RssArticle>> doInBackground(String... strings) {
        RSSHandler handler = new RSSHandler();
        return new Pair<String, ArrayList<RssArticle>>(strings[0], handler.getArticles(strings[0]));
    }

    @Override
    protected void onPostExecute(Pair<String, ArrayList<RssArticle>> rssArticles) {
       ctx.onDataLoaded(rssArticles);
    }
}
