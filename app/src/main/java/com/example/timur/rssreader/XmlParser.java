package com.example.timur.rssreader;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by timur on 21.10.14.
 */
public class XmlParser {
    XmlPullParser parser;

    private static final String ITEM = "item";
    private static final String LINK = "link";
    private static final String TITLE = "title";
    private static final String INFO = "description";

    public XmlParser(XmlPullParser parser) {
        this.parser = parser;
    }

    protected String getTextAndSwitch() throws IOException, XmlPullParserException {
        String result;
        parser.next();
        result = parser.getText();
        parser.next();
        return result;
    }

    public ArrayList<RSSitem> parseXML() throws IOException, XmlPullParserException {
        ArrayList<RSSitem> rssArray = new ArrayList<RSSitem>();
        String link = null, title = null, mainInfo = null, tag;
        while (parser.next() != XmlPullParser.END_DOCUMENT) {
            if (parser.getEventType() == XmlPullParser.START_TAG) {
                if (parser.getName().equals(ITEM)) {
                    while (parser.next() != XmlPullParser.END_TAG || !parser.getName().equals(ITEM)) {
                        if (parser.getEventType() != XmlPullParser.START_TAG) {
                            continue;
                        }
                        tag = parser.getName();
                        if (tag.equals(LINK)) {
                            link = getTextAndSwitch();
                        } else {
                            if (tag.equals(TITLE)) {
                                title = getTextAndSwitch();
                            } else {
                                if (tag.equals(INFO)) {
                                    while (parser.next() != XmlPullParser.END_TAG || !parser.getName().equals(INFO)) {
                                        if (parser.getEventType() == XmlPullParser.TEXT) {
                                            mainInfo += parser.getText();
                                        }
                                    }
                                }
                            }
                        }
                    }
                    rssArray.add(new RSSitem(link, title, mainInfo));
                }
            }
        }
        return rssArray;
    }
}

