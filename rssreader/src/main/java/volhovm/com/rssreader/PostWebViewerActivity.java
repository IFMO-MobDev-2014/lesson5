package volhovm.com.rssreader;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * @author volhovm
 *         Created on 11/6/14
 */

public class PostWebViewerActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WebView view = new WebView(this);
        view.setWebViewClient(new WebViewClient());
        setContentView(view);
        view.loadUrl(String.valueOf(getIntent().getData()));
    }
}
