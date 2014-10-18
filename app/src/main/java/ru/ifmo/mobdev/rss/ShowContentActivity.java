package ru.ifmo.mobdev.rss;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

/**
 * @author sugakandrey
 */
public class ShowContentActivity extends Activity {
    private ProgressDialog progressDialog;
    private boolean timeout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        timeout = true;
        setContentView(R.layout.content_view);
        progressDialog = new ProgressDialog(this, ProgressDialog.THEME_HOLO_DARK);
        progressDialog.setMessage("Loading...");
        WebView view = (WebView) findViewById(R.id.content_view);
        String url = getIntent().getStringExtra(LoaderActivity.CONTENT_URL);

        view.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(ShowContentActivity.this, description, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                progressDialog.show();
                Runnable r = new Runnable() {
                    @Override
                    public void run() {
                        if (timeout){
                            showErrorToast();
                            progressDialog.dismiss();
                            finish();
                        }
                    }
                };
                new Handler().postDelayed(r, 10000);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                timeout = false;
                progressDialog.dismiss();
            }
        });
        view.loadUrl(url);
    }

    private void showErrorToast() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(ShowContentActivity.this, "Error loading webpage. Please check your internet connection.", Toast.LENGTH_LONG).show();
            }
        });
    }
}
