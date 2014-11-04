package ru.eugene.listviewpractice2;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class MyActivity extends ListActivity {
    Intent rssIntent;
    List<String> articles = new ArrayList<String>();
    List<String> urlArticles = new ArrayList<String>();

    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        articles.add("bbc news");
        urlArticles.add("http://feeds.bbci.co.uk/news/rss.xml");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, articles);
        setListAdapter(arrayAdapter);
        rssIntent = new Intent(this, RssFeed.class);
        rssIntent.putExtra("url", urlArticles.get(0));
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        startActivity(rssIntent);
    }

}
