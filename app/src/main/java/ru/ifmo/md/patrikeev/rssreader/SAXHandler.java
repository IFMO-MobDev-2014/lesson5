package ru.ifmo.md.patrikeev.rssreader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.Html;
import android.util.Log;
import android.view.View;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by sergey on 20.10.14.
 */
public class SAXHandler extends DefaultHandler {

    private final int LIMIT_ITEMS = 20;
    private RSSItem currentItem = new RSSItem();
    private StringBuilder buffer = new StringBuilder();
    public ArrayList<RSSItem> rssItems;

    @Override
    public void startDocument() throws SAXException {
        rssItems = new ArrayList<RSSItem>();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        buffer = new StringBuilder();
        if (qName.equalsIgnoreCase("item"))
            currentItem = new RSSItem();
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (currentItem != null) {
            if (buffer != null) {
                if (localName.equalsIgnoreCase("title")) {
                    currentItem.setTitle(buffer.toString());
                } else if (localName.equalsIgnoreCase("description")) {
                    CharSequence beautyHTML = Html.fromHtml(buffer.toString());
                    currentItem.setDescription(beautyHTML.toString());
                } else if (localName.equalsIgnoreCase("pubDate")) {
                    currentItem.setPubDate(buffer.toString());
                } else if (localName.equalsIgnoreCase("link")) {
                    currentItem.setLink(buffer.toString());
                }
            }
            if (localName.equalsIgnoreCase("item")) {
                if (rssItems.size() < LIMIT_ITEMS) {
                    rssItems.add(currentItem);
                }
                currentItem = new RSSItem();
            }
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        buffer.append(ch, start, length);
    }

}