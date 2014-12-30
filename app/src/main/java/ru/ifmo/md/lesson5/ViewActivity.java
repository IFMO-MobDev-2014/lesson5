package ru.ifmo.md.lesson5;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;


public class ViewActivity extends Activity {

    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.postview);
        Bundle bundle = this.getIntent().getExtras();
        String postLink = bundle.getString("url");

        webView = (WebView)this.findViewById(R.id.webView);
        webView.loadUrl(postLink);
    }
}
