package com.example.rssreader;

import android.text.Html;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by Яна on 03.02.2015.
 */
public class Parser {
    URL feedUrl;

    public Parser(URL feedUrl) {
        this.feedUrl = feedUrl;
    }

    public ArrayList<Item> parse() throws IOException,
            SAXException, ParserConfigurationException {

        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        SAXParser saxParser = saxParserFactory.newSAXParser();
        XMLReader xmlReader = saxParser.getXMLReader();
        RSSHandler rssHandler = new RSSHandler();
        xmlReader.setContentHandler(rssHandler);
        String header = feedUrl.openConnection().getHeaderField("Content-Type");
        String encoding = "windows-1251";
        if (header != null && header.contains("charset=")) {
            Matcher mt = Pattern.compile("charset=([^\\s]+)").matcher(header);
            mt.find();
            encoding = mt.group(1);
        }
        xmlReader.parse(new InputSource(new InputStreamReader(feedUrl.openConnection().getInputStream(), encoding)));
        return rssHandler.articles;
    }

    public class RSSHandler extends DefaultHandler {
        private Item currentArticle;
        public ArrayList<Item> articles = new ArrayList<Item>();
        boolean isInItem = false;

        StringBuffer chars = new StringBuffer();

        public RSSHandler() {
        }

        public void startElement(String uri, String localName, String qName, Attributes attributes) {
            if (qName.equalsIgnoreCase("item")) {
                currentArticle = new Item();
                isInItem = true;
            }
        }

        public void endElement(String uri, String localName, String qName) throws SAXException {
            if (currentArticle != null) {
                if (localName.equalsIgnoreCase("title") && isInItem) {
                    currentArticle.title = chars.toString().trim();
                } else if (localName.equalsIgnoreCase("link") && isInItem) {
                    currentArticle.link = chars.toString().trim();
                } else if (localName.equalsIgnoreCase("description") && isInItem) {

                    currentArticle.description = Html.fromHtml(chars.toString().trim()).toString();
                } else if (localName.equalsIgnoreCase("pubDate") && isInItem) {
                    currentArticle.pubDate = chars.toString().trim();
                } else if (localName.equalsIgnoreCase("item")) {
                    articles.add(currentArticle);
                    isInItem = false;
                }
            }
            chars.setLength(0);
        }

        public void characters(char ch[], int start, int length) {
            chars.append(new String(ch, start, length));
        }
    }
}
















