package com.example.rssreader;


import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Амир on 04.11.2014.
 */
public class XMLParser {
    public static List<ContentItem> parse(InputStream input) throws Exception{
        List<ContentItem> list= new ArrayList<ContentItem>();
        try {
            XmlPullParserFactory xmlFactoryObject = XmlPullParserFactory.newInstance();
            XmlPullParser parser = xmlFactoryObject.newPullParser();
            parser.setInput(input, null);
            String title = "";
            String link = "";
            String pubDate = "";
            String description = "";
            String lastTag = "";
            int eventType = parser.getEventType();
            while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    String name = parser.getName();
                    if (name.equals("link")) {
                        link = parser.nextText();
                    }
                    if (name.equals("title")) {
                        title = parser.nextText();
                    }
                    if (name.equals("pubDate")) {
                        pubDate = parser.nextText();
                    }
                    if (name.equals("description")) {
                        description = parser.nextText();
                    }
                }
                if (eventType == XmlPullParser.END_TAG) {
                    if (parser.getName().equals("item")) {
                        list.add(new ContentItem(link, title, pubDate, description));
                    }
                }

                eventType = parser.next();
            }
            return list;
        } catch (XmlPullParserException e) {
            throw new Exception();
        } catch (IOException e) {
            throw new Exception();
        }
    }
}
