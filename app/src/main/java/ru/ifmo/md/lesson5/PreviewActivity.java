package ru.ifmo.md.lesson5;

/**
 * Created by lightning95 on 10/21/14.
 */

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class PreviewActivity extends Activity {
    public static final String URL = "URL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);

        String url = getIntent().getStringExtra(URL);
        WebView webView = (WebView) findViewById(R.id.webView);
        webView.loadUrl(url);
    }
}
