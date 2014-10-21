package com.example.alexey.lesson5;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;


public class MyActivity3 extends Activity {


    int g;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_activity3);
        WebView webView=(WebView) findViewById(R.id.webView);
        g=getIntent().getExtras().getInt("count");
        webView.loadUrl(MyActivity.c.get(g));

    }
}
