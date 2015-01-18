package ru.itmo.rss;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;
import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;
import java.util.List;

public class DownloadRSS extends AsyncTask<String, Void, List<ItemRSS>> {
    private final ListView view;
    private final Context context;

    public DownloadRSS(ListView view, Context context) {
        this.view = view;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected List<ItemRSS> doInBackground(String[] s) {
        try {
            return RSSParser.parseFeed(s[0]);
        } catch (IOException | XmlPullParserException e) {
            Log.d("APP", e.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<ItemRSS> items) {
        super.onPostExecute(items);
        if (items == null) {
            Toast.makeText(context, "Could not download feed", Toast.LENGTH_SHORT).show();
        } else {
            view.setAdapter(new RSSAdapter(items, context));
        }
    }
}