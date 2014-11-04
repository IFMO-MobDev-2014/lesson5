package ru.eugene.listviewpractice2;

import android.util.Log;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eugene on 11/4/14.
 */
public class HandlerRSS extends DefaultHandler {
    String encodingXML = null;
    List<ItemObject> itemsData = null;
    ItemObject itemObject = null;
    Boolean item = false;
    Boolean title = false;
    Boolean description = false;
    Boolean link = false;
    Boolean pubDate = false;
    String LOG = "HandlerRSS";
    String textTitle = "";
    String textDescription = "";
    String textLink = "";
    String textPubDate = "";


    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (item) {
            String temp = new String(ch, start, length);
            if (title) {
                textTitle += temp;
            } else if (link) {
                textLink += temp;
            } else if (description) {
                textDescription += temp;
            } else if (pubDate) {
                textPubDate += temp;
            }
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (item) {
            if (qName.equalsIgnoreCase("item")) {
                itemsData.add(itemObject);
                item = false;
            } else if (qName.equalsIgnoreCase("title") && title) {
                title = false;
                itemObject.setTitle(textTitle);
                textTitle = "";
            } else if (qName.equalsIgnoreCase("description") && description) {
                description = false;
                itemObject.setDescription(textDescription);
                textDescription = "";
            } else if (qName.equalsIgnoreCase("link") && link) {
                link = false;
                itemObject.setLink(textLink);
                textLink = "";
            } else if (qName.equalsIgnoreCase("pubDate") && pubDate) {
                pubDate = false;
                itemObject.setPubDate(textPubDate);
                textPubDate = "";
            }
        }
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (item) {
            if (qName.equalsIgnoreCase("title")) {
                title = true;
            } else if (qName.equalsIgnoreCase("description")) {
                description = true;
            } else if (qName.equalsIgnoreCase("link")) {
                link = true;
            } else if (qName.equalsIgnoreCase("pubDate")) {
                pubDate = true;
            }
        }

        if (qName.equalsIgnoreCase("item")) {
            itemObject = new ItemObject();
            if (itemsData == null)
                itemsData = new ArrayList<ItemObject>();
            item = true;
        }
    }

    List<ItemObject> getItemsData() {
        return itemsData;
    }
}
