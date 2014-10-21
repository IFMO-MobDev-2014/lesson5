package com.example.vanyadx.rss1;

/**
 * Created by vanyadx on 20.10.14.
 */
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebActivity extends Activity {
    WebView myWeb ;
    String address;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        myWeb = (WebView)findViewById(R.id.webview);
        address = getIntent().getExtras().getString("newAddress");
        MyAsyncTask my = new MyAsyncTask();
        my.execute();



    }
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;

    }
    public class MyAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        public Void doInBackground(Void... params) {
            myWeb.setWebViewClient(new WebViewClient());
            myWeb.getSettings().setJavaScriptEnabled(true);
            myWeb.loadUrl(address);

            return null;
        }
    }

}
