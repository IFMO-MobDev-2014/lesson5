package ru.ifmo.md.lesson5;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class Feeds extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        TextView x = (TextView) findViewById(R.id.textView);
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
        new DataDownloader((MyCustomAdapter) getListAdapter()).execute();

    }
}
