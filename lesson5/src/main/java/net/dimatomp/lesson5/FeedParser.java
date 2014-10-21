package net.dimatomp.lesson5;

import android.content.ContentValues;
import android.text.Html;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import static net.dimatomp.lesson5.FeedColumns.ENTRY_DATE;
import static net.dimatomp.lesson5.FeedColumns.ENTRY_DESCRIPTION;
import static net.dimatomp.lesson5.FeedColumns.ENTRY_TITLE;
import static net.dimatomp.lesson5.FeedColumns.ENTRY_URL;
import static net.dimatomp.lesson5.FeedColumns.FEED_DESCRIPTION;
import static net.dimatomp.lesson5.FeedColumns.FEED_TITLE;
import static net.dimatomp.lesson5.FeedColumns.FEED_WEBSITE;

/**
 * Created by dimatomp on 20.10.14.
 */
public class FeedParser extends DefaultHandler {
    private static final SimpleDateFormat format = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z", Locale.US);
    StringBuilder builder = new StringBuilder();
    String curInfoField;
    ContentValues feedInfo = new ContentValues();
    ContentValues itemInfo;
    private ParserCallbacks callbacks;

    public FeedParser(ParserCallbacks callbacks) {
        this.callbacks = callbacks;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        switch (qName) {
            case "rss":
                // TODO support RSS 1.0 and others
                break;
            case "item":
                itemInfo = new ContentValues();
                break;
            case "title":
                curInfoField = (itemInfo == null) ? FEED_TITLE : ENTRY_TITLE;
                break;
            case "link":
                curInfoField = (itemInfo == null) ? FEED_WEBSITE : ENTRY_URL;
                break;
            case "description":
                curInfoField = (itemInfo == null) ? FEED_DESCRIPTION : ENTRY_DESCRIPTION;
                break;
            case "pubDate":
                curInfoField = ENTRY_DATE;
                break;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (curInfoField != null) {
            String repr = Html.fromHtml(builder.toString()).toString();
            if (itemInfo == null)
                feedInfo.put(curInfoField, builder.toString());
            else {
                if (curInfoField.equals(ENTRY_DATE))
                    try {
                        repr = Long.toString(format.parse(repr).getTime());
                    } catch (ParseException e) {
                        throw new SAXException(e);
                    }
                itemInfo.put(curInfoField, repr);
            }
        }
        builder = new StringBuilder();
        curInfoField = null;
        if (qName.equals("item")) {
            callbacks.insertEntryIfNew(itemInfo);
            itemInfo = null;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (curInfoField != null) {
            for (int i = start; i < start + length; i++)
                builder.append(ch[i]);
        }
    }

    @Override
    public void endDocument() throws SAXException {
        callbacks.updateFeedInfo(feedInfo);
    }

    public interface ParserCallbacks {
        void updateFeedInfo(ContentValues values);

        boolean insertEntryIfNew(ContentValues values);
    }
}
