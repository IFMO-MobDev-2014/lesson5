package com.example.vlad107.rssreader;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ListView;
import android.widget.Toast;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by vlad107 on 21.10.14
 */
public class GetFeed extends AsyncTask<String, Void, MyHandler> {

    ListView listView = null;
    Context context;
    boolean error;

    public GetFeed(ListView listView, Context context) {
        this.listView = listView;
        this.context = context;
    }

    @Override
    protected MyHandler doInBackground(String... strings) {
        String link = strings[0];
        try {
            URL url = new URL(link);
            URLConnection con = url.openConnection();
            SAXParserFactory parserFactory = SAXParserFactory.newInstance();
            SAXParser saxParser = parserFactory.newSAXParser();
            MyHandler handler = new MyHandler();
            saxParser.parse(con.getInputStream(), handler);
            error = false;
            return handler;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        error = true;
        return null;
    }

    @Override
    protected void onPostExecute(MyHandler myHandler) {
        if (error) {
            Toast.makeText(context, "Page is not available", Toast.LENGTH_LONG).show();
            return;
        }
        listView.setAdapter(new FeedAdapter(myHandler.getFeed()));
    }
}
