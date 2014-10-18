package com.example.ru.rssreader;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
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
            this.title = "";
            this.description = "";
            this.link = "";
            this.date = "";
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        public String getLink() {
            return link;
        }

        public String getDate() {
            return date;
        }

        public void setTitle(String title) {
            this.title = title;
        }


        public void setDescription(String description) {
            this.description = description;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public void setDate(String date) {
            this.date = date;
        }


        @Override
        public String toString() {
            return getDate() + "\n" + getTitle();
        }

    }

    public static class RSSParser {
        private final static int connectTimeout = 15000;
        private String descriptionTag;
        private String dateTag;
        private ArrayList<Record> rssRecords;

        public RSSParser() {
            this.descriptionTag = "";
            this.dateTag = "";
            this.rssRecords = null;
        }

        public ArrayList<Record> getRecords() {
            return rssRecords;
        }

        private void setFormat(String descriptionTag, String dateTag) {
            this.descriptionTag = descriptionTag;
            this.dateTag = dateTag;
        }

        public void parse(String rss) throws Exception {
            try {
                URL url = new URL(rss);
                URLConnection connection = url.openConnection();
                connection.setConnectTimeout(connectTimeout);
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                InputStream input = connection.getInputStream();
                Document document = builder.parse(input);
                Element root = document.getDocumentElement();
                NodeList rootItem = root.getElementsByTagName("item");
                NodeList rootRecord = root.getElementsByTagName("entry");
                if (rootItem.getLength() > 0) {
                    setFormat("description", "pubDate");
                    parseRecords(rootItem);
                } else if (rootRecord.getLength() > 0) {
                    setFormat("summary", "published");
                    parseRecords(rootRecord);
                } else throw new Exception();
            } catch (Exception e) {
                RSSActivity.isSuccess = false;
                e.printStackTrace();
            }
        }

        private void parseRecords(NodeList root) {
            try {
                rssRecords = new ArrayList<Record>();
                for (int i = 0; i < root.getLength(); i++) {
                    Element currentElement = (Element) root.item(i);
                    Element titleElement = (Element) currentElement.getElementsByTagName("title").item(0);
                    Element descriptionElement = (Element) currentElement.getElementsByTagName(descriptionTag).item(0);
                    Element dateElement = (Element) currentElement.getElementsByTagName(dateTag).item(0);
                    Element linkElement = (Element) currentElement.getElementsByTagName("link").item(0);
                    Record record = new Record();
                    if (titleElement != null && titleElement.getFirstChild() != null && titleElement.getFirstChild().getNodeValue() != null)
                        record.setTitle(titleElement.getFirstChild().getNodeValue());
                    if (descriptionElement != null && descriptionElement.getFirstChild() != null && descriptionElement.getFirstChild().getNodeValue() != null)
                        record.setDescription(descriptionElement.getFirstChild().getNodeValue());
                    if (dateElement != null && dateElement.getFirstChild() != null && dateElement.getFirstChild().getNodeValue() != null)
                        record.setDate(dateElement.getFirstChild().getNodeValue());
                    if (linkElement != null && linkElement.getFirstChild() != null && linkElement.getFirstChild().getNodeValue() != null)
                        record.setLink(linkElement.getFirstChild().getNodeValue());

                    rssRecords.add(record);
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
            RSSParser rssParser = new RSSParser();
            try {
                rssParser.parse(strings[0]);
                return rssParser.getRecords();
            } catch (Exception e) {
                isSuccess = false;
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Record> records) {
            try {
                setProgressBarIndeterminateVisibility(false);
                if (!isSuccess) {
                    Toast.makeText(getApplicationContext(), getString(R.string.error_msg), Toast.LENGTH_SHORT).show();
                    finish();
                }
                linkRecords.clear();
                for (Record record : records) linkRecords.add(record);
                arrayAdapter1.notifyDataSetChanged();
            } catch (Exception e) {
                isSuccess = false;
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.link_activity);
        setProgressBarIndeterminateVisibility(true);
        Intent intent = getIntent();
        ListView listView1 = (ListView) findViewById(R.id.listView);
        arrayAdapter1 = new ArrayAdapter<Record>(this, R.layout.text_view_main, linkRecords);
        listView1.setAdapter(arrayAdapter1);
        isSuccess = true;
        new RSSLoader().execute(intent.getStringExtra("link"));
        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), WebViewActivity.class);
                intent.putExtra("link", linkRecords.get(i).getLink());
                if (linkRecords.get(i).getDescription() != null) {
                    intent.putExtra("title", linkRecords.get(i).getTitle());
                    intent.putExtra("description", linkRecords.get(i).getDescription());

                }
                startActivity(intent);
            }

        });
    }


}
