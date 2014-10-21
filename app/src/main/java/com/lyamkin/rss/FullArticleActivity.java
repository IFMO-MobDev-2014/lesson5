package com.lyamkin.rss;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

public class FullArticleActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WebView articleView = new WebView(this);
        setContentView(articleView);

        Intent fromFeed = getIntent();
        String articleLink = fromFeed.getStringExtra(FeedActivity.EXTRA_URL);
        String articleTitle = fromFeed.getStringExtra(FeedActivity.EXTRA_TITLE);

        setTitle(articleTitle);

        articleView.loadUrl(articleLink);
    }
}