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
import android.view.Window;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import static net.dimatomp.lesson5.FeedColumns.ENTRY_DATE;
import static net.dimatomp.lesson5.FeedColumns.ENTRY_DESCRIPTION;
import static net.dimatomp.lesson5.FeedColumns.ENTRY_TITLE;
import static net.dimatomp.lesson5.FeedColumns.ENTRY_URL;

public class FeedEntries extends ListActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    public static final String EXTRA_FEED_ID = "net.dimatomp.lesson5.FeedEntries.EXTRA_FEED_ID";
    static SimpleCursorAdapter.ViewBinder binder = new SimpleCursorAdapter.ViewBinder() {
        @Override
        public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
            if (cursor.getColumnName(columnIndex).equals(ENTRY_DATE))
                ((TextView) view).setText(DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.DEFAULT, Locale.getDefault())
                        .format(new Date(cursor.getLong(columnIndex))));
            else
                ((TextView) view).setText(cursor.getString(columnIndex));
            return true;
        }
    };
    boolean refreshButtonShown = true;
    Menu menu;

    void setRefreshButtonShown(boolean shown) {
        refreshButtonShown = shown;
        if (menu != null)
            menu.findItem(R.id.action_refresh).setVisible(shown);
        setProgressBarIndeterminateVisibility(!shown);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case 0:
                Uri uri = Uri.parse("content://net.dimatomp.feeds.provider/entries?feedId=" + args.getInt(EXTRA_FEED_ID));
                return new CursorLoader(this, uri, null, null, null, ENTRY_DATE + " DESC");
            case 1:
                setRefreshButtonShown(false);
                return new FeedLoaderWithUpdate(this, args.getInt(EXTRA_FEED_ID), null, null, null, ENTRY_DATE + " DESC");
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        ((CursorAdapter) getListAdapter()).swapCursor(data);
        if (loader.getId() == 0)
            getLoaderManager().initLoader(1, getIntent().getExtras(), this);
        else {
            setProgressBarIndeterminateVisibility(false);
            setRefreshButtonShown(true);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        ((CursorAdapter) getListAdapter()).swapCursor(null);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Cursor entry = (Cursor) getListAdapter().getItem(position);
        String address = entry.getString(entry.getColumnIndex(ENTRY_URL));
        if (address != null && !address.isEmpty()) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(address));
            startActivity(intent);
        }
    }

    public void expandCollapse(View view) {
        View parent = (View) view.getParent();
        View content = parent.findViewById(R.id.short_description);
        content.setVisibility(content.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
        parent.callOnClick();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_feed_entries);

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.feed_entry, null,
                new String[]{ENTRY_TITLE, ENTRY_DATE, ENTRY_DESCRIPTION},
                new int[]{android.R.id.text1, android.R.id.text2, R.id.short_description}, 0);
        adapter.setViewBinder(binder);
        setListAdapter(adapter);
        getLoaderManager().initLoader(0, getIntent().getExtras(), this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_feed_entries, menu);
        this.menu = menu;
        setRefreshButtonShown(refreshButtonShown);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                getLoaderManager().restartLoader(1, getIntent().getExtras(), this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
