package ru.ifmo.md.lesson5;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;


public class MainActivity extends Activity {

    private ListView listView;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        intent = new Intent(this, WebActivity.class);

        listView = (ListView) findViewById(R.id.listView);
        RssDownloader rssDownloader = new RssDownloader(this, listView);
        rssDownloader.execute("http://bash.im/rss");

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MyListAdapter adapter = (MyListAdapter) adapterView.getAdapter();
                RssItem item = (RssItem) adapter.getItem(i);

                intent.putExtra("URL_PREVIEW", item.getLink());
                startActivity(intent);
            }
        });

    }

}
