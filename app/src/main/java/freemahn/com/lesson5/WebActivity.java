package freemahn.com.lesson5;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

/**
 * Created by Freemahn on 21.10.2014.
 */
public class WebActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_web);
        WebView wv = (WebView) findViewById(R.id.web_view);
        wv.loadUrl(getIntent().getStringExtra("link"));

    }
}
