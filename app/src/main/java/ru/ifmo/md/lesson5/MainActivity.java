package ru.ifmo.md.lesson5;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class MainActivity extends Activity {
    private static final String RSS_LINK = "http://bash.im/rss/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView view = (ListView) findViewById(R.id.rssList);

        new RssDownloader(view, this).execute(RSS_LINK);

        final Intent intent = new Intent(this, PreviewActivity.class);

        view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RssItemAdapter adapter = (RssItemAdapter) parent.getAdapter();
                RssItem item = (RssItem) adapter.getItem(position);
                intent.putExtra(PreviewActivity.URL, item.getLink());
                startActivity(intent);
            }
        });
    }
}