package ru.android.german.lesson5;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

/**
 * Created by german on 21.10.14.
 */
public class WebActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String link = intent.getStringExtra("link");
        setContentView(R.layout.web_layout);
        WebView webView = (WebView)findViewById(R.id.webView);
        webView.loadUrl(link);
        webView.getSettings().setJavaScriptEnabled(true);
    }
}
