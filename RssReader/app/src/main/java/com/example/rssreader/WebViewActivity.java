package com.example.rssreader;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.webkit.WebView;

/**
 * Created by Яна on 13.02.2015.
 */
public class WebViewActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web);

        String url = getIntent().getStringExtra("url");
        WebView webView = (WebView)findViewById(R.id.webView);
        webView.loadUrl(url);
    }
}
