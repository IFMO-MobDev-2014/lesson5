package com.example.home.lesson5;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

/**
 * Created by Home on 19.10.2014.
 */
public class WebActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_activity);

        String s = getIntent().getStringExtra("link");
        ((WebView) findViewById(R.id.webView)).loadUrl(s);
    }
}
