package com.example.vitalik.lesson5;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

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


public class MyActivity extends Activity {
    private ArrayAdapter<Feed> aa;
    ArrayList<Feed> feeds;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        textView = (TextView)findViewById(R.id.textView);
        ListView listView = (ListView)findViewById(R.id.myListView);
        feeds = new ArrayList<Feed>();
        aa = new ArrayAdapter<Feed>(this, android.R.layout.simple_list_item_1, feeds);
        listView.setAdapter(aa);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MyActivity.this, WebActivity.class);
                intent.putExtra("link", feeds.get(i).getLink());
                startActivity(intent);
            }
        });
        refreshFeed();
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MyActivity.this);
                builder.setTitle(feeds.get(i).getTitle());
                builder.setMessage(feeds.get(i).getIssue());
                builder.setCancelable(true);
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() { // Кнопка ОК
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss(); // Отпускает диалоговое окно
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.refresh_settings) {
            refreshFeed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void refreshFeed() {
        new LoadFeeds().execute(String.valueOf(R.string.feed));
    }


    class LoadFeeds extends AsyncTask<String, String, Element> {
        String rssFeed = getString(R.string.feed);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected Element doInBackground(String... strings) {
            try {
                URL url = new URL(rssFeed);
                URLConnection connection;
                connection = url.openConnection();
                HttpURLConnection httpConnection = (HttpURLConnection)connection;
                int responseCode = httpConnection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    InputStream in = httpConnection.getInputStream();
                    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                    DocumentBuilder db = dbf.newDocumentBuilder();
                    Document dom = db.parse(in);
                    Element docEle = dom.getDocumentElement();
                    //NodeList nl = docEle.getElementsByTagName("entry");
                    return docEle;
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onProgressUpdate(String... strings) {
            aa.notifyDataSetChanged();
        }
        @Override
        protected void onPostExecute(Element element) {
            textView.setText(element.getElementsByTagName("title").item(0).getFirstChild().getNodeValue());
            NodeList nodeList = element.getElementsByTagName("entry");
            if (nodeList != null && nodeList.getLength() > 0) {
                feeds.clear();
                for (int i = 0 ; i < nodeList.getLength(); i++) {
                    Element entry = (Element)nodeList.item(i);
                    Element title = (Element)entry.getElementsByTagName("title").item(0);
                    Element when = (Element)entry.getElementsByTagName("published").item(0);
                    Element link = (Element)entry.getElementsByTagName("link").item(0);
                    Element summary = (Element)entry.getElementsByTagName("summary").item(0);


                    String details = title.getFirstChild().getNodeValue();
                    String linkString = link.getAttribute("href");
                    String summaryS = summary.getFirstChild().getNodeValue();
                    String dt = when.getFirstChild().getNodeValue();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'");
                    Date fdate = new GregorianCalendar(0,0,0).getTime();
                    try {
                        fdate = sdf.parse(dt);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    feeds.add(new Feed(details, summaryS, linkString, fdate));
                    aa.notifyDataSetChanged();
                }
            }
        }
    }
}
