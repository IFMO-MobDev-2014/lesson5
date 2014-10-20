package ru.ifmo.md.lesson5.rssreader.parser;

import android.util.Pair;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParserFactory;

import ru.ifmo.md.lesson5.rssreader.RSSChannel;
import ru.ifmo.md.lesson5.rssreader.RSSItem;

/**
 * Created by Nikita Yaschenko on 21.10.14.
 */
public class RSSReader {

    private static final String ns = null;

    public Pair<RSSChannel, List<RSSItem>> parse(URL url)
            throws IOException, XmlPullParserException, ParseException {
        return parse(url.openStream());
    }

    public Pair<RSSChannel, List<RSSItem>> parse(InputStream stream)
            throws IOException, XmlPullParserException, ParseException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(stream, null);
            parser.nextTag();
            return readFeed(parser);
        } finally {
            stream.close();
        }
    }

    private Pair<RSSChannel, List<RSSItem>> readFeed(XmlPullParser parser) throws IOException, XmlPullParserException, ParseException {
        List<RSSItem> items = new ArrayList<RSSItem>();
        RSSChannel channel = new RSSChannel();

        parser.require(XmlPullParser.START_TAG, ns, "channel");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("entry")) {
                items.add(readEntry(parser));
            } else if (name.equals("title")) {
                channel.setTitle(readText(parser));
            } else if (name.equals("link")) {
                channel.setUrl(readText(parser));
            } else if (name.equals("description")) {
                channel.setDescription(readText(parser));
            } else {
                skip(parser);
            }
        }
        return Pair.create(channel, items);
    }

    private RSSItem readEntry(XmlPullParser parser)
            throws XmlPullParserException, IOException, ParseException {
        parser.require(XmlPullParser.START_TAG, ns, "entry");

        RSSItem item = new RSSItem();

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("title")) {
                item.setTitle(readBasicTag(parser, "title"));
            } else if (name.equals("link")) {
                item.setUrl(readBasicTag(parser, "link"));
            } else if (name.equals("pubDate")) {
                item.setDate(readBasicTag(parser, "pubDate"));
            } else if (name.equals("description")) {
                item.setDescription(readBasicTag(parser, "description"));
            } else {
                skip(parser);
            }
        }
        return item;
    }

    private String readBasicTag(XmlPullParser parser, String tag)
            throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, tag);
        String result = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, tag);
        return result;
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

}
