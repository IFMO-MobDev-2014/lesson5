package com.example.rssreader;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Rss extends Activity {

    public class Node {

        private String title;
        private String description;
        private Date date;
        private String link;

        Node (String title, String description, Date date, String link) {
            this.title = title;
            this.description = description;
            this.date = date;
            this.link = link;
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        public Date getDate() {
            return date;
        }

        public String getLink() {
            return link;
        }
    }

    private ArrayList<Node> nodes;
    private ListView listView1;
    private String[] from = new String[]{"title", "date", "description"};
    private int[] to;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rss);
        to = new int[] {R.id.title, R.id.date, R.id.description};
        listView1 = (ListView) findViewById(R.id.listView1);
        RSSTape rssTape = new RSSTape();
        rssTape.execute();
        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> av, View view, int index, long arg3) {

                Intent intent = new Intent(Rss.this, Web.class);
                intent.putExtra("link", nodes.get(index).getLink());
                Log.i("Link", nodes.get(index).getLink());
                startActivity(intent);
            }
        });
    }

    private class RSSTape extends AsyncTask < Void, Void, ArrayList<Node> > {

        @Override
        protected ArrayList<Node> doInBackground(Void... params) {
            return getTape();
        }

        @Override
        protected void onPostExecute(ArrayList<Node> result) {
            nodes = result;
            ArrayList<Map<String, String>> items = new ArrayList<Map<String, String>>();
            HashMap<String, String> map;
            for (int i = 0; i < nodes.size(); i++) {
                map = new HashMap<String, String>();
                map.put("title", nodes.get(i).getTitle());
                map.put("date", nodes.get(i).getDate().toString());
                map.put("description", nodes.get(i).getDescription());
                items.add(map);
            }
            SimpleAdapter adapter = new SimpleAdapter(Rss.this, items, R.layout.listview, from, to);
            listView1.setAdapter(adapter);
        }

        private ArrayList<Node> getTape() {
            String request;
            ArrayList<Node> result = new ArrayList<Node>();
            request = "http://bash.im/rss/";
            InputStream inputStream = null;
            URL url;
            HttpURLConnection connect;
            try {
                url = new URL(request);
                connect = (HttpURLConnection) url.openConnection();
                DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
                inputStream = connect.getInputStream();
                Document document = documentBuilder.parse(inputStream);
                Element element = document.getDocumentElement();
                NodeList nodeList = element.getElementsByTagName("item");
                for (int i = 0; i < nodeList.getLength(); i++)  {
                    Element main = (Element) nodeList.item(i);
                    Element child = (Element) main.getElementsByTagName("title").item(0);
                    String title = child.getFirstChild().getNodeValue();
                    child = (Element) main.getElementsByTagName("description").item(0);
                    String description = child.getFirstChild().getNodeValue();
                    child = (Element) main.getElementsByTagName("pubDate").item(0);
                    Date date = new Date(child.getFirstChild().getNodeValue());
                    child = (Element) main.getElementsByTagName("link").item(0);
                    String link = child.getFirstChild().getNodeValue();
                    result.add(new Node(title, description, date, link));
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            finally {
                try {
                    inputStream.close();
                } catch (IOException e) {
                }
            }
            return result;
        }
    }
}