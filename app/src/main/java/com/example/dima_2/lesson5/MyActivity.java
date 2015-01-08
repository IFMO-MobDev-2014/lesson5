package com.example.dima_2.lesson5;

import android.app.Activity;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.content.Intent;
import android.content.Context;

import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView;

public class MyActivity extends Activity {

    public RSSList list = new RSSList();
    public Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        String source = getResources().getString(R.string.source);
        RSSLoader loader = new RSSLoader(source, list);

        ListView views = (ListView)this.findViewById(R.id.listView);
        int layoutId = android.R.layout.simple_list_item_1;
        list.adapter = new ArrayAdapter<RSSElement>(this, layoutId, list.getList());
        views.setAdapter(list.adapter);

        views.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(context, WebActivity.class);
                intent.putExtra("url", list.getUrl(i));
                startActivity(intent);
            }
        });
        loader.load();
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
