package ru.ifmo.lesson5;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class MainActivity extends Activity {

    public RSSContainer container = new RSSContainer();
    public Context ctx = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String rss_source = getResources().getString(R.string.rss_sourse);
        RSSLoader loader = new RSSLoader(rss_source, container);

        ListView lv = (ListView)this.findViewById(R.id.listView);
        int layoutID = android.R.layout.simple_list_item_1;
        container.aa = new ArrayAdapter<RSSItem>(this, layoutID , container.getContainer());
        lv.setAdapter(container.aa);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(ctx, WebActivity.class);
                intent.putExtra("url", container.getItem(i).url);
                startActivity(intent);
            }
        });
        loader.load();
    }
}
