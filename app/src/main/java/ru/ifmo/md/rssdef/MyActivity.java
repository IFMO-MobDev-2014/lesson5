package ru.ifmo.md.rssdef;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ListView;


public class MyActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        ListView lv = (ListView) findViewById(android.R.id.list);
        ListAdapter listAdapter = new ListAdapter(this);
        lv.setAdapter(listAdapter);
    }
}
