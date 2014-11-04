package ru.eugene.listviewpractice2;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;


public class ShowFeed extends Activity {
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_feed);

        webView = (WebView) findViewById(R.id.showFeed); //This is the id you gave
        String url = getIntent().getStringExtra("url");
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setSupportZoom(true);
        webView.loadUrl(url);

    }
}
