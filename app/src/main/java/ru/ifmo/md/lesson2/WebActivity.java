package ru.ifmo.md.lesson2;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;


public class WebActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        String s = getIntent().getStringExtra("url");
        WebView wv = (WebView) findViewById(R.id.web);
        wv.loadUrl(s);
    }


}
