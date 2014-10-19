package ru.ifmo.ctd.fotjev.rss_reader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class ContentView extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_content_view);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        String link = extras.getString("link");

        WebView wv = (WebView) findViewById(R.id.webView);
        wv.setWebViewClient(new WebViewClient()); // without this line webview will launch browser
        wv.loadUrl(link);


    }

}
