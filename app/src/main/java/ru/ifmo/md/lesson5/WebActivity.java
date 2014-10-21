package ru.ifmo.md.lesson5;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        String url = getIntent().getStringExtra(getString(R.string.URL));
        WebView wv = (WebView) findViewById(R.id.webView);

        wv.setWebViewClient(new WebViewClient());
        wv.loadUrl(url);
    }

}
