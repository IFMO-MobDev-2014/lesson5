package ru.ifmo.mobdev.rss;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;

import ru.ifmo.mobdev.rss.utils.RssArticle;

/**
 * @author sugakandrey
 */
public class LoaderActivity extends Activity implements SwipeRefreshLayout.OnRefreshListener {
    static final String CONTENT_URL = "ru.ifmo.mobdev.rss.content";
    public static RssAdapter adapter;
    private SwipeRefreshLayout swipeLayout;
    private ArrayList<String> sources = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loader);
        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_feed);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeResources(android.R.color.holo_blue_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_red_dark);
        adapter = new RssAdapter(this);
        ListView view = (ListView) findViewById(R.id.feed);
        view.setAdapter(adapter);
        sources.add("http://feeds.bbci.co.uk/news/rss.xml");
        sources.add("http://echo.msk.ru/interview/rss-fulltext.xml");
        for (String s : sources) {
            new RssReaderTask(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, s);
        }
    }

    public void onDataLoaded(ArrayList<RssArticle> items) {
        adapter.addItems(items);
        adapter.notifyDataSetChanged();
    }

    public void onArticleClick(String url) {
        Intent intent = new Intent(LoaderActivity.this, ShowContentActivity.class);
        intent.putExtra(CONTENT_URL, url);
        startActivity(intent);
    }

    @Override
    public void onRefresh() {
        refreshArticles();
        adapter.notifyDataSetChanged();
        new Handler().postDelayed(new Runnable() {
            @Override public void run() {
                swipeLayout.setRefreshing(false);
            }
        }, 3000);
    }

    private void refreshArticles() {
        new RssReaderTask(this).execute("http://feeds.bbci.co.uk/news/rss.xml");
    }
}
