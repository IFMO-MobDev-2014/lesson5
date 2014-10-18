package com.pinguinson.reader.parsers;

import android.os.AsyncTask;
import android.util.Log;
import android.util.Xml;

import com.pinguinson.reader.activities.ReaderActivity;
import com.pinguinson.reader.models.Article;
import com.pinguinson.reader.models.Feed;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by pinguinson on 17.10.2014.
 */
public class StackOverflowFeedParser extends AsyncTask<String, Void, Feed> {
    public static final String FEED_TITLE = "Stack Overflow: android";
    ReaderActivity activity;

    public StackOverflowFeedParser(ReaderActivity activity) {
        this.activity = activity;
    }

    @Override
    protected Feed doInBackground(String... strings) {
        try {
            String link = strings[0];
            URL url = new URL(link);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            int responseCode = connection.getResponseCode();
            Log.d("DEBUG", "Response code is: " + responseCode);
            InputStream is = connection.getInputStream();
            return parse(is);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Feed parse(InputStream is) throws IOException, XmlPullParserException {
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

    public Feed readFeed(XmlPullParser parser) throws IOException, XmlPullParserException {
        ArrayList<Article> articles = new ArrayList<Article>();
        Feed feed = new Feed();

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("entry")) {
                articles.add(readArticle(parser));
            } else {
                skip(parser);
            }

        }
        feed.setArticles(articles);
        feed.setTitle(FEED_TITLE);
        return feed;
    }

    private void skip(XmlPullParser parser) throws IOException, XmlPullParserException {
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

    private Article readArticle(XmlPullParser parser) throws IOException, XmlPullParserException {
        String title = null;
        String author = null;
        String url = null;

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("title")) {
                title = readTitle(parser);
            } else if (name.equals("author")) {
                author = findName(parser);
            } else if (name.equals("link")) {
                url = readLink(parser);
            } else {
                skip(parser);
            }
        }
        return new Article(title, url, author);
    }

    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = null;
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    private String readTitle(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "title");
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, "title");
        return title;
    }

    private String findName(XmlPullParser parser) throws IOException, XmlPullParserException {
        String author = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("name")) {
                author = readAuthor(parser);
            } else {
                skip(parser);
            }

        }
        return author;
    }

    private String readAuthor(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "name");
        String author = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, "name");
        return author;
    }

    private String readLink(XmlPullParser parser) throws IOException, XmlPullParserException {
        String link = "";
        parser.require(XmlPullParser.START_TAG, null, "link");
        String tag = parser.getName();
        String relType = parser.getAttributeValue(null, "rel");
        if (tag.equals("link")) {
            if (relType.equals("alternate")) {
                link = parser.getAttributeValue(null, "href");
                parser.nextTag();
            }
        }
        parser.require(XmlPullParser.END_TAG, null, "link");
        return link;
    }

    @Override
    protected void onPostExecute(Feed feed) {
        super.onPostExecute(feed);
        activity.onParsingFinished(feed);
    }
}
