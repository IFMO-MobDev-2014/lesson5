package com.example.kirill.rssreader;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Kirill on 20.10.2014.
 */
class RSSLoadTask extends AsyncTask<String, Integer, ArrayList<FeedItem>> {

    private Context mContext = null;
    private RSSLoader loader = null;
    private OnTaskCompleted listener = null;

    public RSSLoadTask(Context mContext, OnTaskCompleted listener) {
        this.mContext = mContext;
        this.loader = new RSSLoader(mContext);
        this.listener = listener;
    }

    protected ArrayList<FeedItem> doInBackground(String... params) {
        ArrayList<FeedItem> result = null;
        if (RSSLoader.isOnline(mContext))
        {
            try {
                XMLParser parser = new XMLParser();
                //BufferedInputStream data = loader.load(params[0]);
                //InputStream data = loader.load(params[0]);
                String data = loader.load(params[0]);
                parser.parse(data);
                result = parser.getResult();
            } catch (Exception e) {
                Log.e("Parsing", e.getMessage());
            }
        }
        return result;
    }

    protected void onPreExecute() {
        //Toast.makeText(mContext, "Start loading", Toast.LENGTH_SHORT).show();
        Log.i("", "Start loading");
        super.onPreExecute();
    }

    protected void onPostExecute(ArrayList<FeedItem> result) {
        //Toast.makeText(mContext, "Finished!", Toast.LENGTH_SHORT).show();
        Log.i("", "Finished!");
        listener.onTaskCompleted(result);
        super.onPostExecute(result);
    }
}