package ru.ifmo.md.lesson5;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class Show extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result);

        WebView webView = (WebView) findViewById(R.id.show);
        webView.setBackgroundColor((int)(0x808080));
        String html = getIntent().getStringExtra("link");
        webView.loadDataWithBaseURL(null, html ,"text/html", "UTF-8", null);

    }
}