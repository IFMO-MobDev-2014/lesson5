package com.example.kirill.rssreader;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

/**
 * Created by Kirill on 21.10.2014.
 */
public class ShowArticle extends Activity {

    String link = "";
    WebView webView = null;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Bundle extras = getIntent().getExtras();
        link = extras.getString("link");
        setContentView(R.layout.activity_show_article);
        webView = (WebView) this.findViewById(R.id.webview);
        webView.loadUrl(link);
    }

}
