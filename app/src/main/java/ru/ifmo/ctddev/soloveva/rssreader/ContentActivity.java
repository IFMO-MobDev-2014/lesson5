package ru.ifmo.ctddev.soloveva.rssreader;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;


public class ContentActivity extends Activity {
    public static final String HTML_KEY = "html";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String html = getIntent().getStringExtra(HTML_KEY);
        WebView view = new WebView(this);
        view.loadData(html, "text/html", "UTF-8");
        setContentView(view);
    }
}
