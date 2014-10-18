package com.pinguinson.reader.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebView;

import com.pinguinson.reader.R;

/**
 * Created by pinguinson on 18.10.2014.
 */

public class WebViewActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String url = getIntent().getStringExtra(ReaderActivity.URL_INTENT);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.web_view);
        WebView webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);
    }
}
