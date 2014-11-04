package com.example.rssreader;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.InputStream;
import java.net.URL;
import java.util.List;


/**
 * Created by Амир on 04.11.2014.
 */
public class DownloadContent extends AsyncTask<String, Void, List<ContentItem>> {
    ItemAdapter adapter;
    Context context;

    public DownloadContent(ItemAdapter adapter, Context context) {
        this.context = context;
        this.adapter = adapter;
    }

    @Override
    protected List<ContentItem> doInBackground(String... params) {
        try {
            InputStream input = new URL(params[0]).openStream();
            return XMLParser.parse(input);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<ContentItem> list) {
        super.onPostExecute(list);
        if (list == null) {
            Toast.makeText(context, "No internet connection", Toast.LENGTH_LONG).show();
            return;
        }
        for (ContentItem x : list) {
            Log.d("link", x.link);
            Log.d("title", x.title);
            Log.d("date", x.pubDate);
            Log.d("description", x.description);
        }
        adapter.items = list;
        adapter.notifyDataSetChanged();
    }
}
