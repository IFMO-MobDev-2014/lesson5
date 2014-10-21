package com.pokrasko.lesson5;

import android.app.ListActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by pokrasko on 21.10.14.
 */
public class MainActivity extends ListActivity {
    private Resources res;

    private TextView descriptionView;
    private Button refreshButton;
    private ListView listView;
    private TextView emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        setTitle(R.string.default_rss_title);

        res = getResources();

        descriptionView = (TextView) findViewById(R.id.description);
        listView = (ListView) findViewById(android.R.id.list);
        emptyView = (TextView) findViewById(R.id.empty);

        FeedAdapter adapter = new FeedAdapter(new Feed(
                ""
              , ""));
        setListAdapter(adapter);

        refreshButton = (Button) findViewById(R.id.refreshButton);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRefresh();
            }
        });

        onRefresh();
    }

    public void setDescription(String description) {
        descriptionView.setText(description);
    }

    public void setFeed(final Feed feed) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < feed.getSize(); i++) {
                    ((FeedAdapter) getListAdapter()).add(feed.getItem(i));
                }
            }
        });
    }

    public void onRefresh() {
        refreshButton.setEnabled(false);
        refreshButton.setText(R.string.refreshing);
        listView.setVisibility(View.GONE);
        emptyView.setVisibility(View.VISIBLE);
        new RSSLoader(this).execute(res.getString(R.string.default_rss_link));
    }

    public void onRefreshed(boolean valid, String newTitle, final String errorMessage) {
        if (valid) {
            emptyView.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
        } else {
            emptyView.setText(errorMessage);
        }
        setTitle(newTitle);
        refreshButton.setText(R.string.refresh);
        refreshButton.setEnabled(true);
    }

    public void showMore(String title, String link) {
        Intent intent = new Intent(this, WebActivity.class);
        intent.putExtra("link", link);
        intent.putExtra("title", title);
        startActivity(intent);
    }
}
