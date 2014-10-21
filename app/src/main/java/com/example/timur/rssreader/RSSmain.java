package com.example.timur.rssreader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;


public class RSSmain extends Activity {

    public static final String URL = "http://feeds.bbci.co.uk/news/rss.xml";

    ListView listView;
    ArrayAdapter<String> adapter;
    ArrayList<RSSitem> rSSItems;
    RSSfetcher rSSfetcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rssmain);
        final Intent intent = new Intent(this, Preview.class);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String s = ((ArrayAdapter<String>) parent.getAdapter()).getItem(position);
                String url = s.substring(0, s.indexOf('\n'));
                intent.putExtra("url", url);
                startActivity(intent);
            }
        });
        rSSfetcher = new RSSfetcher(this);
        try {
            rSSItems = rSSfetcher.execute(URL).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        adapter.clear();
        for (int i = 0; i < rSSItems.size(); i++) {
            String text = rSSItems.get(i).getLink() + "\n";
            text += rSSItems.get(i).getTitle() + "\n";
            text += rSSItems.get(i).getMainInfo() + "\n";
            adapter.add(text + "\n");
        }
        if (adapter.isEmpty()) {
            try {
                throw new Exception();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            listView.setAdapter(adapter);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.rssmain, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
