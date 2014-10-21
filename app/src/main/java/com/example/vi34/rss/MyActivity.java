package com.example.vi34.rss;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class MyActivity extends Activity {

    ListView listView;
    TextView textView;
    Intent intent;
    public static List<Entry> list;
    AtomDownloader downloader;
    public static MyAdapter adapter;
    String channel = "http://stackoverflow.com/feeds/tag/android";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        listView = (ListView) findViewById(R.id.listView);
        textView = (TextView) findViewById(R.id.txt);
        textView.setText("Active questions tagged android - Stack Overflow");
        intent = new Intent(this, ContentActivity.class);
        list = new ArrayList<Entry>();
        downloader = new AtomDownloader();

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            downloader.execute(channel);
        } else {
            Toast.makeText(this,"Check your Internet Connection", 3).show();
        }

        adapter = new MyAdapter(list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Entry item = (Entry) parent.getItemAtPosition(position);
                intent.putExtra("summary",item.summary);
                intent.putExtra("title",item.title);
                startActivity(intent);
            }
        });
    }


    public void reloadClicked(View view) {
        list.clear();
        adapter.notifyDataSetChanged();
        new AtomDownloader().execute(channel);
    }

}
