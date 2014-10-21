package ru.ifmo.md.lesson5;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class MyActivity extends Activity {
    static String key = "key";
    static DataStorage ds;
    ListAdapter listAdapter;
    Intent intent;
    ListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        lv = (ListView) findViewById(R.id.list_view);
        ds = new DataStorage(this);
        listAdapter = new ListAdapter(this, ds);
        lv.setAdapter(listAdapter);
        intent = new Intent(this, WebViewActivity.class);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pisition, long id) {
                String link = ds.readRssData().get(pisition).link;
                intent.putExtra(key, link);
                startActivity(intent);
            }
        });
    }

    public void addUrlAction(View v) {
        AddNewResourceDialog md = new AddNewResourceDialog(this);
        md.initFields(ds, listAdapter, lv);
        md.show();
    }

    public void editResources(View v) {
        EditResourceDialog dialog = new EditResourceDialog(this);
        dialog.prepare(ds, lv, listAdapter);
        dialog.show();
    }

    public void update(View v) {
        ds.updateInformation();

        listAdapter.notifyDataSetChanged();
        lv.setAdapter(listAdapter);
    }
}
