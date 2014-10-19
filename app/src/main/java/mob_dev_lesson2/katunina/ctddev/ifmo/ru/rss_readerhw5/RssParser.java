package mob_dev_lesson2.katunina.ctddev.ifmo.ru.rss_readerhw5;

import android.os.AsyncTask;
import android.text.Html;

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
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by Евгения on 19.10.2014.
 */
public class RssParser extends DefaultHandler {
    private List<FeedItem> feedItems = null;
    private String currentTitle, currentDescription, currentLink;
    private StringBuilder sb = new StringBuilder();
    private boolean inItem = false;
    private FeedParsedCallback feedParsedCallback;

    interface FeedParsedCallback {
         void onFeedParsed(List<FeedItem> feedItems);
    }

    class GetFeedTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            try {
                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet(URI.create(strings[0]));
                HttpResponse response = client.execute(request);
                HttpEntity e = response.getEntity();
                Reader r = new InputStreamReader(e.getContent(), "Windows-1251");
                SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
                SAXParser newSAXParser = saxParserFactory.newSAXParser();
                XMLReader xmlReader = newSAXParser.getXMLReader();
                xmlReader.setContentHandler(RssParser.this);
                xmlReader.parse(new InputSource(r));
            } catch (IOException | SAXException | ParserConfigurationException ignored) {
                return null;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            feedParsedCallback.onFeedParsed(feedItems);
        }
    }

    public RssParser(FeedParsedCallback feedParsedCallback, String url) {
        this.feedParsedCallback = feedParsedCallback;
        new GetFeedTask().execute(url);
    }

    @Override
    public void startDocument() throws SAXException {
        feedItems = new ArrayList<FeedItem>();
    }

    @Override
    public void endDocument() throws SAXException {
        super.endDocument();
    }

    @Override
    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {
        sb.setLength(0);
        if (localName.equals("item")) {
            inItem = true;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        if (localName.equals("item")) {
            feedItems.add(new FeedItem(currentTitle, currentDescription, currentLink));
            inItem = false;
        } else if (localName.equalsIgnoreCase("title")) {
            currentTitle = sb.toString();
        } else if (localName.equalsIgnoreCase("description")) {
            currentDescription = sb.toString();
        } else if (localName.equalsIgnoreCase("link")) {
            currentLink = sb.toString();
        }
    }

    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {
        String tempString = new String(ch, start, length);
        sb.append(tempString);
    }
}

class FeedItem {
    String title, description, link;

    public FeedItem(String title, String description, String link) {
        this.title = title;
        this.description = description;
        this.link = link;
    }

    @Override
    public String toString() {
        return title;
    }
}