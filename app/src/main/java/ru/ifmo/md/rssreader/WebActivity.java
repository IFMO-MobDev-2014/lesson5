package ru.ifmo.md.rssreader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;


public class WebActivity extends Activity {
    private WebView webView;
    private TextView titleText;
    private TextView dateText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        webView = (WebView) findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return false;
            }
        });
        titleText = (TextView) findViewById(R.id.title);
        dateText = (TextView) findViewById(R.id.time);
        Intent intent = getIntent();
        titleText.setText(intent.getStringExtra("title"));
        dateText.setText(intent.getStringExtra("pubDate"));
        webView.loadUrl(intent.getStringExtra("link"));
    }
}
