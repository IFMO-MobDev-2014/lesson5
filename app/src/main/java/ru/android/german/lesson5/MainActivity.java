package ru.android.german.lesson5;

import android.app.Activity;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by german on 20.10.14.
 */
public class MainActivity extends Activity {
    ListView listView;
    ArrayAdapter<Feed> adapter;

    ArrayList<Feed> feeds = new ArrayList<Feed>();
    FeedLoader feedLoader = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mian);

        listView = (ListView)this.findViewById(R.id.listView);
        int layoutID = android.R.layout.simple_list_item_1;
        adapter = new ArrayAdapter<Feed>(this, layoutID, feeds);
        listView.setAdapter(adapter);

        refreshFeeds();
    }

    private void refreshFeeds() {
        if (feedLoader == null || feedLoader.getStatus().equals(AsyncTask.Status.FINISHED)) {
            feeds.clear();
            feedLoader = new FeedLoader();
            feedLoader.execute((Void[])null);
        }
    }

    private void addNewFeed(Feed feed) {
        feeds.add(feed);
        adapter.notifyDataSetChanged();
    }

    private class FeedLoader extends AsyncTask<Void, Feed, Element> {
        @Override
        protected Element doInBackground(Void... voids) {
            URL url;
            try {
                url = new URL(getString(R.string.feed));

                URLConnection connection;
                connection = url.openConnection();

                HttpURLConnection httpConnection = (HttpURLConnection)connection;
                int responseCode = httpConnection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    InputStream in = httpConnection.getInputStream();

                    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                    DocumentBuilder db = dbf.newDocumentBuilder();

                    Document document = db.parse(in);
                    Element element = document.getDocumentElement();
                    return element;
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Feed... values) {
            super.onProgressUpdate(values);
        }

//        @Override
//        protected void onPostExecute(Element element) {
//            super.onPostExecute(element);
//            NodeList nodeList = element.getElementsByTagName("entry");
//            if (nodeList != null && nodeList.getLength() > 0) {
//                feeds.clear();
//                for (int i = 0; i < nodeList.getLength(); i++) {
//                    Element entry = (Element)nodeList.item(i);
//                    Element title = (Element)entry.getElementsByTagName("title").item(0);
//                    Element g = (Element)entry.getElementsByTagName("georss:point").item(0);
//                    Element when = (Element)entry.getElementsByTagName("updated").item(0);
//                    Element link = (Element)entry.getElementsByTagName("link").item(0);
//
//                    if (g == null) continue;
//
//                    String details = title.getFirstChild().getNodeValue();
//                    String linkString = "http://earthquake.usgs.gov" + link.getAttribute("href");
//
//                    String point;
//                    point = g.getFirstChild().getNodeValue();
//                    String dt = when.getFirstChild().getNodeValue();
//                    SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd'T'hh:mm:ss'Z'");
//                    Date qdate = new GregorianCalendar(0, 0, 0).getTime();
//                    try {
//                        qdate = sdf.parse(dt);
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
//
//                    String[] location = point.split(" ");
//                    Location l = new Location("dummyGPS");
//                    l.setLatitude(Double.parseDouble(location[0]));
//                    l.setLongitude(Double.parseDouble(location[1]));
//
//                    String magnitudeString = details.split(" ")[1];
//                    int end = magnitudeString.length() - 1;
//                    double magnitude = Double.parseDouble(magnitudeString.substring(0, end));
//
//                    details = details.split(",")[1].trim();
//
//                    Feed feed = new Feed(qdate, details, l, magnitude, linkString);
//                    addNewFeed(feed);
//                 //   publishProgress(feed);
//                }
//            }
//        }

        @Override
        protected void onPostExecute(Element element) {
            super.onPostExecute(element);
            Element channel = (Element)element.getElementsByTagName("channel").item(0);
            NodeList items = channel.getElementsByTagName("item");
            if (items != null && items.getLength() > 0) {
                for (int i = 0; i < items.getLength(); i++) {
                    Element item = (Element)items.item(i);
                    Element titleElement = (Element)item.getElementsByTagName("title").item(0);
                    Element linkElement = (Element)item.getElementsByTagName("link").item(0);

                    String title = titleElement.getFirstChild().getNodeValue();
                    String link = linkElement.getFirstChild().getNodeValue();

                    System.out.println(title);

                    addNewFeed(new Feed(title, link));
                }
            }
//            System.out.println(items.getLength());
        }
    }
}
