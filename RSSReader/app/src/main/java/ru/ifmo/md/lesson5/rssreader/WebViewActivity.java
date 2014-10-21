package ru.ifmo.md.lesson5.rssreader;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;

import ru.ifmo.md.lesson5.rssreader.utils.ItemLoader;
import ru.ifmo.md.lesson5.rssreader.utils.RSSItem;


public class WebViewActivity extends Activity implements LoaderManager.LoaderCallbacks<RSSItem> {
    private static final String EXTRA_ITEM_ID = "ITEM_ID";
    private static final String EXTRA_ITEM_TITLE = "ITEM_TITLE";
    private static final int LOADER_ITEM = 1;

    private WebView mWebView;
    private RSSManager mRSSManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        Intent intent = getIntent();
        long itemId = intent.getLongExtra(EXTRA_ITEM_ID, -1);
        String title = intent.getStringExtra(EXTRA_ITEM_TITLE);
        if (title != null) {
            setTitle(title);
        }
        mRSSManager = RSSManager.get(getApplicationContext());
        mWebView = (WebView) findViewById(R.id.webView);

        Bundle args = new Bundle();
        args.putLong(EXTRA_ITEM_ID, itemId);
        getLoaderManager().initLoader(LOADER_ITEM, args, this);

    }


    @Override
    public Loader<RSSItem> onCreateLoader(int i, Bundle bundle) {
        return new ItemLoader(this, bundle, mRSSManager);
    }

    @Override
    public void onLoadFinished(Loader<RSSItem> rssItemLoader, RSSItem rssItem) {
        String url = rssItem.getUrl();
        mWebView.loadUrl(url);
    }

    @Override
    public void onLoaderReset(Loader<RSSItem> rssItemLoader) {

    }
}
