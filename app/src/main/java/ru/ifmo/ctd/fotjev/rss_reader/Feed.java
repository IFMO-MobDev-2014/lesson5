package ru.ifmo.ctd.fotjev.rss_reader;

import android.app.Activity;
import android.content.Intent;
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

    private ArrayList<HashMap<String, Spanned>> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        new FeedDLParseTask(new Callback<ArrayList<HashMap<String, Spanned>>>() {
            @Override
            public void onSuccess(ArrayList<HashMap<String, Spanned>> result) {
                items = result;
                addToListView();
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(Feed.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        }).execute("http://stackoverflow.com/feeds/tag/android");
    }


    public void addToListView() {
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
                // open link

                Intent intent = new Intent(Feed.this, ContentView.class);
                intent.putExtra("link", link);

                //Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                startActivity(intent);
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

}
