package com.daria.lesson5.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.daria.lesson5.R;
import com.daria.lesson5.ArticleAdapter;

import elements.Feed;
import parser.StackOverflowFeedParser;


public class ReaderActivity extends Activity {
    public static final String STACK_OVERFLOW = "http://stackoverflow.com/feeds/tag/android";
    public static final String URL_INTENT = "URL";

    ArticleAdapter adapter;
    Intent fullscreen;
    Feed feed = new Feed();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);
        fullscreen = new Intent(this, WebViewActivity.class);
        new StackOverflowFeedParser(this).execute(STACK_OVERFLOW);
        }

    public void onParsingFinished(final Feed feed) {
        this.feed = feed;
        setTitle(feed.getTitle());
        adapter = new ArticleAdapter(this, feed);
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String url = feed.getArticle(i).getUrl();
                fullscreen.putExtra(URL_INTENT, url);
                startActivity(fullscreen);
            }

        });
        String title = "Found " + feed.totalArticles() + " articles";
        TextView text = (TextView) findViewById(R.id.text);
        text.setText(title);
        Log.d("PARSING", "Parsing Finished");
    }
}


