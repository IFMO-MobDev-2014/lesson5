package ru.ifmo.lesson5;

import android.os.AsyncTask;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by creed on 21.10.14.
 */

public class RSSLoader {
    private String url;
    private RSSContainer container;
    RSSLoader(String url, RSSContainer container) {
        this.url = url;
        this.container = container;
    }
    public void load() {
        LoadRSS task = new LoadRSS();
        task.execute(url);
    }
    class LoadRSS extends AsyncTask<String, Void, Element> {
        @Override
        protected Element doInBackground(String... s) {
            try {
                URL obj = new URL(s[0]);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                con.setRequestMethod("GET");

                InputStream in = con.getInputStream();
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();

                Document dom = db.parse(in);
                Element docEle = dom.getDocumentElement();
                return docEle;
            } catch(MalformedURLException e) {
                System.err.println("LoadRSS occurs MalformedURLException");
                e.printStackTrace();
            } catch (IOException e) {
                System.err.println("LoadRSS occurs IOException");
                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                System.err.println("LoadRSS occurs ParserConfigurationException");
                e.printStackTrace();
            } catch (SAXException e) {
                System.err.println("LoadRSS occurs SAXException");
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Element docEle) {
            super.onPostExecute(docEle);
            container.clear();
            NodeList nl = docEle.getElementsByTagName("entry");
            if (nl != null && nl.getLength() > 0) {
                for (int i = 0 ; i < nl.getLength(); i++) {
                    Element entry = (Element)nl.item(i);
                    Element title = (Element)entry.getElementsByTagName("title").item(0);
                    Element link = (Element)entry.getElementsByTagName("link").item(0);
                    String titleString = title.getFirstChild().getNodeValue();
                    String linkString = link.getAttribute("href");

                    RSSItem item = new RSSItem(titleString, linkString);
                    container.add(item);
                }
            }
        }

    }
}
