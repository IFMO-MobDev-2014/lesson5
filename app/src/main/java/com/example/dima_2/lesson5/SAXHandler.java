package com.example.dima_2.lesson5;

import android.text.Html;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

/**
 * Created by Dima_2 on 16.01.2015.
 */
public class SAXHandler extends DefaultHandler {
    private RSSElement currentItem;
    private StringBuilder buffer;
    private String tTitle, tLink, tDescription, tPubDate;

    private final int LIMIT_ELEMENTS = 50;

    public String channelName;
    public ArrayList<RSSElement> rssItems = new ArrayList<RSSElement>();

    @Override
    public void startDocument() throws SAXException {
        rssItems = new ArrayList<RSSElement>();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        buffer = new StringBuilder();
        if (qName.equalsIgnoreCase("channel")) {
            //nothing to be done
        } else if (qName.equalsIgnoreCase("item")) {
            if (currentItem == null) {
                channelName = tTitle;
            }
            currentItem = new RSSElement();
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        buffer.append(ch, start, length);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (localName.equalsIgnoreCase("channel")) {
            //nothing to be done
        } else if (localName.equalsIgnoreCase("item")) {
            if (currentItem != null) {
                currentItem.title = tTitle;
                currentItem.description = Html.fromHtml(tDescription).toString();
                currentItem.pubDate = tPubDate;
                currentItem.link = tLink;
                if (rssItems.size() < LIMIT_ELEMENTS) {
                    rssItems.add(currentItem);
                }
                currentItem = new RSSElement();
            }
        }

        if (localName.equalsIgnoreCase("title")) {
            tTitle = buffer.toString();
        }
        else if (localName.equalsIgnoreCase("link")) {
            tLink = buffer.toString();
        }
        else if (localName.equalsIgnoreCase("description")) {
            tDescription = buffer.toString();
        }
        else if (localName.equalsIgnoreCase("pubDate")) {
            tPubDate = buffer.toString();
        }
    }
}