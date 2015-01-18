package ru.itmo.rss;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;


public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView view = (ListView)findViewById(R.id.feedList);
        new DownloadRSS(view, this).execute("http://feeds.bbci.co.uk/news/rss.xml");
        final Intent intent = new Intent(this, PreviewActivity.class);
        view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RSSAdapter adapter = (RSSAdapter) parent.getAdapter();
                ItemRSS item = (ItemRSS) adapter.getItem(position);
                intent.putExtra(PreviewActivity.PREVIEW_URL, item.getLink());
                startActivity(intent);
            }
        });
    }
}