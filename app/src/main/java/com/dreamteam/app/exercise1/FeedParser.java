package com.dreamteam.app.exercise1;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by flyingleafe on 21.10.14.
 */
public class FeedParser {
    DocumentBuilder builder;

    public FeedParser() {
        try {
            builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    public Feed parse(InputStream stream) {
        try {
            Document document = builder.parse(stream);
            String title = document.getElementsByTagName("title").item(0).getTextContent();
            String desc = document.getElementsByTagName("description").item(0).getTextContent();
            Feed feed = new Feed(title, desc);
            NodeList items = document.getElementsByTagName("item");
            for(int i = 0; i < items.getLength(); i++) {
                Element item = (Element) items.item(i);
                String itemTitle = item.getElementsByTagName("title").item(0).getTextContent();
                String itemDesc = item.getElementsByTagName("description").item(0).getTextContent();
                String itemLink = item.getElementsByTagName("link").item(0).getTextContent();
                feed.addItem(new FeedItem(itemTitle, itemDesc, itemLink));
            }
            return feed;
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
