package volhovm.com.rssreader;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class RSSMainActivity extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, LoaderManager.LoaderCallbacks<Feed> {

    public static final int FEED_UPDATE_LOADER = 0;
    public static final int FEED_DB_LOADER = 1;
    private static final String FEED_INDEX = "feed_index";

    private MenuItem refreshItem;
    private NavigationDrawerFragment navigationDrawerFragment;
    private CharSequence title;
    private FeedDAO dataSource;
    private int currentFeed = 0;

    ArrayList<Feed> feeds = new ArrayList<Feed>();
    PostAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dataSource = new FeedDAO(this);
        dataSource.open();
        updateNavBarList();
        if (feeds.isEmpty()) {
            addFeed(new Feed("Bash", "http://bash.im/rss/"));
            addFeed(new Feed("Liga", "http://news.liga.net/news/rss.xml"));
        }

        adapter = new PostAdapter(this, feeds.get(0));
        title = getTitle();
        setContentView(R.layout.activity_rssmain);
        navigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        navigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    private void updateNavBarList() {
        feeds.clear();
        feeds.addAll(dataSource.getEmptyFeeds());
        Collections.sort(feeds, new Comparator<Feed>() {
                    @Override
                    public int compare(Feed feed, Feed feed2) {
                        return feed.feedName.compareTo(feed2.feedName);
                    }
                }
        );
        if (navigationDrawerFragment != null) navigationDrawerFragment.initNavigationBarList();
    }
    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        while (feeds.isEmpty()) {}
            Bundle bundle = new Bundle();
            bundle.putInt(FEED_INDEX, position);
        if (feeds.get(position).size() == 0) {
            getLoaderManager().restartLoader(FEED_DB_LOADER, bundle, this).forceLoad();
        }
        if (feeds.get(position).size() == 0) {
            getLoaderManager().restartLoader(FEED_UPDATE_LOADER, bundle, this).forceLoad();
        }
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position))
                .commit();
        currentFeed = position;
    }

    public void onSectionAttached(int number) {
        if (number >= feeds.size()) title = "";
        else title = feeds.get(number).feedName;
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(title);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!navigationDrawerFragment.isDrawerOpen()) {
            getMenuInflater().inflate(R.menu.rssmain, menu);
            refreshItem = menu.findItem(R.id.refresh_content);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.refresh_content: {
                refreshItem.setActionView(R.layout.actionbar_spinner);
                Toast.makeText(this, "Refreshing", Toast.LENGTH_SHORT).show();
                Bundle bundle = new Bundle();
                bundle.putInt(FEED_INDEX, currentFeed);
                getLoaderManager().restartLoader(FEED_UPDATE_LOADER, bundle, this).forceLoad();
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    public void addFeed(Feed feed) {
        feeds.add(feed);
        Bundle bundle = new Bundle();
        bundle.putInt(FEED_INDEX, feeds.size() - 1);
        getLoaderManager().restartLoader(FEED_UPDATE_LOADER, bundle, this).forceLoad();
    }

    @Override
    public Loader<Feed> onCreateLoader(int i, Bundle bundle) {
        switch (i) {
            case FEED_UPDATE_LOADER: {
                return new RSSLoader(this, feeds.get(bundle.getInt(FEED_INDEX)), dataSource);
            }
            case FEED_DB_LOADER: {
                return new FeedUpdateLoader(this, feeds.get(bundle.getInt(FEED_INDEX)), dataSource);
            }
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Feed> feedLoader, Feed feed) {
        if (refreshItem != null) refreshItem.setActionView(null);
        final Feed feed2 = feed;
        if (adapter != null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.replaceFeed(feed2);
                    adapter.notifyDataSetChanged();
                    ((PlaceholderFragment) getFragmentManager().findFragmentById(R.id.container)).listView.requestLayout();
                }
            });
        }
        updateNavBarList();
    }

    @Override
    public void onLoaderReset(Loader<Feed> feedLoader) {
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                adapter.replaceFeed(null);
//                adapter.notifyDataSetChanged();
//            }
//        });
    }
}
