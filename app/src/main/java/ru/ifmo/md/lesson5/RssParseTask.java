package ru.ifmo.md.lesson5;

import android.app.Activity;
import android.os.AsyncTask;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by Comradekoval on 20/10/2014.
 */
public class RssParseTask extends AsyncTask<String, Integer, LinkedList<Item>> {

    static int TAGS = 4;
    static String[] tags;
    Activity activity;

    RssParseTask(Activity act) {
        activity = act;
    }

    public LinkedList<Item> doInBackground(String... strings) {
        tags = new String[]{"title", "description", "link", "pubDate"};
        LinkedList<Item> items = MainActivity.items;
        try {
            URL url = new URL(strings[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(10000);
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = builder.parse(connection.getInputStream());
            NodeList elements = document.getElementsByTagName("item");
            if (elements.getLength() == 0) {
                elements = document.getElementsByTagName("entry");
                tags[1] = "summary";
                tags[2] = "id";
                tags[3] = "published";
            }
            for (int i = 0; i < elements.getLength(); i++) {
                boolean success = true;
                String[] lTags = {"", "", "", ""};
                Element current = (Element) elements.item(i);
                for (int j = 0; j < TAGS; j++)
                    try {
                        lTags[j] = current.getElementsByTagName(tags[j]).item(0).getFirstChild().getNodeValue();
                    } catch (Exception e) {
                        success = false;
                    }
                if (success) {
                    items.add(new Item(lTags));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return items;
    }

    @Override
    protected void onPostExecute(LinkedList<Item> params) {
        activity.runOnUiThread(new Runnable() {
            public void run() {
                MainActivity.listView.setAdapter(MainActivity.adapter);
                //MainActivity.adapter.notifyDataSetChanged();
            }
        });
    }
}
