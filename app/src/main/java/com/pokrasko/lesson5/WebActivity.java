package com.pokrasko.lesson5;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

/**
 * Created by pokrasko on 21.10.14.
 */
public class WebActivity extends Activity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        webView = new WebView(this);
        setContentView(webView);

        setTitle(getIntent().getStringExtra("title"));

        String link = getIntent().getStringExtra("link");
        webView.loadUrl(link);
    }
}
