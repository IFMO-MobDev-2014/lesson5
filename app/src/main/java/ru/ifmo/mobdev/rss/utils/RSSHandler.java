package ru.ifmo.mobdev.rss.utils;

import android.util.Log;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * @author sugakandrey
 */
public class RSSHandler extends DefaultHandler {

    private RssArticle currentArticle;
    private ArrayList<RssArticle> articles = new ArrayList<RssArticle>();

    private int numberOfArticles = 0;

    private static final int ARTICLES_LIMIT = 15;


    StringBuffer chars = new StringBuffer();


    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        chars = new StringBuffer();
        if (qName.equalsIgnoreCase("item"))
            currentArticle = new RssArticle();
        if (currentArticle != null)
            if (qName.equalsIgnoreCase("media:thumbnail")) {
                currentArticle.setThumbnailUrl(attributes.getValue("url"));
            }
    }


    public void endElement(String uri, String localName, String qName) throws SAXException {

        if (currentArticle != null) {
            if (localName.equalsIgnoreCase("title")) {
                currentArticle.setTitle(chars.toString());
            } else if (localName.equalsIgnoreCase("description")) {
                currentArticle.setDescription(chars.toString());
            } else if (localName.equalsIgnoreCase("pubDate")) {
                currentArticle.setDate(chars.toString());
            } else if (localName.equalsIgnoreCase("link")) {
                currentArticle.setUrl(chars.toString());
            }
            if (localName.equalsIgnoreCase("item")) {
                articles.add(currentArticle);
                currentArticle = new RssArticle();
                numberOfArticles++;
                if (numberOfArticles >= ARTICLES_LIMIT) {
                    throw new SAXException();
                }
            }
        }
    }


    public void characters(char ch[], int start, int length) {
        chars.append(new String(ch, start, length));
    }


    public ArrayList<RssArticle> getArticles(String feedUrl) {
        URL url;
        try {
            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser parser = spf.newSAXParser();
            XMLReader reader = parser.getXMLReader();
            url = new URL(feedUrl);
            reader.setContentHandler(this);
            InputSource source = new InputSource(url.openStream());
            reader.parse(source);
        } catch (IOException e) {
            Log.e("IO Error", e.toString());
        } catch (SAXException e) {
            Log.e("SAX Handler error", e.toString());
        } catch (ParserConfigurationException e) {
            Log.e("Parsing error", e.toString());
        }
        return articles;
    }
}