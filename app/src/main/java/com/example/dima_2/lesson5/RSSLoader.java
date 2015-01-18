package com.example.dima_2.lesson5;

import android.os.AsyncTask;
import android.util.Log;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import java.io.*;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;


/**
 * Created by Dima_2 on 08.01.2015.
 */
public class RSSLoader {
    public String url;
    public RSSList list;

    public RSSLoader(String url, RSSList list) {
        this.url = url;
        this.list = list;
    }

    class Loader extends AsyncTask<String, Void, Element> {
        @Override
        protected Element doInBackground(String[] s) {
            try {
                URL url = new URL(s[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                InputStream inputStream = connection.getInputStream();
                DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = builderFactory.newDocumentBuilder();

                Document document = builder.parse(inputStream);
                return document.getDocumentElement();
            } catch (MalformedURLException e) {
                System.out.println("Class Loader throws MalformedURLException");
                Log.i("Exception", "Class Loader throws MalformedURLException");
                e.printStackTrace();
            } catch (ProtocolException e) {
                System.out.println("Class Loader throws ProtocolException");
                Log.i("Exception", "Class Loader throws ProtocolException");
                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                System.out.println("Class Loader throws ParserConfigurationException");
                Log.i("Exception", "Class Loader throws ParserConfigurationException");
                e.printStackTrace();
            } catch (SAXException e) {
                System.out.println("Class Loader throws SAXException");
                Log.i("Exception", "Class Loader throws SAXException");
                e.printStackTrace();
            } catch (IOException e) {
                System.out.println("Class Loader throws IOException");
                Log.i("Exception", "Class Loader throws IOException");
                e.printStackTrace();
            } catch (Throwable e) {
                System.out.println("Class Loader throws unknown exception");
                Log.i("Exception", "Class Loader throws unknown exception");
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Element documentElement) {
            super.onPostExecute(documentElement);
            list.clearAll();
            NodeList nodeList = documentElement.getElementsByTagName("item");
            if (nodeList == null || nodeList.getLength() == 0)
                return;
            for (int i = 0; i < nodeList.getLength(); i++) {
                Element entry = (Element)nodeList.item(i);
                Element title = (Element)entry.getElementsByTagName("title").item(0);
                Element link = (Element)entry.getElementsByTagName("link").item(0);
                String name = title.getFirstChild().getNodeValue();
                String url = link.getFirstChild().getNodeValue();

                RSSElement element = new RSSElement(name, url);
                list.addElement(element);
            }
        }
    }

    public void load() {
        Loader loader = new Loader();
        loader.execute(url);
    }
}
