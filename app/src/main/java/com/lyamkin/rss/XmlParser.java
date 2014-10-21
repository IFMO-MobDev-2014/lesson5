package com.lyamkin.rss;

import android.os.AsyncTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParserFactory;

public class XmlParser extends DefaultHandler {
    private final List<Article> articleList;
    private String title;
    private String link;
    private StringBuilder sb = new StringBuilder();
    private FeedParsed feedParsed;

    public XmlParser(FeedParsed feedParsedCallback, String url) {
        articleList = new ArrayList<Article>();
        this.feedParsed = feedParsedCallback;
        new GetFeedTask().execute(url);
    }

    class GetFeedTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            try {
                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet(URI.create(strings[0]));
                HttpResponse response = client.execute(request);
                HttpEntity content = response.getEntity();

                Reader reader = new InputStreamReader(content.getContent(), "UTF-8");

                XMLReader xmlReader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
                xmlReader.setContentHandler(XmlParser.this);
                xmlReader.parse(new InputSource(reader));
            } catch (Exception e) {
                return null;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            feedParsed.onFeedParsed(articleList);
        }
    }

    @Override
    public void endDocument() throws SAXException {
        super.endDocument();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        sb.setLength(0);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (localName.equals("item")) {
            articleList.add(new Article(title, link));
        } else if (localName.equalsIgnoreCase("title")) {
            title = sb.toString();
        } else if (localName.equalsIgnoreCase("link")) {
            link = sb.toString();
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        String string = new String(ch, start, length);
        sb.append(string);
    }
}


