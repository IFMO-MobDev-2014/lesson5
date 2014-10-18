package ru.ifmo.mobdev.rss;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.EditText;
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
    private EditText text;
    private ArrayList<String> sources = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loader);
        text = (EditText) findViewById(R.id.feed_address);
        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_feed);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeResources(android.R.color.holo_blue_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_red_dark);
        adapter = new RssAdapter(this);
        ListView view = (ListView) findViewById(R.id.feed);
        view.setAdapter(adapter);
        sources.add("http://feeds.bbci.co.uk/news/rss.xml");
        for (String s : sources) {
            new RssReaderTask(this).execute(s);
        }
    }

    public void onDataLoaded(Pair<String, ArrayList<RssArticle>> feed) {
        if (feed != null) {
            if (adapter.get(feed.first) == null) {
                adapter.put(feed);
            }
            else {
                adapter.remove(feed.first);
                adapter.put(feed);
            }
        }
    }

    public void onArticleClick(String url) {
        Intent intent = new Intent(LoaderActivity.this, ShowContentActivity.class);
        intent.putExtra(CONTENT_URL, url);
        startActivity(intent);
    }

    @Override
    public void onRefresh() {
        refreshArticles();
        new Handler().postDelayed(new Runnable() {
            @Override public void run() {
                swipeLayout.setRefreshing(false);
            }
        }, 3000);
    }

    private void refreshArticles() {
        for (String s : sources) {
            new RssReaderTask(this).execute(s);
        }
    }

    public void onAddClick(View view) {
        String url = text.getText().toString();
        new RssReaderTask(this).execute(url);
        text.setText("");
    }
}
