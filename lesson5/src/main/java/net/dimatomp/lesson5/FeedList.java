package net.dimatomp.lesson5;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import static net.dimatomp.lesson5.FeedColumns.FEED_DESCRIPTION;
import static net.dimatomp.lesson5.FeedColumns.FEED_TITLE;
import static net.dimatomp.lesson5.FeedColumns._ID;

public class FeedList extends ListActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final SimpleCursorAdapter.ViewBinder binder = new SimpleCursorAdapter.ViewBinder() {
        @Override
        public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
            String text = cursor.getString(columnIndex);
            if (view.getId() == android.R.id.text2 && (text == null || text.isEmpty()))
                view.setVisibility(View.GONE);
            ((TextView) view).setText(text);
            return true;
        }
    };
    boolean resumedAfterPause = false;

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                this, Uri.parse("content://net.dimatomp.feeds.provider/feeds"),
                null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        ((CursorAdapter) getListAdapter()).swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        ((CursorAdapter) getListAdapter()).swapCursor(null);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Cursor feed = (Cursor) getListAdapter().getItem(position);
        int feedId = feed.getInt(feed.getColumnIndex(_ID));
        Intent intent = new Intent(this, FeedEntries.class);
        intent.putExtra(FeedEntries.EXTRA_FEED_ID, feedId);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_list);

        SimpleCursorAdapter listAdapter = new SimpleCursorAdapter(this, R.layout.feed_item, null,
                new String[]{FEED_TITLE, FEED_DESCRIPTION},
                new int[]{android.R.id.text1, android.R.id.text2}, 0);
        listAdapter.setViewBinder(binder);
        setListAdapter(listAdapter);
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    protected void onPause() {
        resumedAfterPause = true;
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (resumedAfterPause)
            getLoaderManager().restartLoader(0, null, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_feed_list, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            try {
                Uri refUri = data.getData();
                Intent showEntries = new Intent(this, FeedEntries.class);
                showEntries.putExtra(FeedEntries.EXTRA_FEED_ID, Integer.parseInt(refUri.getQueryParameter("feedId")));
                startActivity(showEntries);
            } catch (NumberFormatException | NullPointerException e) {
                Toast.makeText(this, R.string.invalid_feed_uri, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_new_feed:
                startActivityForResult(new Intent(this, NewFeedForm.class), 1);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
