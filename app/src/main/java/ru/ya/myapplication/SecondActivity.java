package ru.ya.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by vanya on 21.10.14.
 */
public class SecondActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.html_view);
        //URL url = new URL(getIntent().getStringExtra("url"));
        WebView webView = (WebView)findViewById(R.id.webview);
        String s = getIntent().getStringExtra("url");
        Log.e("abacaba", s);
        //if (webView == null)
        webView.loadUrl(s);
        //R.layout
        //R.id.
    }
}
