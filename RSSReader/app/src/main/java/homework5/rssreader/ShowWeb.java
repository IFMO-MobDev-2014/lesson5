package homework5.rssreader;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

/**
 * Created by Anstanasia on 21.10.2014.
 */
public class ShowWeb extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("ShowWeb", "123");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.rss_item_displayer);
        WebView webView = (WebView)findViewById(R.id.webView);
        String url = getIntent().getStringExtra("url");
        webView.loadUrl(url);
    }
}
