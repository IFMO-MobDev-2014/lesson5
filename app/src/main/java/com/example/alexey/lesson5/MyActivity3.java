package com.example.alexey.lesson5;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

import java.net.MalformedURLException;
import java.net.URL;


public class MyActivity3 extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_activity3);
        WebView webView=(WebView) findViewById(R.id.webView);
        String g=getIntent().getExtras().getString("count");
        webView.loadUrl(g);

    }
}
