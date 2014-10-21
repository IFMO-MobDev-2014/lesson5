package ru.ifmo.md.lesson5;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;


public class Web extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webcontentfromlink);
        WebView w = (WebView) findViewById(R.id.webview);
        String s = getIntent().getStringExtra("text");
        w.getSettings().setJavaScriptEnabled(true);
        w.loadDataWithBaseURL(null, s, "text/html", "UTF-8", null);
    }
    public void onBackPressed() {
        Intent intent = new Intent(Web.this, MyActivity.class);
        startActivity(intent);
        finish();
    }
}