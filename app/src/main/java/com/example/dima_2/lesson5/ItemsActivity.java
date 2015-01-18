package com.example.dima_2.lesson5;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

/**
 * Created by Dima_2 on 16.01.2015.
 */
public class ItemsActivity extends ListActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private SimpleCursorAdapter adapter = null;
    private int channelID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        channelID = getIntent().getIntExtra("channel_id", 0);
        String[] f = new String[]{ItemsTable.COLUMN_TITLE, ItemsTable.COLUMN_DESCRIPTION};
        int[] t = new int[] {android.R.id.text1, android.R.id.text2};
        adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, null, f, t, 0);
        setListAdapter(adapter);
        getLoaderManager().initLoader(0, null, this);
    }

    private void fillData() {
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    protected void onListItemClick(ListView listView, View view, int i, long id) {
        super.onListItemClick(listView, view, i, id);
        Uri elementsUri = Uri.withAppendedPath(DatabaseContentProvider.CONTENT_URI_ITEMS, String.valueOf(id));
        Cursor cursor = getContentResolver().query(elementsUri, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            String link = cursor.getString(cursor.getColumnIndexOrThrow(ItemsTable.COLUMN_LINK));
            Intent intent = new Intent(this, ShowActivity.class);
            intent.putExtra(ShowActivity.EXTRA_KEY_URL, link);
            cursor.close();
            startActivity(intent);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] trash = { ItemsTable.COLUMN_ID, ItemsTable.COLUMN_TITLE,
                                ItemsTable.COLUMN_DESCRIPTION,
                                ItemsTable.COLUMN_PUB_DATE, ItemsTable.COLUMN_LINK,
                                ItemsTable.COLUMN_CHANNEL_ID
                                };
        return new CursorLoader(this, DatabaseContentProvider.CONTENT_URI_ITEMS, trash, ItemsTable.COLUMN_CHANNEL_ID + "=" + channelID, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        adapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        adapter.swapCursor(null);
    }
}
