package ru.ifmo.ctd.fotjev.rss_reader;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Spanned;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;


public class Feed extends Activity {

    private ListView feedView;
    private ArrayList<HashMap<String, Spanned>> feed;

    private final String defaultFeed = "http://stackoverflow.com/feeds/tag/android";
    //private final String defaultFeed = "http://bash.im/rss/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        new FeedDLParseTask(new Callback<ArrayList<HashMap<String, Spanned>>>() {
            @Override
            public void onSuccess(ArrayList<HashMap<String, Spanned>> result) {
                feed = result;
                addToListView(result);
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(Feed.this, "ERROR: " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        }).execute(defaultFeed);
    }


    public void addToListView(ArrayList<HashMap<String, Spanned>> items) {
        feedView = (ListView) findViewById(R.id.listView);
        ListAdapter adapter = new SimpleAdapter(this, items, R.layout.feed_item,
                new String[]{RSSAtomParser.TITLE, RSSAtomParser.LINK, RSSAtomParser.DESC},
                new int[]{R.id.title, R.id.link, R.id.descr});

        feedView.setAdapter(adapter);

        feedView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                String link = ((TextView) view.findViewById(R.id.link)).getText().toString();

                // open link in webview
                Intent intent = new Intent(Feed.this, ContentView.class);
                intent.putExtra("link", link);
                startActivity(intent);

                // open link in browser:
                //Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));

            }
        });
        feedView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {

                String desc = ((TextView) view.findViewById(R.id.descr)).getText().toString();
                // Show description
                Toast.makeText(getApplicationContext(), desc, Toast.LENGTH_LONG).show();
                return true;
            }

        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        addToListView(feed);
    }

}
