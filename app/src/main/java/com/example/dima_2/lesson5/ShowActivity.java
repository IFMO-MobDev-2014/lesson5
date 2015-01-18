package com.example.dima_2.lesson5;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by Dima_2 on 16.01.2015.
 */
public class ShowActivity extends Activity {

    public static final String EXTRA_KEY_URL = "URL";

    private WebView webView;
    private ProgressDialog progressDialog;
    private boolean timeout = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        progressDialog = new ProgressDialog(this, AlertDialog.THEME_HOLO_DARK);
        progressDialog.setMessage("Loading");
        String url = getIntent().getStringExtra(EXTRA_KEY_URL);
        webView = (WebView)findViewById(R.id.content_view);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                progressDialog.show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (timeout) {
                            progressDialog.dismiss();
                            showError();
                            finish();
                        }
                    }
                }, 10000);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                timeout = false;
                progressDialog.dismiss();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        webView.loadUrl(url);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView != null && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void showError() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error").setMessage(R.string.error_loading_webpage);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
