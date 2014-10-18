package com.example.ru.rssreader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

public class WebViewActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_activity);
        WebView webView1 = (WebView) findViewById(R.id.webView);
        Intent intent = getIntent();
        if (intent.getStringExtra("title") == null || intent.getStringExtra("description") == null) {
            webView1.loadUrl(intent.getStringExtra("link"));
        } else {
            webView1.loadData(intent.getStringExtra("title") + "<br>" + intent.getStringExtra("description"), "text/html; charset=UTF-8", null);
        }
    }
}

