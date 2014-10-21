package ru.ifmo.md.lesson5.rssreader;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import ru.ifmo.md.lesson5.rssreader.utils.ItemsLoader;
import ru.ifmo.md.lesson5.rssreader.utils.RSSChannel;


public class RssActivity extends ListActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String EXTRA_RSS_ID = "RSS_ID";

    private static final String EXTRA_ITEM_TITLE = "ITEM_TITLE";
    private static final String EXTRA_ITEM_ID = "ITEM_ID";
    private static final int LOADER_ITEMS = 1;

    private RSSManager mRssManager;
    private long mRssId;
    private SimpleCursorAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rss);

        mRssManager = RSSManager.get(this);
        mRssId = getIntent().getLongExtra(EXTRA_RSS_ID, -1);
        Log.d("TAG", "channel id: " + mRssId);
        RSSChannel channel = mRssManager.getChannel(mRssId);

        if (channel != null && channel.getTitle() != null) {
            setTitle(channel.getTitle());
        }

        setupViews();
    }

    @Override
    protected void onListItemClick(ListView lv, View v, int position, long id) {
        Cursor cursor = (Cursor) mAdapter.getItem(position);
        long itemId = cursor.getLong(cursor.getColumnIndex("_id"));
        String title = cursor.getString(cursor.getColumnIndex("title"));
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra(EXTRA_ITEM_ID, itemId);
        intent.putExtra(EXTRA_ITEM_TITLE, title);
        startActivity(intent);
    }

    private void setupViews() {
        mAdapter = new SimpleCursorAdapter(
                this,
                android.R.layout.simple_list_item_2,
                null,
                new String[]{
                        RSSDatabaseHelper.COLUMN_ITEM_TITLE,
                        RSSDatabaseHelper.COLUMN_ITEM_DESCRIPTION
                },
                new int[]{
                        android.R.id.text1,
                        android.R.id.text2
                },
                0
        );

        setListAdapter(null);
        Bundle args = new Bundle();
        args.putLong(EXTRA_RSS_ID, mRssId);
        getLoaderManager().initLoader(LOADER_ITEMS, args, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new ItemsLoader(this, mRssManager, bundle);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        Log.d("TAG", "" + cursor.getCount());
        if (getListAdapter() == null) {
            setListAdapter(mAdapter);
        }
        mAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }
}
