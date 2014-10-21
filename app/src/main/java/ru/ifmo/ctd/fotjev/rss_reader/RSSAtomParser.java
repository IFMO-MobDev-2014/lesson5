package ru.ifmo.ctd.fotjev.rss_reader;

import android.text.Html;
import android.text.Spanned;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by fotyev on 19-Oct-14.
 */
public class RSSAtomParser {
    public final static String TITLE = "title";
    public final static String LINK = "link";
    public final static String DESC = "description";
    public final static String DATE = "pubDate";


    private XmlPullParserFactory xmlFactoryObject;
    private XmlPullParser parser;

    private enum FeedType {
        UNKNOWN,
        RSS,
        ATOM
    }

    FeedType type = FeedType.UNKNOWN;

    public RSSAtomParser(InputStream stream) throws Exception {
        xmlFactoryObject = XmlPullParserFactory.newInstance();
        parser = xmlFactoryObject.newPullParser();
        parser.setInput(stream, null);

    }

    static public Spanned htmlFromString(String s) {
        return Html.fromHtml(s);
    }

    private void parseTag(ArrayList<HashMap<String, Spanned>> feed, String tag, String text) {
        switch (tag) {
            case "title": // add to last entry
                feed.get(feed.size() - 1).put(TITLE, htmlFromString(text));
                break;
            case "link":
                if (type != FeedType.RSS) { // collision with Atom's link
                    break;
                }
            case "id":
                feed.get(feed.size() - 1).put(LINK, htmlFromString(text));
                break;
            case "pubDate":
            case "published":
                feed.get(feed.size() - 1).put(DATE, htmlFromString(text));
                break;
            case "description":
            case "summary":
                feed.get(feed.size() - 1).put(DESC, htmlFromString(text));
                break;
        }
    }

    public ArrayList<HashMap<String, Spanned>> parse() throws Exception {
        ArrayList<HashMap<String, Spanned>> feed = new ArrayList<HashMap<String, Spanned>>();
        int event = parser.getEventType();
        String text = "";
        while (event != XmlPullParser.END_DOCUMENT) {
            String tag = parser.getName();
            switch (event) {
                case XmlPullParser.START_TAG:
                    if (type == FeedType.UNKNOWN) {
                        if (tag.equals("rss")) {
                            type = FeedType.RSS;
                        } else if (tag.equals("feed")) {
                            type = FeedType.ATOM;
                        }
                    } else if (tag.equals("item") || tag.equals("entry")) {
                        feed.add(new HashMap<String, Spanned>()); // new entry
                    }
                    break;
                case XmlPullParser.TEXT:
                    if (type != FeedType.UNKNOWN) {
                        text = parser.getText();
                    }
                    break;
                case XmlPullParser.END_TAG:
                    if (type != FeedType.UNKNOWN && !feed.isEmpty()) {
                        parseTag(feed, tag, text);
                    }
                    break;
            }
            event = parser.next();
        }
        return feed;
    }
}
