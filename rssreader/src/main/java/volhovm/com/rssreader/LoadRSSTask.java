package volhovm.com.rssreader;

import android.os.AsyncTask;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

/**
 * @author volhovm
 *         Created on 10/21/14
 */

public class LoadRSSTask extends AsyncTask<URL, Void, ArrayList<LoadRSSTask.Item>>{
    public class Item {
        String title;
        URL link;
        String description;
        URL enclosure;

        public Item(String title, URL link, String summary, URL enclosure) {
            this.title = title;
            this.link = link;
            this.description = summary;
            this.enclosure = enclosure;
        }
    }
    private String ns = null;
    private MainActivity activity;

    public LoadRSSTask(MainActivity activity) {
        this.activity = activity;
    }

    @Override
    protected ArrayList<Item> doInBackground(URL... urls) {
        ArrayList<Item> entries = new ArrayList<Item>();
        try {
            InputStream stream = urls[0].openStream();
            XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(stream, null);
            parser.nextTag();
            parser.require(XmlPullParser.START_TAG, null, "rss");
            while (parser.next() != XmlPullParser.END_DOCUMENT) {
                String name = parser.getName();
                if (name != null && name.equals("item")) {
                    entries.add(readItem(parser));
                }
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return entries;
    }

    private Item readItem(XmlPullParser parser) throws IOException, XmlPullParserException {
        String title = null;
        URL link = null;
        String description = null;
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
            } else {
                skip(parser);
            }
        }
        return new Item(title, link, description, enclosure);
    }

    private URL readEnclosure(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "enclosure");
        URL enclosure = null;
        String tag = parser.getName();
        String type = parser.getAttributeValue(null, "type");
        if (tag.equals("link")) {
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
        String summary = readText(parser);
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

    @Override
    protected void onPostExecute(ArrayList<Item> items) {
        activity.onRSSUpdate(items);
//        super.onPostExecute(items);
    }
}
