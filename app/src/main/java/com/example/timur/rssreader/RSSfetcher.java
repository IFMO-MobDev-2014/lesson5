package com.example.timur.rssreader;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Xml;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import org.xmlpull.v1.XmlPullParser;



/**
 * Created by timur on 21.10.14.
 */

public class RSSfetcher extends AsyncTask<String, String, ArrayList<RSSitem>> {
    Context context;
    ProgressDialog dialog;

    RSSfetcher(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog = new ProgressDialog(context);
        dialog.setMessage("Please, wait");
        dialog.setIndeterminate(true);
        dialog.setCancelable(true);
        dialog.show();
    }

    @Override
    protected  ArrayList<RSSitem> doInBackground(String... params) {
        ArrayList<RSSitem> items = null;
        try {
            URL url = new URL(params[0]);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            InputStream is = connection.getInputStream();
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(is, null);
            items = new XmlParser(parser).parseXML();
        } catch (Exception e) {
            Toast.makeText(context, "Network error", Toast.LENGTH_SHORT).show();
            items = null;
        }
        return items;
    }

    @Override
    protected void onPostExecute(ArrayList<RSSitem> items) {
        super.onPostExecute(items);
        dialog.dismiss();
    }
}
