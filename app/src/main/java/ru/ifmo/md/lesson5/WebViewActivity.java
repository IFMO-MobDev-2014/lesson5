package ru.ifmo.md.lesson5;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

/**
 * Created by Svet on 21.10.2014.
 */
public class WebViewActivity extends Activity {
    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        WebView webView = new WebView(this);
        setContentView(webView);
        Intent intent = getIntent();
        webView.loadUrl(intent.getStringExtra(MyActivity.key));
    }
}
