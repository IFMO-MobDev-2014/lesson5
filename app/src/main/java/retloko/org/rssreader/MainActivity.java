package retloko.org.rssreader;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends ListActivity {
    private static final String RSS_URL = "http://stackoverflow.com/feeds/tag/android";

    private Button refreshButton;
    private RssLoader rssLoader;
    private TextView emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rssLoader = new RssLoader(this);

        emptyView = (TextView) findViewById(android.R.id.empty);

        refreshButton = (Button) findViewById(R.id.refresh);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRefresh();
            }
        });

        onRefresh();
    }

    public void onRefresh() {
        refreshButton.setEnabled(false);
        refreshButton.setText(R.string.refreshing_btn);
        emptyView.setText(R.string.loading_msg);
        rssLoader.loadUrl(RSS_URL);
    }

    public void onRefreshDone() {
        refreshButton.setText(R.string.refresh_btn);
        refreshButton.setEnabled(true);
    }

    public void onNetworkError(Exception e) {
        emptyView.setText(R.string.net_error_msg);
        onRefreshDone();
    }

    public void onFeedError(Exception e) {
        emptyView.setText(R.string.feed_error_msg);
        onRefreshDone();
    }
}
