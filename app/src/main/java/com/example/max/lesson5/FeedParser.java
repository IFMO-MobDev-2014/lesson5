package com.example.max.lesson5;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FeedParser extends AsyncTask<String, Void, List<Feed>> {

    private final Context context;

    public FeedParser(Context context) {
        this.context = context;
    }

    @Override
    protected List<Feed> doInBackground(String... params) {
        try {
            URL url = new URL(params[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            InputStream is = connection.getInputStream();
            return parse(is);
        } catch (IOException e) {
            Log.i("ParseException", "IOException");
        } catch (XmlPullParserException e) {
            Log.i("ParseException", "XmlPullParserException");
        }
        return null;
    }

    public List<Feed> parse(InputStream is) throws IOException, XmlPullParserException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(is, null);
            parser.nextTag();
            return readFeed(parser);
        } finally {
            is.close();
        }
    }

    private List<Feed> readFeed(XmlPullParser parser) throws IOException, XmlPullParserException {
        List<Feed> entries = new ArrayList<Feed>();
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("entry")) {
                entries.add(readEntry(parser));
            } else {
                skip(parser);
            }
        }
        return entries;
    }

    private Feed readEntry(XmlPullParser parser) throws IOException, XmlPullParserException {
        String title = null;
        String author = null;
        String link = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("title")) {
                title = readTitle(parser);
            } else if (name.equals("author")) {
                author = readAuthor(parser);
            } else if (name.equals("link")) {
                link = readLink(parser);
            } else {
                skip(parser);
            }
        }
        return new Feed(link, title, author);
    }

    private String readTitle(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "title");
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, "title");
        return title;
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

    private String readAuthor(XmlPullParser parser) throws IOException, XmlPullParserException {
        String author = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("name")) {
                parser.require(XmlPullParser.START_TAG, null, "name");
                author = readText(parser);
                parser.require(XmlPullParser.END_TAG, null, "name");
            } else {
                skip(parser);
            }
        }
        return author;
    }

    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = null;
        if (parser.next() == XmlPullParser.TEXT) {
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

    @Override
    protected void onPostExecute(List<Feed> feeds) {
        super.onPostExecute(feeds);
        if (feeds == null) {
            feeds = new ArrayList<Feed>();
            feeds.add(new Feed("", "No Internet Connection. Please check your Internet Connection and try again", ""));
            MyActivity.view.setAdapter(new FeedAdapter(feeds, context));
        } else {
            MyActivity.view.setAdapter(new FeedAdapter(feeds, context));
        }
    }

}
