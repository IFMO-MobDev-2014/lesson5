package ru.ifmo.md.lesson5;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;


public class MainActivity extends Activity {

    enum listOfTargets {
        TITLE, IGNORE, LINK, DATE, DESCRIPTION
    }

    ArrayList<Data> resDataList;

    private class SAXPars extends DefaultHandler {

        SAXPars () {
            super();
        }

        private listOfTargets target;
        private Data elementData = new Data();

        @Override
        public void startDocument() throws SAXException {

        }

        @Override
        public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
            if (qName.equals("item")) {
                elementData = new Data();
                target = listOfTargets.IGNORE;
            } else if (qName.equals("title")) {
                target = listOfTargets.TITLE;
            } else if (qName.equals("description")) {
                target = listOfTargets.DESCRIPTION;
            } else if (qName.equals("link")) {
                target = listOfTargets.LINK;
            } else target = listOfTargets.IGNORE;
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            String content = new String(ch, start, length);
            if (target != null)
                switch (target) {
                    case TITLE:
                        if (content.length() != 0) {
                            if (elementData.aTitle != null) {
                                elementData.aTitle += content;
                            } else {
                                elementData.aTitle = content;
                            }
                        }
                        break;
                    case LINK:
                        if (content.length() != 0) {
                            if (elementData.aLink != null) {
                                elementData.aLink += content;
                            } else {
                                elementData.aLink = content;
                            }
                        }
                        break;
                    case DESCRIPTION:
                        if (content.length() != 0) {
                            if (elementData.aDescription != null) {
                                elementData.aDescription += content;
                            } else {
                                elementData.aDescription = content;
                            }
                        }
                        break;
                }
        }

        @Override
        public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
            if (qName.equals("item")) {
                resDataList.add(elementData);
            } else {
                target = listOfTargets.IGNORE;
            }
        }

        @Override
        public void endDocument() {

        }
    }

    private class RssDataController extends AsyncTask<String, Integer, ArrayList<Data>> {

        @Override
        protected ArrayList<Data> doInBackground(String... params) {
            String urlStr = params[0];
            InputStream is = null;
            resDataList = new ArrayList<Data>();
            try {
                URL url = new URL(urlStr);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                int response = connection.getResponseCode();
                is = connection.getInputStream();

                SAXParserFactory factory = SAXParserFactory.newInstance();
                SAXParser parser = factory.newSAXParser();
                SAXPars saxp = new SAXPars();
                parser.parse(is, saxp);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            }

            return resDataList;
        }

        @Override
        protected void onPostExecute(ArrayList<Data> result) {
            for (int i = 0; i < result.size(); i++) {
                postAdapter.add(result.get(i));
            }
        }
    }

    private ArrayList<Data> listData = new ArrayList<Data>();
    private ListView listView;
    private DataItemAdapter postAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postlist);

        listView = (ListView) this.findViewById(R.id.dataListView);
        listView.setOnItemClickListener(onItemClickListener);
        postAdapter = new DataItemAdapter(this, R.layout.postitem, listData);
        listView.setAdapter(postAdapter);
        RssDataController contr = new RssDataController();
        contr.execute("http://echo.msk.ru/interview/rss-fulltext.xml");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.postlist, menu);
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
        return super.onOptionsItemSelected(item);
    }

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                long arg3) {
            Data data = listData.get(arg2);

            Bundle postInfo = new Bundle();
            postInfo.putString("url", data.aLink);

            Intent postviewIntent = new Intent(MainActivity.this, ViewActivity.class);
            postviewIntent.putExtras(postInfo);
            startActivity(postviewIntent);
        }
    };
}

