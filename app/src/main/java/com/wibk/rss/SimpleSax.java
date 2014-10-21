package com.wibk.rss;

import android.util.Log;

import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class SimpleSax extends DefaultHandler {
    private static final DateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", Locale.US);
    public static final String LOG_TAG = SimpleSax.class.getSimpleName();

    public static List<RssItem> parseInputSource(InputSource is) throws SAXException {
        try {
            SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
            SAXParser saxParser = saxParserFactory.newSAXParser();
            SimpleSax handler = new SimpleSax();
            saxParser.parse(is, handler);
            return handler.getRssItemList();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "Error occurred due to invalid parser configuration");
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "Error occurred during parsing");
        }
        return new ArrayList<RssItem>();
    }

    public List<RssItem> getRssItemList() {
        return rssItemList;
    }

    private List<RssItem> rssItemList = new ArrayList<RssItem>();

    private boolean bTitle = false;
    private boolean bDescription = false;
    private boolean bDate = false;
    private boolean bItem = false;
    private boolean bLink = false;

    private RssItem rssItem;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes)
            throws SAXException {
        if (qName.equalsIgnoreCase("item")) {
            rssItem = new RssItem();
            bItem = true;
        } else {
            if (bItem) {
                if (qName.equalsIgnoreCase("title")) {
                    bTitle = true;
                } else if (qName.equalsIgnoreCase("description")) {
                    bDescription = true;
                } else if (qName.equalsIgnoreCase("pubDate")) {
                    bDate = true;
                } else if (qName.equalsIgnoreCase("link")) {
                    bLink = true;
                }
            }
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equalsIgnoreCase("item") && bItem) {
            rssItemList.add(rssItem);
            bItem = false;
        }
    }

    @Override
    public void characters(char ch[], int start, int length) throws SAXException {
        if (bTitle) {
            rssItem.setTitle(new String(ch, start, length));
            bTitle = false;
        } else if (bDescription) {
            rssItem.setDescription(new String(ch, start, length));
            bDescription = false;
        } else if (bDate) {
            try {
                rssItem.setDate(dateFormat.parse(new String(ch, start, length)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            bDate = false;
        } else if (bLink) {
            rssItem.setLink(new String(ch, start, length));
            bLink = false;
        }
    }
}