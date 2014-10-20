package ru.ifmo.md.lesson5;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;


public class SecondActivity extends Activity {

    public WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String string = getIntent().getStringExtra("text");
        setContentView(R.layout.layout2);
        webView = (WebView) findViewById(R.id.webView);
        webView.loadUrl(string);
    }
}