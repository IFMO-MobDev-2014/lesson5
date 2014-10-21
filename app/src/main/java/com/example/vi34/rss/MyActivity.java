package com.example.vi34.rss;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class MyActivity extends Activity {

    ListView listView;
    public static List<Entry> list;
    RssDownloader downloader;
    public static String[] summary = new String[2];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        //list = new ArrayList<Entry>();
        Entry test = new Entry("How to fix", "Prathyusha"," &lt;p&gt;I got a requirement where I need to set only &lt;strong&gt;3 images for every scroll in horizontal listview&lt;/strong&gt; in  android like &lt;a href=&quot;https://github.com/Seitk/InfiniteScrollPicker&quot; rel=&quot;nofollow&quot;&gt;this&lt;/a&gt;. please help me out.Thanks in advance &lt;/p&gt;","http://stackoverflow.com/q/26484014");

        downloader = new RssDownloader();
        downloader.execute("http://stackoverflow.com/feeds/tag/android");

        summary[1] = "AGH!";
        summary[0] = "WOW";

        ListAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, summary);

        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
    }

    public void addClicked(View view) {
        EditText editText = (EditText) findViewById(R.id.edit);
        Toast.makeText(this, editText.getText(), 0).show();

    }


}
