package ru.ifmo.md.lesson5;

import android.content.Context;
import android.os.AsyncTask;
import android.text.Html;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by Snopi on 20.10.2014.
 */
public class DataDownloader extends AsyncTask<String, Void, ArrayList<FeedItem>> {
    MyCustomAdapter adapter;

    public DataDownloader(MyCustomAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    protected ArrayList<FeedItem> doInBackground(String... strings) {
        ArrayList<FeedItem> ans = new ArrayList<FeedItem>();
        String url = "http://bash.im/rss/";
        try {
            Document xmlResponse = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder().parse((InputStream) new URL(url).getContent());
            NodeList nodeList = xmlResponse.getElementsByTagName("item");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Element tmp = (Element) nodeList.item(i);
                FeedItem feed = new FeedItem();
                feed.setDescription(Html.fromHtml(tmp.getElementsByTagName("description").
                        item(0).
                        getFirstChild().
                        getNodeValue()).toString());
                feed.setTitle(tmp.getElementsByTagName("title").
                        item(0).
                        getFirstChild().
                        getNodeValue());
                feed.setUrl(tmp.getElementsByTagName("link").
                        item(0).
                        getFirstChild().
                        getNodeValue());
                ans.add(feed);
            }
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        return ans;
    }

    @Override
    protected void onPostExecute(ArrayList<FeedItem> feedItems) {
        for (FeedItem x : feedItems) {
            adapter.add(x);
        }
    }
}
