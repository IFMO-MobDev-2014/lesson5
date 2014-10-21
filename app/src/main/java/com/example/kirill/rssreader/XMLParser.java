package com.example.kirill.rssreader;

import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;

/**
 * Created by Kirill on 19.10.2014.
 */
public class XMLParser {
    ArrayList<FeedItem> resultList = null;

    private XmlPullParser parser = null;

    public XMLParser() throws IOException, XmlPullParserException {
        resultList = new ArrayList<FeedItem>();
        parser = Xml.newPullParser();
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
    }

    private void parseXML() throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "rss");
        while (true) {
            if (parser.next() == XmlPullParser.START_TAG) break;
        }
        parser.require(XmlPullParser.START_TAG, null, "channel");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("item")) {
                resultList.add(parseItem());
            } else {
                skipTag();
            }
        }
    }
    public void parse(String data) throws IOException, XmlPullParserException{

        InputStream is = new ByteArrayInputStream(data.getBytes("UTF-8"));
        parser.setInput(is, "UTF-8");
        Log.i("data encoding", parser.getInputEncoding());
        parser.nextTag();
        parseXML();
    }

    private FeedItem parseItem() throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "item");
        String link = null;
        String title = null;
        String description = null;
        String pubDate = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("link")) {
                link = parseLink();
            } else if (name.equals("title")) {
                title = parseTitle();
            } else if (name.equals("description")) {
                description = parseDescription();
            } else if(name.equals("pubDate")) {
                pubDate = parsePubDate();
            } else {
                skipTag();
            }
        }
        return new FeedItem(link, title, description, pubDate);
    }
    private String parseDescription() throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "description");
        String result = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, "description");
        return result;
    }
    private String parseTitle() throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "title");
        String result = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, "title");
        return result;
    }
    private String parseLink() throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "link");
        String result = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, "link");
        return result;
    }
    private String parsePubDate() throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "pubDate");
        String result = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, "pubDate");
        return result;
    }
    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    private void skipTag() throws IOException, XmlPullParserException {
        int depth = 1;
        while (depth > 0) {
            switch (parser.next()) {
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
            }
        }
    }

    public ArrayList<FeedItem> getResult() {
        return resultList;
    }
}
