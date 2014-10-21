package com.pokrasko.lesson5;

import android.os.AsyncTask;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by pokrasko on 21.10.14.
 */
public class RSSLoader extends AsyncTask<String, Void, Feed> {
    private MainActivity activity;
    private String rssTitle;
    private String rssDescription;

    private String errorMessage = "";

    public RSSLoader(MainActivity activity) {
        this.activity = activity;
    }

    @Override
    protected Feed doInBackground(String... urlStrs) {
        String urlStr = urlStrs[0];
        Feed feed = null;

        try {
            URL url = new URL(urlStr);
            Document response = DocumentBuilderFactory.newInstance().newDocumentBuilder()
                    .parse((InputStream) url.getContent());
            response.getDocumentElement().normalize();

            Element channel = (Element) response.getElementsByTagName("channel").item(0);
            rssTitle = channel.getElementsByTagName("title").item(0).getTextContent();
            rssDescription = channel.getElementsByTagName("description").item(0).getTextContent();
            NodeList list = channel.getElementsByTagName("item");
            feed = new Feed(rssTitle, rssDescription);

            for (int i = 0; i < list.getLength(); i++) {
                Element item = (Element) list.item(i);
                String itemTitle = item.getElementsByTagName("title").item(0).getTextContent();
                String itemDescription = item.getElementsByTagName("description").item(0).getTextContent();
                String itemLink = item.getElementsByTagName("link").item(0).getTextContent();
                feed.addItem(new FeedItem(itemTitle, itemDescription, itemLink));
            }
        } catch (IOException e) {
            errorMessage = activity.getResources().getString(R.string.no_internet);
        } catch (ParserConfigurationException e) {
            errorMessage = activity.getResources().getString(R.string.bad_feed);
        } catch (SAXException e) {
            errorMessage = activity.getResources().getString(R.string.bad_feed);
        }

        return feed;
    }

    @Override
    protected void onPostExecute(final Feed feed) {
        super.onPostExecute(feed);
        if (feed != null) {
            activity.setDescription(rssDescription);
            activity.setFeed(feed);
            activity.onRefreshed(true, "");
        } else {
            activity.setDescription("");
            activity.onRefreshed(false, errorMessage);
        }
    }
}
