package com.example.kirill.rssreader;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;



import java.util.ArrayList;


public class MainActivity extends ListActivity implements OnTaskCompleted {

    ArrayList<FeedItem> listItems = new ArrayList<FeedItem>();
    ArrayAdapter<FeedItem> adapter;
    WebView webView = null;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        webView = (WebView) findViewById(R.id.description);
        setContentView(R.layout.activity_main);
        adapter = new MySimpleArrayAdapter(this, listItems);
        setListAdapter(adapter);
        listItems.add(new FeedItem(" ", " ", "Press refresh button to download feed", " "));
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions, menu);
        return (super.onCreateOptionsMenu(menu));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                loadRSS();
                break;
            default:
                break;
        }
        return true;
    }

    public void loadRSS() {
        if (RSSLoader.isOnline(this))
        {
            RSSLoadTask task = new RSSLoadTask(this, this);
            //task.execute("http://feeds.bbci.co.uk/news/rss.xml");
            task.execute("http://bash.im/rss/");
        }
        else {
            Toast.makeText(this, "Network error. Please check your Internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    public void onTaskCompleted(ArrayList<FeedItem> newItems) {
        updateList(newItems);
    }


    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        FeedItem item = (FeedItem) getListAdapter().getItem(position);

        Intent i = new Intent(this, ShowArticle.class);
        i.putExtra("link", item.getLink());
        startActivity(i);
    }

    public void updateList(ArrayList<FeedItem> newList) {
        if (newList != null) {
            listItems.clear();
            for (FeedItem i : newList)
                listItems.add(i);
        }
        else {
            listItems.add(new FeedItem("", "Network error", "Can't load data", ""));
        }
        adapter.notifyDataSetChanged();
    }

}


