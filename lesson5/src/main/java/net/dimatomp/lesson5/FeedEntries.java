package net.dimatomp.lesson5;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;

import static net.dimatomp.lesson5.FeedColumns.ENTRY_DATE;
import static net.dimatomp.lesson5.FeedColumns.ENTRY_DESCRIPTION;
import static net.dimatomp.lesson5.FeedColumns.ENTRY_TITLE;

public class FeedEntries extends ListActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    public static final String EXTRA_FEED_ID = "net.dimatomp.lesson5.FeedEntries.EXTRA_FEED_ID";

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case 0:
                Uri uri = Uri.parse("content://net.dimatomp.feeds.provider/entries?feedId=" + args.getInt(EXTRA_FEED_ID));
                return new CursorLoader(this, uri, null, null, null, ENTRY_DATE + " DESC");
            case 1:
                return new FeedLoaderWithUpdate(this, args.getInt(EXTRA_FEED_ID), null, null, null, ENTRY_DATE + " DESC");
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        ((CursorAdapter) getListAdapter()).swapCursor(data);
        if (loader.getId() == 0)
            getLoaderManager().initLoader(1, getIntent().getExtras(), this);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        ((CursorAdapter) getListAdapter()).swapCursor(null);
    }

    public void expand(View view) {
        View description = ((ViewGroup) view.getParent()).findViewById(R.id.detailed_info);
        switch (description.getVisibility()) {
            case View.VISIBLE:
                description.setVisibility(View.GONE);
                ((ExpandCollapseView) view).setExpanded(false);
                break;
            case View.GONE:
                description.setVisibility(View.VISIBLE);
                ((ExpandCollapseView) view).setExpanded(true);
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_entries);

        ListAdapter adapter = new SimpleCursorAdapter(this, R.layout.feed_entry, null,
                new String[]{ENTRY_TITLE, ENTRY_DATE, ENTRY_DESCRIPTION},
                new int[]{android.R.id.text1, android.R.id.text2, R.id.short_description}, 0);
        setListAdapter(adapter);
        getLoaderManager().initLoader(0, getIntent().getExtras(), this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_feed_entries, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
