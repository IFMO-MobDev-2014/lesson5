package net.dimatomp.lesson5;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import static net.dimatomp.lesson5.FeedColumns.ENTRY_DATE;
import static net.dimatomp.lesson5.FeedColumns.ENTRY_DESCRIPTION;
import static net.dimatomp.lesson5.FeedColumns.ENTRY_TITLE;
import static net.dimatomp.lesson5.FeedColumns.ENTRY_URL;

public class FeedEntries extends ListActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    public static final String EXTRA_FEED_ID = "net.dimatomp.lesson5.FeedEntries.EXTRA_FEED_ID";
    private static final String COLLAPSED_ENTRIES = "collapsedEntries";
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
    RefreshButtonToggler refreshButtonToggler = new RefreshButtonToggler();
    boolean expandingCollapsing = false;
    Set<Long> collapsedEntries = new HashSet<>();

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case 0:
                Uri uri = Uri.parse("content://net.dimatomp.feeds.provider/entries?feedId=" + args.getInt(EXTRA_FEED_ID));
                return new CursorLoader(this, uri, null, null, null, ENTRY_DATE + " DESC");
            case 1:
                setProgressBarIndeterminateVisibility(true);
                refreshButtonToggler.setVisible(false);
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
            collapsedEntries.clear();
            setProgressBarIndeterminateVisibility(false);
            refreshButtonToggler.setVisible(true);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        ((CursorAdapter) getListAdapter()).swapCursor(null);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(COLLAPSED_ENTRIES, collapsedEntries.toArray());
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        if (expandingCollapsing) {
            int visibility = v.findViewById(R.id.short_description).getVisibility();
            if (visibility == View.GONE)
                collapsedEntries.add(id);
            else
                collapsedEntries.remove(id);
            expandingCollapsing = false;
            return;
        }
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

        SimpleCursorAdapter adapter = new ExpandableItemCursorAdapter(this, R.layout.feed_entry, null,
                new String[]{ENTRY_TITLE, ENTRY_DATE, ENTRY_DESCRIPTION},
                new int[]{android.R.id.text1, android.R.id.text2, R.id.short_description}, 0);
        adapter.setViewBinder(binder);
        setListAdapter(adapter);
        getLoaderManager().initLoader(0, getIntent().getExtras(), this);

        if (savedInstanceState != null && savedInstanceState.containsKey(COLLAPSED_ENTRIES))
            Collections.addAll(collapsedEntries, ((Long[]) savedInstanceState.getSerializable(COLLAPSED_ENTRIES)));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_feed_entries, menu);
        refreshButtonToggler.setRefreshButton(menu.findItem(R.id.action_refresh));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                getLoaderManager().restartLoader(1, getIntent().getExtras(), this);
                return true;
            case R.id.action_expand_all:
                collapsedEntries.clear();
                ((BaseAdapter) getListAdapter()).notifyDataSetChanged();
                return true;
            case R.id.action_collapse_all:
                BaseAdapter adapter = (BaseAdapter) getListAdapter();
                for (int i = 0; i < adapter.getCount(); i++)
                    collapsedEntries.add(adapter.getItemId(i));
                adapter.notifyDataSetChanged();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private static class RefreshButtonToggler implements Runnable {
        final Thread runner;
        MenuItem refreshButton;
        boolean visible = true;

        {
            runner = new Thread(this);
            runner.start();
        }

        public void setVisible(boolean visible) {
            if (refreshButton == null)
                this.visible = visible;
            else
                refreshButton.setVisible(visible);
        }

        public synchronized void setRefreshButton(MenuItem refreshButton) {
            this.refreshButton = refreshButton;
            notify();
        }

        @Override
        public synchronized void run() {
            while (refreshButton == null)
                try {
                    wait();
                } catch (InterruptedException ignore) {
                }
            refreshButton.setVisible(visible);
        }
    }

    private class ExpandableItemCursorAdapter extends SimpleCursorAdapter {
        private ExpandableItemCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
            super(context, layout, c, from, to, flags);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View result = super.getView(position, convertView, parent);
            boolean collapsed = collapsedEntries.contains(getItemId(position));
            result.findViewById(R.id.short_description).setVisibility(collapsed ? View.GONE : View.VISIBLE);
            return result;
        }
    }
}
