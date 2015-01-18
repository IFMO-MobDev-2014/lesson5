package ru.itmo.rss;

import android.util.Xml;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RSSParser {
    public static List<ItemRSS> parseFeed(String url) throws IOException, XmlPullParserException {
        return new RSSParser(url).parseFeed();
    }

    private final XmlPullParser xmlPullParser;

    private RSSParser(String url) throws IOException, XmlPullParserException {
        InputStream stream = new URL(url).openStream();
        xmlPullParser = Xml.newPullParser();
        xmlPullParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        xmlPullParser.setInput(stream, null);
        xmlPullParser.nextTag();
    }

    private List<ItemRSS> parseFeed() throws IOException, XmlPullParserException {
        List<ItemRSS> result = new ArrayList<>();
        xmlPullParser.require(XmlPullParser.START_TAG, null, "rss");
        while (true) {
            if (xmlPullParser.next() == XmlPullParser.START_TAG)
                break;
        }
        xmlPullParser.require(XmlPullParser.START_TAG, null, "channel");
        while (xmlPullParser.next() != XmlPullParser.END_TAG) {
            if (xmlPullParser.getEventType() != XmlPullParser.START_TAG)
                continue;
            String name = xmlPullParser.getName();
            if (name.equals("item")) {
                result.add(parseItem());
            } else {
                skipTag();
            }
        }
        return result;
    }

    private void skipTag() throws IOException, XmlPullParserException {
        int level = 1;
        while (level >= 1) {
            int cur = xmlPullParser.next();
            if (cur == XmlPullParser.START_TAG)
                level++;
            else if (cur == XmlPullParser.END_TAG)
                level--;
        }
    }


    private ItemRSS parseItem() throws IOException, XmlPullParserException {
        xmlPullParser.require(XmlPullParser.START_TAG, null, "item");
        String link = null;
        String title = null;
        String description = null;
        while (xmlPullParser.next() != XmlPullParser.END_TAG) {
            if (xmlPullParser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = xmlPullParser.getName();
            switch (name) {
                case "link":
                    xmlPullParser.require(XmlPullParser.START_TAG, null, "link");
                    link = readText(xmlPullParser);
                    xmlPullParser.require(XmlPullParser.END_TAG, null, "link");
                    break;
                case "title":
                    xmlPullParser.require(XmlPullParser.START_TAG, null, "title");
                    title = readText(xmlPullParser);
                    xmlPullParser.require(XmlPullParser.END_TAG, null, "title");
                    break;
                case "description":
                    xmlPullParser.require(XmlPullParser.START_TAG, null, "description");
                    description = readText(xmlPullParser);
                    xmlPullParser.require(XmlPullParser.END_TAG, null, "description");
                    break;
                default:
                    skipTag();
                    break;
            }
        }
        return new ItemRSS(link, title, description);
    }

    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }
}