package ru.ifmo.md.lesson5;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;


public class MyActivity extends Activity {

    static DataStorage ds;
    ListAdapter listAdapter;
    ListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        lv = (ListView) findViewById(R.id.list_view);
        ds = new DataStorage(this);
        listAdapter = new ListAdapter(this, ds);
        lv.setAdapter(listAdapter);
    }

    public void addUrlAction(View v) {
        MyDialog md = new MyDialog(this);
        md.initFields(ds, listAdapter, lv);
        md.show();
    }
}
