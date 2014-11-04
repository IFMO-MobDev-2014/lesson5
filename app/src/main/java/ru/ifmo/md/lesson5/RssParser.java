package ru.ifmo.md.lesson5;

import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lightning95 on 10/21/14.
 */

public class RssParser {
    private final XmlPullParser parser;

    private RssParser(String url) throws IOException, XmlPullParserException {
        InputStream stream = new URL(url).openStream();

        parser = Xml.newPullParser();
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        parser.setInput(stream, null);
        parser.nextTag();
    }

    public static List<RssItem> parseRss(String url) throws IOException, XmlPullParserException {
        return new RssParser(url).parseRss();
    }

    private List<RssItem> parseRss() throws IOException, XmlPullParserException {
        List<RssItem> items = new ArrayList<RssItem>();
        parser.require(XmlPullParser.START_TAG, null, "rss");
        while (true) {
            if (parser.next() == XmlPullParser.START_TAG) {
                break;
            }
        }
        parser.require(XmlPullParser.START_TAG, null, "channel");

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String name = parser.getName();
            if (name.equals("item")) {
                items.add(findItem());
            } else {
                Log.d("SKIP", "SKIPPING " + name);
                skipTag();
            }
        }

        return items;
    }

    private void skipTag() throws IOException, XmlPullParserException {
        for (int depth = 1; depth > 0; ) {
            int tag = parser.next();
            if (tag == XmlPullParser.START_TAG) {
                ++depth;
            } else if (tag == XmlPullParser.END_TAG) {
                --depth;
            }
        }
    }

    private RssItem findItem() throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "item");

        String link = null;
        String title = null;
        String description = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String tag = parser.getName();

            if (tag.equals("link")) {
                link = parseTag(tag);
            } else if (tag.equals("title")) {
                title = parseTag(tag);
            } else if (tag.equals("description")) {
                description = parseTag(tag);
            } else {
                Log.d("SKIP", "SKIPPING " + tag);
                skipTag();
            }
        }

        return new RssItem(link, title, description);
    }

    private String parseTag(String tag) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, tag);

        String text = "";

        if (parser.next() == XmlPullParser.TEXT) {
            text = parser.getText().replace("<br>", "\n").replace("&quot;", "\"");
            parser.nextTag();
        }

        parser.require(XmlPullParser.END_TAG, null, tag);

        return text;
    }
}
