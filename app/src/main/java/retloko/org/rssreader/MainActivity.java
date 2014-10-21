package retloko.org.rssreader;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends ListActivity {
    private static final String RSS_URL = "http://bash.im/rss";

    private RssLoader rssLoader;
    private TextView emptyView;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rssLoader = new RssLoader(this);

        emptyView = (TextView) findViewById(android.R.id.empty);
        listView = (ListView) findViewById(android.R.id.list);

        onRefresh();
    }

    public void onRefresh() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                emptyView.setText(R.string.loading_msg);
                rssLoader.loadUrl(RSS_URL);
            }
        });
    }

    public void onRefreshDone(EntryAdapter adapter) {
        listView.setAdapter(adapter);
    }

    public void onNetworkError(Exception e) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setTitle(R.string.app_name);
                emptyView.setText(R.string.net_error_msg);
            }
        });
    }

    public void onFeedError(Exception e) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setTitle(R.string.app_name);
                emptyView.setText(R.string.feed_error_msg);
            }
        });
    }
}
