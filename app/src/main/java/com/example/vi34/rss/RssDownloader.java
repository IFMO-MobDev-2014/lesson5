package com.example.vi34.rss;

import android.os.AsyncTask;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vi34 on 20.10.14.
 */
public class RssDownloader extends AsyncTask<String,Void,List<Entry>> {

    @Override
    protected List<Entry> doInBackground(String... params) {
        try {
            URL url = new URL(params[0]);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            InputStream inputStream = conn.getInputStream();

            try {
                XmlPullParser parser = Xml.newPullParser();
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                parser.setInput(inputStream,null);
                parser.nextTag();
                return readFeed(parser);
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } finally {
                inputStream.close();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected void onPostExecute(List<Entry> list) {
        MyActivity.list = list;
    }

    private List<Entry> readFeed(XmlPullParser parser) throws IOException, XmlPullParserException{
        List<Entry> entries = new ArrayList<Entry>();
        parser.require(XmlPullParser.START_TAG, null, "feed");
        while (parser.next() != parser.END_TAG) {
            if(parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String tag = parser.getName();
            if(tag.equals("entry")) {
                entries.add(readEntry(parser));
            } else {
                skip(parser);
            }

        }
        return entries;
    }

    private Entry readEntry(XmlPullParser parser) throws XmlPullParserException, IOException{
        String name = null;
        String title = null;
        String summary = null;
        String link = null;
        parser.require(XmlPullParser.START_TAG, null, "entry");
        while (parser.next() != parser.END_TAG) {
            if(parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String tag = parser.getName();
            if(tag.equals("title")) {
                title = readTitle(parser);
            } else if(tag.equals("author")) {
                name = readName(parser);
            } else if(tag.equals("summary")) {
                summary = readSummary(parser);
            } else if(tag.equals("link")) {
                link = readLink(parser);
            } else {
                skip(parser);
            }

        }

        return new Entry(title,name,summary,link);
    }

    private String readTitle(XmlPullParser parser) throws IOException, XmlPullParserException{
        parser.require(XmlPullParser.START_TAG, null, "title");
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, "title");
        return title;
    }

    private String readName(XmlPullParser parser) throws  IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "author");
        parser.nextTag();

        parser.require(XmlPullParser.START_TAG, null, "name");
        String name = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, "name");

        parser.nextTag();
        skip(parser);
        parser.nextTag();

        parser.require(XmlPullParser.END_TAG, null, "author");
        return name;
    }

    private String readLink(XmlPullParser parser) throws IOException, XmlPullParserException {
        String link = "";
        parser.require(XmlPullParser.START_TAG, null, "link");
        String tag = parser.getName();
        String relType = parser.getAttributeValue(null, "rel");
        if (tag.equals("link")) {
            if (relType.equals("alternate")){
                link = parser.getAttributeValue(null, "href");
                parser.nextTag();
            }
        }
        parser.require(XmlPullParser.END_TAG, null, "link");
        return link;
    }

    private String readSummary(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "summary");
        String summary = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, "summary");
        return summary;
    }

    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException{
        String result = "";
        if(parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
}
