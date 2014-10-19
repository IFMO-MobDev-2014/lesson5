package odeen.rssreader;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.ResultReceiver;
import android.renderscript.Element;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by Женя on 18.10.2014.
 */
public class NewsFetcherService extends IntentService {


    public NewsFetcherService() {
        super("NewsFetcherIntentService");
    }


    private News parseItemLenta(Node v) {
        NodeList nodeList = v.getChildNodes();
        News news = new News();
        for (int i = 0; i < nodeList.getLength(); i++) {
            if (nodeList.item(i).getNodeName().equals("guid")) {
                news.setURL(nodeList.item(i).getTextContent());
            }
            if (nodeList.item(i).getNodeName().equals("title")) {
                news.setTitle(nodeList.item(i).getTextContent());
            }
            if (nodeList.item(i).getNodeName().equals("description")) {
                news.setDescription(nodeList.item(i).getTextContent());
            }
            if (nodeList.item(i).getNodeName().equals("pubDate")) {
                try {
                    news.setPubDate(new SimpleDateFormat("E, dddd MMMM yyyy k:m:s z").parse(nodeList.item(i).getTextContent()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            if (nodeList.item(i).getNodeName().equals("enclosure")) {
                try {
                    news.decodeBitmap(nodeList.item(i).getAttributes().getNamedItem("url").getNodeValue());
                    news.setImageLink(nodeList.item(i).getAttributes().getNamedItem("url").getNodeValue());
                } catch (IOException e) {
                    sendBroadcast(Constants.BROADCAST_ACTION_INTERNET_PROBLEM, null);
                    e.printStackTrace();
                    return null;
                }
            }
        }
        return news;
    }
    private News parseItemBBC(Node v) {
        NodeList nodeList = v.getChildNodes();
        News news = new News();
        for (int i = 0; i < nodeList.getLength(); i++) {
            if (nodeList.item(i).getNodeName().equals("guid")) {
                news.setURL(nodeList.item(i).getTextContent());
            }
            if (nodeList.item(i).getNodeName().equals("title")) {
                news.setTitle(nodeList.item(i).getTextContent());
            }
            if (nodeList.item(i).getNodeName().equals("description")) {
                news.setDescription(nodeList.item(i).getTextContent());
            }
            if (nodeList.item(i).getNodeName().equals("pubDate")) {
                try {
                    news.setPubDate(new SimpleDateFormat("E, dddd MMMM yyyy k:m:s z").parse(nodeList.item(i).getTextContent()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            if (nodeList.item(i).getNodeName().equals("media:thumbnail") &&
                    nodeList.item(i).getAttributes().getNamedItem("width").getNodeValue().equals("144")) {
                news.setImageLink(nodeList.item(i).getAttributes().getNamedItem("url").getNodeValue());
                try {
                    news.decodeBitmap(nodeList.item(i).getAttributes().getNamedItem("url").getNodeValue());
                } catch (IOException e) {
                    sendBroadcast(Constants.BROADCAST_ACTION_INTERNET_PROBLEM, null);
                    e.printStackTrace();
                    return null;
                }
            }
        }
        return news;
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        URL url = null;
        try {
            url = new URL((String) intent.getSerializableExtra(Constants.URL_DATA));
        } catch (MalformedURLException e) {
            return;
        }

        Document doc = null;
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = null;
            db = dbf.newDocumentBuilder();
            doc = db.parse(url.openStream());
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            sendBroadcast(Constants.BROADCAST_ACTION_INTERNET_PROBLEM, null);
            e.printStackTrace();
            return;
        }

        Node rssNode = null;
        for (int i = 0; i < doc.getChildNodes().getLength(); i++) {
            if (doc.getChildNodes().item(i).getNodeName().equals("rss")) {
                rssNode = doc.getChildNodes().item(i);
                break;
            }
        }
        RSSReaderDBSource db = new RSSReaderDBSource(getApplicationContext());
        db.open();
        NodeList itemList = rssNode.getChildNodes().item(1).getChildNodes();
        for (int i = 0; i < itemList.getLength(); i++) {
            if (itemList.item(i).getNodeName().equals("item")) {
                News news = parseItemLenta(itemList.item(i));
                sendBroadcast(Constants.BROADCAST_ACTION_PROCESS, news);
                db.insertNews(news);
                Log.i("WORKING", "HARD");
            }
        }
        db.close();
        sendBroadcast(Constants.BROADCAST_ACTION_FINISHED, null);
    }


    public void sendBroadcast(String message, News news) {
        Intent intent = new Intent(Constants.BROADCAST_ACTION);
        intent.putExtra(Constants.BROADCAST_ACTION, message);
        if (news != null) {
            RSSNewsLibrary.get(getApplicationContext()).addToPipe(news);
        }
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}
