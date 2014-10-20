package com.dreamteam.app.exercise1;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends ListActivity {

    TextView channelTitle;
    TextView channelDesc;
    FeedAdapter adapter;
    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        channelTitle = (TextView) findViewById(R.id.channel_title);
        channelDesc = (TextView) findViewById(R.id.channel_desc);
        list = getListView();
        adapter = new FeedAdapter(new Feed());
        setListAdapter(adapter);
        refreshFeed(null);
    }

    public void showToast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    public void setListContents(Feed contents) {
        channelTitle.setText(contents.getTitle());
        channelDesc.setText(contents.getDescription());
        adapter.setData(contents);
        adapter.notifyDataSetChanged();
    }

    public void refreshFeed(View view) {
        showToast(getString(R.string.feed_refresh));
        FetchFeedTask task = new FetchFeedTask(this);
        task.execute("http://bash.im/rss/");
    }

    public void startWebPreview(String url) {
        Intent intent = new Intent(this, WebPreview.class);
        intent.putExtra("url", url);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
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
