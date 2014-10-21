package retloko.org.rssreader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

public class ArticleActivity extends Activity {
    private Intent starter;
    private WebView articleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        articleView = new WebView(this);
        setContentView(articleView);

        starter = getIntent();
        String articleTitle = starter.getStringExtra(EntryAdapter.ARTICLE_TITLE);
        String articleUrl = starter.getStringExtra(EntryAdapter.ARTICLE_URL);

        setTitle(articleTitle);

        articleView.loadUrl(articleUrl);
    }
}
