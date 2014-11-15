package default_.lesson5;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import default_.lesson5.auxillary.*;
import default_.lesson5.parsers.FeedParser;


public class RSSActivity extends Activity {
    public static final String BBC = "http://feeds.bbci.co.uk/news/rss.xml";
    public static final String URL_INTENT = "URL";
    EntryAdapter adapter;
    Intent intent;
    Feed feed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rss);
        intent = new Intent(this, WebActivity.class);
        new FeedParser(RSSActivity.this).execute(BBC);
    }

    public void onFinished(final Feed feed) {
        this.feed = feed;
        setTitle(feed.getTitle());
        adapter = new EntryAdapter(this, feed);
        ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String url = feed.getEntry(i).getLink();
                intent.putExtra(URL_INTENT, url);
                startActivity(intent);
            }
        });
        String title = "Total articles found: " + feed.countEntries();
        TextView text = (TextView) findViewById(R.id.text);
        text.setText(title);
        Log.d("PARSING", "Parsing finished");
    }
}