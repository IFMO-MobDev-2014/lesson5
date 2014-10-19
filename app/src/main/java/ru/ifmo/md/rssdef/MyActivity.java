package ru.ifmo.md.rssdef;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
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
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_add_rss);
        final EditText name = (EditText) dialog.findViewById(R.id.rss_name);
        final EditText url = (EditText) dialog.findViewById(R.id.rss_url);

        Button addButton = (Button) dialog.findViewById(R.id.add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String rssName = name.getText().toString();
                String rssUrl = url.getText().toString();
                ds.put(rssName, rssUrl);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listAdapter.notifyDataSetChanged();
                    }
                });
                lv.setAdapter(listAdapter);
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
