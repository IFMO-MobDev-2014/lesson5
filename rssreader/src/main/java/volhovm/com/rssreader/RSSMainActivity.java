package volhovm.com.rssreader;

import android.app.*;
import android.content.DialogInterface;
import android.content.Loader;
import android.os.AsyncTask;
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
        feeds = dataSource.getEmptyFeeds();
        if (feeds.isEmpty()) {
            feeds.add(new Feed("Bash", "http://bash.im/rss/"));
            feeds.add(new Feed("MyAnimeList", "http://myanimelist.net/rss.php?type=news"));
            feeds.add(new Feed("Diletant", "http://diletant.ru/rss/news/"));
            feeds.add(new Feed("Rain", "http://tvrain.ru/export/rss/news.xml"));
            feeds.add(new Feed("Esquire", "http://feeds.feedburner.com/esquire-ru?format=xml"));
            feeds.add(new Feed("Linux", "http://feeds.feedburner.com/LinuxJournal-BreakingNews?format=xml"));
            feeds.add(new Feed("Shortiki", "http://shortiki.com/rss.php"));
            updateNavBarList();
        }
        adapter = new PostAdapter(this, new Feed(feeds.get(0)));
        title = getTitle();
        setContentView(R.layout.activity_rssmain);
        navigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        navigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    private void updateNavBarList() {
        feeds.addAll(dataSource.getEmptyFeeds());
        Collections.sort(feeds, new Comparator<Feed>() {
                    @Override
                    public int compare(Feed feed, Feed feed2) {
                        return feed.feedName.compareTo(feed2.feedName);
                    }
                }
        );
        int i = 1;
        while (i < feeds.size()) {
            if (feeds.get(i).feedUrl.equals(feeds.get(i - 1).feedUrl)) {
                feeds.remove(i);
            } else i++;
        }
        if (navigationDrawerFragment != null) navigationDrawerFragment.initNavigationBarList();
    }

    public void showFeed(Feed feed) {
        final Feed feed2 = feed;
        if (adapter != null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.replaceFeed(new Feed(feed2));
                    adapter.notifyDataSetChanged();
//                    ((PlaceholderFragment) getFragmentManager().findFragmentById(R.id.container)).listView.requestLayout();
                }
            });
        }
        if (refreshItem != null) refreshItem.setActionView(null);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        while (feeds.isEmpty()) {
        }
        Bundle bundle = new Bundle();
        bundle.putInt(FEED_INDEX, position);
        if (feeds.get(position).size() == 0) {
            getLoaderManager().restartLoader(FEED_DB_LOADER, bundle, this).forceLoad();
            showFeed(feeds.get(position));
        } else {
            showFeed(feeds.get(position));
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
            if (refreshItem != null && feeds.get(currentFeed).isEmpty())
                refreshItem.setActionView(R.layout.actionbar_spinner);
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
            case R.id.remove_content: {
                final AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setTitle("Deleting feed");
                alert.setMessage("Are you sure you want to delete this feed?");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (feeds.size() > 1) {
                            Feed feed = feeds.get(currentFeed);
                            feeds.remove(currentFeed);
                            if (navigationDrawerFragment != null) navigationDrawerFragment.initNavigationBarList();
                            new AsyncTask<Feed, Void, Void>() {
                                @Override
                                protected Void doInBackground(Feed... feeds) {
                                    dataSource.deleteFeed(feeds[0]);
                                    return null;
                                }
                            }.execute(feed);
                            if (feeds.size() > 1) {
                                currentFeed = 0;
                                onNavigationDrawerItemSelected(0);
                            }
                        } else {
                            Toast.makeText(getBaseContext(), "Can't delete feeds, please add some.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                alert.setNegativeButton("No", null);
                alert.show();
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
        if (feedLoader.getId() == FEED_DB_LOADER && feed == null) {
            Bundle bundle = new Bundle();
            bundle.putInt(FEED_INDEX, currentFeed);
            getLoaderManager().restartLoader(FEED_UPDATE_LOADER, bundle, this).forceLoad();
        } else {
            for (int i = 0; i < feeds.size(); i++) {
                if (feeds.get(i).feedUrl.equals(feed.feedUrl)) {
                    feeds.set(i, feed);
                    break;
                }
                if (i == feeds.size() - 1 && !feeds.get(i).feedUrl.equals(feed.feedUrl)) {
                    feeds.add(feed);
                }
            }
            showFeed(feed);
            updateNavBarList();
        }
    }

    @Override
    public void onLoaderReset(Loader<Feed> feedLoader) {
    }
}
