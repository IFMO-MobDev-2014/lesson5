package com.daria.lesson5.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebView;

import com.daria.lesson5.R;

/**
 * Created by daria on 29.10.14.
 */
public class WebViewActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String url = getIntent().getStringExtra(ReaderActivity.URL_INTENT);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.webview);
        WebView webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);
    }
}
