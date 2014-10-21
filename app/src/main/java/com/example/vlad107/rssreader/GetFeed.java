package com.example.vlad107.rssreader;

import android.os.AsyncTask;
import android.widget.ListView;

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

    public GetFeed(ListView listView) {
        this.listView = listView;
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
        return null;
    }

    @Override
    protected void onPostExecute(MyHandler myHandler) {
        listView.setAdapter(new FeedAdapter(myHandler.getFeed()));
    }
}
