package volhovm.com.rssreader;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.text.Html;
import android.util.Log;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import volhovm.com.rssreader.Feed.Item;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author volhovm
 *         Created on 10/21/14
 */

public class RSSLoader extends AsyncTaskLoader<Feed> {

    private final Feed feed;
    private final String ns = null;
    private FeedDAO feedDAO;

    public RSSLoader(Context context, Feed feed, FeedDAO feedDAO) {
        super(context);
        this.feed = feed;
        this.feedDAO = feedDAO;
    }

    @Override
    public Feed loadInBackground() {
        Feed newFeed = new Feed(feed.feedName, feed.feedUrl);
        Log.d("Loading", "RSS Load started");
        try {
            InputStream stream = new URL(newFeed.feedUrl).openStream();
            XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(stream, null);
            parser.nextTag();
            parser.require(XmlPullParser.START_TAG, null, "rss");
            while (parser.next() != XmlPullParser.END_DOCUMENT) {
                String name = parser.getName();
                if (name != null && name.equals("item")) {
                    newFeed.addItem(readItem(parser));
                }
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        feedDAO.deleteFeed(feed); //database can be closed in the other thread!
        feedDAO.putFeed(newFeed);
        return newFeed;
    }

    private Item readItem(XmlPullParser parser) throws IOException, XmlPullParserException {
        String title = null;
        URL link = null;
        String description = null;
        Date pubdate = null;
        URL enclosure = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("title")) {
                title = readTitle(parser);
            } else if (name.equals("enclosure")) {
                enclosure = readEnclosure(parser);
            } else if (name.equals("link")) {
                link = readLink(parser);
            } else if (name.equals("description")) {
                description = readDescription(parser);
            } else if (name.equals("pubDate")) {
                try {
                    pubdate = readPubDate(parser);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
                skip(parser);
            }
        }
        return new Item(title, description, link, enclosure, pubdate);
    }

    private Date readPubDate(XmlPullParser parser) throws ParseException, IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "pubDate");
        DateFormat formatter = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
        Date date = formatter.parse(readText(parser));
        parser.require(XmlPullParser.END_TAG, ns, "pubDate");
        return date;
    }

    private URL readEnclosure(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "enclosure");
        URL enclosure = null;
        String tag = parser.getName();
        String type = parser.getAttributeValue(null, "type");
        if (tag.equals("enclosure")) {
            if (type.equals("image/jpeg")){
                enclosure = new URL(parser.getAttributeValue(null, "url"));
                parser.nextTag();
            }
        }
        parser.require(XmlPullParser.END_TAG, ns, "enclosure");
        return enclosure;
    }

    // Processes title tags in the feed.
    private String readTitle(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "title");
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "title");
        return title;
    }

    // Processes link tags in the feed.
    private URL readLink(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "link");
        URL link = new URL(readText(parser));
        parser.require(XmlPullParser.END_TAG, ns, "link");
        return link;
    }

    // Processes summary tags in the feed.
    private String readDescription(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "description");
        String summary = Html.fromHtml(readText(parser)).toString();
        parser.require(XmlPullParser.END_TAG, ns, "description");
        return summary;
    }

    // For the tags title and summary, extracts their text values.
    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
}
