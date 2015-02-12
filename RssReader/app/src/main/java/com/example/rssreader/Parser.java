package com.example.rssreader;

import android.sax.Element;
import android.sax.EndElementListener;
import android.sax.EndTextElementListener;
import android.sax.RootElement;
import android.text.Html;
import android.util.Log;
import android.util.Xml;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
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

    public ArrayList<Item> parse() {
        try {
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
            Log.i("Encoding", encoding);
            xmlReader.parse(new InputSource(new InputStreamReader(feedUrl.openConnection().getInputStream(), encoding)));
            return rssHandler.articles;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
        /*final Item currentItem = new Item();
        final ArrayList<Item> items = new ArrayList<Item>();

        RootElement root = new RootElement("html");
        Element rss = root.getChild("rss");
        Element channel = rss.getChild("channel");
        final Element item = channel.getChild("item");

        item.setEndElementListener(new EndElementListener() {
            public void end() {
                items.add(currentItem);
            }
        });

        item.getChild("title")
                .setEndTextElementListener(new EndTextElementListener() {
                    @Override
                    public void end(String body) {
                        currentItem.title = body;
                    }
                });

        item.getChild("link")
                .setEndTextElementListener(new EndTextElementListener() {
                    @Override
                    public void end(String body) {
                        currentItem.link = body;
                    }
                });

        try {
            Xml.parse(this.getInputStream(), Xml.Encoding.UTF_8,
                    );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return items;*/
    }

    public InputStream getInputStream(){
        try {
            return feedUrl.openConnection().getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public class RSSHandler extends DefaultHandler {
        private Item currentArticle;
        public ArrayList<Item> articles = new ArrayList<Item>();

        //private int numOfItems = 0;

        StringBuffer chars = new StringBuffer();
        public RSSHandler(){}

        public void startElement(String uri, String localName, String qName, Attributes attributes) {
            if(qName.equalsIgnoreCase("item")) {
                currentArticle = new Item();
            }
        }

        public void endElement(String uri, String localName, String qName) throws SAXException {
            if (currentArticle != null) {
                if (localName.equalsIgnoreCase("title")) {
                    currentArticle.title = chars.toString();
                } else if (localName.equalsIgnoreCase("link")) {
                    currentArticle.link = chars.toString();
                }
                if (localName.equalsIgnoreCase("item")) {
                    articles.add(currentArticle);
                    currentArticle = new Item();
                }
            }
        }

        public void characters(char ch[], int start, int length) {
            chars.append(new String(ch, start, length));
        }
    }
}
















