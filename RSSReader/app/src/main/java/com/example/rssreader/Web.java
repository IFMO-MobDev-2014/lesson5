package com.example.rssreader;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class Web extends Activity {
    private WebView webView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web);
        String link = getIntent().getStringExtra("link");
        webView = (WebView) findViewById(R.id.webView);
        webView.loadUrl(link);
    }
}