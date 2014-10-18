package com.example.ru.rssreader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import java.lang.String;
import java.util.ArrayList;

public class MainActivity extends Activity {

    private ArrayAdapter<String> arrayAdapter1;
    private ArrayList<String> rssNames;
    private EditText editText1;

    void addDefaultRSS() {
        rssNames.add(getString(R.string.bbc_rss));
        rssNames.add(getString(R.string.echo_msk_rss));
        rssNames.add(getString(R.string.bash_rss));
        rssNames.add(getString(R.string.stackoverflow_rss));
        rssNames.add(getString(R.string.habr_rss));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView listView1 = (ListView) findViewById(R.id.listView);
        ImageButton button1 = (ImageButton) findViewById(R.id.imageButton);
        editText1 = (EditText) findViewById(R.id.editText);
        rssNames = new ArrayList<String>();
        addDefaultRSS();
        arrayAdapter1 = new ArrayAdapter<String>(this, R.layout.text_view_main, rssNames);
        listView1.setAdapter(arrayAdapter1);
        button1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                try {
                    rssNames.add(editText1.getText().toString());
                    editText1.setText("");
                    arrayAdapter1.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), RSSActivity.class);
                intent.putExtra("link", rssNames.get(i));
                startActivity(intent);
            }
        });
    }


}
