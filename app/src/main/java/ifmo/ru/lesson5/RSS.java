package ifmo.ru.lesson5;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class RSS extends ListActivity {

    RSSDownloader downloader;
    public List<String> subscriptions = new ArrayList<String>();
    public List<RSSItem> rssItems;
    public RSSAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rss);

        subscriptions.add("http://bash.im/rss/");
        downloader = new RSSDownloader(this);
        try {
            rssItems = downloader.execute(subscriptions).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        adapter = new RSSAdapter(rssItems);
        setListAdapter(adapter);

        final Intent intent = new Intent(this, PreviewActivity.class);
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String url = rssItems.get(position).getUrl();
                intent.putExtra("url", url);
                startActivity(intent);
            }
        });
    }
}
