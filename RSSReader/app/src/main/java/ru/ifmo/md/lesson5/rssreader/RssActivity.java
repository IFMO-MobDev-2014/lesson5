package ru.ifmo.md.lesson5.rssreader;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class RssActivity extends Activity {
    private static final String EXTRA_RSS_ID = "RSS_ID";

    private RssManager mRssManager;
    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rss);

        mRssManager = RssManager.get(this);
        mWebView = (WebView) findViewById(R.id.webView);
        mWebView.setWebViewClient(new WebViewClient());

        long id = getIntent().getLongExtra(EXTRA_RSS_ID, -1);
        Log.d("TAG", "RssActivity id: " + id);
        if (id != -1) {
            RssItem rssItem = mRssManager.getRss(id);
            if (rssItem == null) {
                finish();
            }
            mWebView.loadUrl(rssItem.getUrl());
        } else {
            finish();
        }
    }

}
