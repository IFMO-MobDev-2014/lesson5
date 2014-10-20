package com.example.vitalik.lesson5;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

/**
 * Created by vitalik on 20.10.14.
 */
public class WebActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_layoyt);
        WebView webView = (WebView)findViewById(R.id.webView);
        webView.loadUrl(getIntent().getExtras().getString("link"));
        webView.getSettings().setJavaScriptEnabled(true);
    }
}
