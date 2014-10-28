package year2013.ifmo.rssreader;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;


public class ViewActivity extends Activity {

    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.postview);
        Bundle bundle = this.getIntent().getExtras();
        String postContent = bundle.getString("content");

        webView = (WebView)this.findViewById(R.id.webView);
        webView.loadData(postContent, "text/html; charset=utf-8","utf-8");
    }
}
