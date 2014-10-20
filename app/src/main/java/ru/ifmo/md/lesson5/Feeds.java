package ru.ifmo.md.lesson5;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class Feeds extends ListActivity {
    TextView channel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        channel = (TextView) findViewById(R.id.channel);
        ArrayList<FeedItem> arr = new ArrayList<FeedItem>();
        setListAdapter(new MyCustomAdapter(this, arr));

        SwipeDismissListViewTouchListener touchListener = new SwipeDismissListViewTouchListener(
                getListView(),
                new SwipeDismissListViewTouchListener.DismissCallbacks() {
                    @Override
                    public boolean canDismiss(int position) {
                        return true;
                    }

                    @Override
                    public void onDismiss(ListView listView, int[] reverseSortedPositions) {
                        for (int position : reverseSortedPositions) {
                            ((MyCustomAdapter) Feeds.this.getListAdapter()).remove(position);
                        }
                        ((MyCustomAdapter) Feeds.this.getListAdapter()).notifyDataSetChanged();
                    }
                }
        );

        getListView().setOnTouchListener(touchListener);
        getListView().setOnScrollListener(touchListener.makeScrollListener());
        MyCustomAdapter t = (MyCustomAdapter) getListAdapter();
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                FeedItem c = (FeedItem) adapterView.getItemAtPosition(i);
                Intent x = new Intent(Feeds.this, WebActivity.class);
                x.putExtra("URL", c.getUrl());
                Feeds.this.startActivity(x);
            }
        });
        channel.setOnClickListener(new View.OnClickListener() {
            private boolean downloaded = false;
            @Override
            public void onClick(View view) {
                if (downloaded) {
                    return;
                }
                ConnectivityManager connMgr = (ConnectivityManager)
                        getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {
                    new DataDownloader(Feeds.this).execute();
                    downloaded = true;
                } else {
                    Feeds.this.channel.setText("No Internet connection.\n" +
                            "Connect to the Internet and click here.");
                }
            }
        });
        channel.callOnClick();
    }
}
