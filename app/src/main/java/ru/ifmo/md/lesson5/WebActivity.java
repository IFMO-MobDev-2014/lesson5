package ru.ifmo.md.lesson5;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by Snopi on 20.10.2014.
 */
public class WebActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WebView web = new WebView(this);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(web);
        web.setWebViewClient(new WebViewClient());
        String url = getIntent().getExtras().getString("URL");
        web.loadUrl(url);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
