package ru.ifmo.md.lesson5;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ListView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by sultan on 20.10.14.
 */
public class RssDownloader extends AsyncTask<String, Void, ArrayList<RssItem> > {

    private final Context context;
    private final ListView listView;

    public RssDownloader(Context context, ListView listView) {
        this.context = context;
        this.listView = listView;
    }

    @Override
    protected ArrayList<RssItem> doInBackground(String... strings) {

        ArrayList<RssItem> items = new ArrayList<RssItem>();

        for (String string : strings) {
            try {
                InputStream inputStream = new URL(string).openStream();

                XmlPullParserFactory factory = XmlPullParserFactory .newInstance();
                factory.setNamespaceAware(true);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(inputStream, null);

                items.addAll(RssParser.parseRss(xpp));

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
        }

        return items;
    }

    @Override
    protected void onPostExecute(ArrayList<RssItem> rssItems) {
        super.onPostExecute(rssItems);

        MyListAdapter myListAdapter = new MyListAdapter(context, rssItems);
        listView.setAdapter(myListAdapter);
    }

}
