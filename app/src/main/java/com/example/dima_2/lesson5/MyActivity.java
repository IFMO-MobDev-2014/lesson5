package com.example.dima_2.lesson5;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

/**
 * Created by Dima_2 on 16.01.2015.
 */
public class MyActivity extends ListActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int DELETE_ID = Menu.FIRST + 1;

    private SimpleCursorAdapter adapter = null;

    private static final String FIRST_LAUNCH_STRING = "firstLaunch";

    private static final String[] channels = {
        "http://echo.msk.ru/interview/rss-fulltext.xml",
        "http://news.yahoo.com/rss/",
        "http://feeds.bbci.co.uk/news/rss.xml",
        "http://bash.im/rss/"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        getListView().setDividerHeight(2);
        registerForContextMenu(getListView());
        String[] f = new String[]{Table.COLUMN_NAME, Table.COLUMN_LINK};
        int[] t = new int[]{android.R.id.text1, android.R.id.text2};
        adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, null, f, t, 0);
        setListAdapter(adapter);
        getLoaderManager().initLoader(0, null, this);
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        boolean firstLaunchString = preferences.getBoolean(FIRST_LAUNCH_STRING, true);
        if (firstLaunchString && checkConnection()) {
            for (String channel : channels) {
                if (checkConnection()) {
                    checkConnectionAndLoadChannel(channel);
                }
           }
       }
       SharedPreferences.Editor editor = preferences.edit();
       editor.putBoolean(FIRST_LAUNCH_STRING, false);
       editor.apply();
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case DELETE_ID:
                AdapterView.AdapterContextMenuInfo information = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                getContentResolver().delete(Uri.parse(DatabaseContentProvider.CONTENT_URI_CHANNELS + "/" + information.id), null, null);
                getContentResolver().delete(DatabaseContentProvider.CONTENT_URI_ITEMS, ItemsTable.COLUMN_CHANNEL_ID + "=" + information.id, null);
                fillData();
                return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, DELETE_ID, 0, R.string.menu_delete);
    }

    private void addNewChannel() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.add_channel_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(view);
        final EditText userInput = (EditText) view.findViewById(R.id.editTextDialogUserInput);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int id) {
                checkConnectionAndLoadChannel(userInput.getText().toString());
            }
        });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private boolean checkConnection() {
        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    private void checkConnectionAndLoadChannel(String url) {
        if (checkConnection()) {
            Intent myIntent = new Intent(this, RSSLoader.class);
            myIntent.putExtra("url", url);
            startService(myIntent);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Error").setMessage(R.string.no_connection);
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }

    private void fillData() {
        getLoaderManager().initLoader(0, null, this);
    }

    private static final String[] trash = { Table.COLUMN_ID, Table.COLUMN_NAME, Table.COLUMN_LINK };

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this, DatabaseContentProvider.CONTENT_URI_CHANNELS, trash, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        adapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        adapter.swapCursor(null);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Uri channel = Uri.withAppendedPath(DatabaseContentProvider.CONTENT_URI_CHANNELS, String.valueOf(id));
        Cursor cursor = getContentResolver().query(channel, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int channelID = cursor.getInt(cursor.getColumnIndexOrThrow(Table.COLUMN_ID));
            Intent intent = new Intent(this, ItemsActivity.class);
            intent.putExtra("channel_id", channelID);
            startActivity(intent);
            cursor.close();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.listmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.insert:
                addNewChannel();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}