package odeen.rssreader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by Женя on 21.10.2014.
 */
public class RSSListActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rss_list);
        ListView listView = (ListView)findViewById(R.id.rss_listView);
        listView.setAdapter(new ArrayAdapter<String>(this, R.layout.item_rss_list, RSSNewsLibrary.get(this).getChannels()));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(RSSListActivity.this, NewsListActivity.class);
                intent.putExtra(NewsListActivity.CHANNEL_ID, i);
                startActivity(intent);
            }
        });
    }
}
