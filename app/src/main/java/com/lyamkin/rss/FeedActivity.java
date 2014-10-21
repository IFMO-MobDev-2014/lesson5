package com.lyamkin.rss;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;


public class FeedActivity extends ListActivity implements FeedParsed {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new XmlParser(this, "http://echo.msk.ru/interview/rss-fulltext.xml");
    }

    @Override
    protected void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);

        Article item = ((Article) listView.getAdapter().getItem(position));

        Intent toFullArticle = new Intent(this, FullArticleActivity.class);
        toFullArticle.putExtra(EXTRA_TITLE, item.getTitle());
        toFullArticle.putExtra(EXTRA_URL, item.getLink());

        startActivity(toFullArticle);
    }

    public static final String EXTRA_URL = "LINK";
    public static final String EXTRA_TITLE = "TITLE";

    @Override
    public void onFeedParsed(List<Article> articleList) {
        if (articleList == null) {
            Toast.makeText(this, "Error! Check your internet connection and try again.", Toast.LENGTH_LONG).show();
            return;
        }
        ArrayAdapter<Article> adapter = new ArrayAdapter<Article>(this, android.R.layout.simple_list_item_1, articleList);
        setListAdapter(adapter);
    }
}