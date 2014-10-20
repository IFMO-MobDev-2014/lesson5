package ru.ifmo.md.lesson5.rssreader;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class RssActivity extends Activity {
    private static final String EXTRA_RSS_ID = "RSS_ID";

    private RSSManager mRssManager;
    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rss);

        //TODO: implement
        /*
        mRssManager = RSSManager.get(this);
        mWebView = (WebView) findViewById(R.id.webView);
        mWebView.setWebViewClient(new WebViewClient());

        long id = getIntent().getLongExtra(EXTRA_RSS_ID, -1);
        Log.d("TAG", "RssActivity id: " + id);
        if (id != -1) {
            RSSItem RSSItem = mRssManager.getRss(id);
            if (RSSItem == null) {
                finish();
            }
            mWebView.loadUrl(RSSItem.getUrl());
        } else {
            finish();
        }
        */
    }

}
