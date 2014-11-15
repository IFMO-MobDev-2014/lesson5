package default_.lesson5.parsers;

import android.os.AsyncTask;
import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import default_.lesson5.RSSActivity;
import default_.lesson5.auxillary.*;

/**
 * Created by default on 21.10.14.
 */
public class FeedParser extends AsyncTask<String, Void, Feed> {

    private static final String ns = null;
    private static final String FEED_TITLE = "BBC News";
    RSSActivity activity;

    String title = null;
    String description = null;
    String link = null;

    public FeedParser(RSSActivity activity) {
        this.activity = activity;
    }

    @Override
    protected Feed doInBackground(String... strings) {
        try {
            String link = strings[0];
            URL url = new URL(link);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            int responseCode = connection.getResponseCode();
            Log.d("Response code:", Integer.toString(responseCode));
            InputStream is = connection.getInputStream();
            //android.os.Debug.waitForDebugger();
            return process(is);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Feed process(InputStream is) throws IOException, XmlPullParserException {
        try {

            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(is, null);
            parser.nextTag();
            return readFeed(parser);
        } finally {
            is.close();
        }
    }

    public Feed readFeed(XmlPullParser parser) throws IOException, XmlPullParserException {
        ArrayList<Entry> entries = new ArrayList<Entry>();
        Feed feed = new Feed();
        int event;
        event = parser.getEventType();
        String text = null;
        while (event != XmlPullParser.END_DOCUMENT) {
            String name = parser.getName();
            switch (event) {
                case XmlPullParser.START_TAG:
                    break;
                case XmlPullParser.TEXT:
                    text = parser.getText();
                    break;
                case XmlPullParser.END_TAG:
                    if (name.equals("title")) {
                        title = text;
                    } else if (name.equals("link")) {
                        link = text;
                        entries.add(new Entry(link, description, title));
                    } else if (name.equals("description")) {
                        description = text;
                    } else {
                    }
                    break;
            }
            event = parser.next();
        }
        for (int i = 0; i < 2; i++) {
            entries.remove(0);
        }
        feed.setContent(entries);
        feed.setTitle(FEED_TITLE);
        return feed;
    }

    protected void onPostExecute(Feed feed) {
        super.onPostExecute(feed);
        activity.onFinished(feed);
    }
}
