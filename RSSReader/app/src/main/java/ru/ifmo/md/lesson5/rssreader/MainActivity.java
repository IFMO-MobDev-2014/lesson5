package ru.ifmo.md.lesson5.rssreader;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.Intent;
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
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import ru.ifmo.md.lesson5.rssreader.utils.ChannelsLoader;
import ru.ifmo.md.lesson5.rssreader.utils.RSSChannel;
import ru.ifmo.md.lesson5.rssreader.utils.RSSItem;
import ru.ifmo.md.lesson5.rssreader.utils.RSSLoader;


public class MainActivity extends ListActivity {
    private static final int LOADER_RSS = 1;
    private static final int LOADER_CHANNELS = 2;
    private static final String EXTRA_RSS_ID = "RSS_ID";
    private static final String ARGS_URL = "URL";


    private EditText mEditTextAdd;
    private SimpleCursorAdapter mAdapter;
    private String mLastAddUrl = null;

    private RSSManager mManager;

    private final LoaderManager.LoaderCallbacks<Cursor> mChannelLoaderCallbacks =
            new LoaderManager.LoaderCallbacks<Cursor>() {
                @Override
                public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
                    return new ChannelsLoader(getApplicationContext(), mManager);
                }

                @Override
                public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
                    mAdapter.swapCursor(cursor);
                }

                @Override
                public void onLoaderReset(Loader<Cursor> cursorLoader) {

                }
            };
    private final LoaderManager.LoaderCallbacks<RSSChannel> mRSSLoaderCallbacks =
            new LoaderManager.LoaderCallbacks<RSSChannel>() {
                @Override
                public Loader<RSSChannel> onCreateLoader(int i, Bundle bundle) {
                    return new RSSLoader(getApplicationContext(), bundle);
                }

                @Override
                public void onLoadFinished(Loader<RSSChannel> rssChannelLoader, RSSChannel channel) {
                    addRss(channel);
                }

                @Override
                public void onLoaderReset(Loader<RSSChannel> rssChannelLoader) {

                }
            };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mManager = RSSManager.get(this);

        setupViews();
    }

    private void setupViews() {
        mEditTextAdd = (EditText) findViewById(R.id.editText_addUrl);

        mEditTextAdd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String potentialUrl = mEditTextAdd.getText().toString();
                if (Patterns.WEB_URL.matcher(potentialUrl).matches()) {
                    //TODO: constants for colors, make 9-patch
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
                        if (mLastAddUrl != null && potentialUrl.equals(mLastAddUrl)) {
                            return true;
                        }
                        mLastAddUrl = new String(potentialUrl);
                        Bundle args = new Bundle();
                        args.putString(ARGS_URL, potentialUrl);
                        getLoaderManager().initLoader(LOADER_CHANNELS, args, mRSSLoaderCallbacks).forceLoad();
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
                android.R.layout.simple_list_item_2,
                null,
                new String[]{
                        RSSDatabaseHelper.COLUMN_CHANNEL_TITLE,
                        RSSDatabaseHelper.COLUMN_CHANNEL_URL
                },
                new int[]{
                        android.R.id.text1,
                        android.R.id.text2
                },
                0
        );

        setListAdapter(mAdapter);
        registerForContextMenu(findViewById(android.R.id.list));

        getLoaderManager().initLoader(LOADER_RSS, null, mChannelLoaderCallbacks);

    }

    @Override
    protected void onListItemClick(ListView lv, View v, int position, long id) {
        Cursor cursor = (Cursor) mAdapter.getItem(position);
        long rssId = cursor.getLong(cursor.getColumnIndex("_id"));
        Intent intent = new Intent(this, RssActivity.class);
        intent.putExtra(EXTRA_RSS_ID, rssId);
        startActivity(intent);
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
        //TODO: use Channel.getId() instead of position
        long id = acmi.id;
        Log.d("TAG", "id: " + id);
        switch (item.getItemId()) {
            case R.id.delete:
                mManager.deleteChannel(id);
                getLoaderManager().getLoader(LOADER_RSS).forceLoad();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void addRss(RSSChannel channel) {
        if (channel != null) {
            long id = mManager.addChannel(channel);
            // TODO: move to RSSManager
            for (RSSItem item : channel.getRssItems()) {
                item.setChannelId(channel.getId());
            }
            getLoaderManager().getLoader(LOADER_RSS).forceLoad();
            mManager.addItems(channel.getRssItems());
        }
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

}
