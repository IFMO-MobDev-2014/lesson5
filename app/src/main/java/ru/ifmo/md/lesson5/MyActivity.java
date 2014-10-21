package ru.ifmo.md.lesson5;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class MyActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        final Intent intent = new Intent(this, WebActivity.class);

        ListView listView1 = (ListView) findViewById(R.id.listView1);

        new NewsDownloadTask(this, listView1).execute(getString(R.string.URL_BBC_RSS_NEWS));

        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position, long x) {

                NewsItem item = (NewsItem) adapter.getItemAtPosition(position);

                intent.putExtra(getString(R.string.URL), item.getUrl());
                startActivity(intent);
            }
        });

    }
}