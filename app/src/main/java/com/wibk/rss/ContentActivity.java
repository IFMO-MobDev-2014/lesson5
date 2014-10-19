package com.wibk.rss;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.webkit.WebView;


public class ContentActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        Intent intent = getIntent();
        String link = intent.getStringExtra(MainActivity.LINK_EXTRA);
        WebView webView = (WebView) findViewById(R.id.webView);
        webView.loadUrl(link);
    }
}
