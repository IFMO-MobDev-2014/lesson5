package ru.ifmo.md.lesson5;

/**
 * Created by lightning95 on 10/21/14.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.List;

public class RssDownloader extends AsyncTask<String, Void, List<RssItem>> {
    private final ListView view;
    private final Context context;
    private ProgressDialog dialog;

    public RssDownloader(ListView view, Context context) {
        this.view = view;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        dialog = new ProgressDialog(context);
        dialog.setMessage("Downloading, please wait...");
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    protected List<RssItem> doInBackground(String... params) {
        try {
            return RssParser.parseRss(params[0]);
        } catch (IOException e) {
            Log.d("APP", e.getMessage());
        } catch (XmlPullParserException e) {
            Log.d("APP", e.getMessage());
        }

        return null;
    }

    @Override
    protected void onPostExecute(List<RssItem> rssItems) {
        super.onPostExecute(rssItems);

        dialog.dismiss();
        if (rssItems == null) {
            Toast.makeText(context, "Network error", Toast.LENGTH_SHORT).show();
        } else {
            view.setAdapter(new RssItemAdapter(rssItems, context));
        }
    }
}