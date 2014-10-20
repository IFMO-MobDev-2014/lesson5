package easyrssreader.sergeybudkov.ru.easyrssreader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

public class SingleArticle extends Activity {
    WebView webView;
    String link,content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.singlearticle_view);
        webView = (WebView) findViewById(R.id.webView);
        Intent intent = getIntent();
        link = intent.getStringExtra("URL");
        content = intent.getStringExtra("Content");
        if (content == null)
            webView.loadUrl(link);
        else
            webView.loadData(content, "text/html; charset=UTF-8", null);
    }
}
