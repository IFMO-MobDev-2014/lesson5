package com.wibk.rss;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.Toast;

import org.xml.sax.InputSource;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class RssFeedFetcher extends AsyncTask<String, Void, RssFeed> {
    private RssItemAdapter adapter;
    private Context context;

    public RssFeedFetcher(RssItemAdapter adapter, Context context) {
        this.adapter = adapter;
        this.context = context;
    }

    @Override
    protected RssFeed doInBackground(String... strings) {
        Uri uri = Uri.parse(strings[0]);
        try {
            URL url = new URL(uri.toString());
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty("Accept-Charset", "windows-1251");
            httpURLConnection.setRequestProperty("Content-Type", "text/plain; charset=utf-8");
            httpURLConnection.setRequestProperty("Accept-Encoding", "identity");
            httpURLConnection.setRequestProperty("User-Agent", "Mozilla/5.0");
            httpURLConnection.connect();
            InputStream inputStream = httpURLConnection.getInputStream();

            Reader reader = new InputStreamReader(inputStream,"windows-1251");

            InputSource is = new InputSource(reader);
            is.setEncoding("windows-1251");
            return new RssFeed(is);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(RssFeed rssFeed) {
        if (rssFeed == null) {
            Toast toast = Toast.makeText(context, "No network connection", Toast.LENGTH_SHORT);
            toast.show();
        } else {
            List<RssItem> rssItems = rssFeed.getRssItemList();
            for (RssItem rssItem : rssItems) {
                adapter.add(rssItem);
            }
            adapter.notifyDataSetChanged();
        }
    }
}
