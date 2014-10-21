package com.example.vlad107.rssreader;

import android.text.Html;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vlad107 on 21.10.14
 */

public class MyHandler extends DefaultHandler {
    private List<Feed> feed = new ArrayList<Feed>(0);

    private boolean bAuthor = false;
    private boolean bTitle = false;
    private boolean bSummary = false;
    private boolean bName = false;
    private boolean bDate = false;
    private boolean bEntry = false;
    private String nAuthor = null;
    private String nTitle = null;
    private String nSummary = "";
    private String nLink = null;
    private String nDate = null;

    public List<Feed> getFeed() {
        return feed;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        if (qName.equalsIgnoreCase("author")) {
            bAuthor = true;
        } else if (qName.equalsIgnoreCase("title")) {
            bTitle = true;
        } else if (qName.equalsIgnoreCase("summary")) {
            bSummary = true;
        } else if (qName.equalsIgnoreCase("name")) {
            bName = true;
        } else if (qName.equalsIgnoreCase("link") && bEntry) {
            nLink = attributes.getValue("href");
        } else if (qName.equalsIgnoreCase("entry")) {
            bEntry = true;
        } else if (qName.equalsIgnoreCase("updated")) {
            bDate = true;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        if (qName.equalsIgnoreCase("entry")) {
            feed.add(new Feed(nAuthor, nTitle, Html.fromHtml(nSummary).toString(), nDate, nLink));
            bSummary = false;
            nSummary = "";
            bEntry = false;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if ((bAuthor) && (bName)) {
            nAuthor = new String(ch, start, length);
            bAuthor = false;
            bName = false;
        } else if (bTitle) {
            nTitle = new String(ch, start, length);
            bTitle = false;
        } else if (bSummary) {
            nSummary += new String(ch, start, length);
        } else if (bDate) {
            nDate = new String(ch, start, length);
            bDate = false;
        }
    }
}
