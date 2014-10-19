package ru.ifmo.md.lesson5.rssreader;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity
        implements LoaderManager.LoaderCallbacks<Cursor> {
    private EditText mEditTextAdd;
    private ListView mListViewRss;
    private SimpleCursorAdapter mAdapter;

    private static final int LOADER_RSS = 1;
    private static final int CM_ITEM_DELETE = 0;

    private RssManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        manager = RssManager.get(this);

        setupViews();
    }

    private void setupViews() {
        mEditTextAdd = (EditText) findViewById(R.id.editText_addUrl);
        mListViewRss = (ListView) findViewById(R.id.listView_rss);

        mEditTextAdd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {}

            @Override
            public void afterTextChanged(Editable editable) {
                String potentialUrl = mEditTextAdd.getText().toString();
                if (Patterns.WEB_URL.matcher(potentialUrl).matches()) {
                    mEditTextAdd.setBackgroundColor(Color.argb(64, 0x5C, 0x85, 0x5C));
                } else {
                    mEditTextAdd.setBackgroundColor(Color.argb(64, 0xD9, 0x53, 0x4F));
                }
            }
        });

        mEditTextAdd.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == getResources().getInteger(R.integer.actionAdd)) {
                    String potentialUrl = mEditTextAdd.getText().toString();
                    if (Patterns.WEB_URL.matcher(potentialUrl).matches()) {
                        addRss(potentialUrl);
                    } else {
                        Toast.makeText(getApplicationContext(), getString(R.string.bad_url), Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
                return false;
            }
        });

        mAdapter = new SimpleCursorAdapter(
                this,
                android.R.layout.simple_list_item_1,
                null,
                new String[]{RssDatabaseHelper.COLUMN_RSS_URL},
                new int[]{android.R.id.text1},
                0
        );

        mListViewRss.setAdapter(mAdapter);
        registerForContextMenu(mListViewRss);

        getLoaderManager().initLoader(LOADER_RSS, null, this);

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.rss_context, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo acmi =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        long id = acmi.id;
        Log.d("TAG", "id: " + id);
        switch (item.getItemId()) {
            case R.id.delete:
                manager.deleteRss(id);
                getLoaderManager().getLoader(LOADER_RSS).forceLoad();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void addRss(String url) {
        Log.d("TAG", "Add url: " + url);
        Rss rss = new Rss();
        rss.setUrl(url);
        rss.setName(url);
        rss.setFavourite(0);
        long id = manager.insertRss(rss);
        getLoaderManager().getLoader(LOADER_RSS).forceLoad();
        Log.d("TAG", "Rss id: " + id);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new RssLoader(this, manager);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        try {
            mAdapter.swapCursor(cursor);
        } catch (Exception e) {
            Log.d("TAG", e.getMessage());
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private static class RssLoader extends CursorLoader {

        private RssManager manager;

        public RssLoader(Context context, RssManager manager) {
            super(context);
            this.manager = manager;
        }

        @Override
        public Cursor loadInBackground() {
            return manager.getAllRss();
        }
    }
}
