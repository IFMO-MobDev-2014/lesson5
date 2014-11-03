package ru.ifmo.md.patrikeev.rssreader;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;


public class MainActivity extends Activity {

    private final String initialURL = "http://bash.im/rss/";
    private ListView listView;
    private EditText urlText;
    private ArrayList<Feed> feeds = new ArrayList<>();
    private RSSListAdapter rssListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        urlText = (EditText) findViewById(R.id.editText);
        rssListAdapter = new RSSListAdapter(this);
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(rssListAdapter);
        loadRSS(initialURL);
    }

    public void onGetClicked(View view) {
        String stringURL = urlText.getText().toString();
        loadRSS(stringURL);
    }

    public void loadRSS(String url) {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new RSSLoaderTask(this).execute(url);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Error").setMessage(R.string.no_connection);
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }

    public void onRSSFetched(String feedURL, ArrayList<RSSItem> result) {
        if (result != null) {
            Log.d(feedURL, result.toArray().toString());
            feeds.add(new Feed(feedURL, result));
            rssListAdapter.addRssItems(result);
        }
    }

    public void onRSSClicked(String url) {
        Intent intent = new Intent(MainActivity.this, ResultActivity.class);
        intent.putExtra("URL", url);
        startActivity(intent);
    }
}

class Feed {
    String feedURL;
    ArrayList<RSSItem> rssItems;

    Feed(String feedURL, ArrayList<RSSItem> items) {
        this.feedURL = feedURL;
        this.rssItems = items;
    }
}
