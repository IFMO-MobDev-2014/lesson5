package com.example.ru.rssreader;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class RSSActivity extends Activity {

    private ArrayAdapter<Record> arrayAdapter1;
    private ArrayList<Record> linkRecords = new ArrayList<Record>();
    private static boolean isSuccess;

    public static class Record {
        private String title;
        private String description;
        private String link;
        private String date;

        public Record() {
            this.title = null;
            this.description = null;
            this.link = null;
            this.date = null;
        }

        public Record(String title, String description, String link, String date) {
            this.title = title;
            this.description = description;
            this.link = link;
            this.date = date;
        }

        public String getTitle() {
            return this.title;
        }

        public String getDescription() {
            return this.description;
        }

        public String getLink() {
            return this.link;
        }

        public String getDate() {
            return this.date;
        }

        @Override
        public String toString() {
            return date + "\n" + getTitle();
        }

    }

    public static class RSSParser {
        private String descriptionFormat;
        private String dateFormat;
        private ArrayList<Record> rssRecords;

        public RSSParser() {
            this.descriptionFormat = "";
            this.dateFormat = "";
            this.rssRecords = null;
        }

        public ArrayList<Record> getRecords() {
            return rssRecords;
        }

        public void parse(String rss) throws Exception {
            try {
                URL url = new URL(rss);
                URLConnection connection = url.openConnection();
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                InputStream input = connection.getInputStream();
                Document document = builder.parse(input);
                Element main = document.getDocumentElement();
                NodeList rootItem = main.getElementsByTagName("item");
                NodeList rootRecord = main.getElementsByTagName("entry");
                if (rootItem.getLength() > 0) {
                    descriptionFormat = "description";
                    dateFormat = "pubDate";
                    parse(rootItem);
                } else if (rootRecord.getLength() > 0) {
                    descriptionFormat = "summary";
                    dateFormat = "published";
                    parse(rootRecord);
                } else throw new Exception(Resources.getSystem().getString(R.string.error_msg));
            } catch (Exception e) {
                RSSActivity.isSuccess = false;
                e.printStackTrace();
            }
        }

        private void parse(NodeList main) {
            rssRecords = new ArrayList<Record>();
            try {
                for (int i = 0; i < main.getLength(); i++) {
                    Element currentElement = (Element) main.item(i);
                    Element titleElement = (Element) currentElement.getElementsByTagName("title").item(0);
                    Element descriptionElement = (Element) currentElement.getElementsByTagName(descriptionFormat).item(0);
                    Element dateElement = (Element) currentElement.getElementsByTagName(dateFormat).item(0);
                    Element linkElement = (Element) currentElement.getElementsByTagName("link").item(0);
                    String title = "";
                    String description = "";
                    String date = "";
                    String link = "";
                    if (titleElement.getFirstChild().getNodeValue() != null) {
                        title = titleElement.getFirstChild().getNodeValue();
                        description = descriptionElement.getFirstChild().getNodeValue();
                        date = dateElement.getFirstChild().getNodeValue();
                        link = linkElement.getFirstChild().getNodeValue();
                    }
                    rssRecords.add(new Record(title, description, link, date));
                }
            } catch (Exception e) {
                RSSActivity.isSuccess = false;
                e.printStackTrace();
            }
        }

    }

    public class RSSLoader extends AsyncTask<String, Void, ArrayList<Record>> {

        @Override
        protected ArrayList<Record> doInBackground(String... strings) {
            ArrayList<Record> result = null;
            RSSParser rssParser = new RSSParser();
            try {
                rssParser.parse(strings[0]);
                result = rssParser.getRecords();
            } catch (Exception e) {
                isSuccess = false;
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(ArrayList<Record> records) {
            try {
                if (!isSuccess) {
                    Toast.makeText(getApplicationContext(), getString(R.string.error_msg), Toast.LENGTH_SHORT).show();
                    finish();
                }
                linkRecords.clear();
                for (Record record : records) linkRecords.add(record);
                arrayAdapter1.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.link_activity);
        Intent intent = getIntent();
        final String link = intent.getStringExtra("link");
        ListView listView1 = (ListView) findViewById(R.id.listView);
        arrayAdapter1 = new ArrayAdapter<Record>(this, R.layout.text_view_main, linkRecords);
        listView1.setAdapter(arrayAdapter1);
        isSuccess = true;
        new RSSLoader().execute(link);
        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                /*Intent intent = new Intent();
                intent.setClass(getApplicationContext(), WebActivity.class);
                intent.putExtra("link", linkRecords.get(i).getLink());
                if (linkRecords.get(i).getDescription() != null) {
                    intent.putExtra("content", linkRecords.get(i).getTitle() + "<br>" + linkRecords.get(i).getDescription());
                }
                startActivity(intent);*/
            }

        });
    }


}
