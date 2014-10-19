package mob_dev_lesson2.katunina.ctddev.ifmo.ru.rss_readerhw5;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;


public class FeedActivity extends ListActivity implements RssParser.FeedParsedCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new RssParser(this, "http://bash.im/rss/");

    }

    public static final String LINK_EXTRA = "link_extra";
    public static final String DESCRIPTION_EXTRA = "description_extra";
    public static final String TITLE_EXTRA = "title_extra";

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        FeedItem item = ((FeedItem) l.getAdapter().getItem(position));
        Intent intent = new Intent(this, DescriptionActivity.class);
        intent.putExtra(LINK_EXTRA,item.link);
        intent.putExtra(DESCRIPTION_EXTRA, item.description);
        intent.putExtra(TITLE_EXTRA, item.title);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.feed, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }

    @Override
    public void onFeedParsed(List<FeedItem> feedItems) {
        if (feedItems == null) {
            Toast.makeText(this, "Could not get feed", Toast.LENGTH_LONG).show();
            return;
        }
        ArrayAdapter<FeedItem> adapter = new ArrayAdapter<FeedItem>(this, android.R.layout.simple_list_item_1, feedItems);
        setListAdapter(adapter);
    }
}
