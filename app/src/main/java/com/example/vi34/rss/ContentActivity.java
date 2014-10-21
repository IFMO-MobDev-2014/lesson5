package com.example.vi34.rss;

import android.app.Activity;
import android.os.Bundle;
import android.util.Xml;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.TextView;


public class ContentActivity extends Activity {

    WebView webView;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        webView = (WebView) findViewById(R.id.webView);
        textView = (TextView) findViewById(R.id.textTitle);

        String summary = getIntent().getStringExtra("summary");
        textView.setText(getIntent().getStringExtra("title"));
        webView.loadData(summary, "text/html", null);

    }


}
