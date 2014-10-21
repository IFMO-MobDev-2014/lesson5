package ru.ifmo.ctddev.soloveva.rssreader;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


public class ContentActivity extends Activity {
    public static final String HTML_KEY = "html";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String html = getIntent().getStringExtra(HTML_KEY);
        WebView view = new WebView(this);
        try {
            view.loadData(URLEncoder.encode(html, "UTF-8").replaceAll("\\+", "%20"), "text/html", "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.e("RSS", "something went wrong", e);
        }
        setContentView(view);
    }
}
