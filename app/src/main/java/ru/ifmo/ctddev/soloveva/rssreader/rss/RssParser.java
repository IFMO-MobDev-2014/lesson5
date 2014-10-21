package ru.ifmo.ctddev.soloveva.rssreader.rss;

import android.net.http.AndroidHttpClient;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by maria on 21.10.14.
 */
public class RssParser implements Closeable {
    private final AndroidHttpClient client;
    private final SAXParser parser;

    public RssParser() throws ParserConfigurationException, SAXException {
        this.parser = SAXParserFactory.newInstance().newSAXParser();
        this.client = AndroidHttpClient.newInstance("Android");
    }

    public List<Entry> fetchEntries(String url) throws IOException, SAXException {
        HttpGet request = new HttpGet(url);
        HttpResponse response = client.execute(request);
        final List<Entry> entries = new ArrayList<>();
        parser.parse(response.getEntity().getContent(), new DefaultHandler() {
            private StringBuilder title = new StringBuilder();
            private StringBuilder html = new StringBuilder();
            private String currentTag;
            private boolean inside = false;

            @Override
            public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                if ("entry".equalsIgnoreCase(qName)) {
                    inside = true;
                }
                if (inside) {
                    currentTag = qName.toLowerCase();
                }
            }

            @Override
            public void endElement(String uri, String localName, String qName) throws SAXException {
                if ("entry".equalsIgnoreCase(qName)) {
                    entries.add(new Entry(title.toString().trim(), html.toString().trim()));
                    title = new StringBuilder();
                    html = new StringBuilder();
                    inside = false;
                }
            }

            @Override
            public void characters(char[] ch, int start, int length) throws SAXException {
                if (inside && currentTag != null) {
                    switch (currentTag) {
                        case "title":
                            title.append(ch, start, length);
                            break;
                        case "summary":
                            html.append(ch, start, length);
                            break;
                    }
                }
            }
        });
        return entries;
    }

    @Override
    public void close() throws IOException {
        client.close();
    }
}
