package parser;

import android.os.AsyncTask;
import android.util.Log;
import android.util.Xml;

import com.daria.lesson5.activities.ReaderActivity;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.PriorityQueue;

import elements.Article;
import elements.Feed;

/**
 * Created by daria on 29.10.14.
 */
public class StackOverflowFeedParser extends AsyncTask<String, Void, Feed> {
    private static final String FEED_TITLE = "Stack Overflow";
    ReaderActivity activity;
    private static final String ns = null;

    public StackOverflowFeedParser(ReaderActivity activity) {
        this.activity = activity;
    }

    @Override
    protected Feed doInBackground(String... urls) {
        try {
            URL url = new URL(urls[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            int responseCode = connection.getResponseCode();
            Log.d("Debug", "Response code is: " + responseCode);
            InputStream in = connection.getInputStream();
            return parse(in);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public Feed parse(InputStream in) throws IOException, XmlPullParserException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readFeed(parser);
        } finally {
            in.close();
        }
    }

    public Feed readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        Feed feed = new Feed();
        ArrayList<Article> articles = new ArrayList<Article>();
        parser.require(XmlPullParser.START_TAG, ns, "feed");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String name = parser.getName();

            if (name.equals("entry")) {
                articles.add(readEntry(parser));
            }
            else {
                skip(parser);
            }
        }
        feed.setArticles(articles);
        feed.setTitle(FEED_TITLE);
        return feed;
    }

    private Article readEntry(XmlPullParser parser) throws XmlPullParserException, IOException {
        String title = null;
        String url = null;
        String author = null;
        while(parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("title")) {
                title = readTitle(parser);
            }
            else if (name.equals("link")) {
                url = readUrl(parser);
            }
            else if (name.equals("author")) {
                author = findName(parser);
            }
            else {
                skip(parser);
            }
        }
        return new Article(title, url, author);
    }

    private String readTitle(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "title");
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "title");
        return title;
    }

    private String readUrl(XmlPullParser parser) throws XmlPullParserException, IOException {
        String url = "";
        parser.require(XmlPullParser.START_TAG, ns, "link");
        String name = parser.getName();
        String relType = parser.getAttributeValue(null, "rel");
        if (name.equals("link")) {
            if (relType.equals("alternate")) {
                url = parser.getAttributeValue(null, "href");
                parser.nextTag();
            }
        }
        parser.require(XmlPullParser.END_TAG, ns, "link");
        return url;
    }

    private String findName(XmlPullParser parser) throws XmlPullParserException, IOException {
        String author = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("name")) {
                author = readAuthor(parser);
            }
            else  {
                skip(parser);
            }
        }
        return author;
    }

    private String readAuthor(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "name");
        String author = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "name");
        return author;
    }

    private String readText(XmlPullParser parser) throws XmlPullParserException, IOException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    public void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
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

    @Override
    protected void onPostExecute(Feed feed) {
        super.onPostExecute(feed);
        activity.onParsingFinished(feed);
    }

}
