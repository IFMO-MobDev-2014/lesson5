package ru.ya.myapplication;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Adapter;
import android.widget.ListView;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.*;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by vanya on 21.10.14.
 */
public class UploadXML extends AsyncTask < String, Void, RSSFeed > {
    ListView listView;

    public UploadXML(ListView listView) {
        this.listView = listView;
    }

    @Override
    protected void onPostExecute(RSSFeed rssFeed) {
        if (rssFeed == null)
            Log.e("abacaba6", "abacaba");
        MyAdapter myAdapter = new MyAdapter();
        listView.setAdapter(myAdapter);
        myAdapter.arrayList = rssFeed.arrayList;
        myAdapter.notifyDataSetChanged();
    }

    @Override
    protected RSSFeed doInBackground(String... strings) {
        try {
            URL url = new URL(strings[0]);
            //URLConnection urlConnection = url.openConnection();
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();

            InputStream inputStream = httpURLConnection.getInputStream();
            Reader reader = new InputStreamReader(inputStream, "windows-1251");
            InputSource is = new InputSource(reader);
            is.setEncoding("windows-1251");


            SAXParser saxParser = SAXParserFactory.newInstance().newSAXParser();
            MyHandler myHandler = new MyHandler();
            saxParser.parse(is, myHandler);

            return myHandler.getRSSFeed();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        return null;
    }
}