package ru.ifmo.md.patrikeev.rssreader;

import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by sergey on 20.10.14.
 */
public class RSSLoaderTask extends AsyncTask<String, Void, Void> {

    private String feedURL;
    private MainActivity context;
    private ArrayList<RSSItem> result = new ArrayList<>();

    RSSLoaderTask(MainActivity context) {
        feedURL = new String();
        this.context = context;
    }

    @Override
    protected Void doInBackground(String... strings) {
        try {
            feedURL = strings[0];
            URL url = new URL(feedURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = connection.getInputStream();
            Reader reader = new InputStreamReader(inputStream, "windows-1251");
//            Reader reader = new InputStreamReader(inputStream, "utf-8");
//          I didn't get how to calculate encoding by giving XML...
            InputSource inputSource = new InputSource(reader);
            SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
            SAXHandler handler = new SAXHandler();
            parser.parse(inputSource, handler);
            result = handler.rssItems;
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        context.onRSSFetched(feedURL, result);
    }
}
