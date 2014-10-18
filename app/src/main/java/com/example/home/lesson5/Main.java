package com.example.home.lesson5;

import android.app.Activity;
import android.app.ListActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class Main extends ListActivity {

    public class MyAdapter<T> extends BaseAdapter {

        List<T> list;

        public MyAdapter(List<T> list) {
            this.list = list;
        }

        public void add(T item) {
            list.add(item);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.my_item_1, viewGroup, false);
            ((TextView) view.findViewById(R.id.title)).setText("mamka tvoya");
            ((TextView) view.findViewById(R.id.content)).setText((String) getItem(i));
            return view;
        }
    }

    public class Downloader extends AsyncTask<String, Void, String> {

        ListView listView;

        public Downloader(ListView listView) {
            this.listView = listView;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(String... strings) {

            String rssUrl = strings[0];
            String s;

            URL url = null;
            try {
                url = new URL(rssUrl);
                URLConnection urlConnection = url.openConnection();

                SAXParser saxParser = SAXParserFactory.newInstance().newSAXParser();

                DefaultHandler handler = new DefaultHandler() {
                    @Override
                    public void startDocument() throws SAXException {
                        super.startDocument();
                    }

                    @Override
                    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                        super.startElement(uri, localName, qName, attributes);
                    }

                    @Override
                    public void endElement(String uri, String localName, String qName) throws SAXException {
                        super.endElement(uri, localName, qName);
                    }

                    @Override
                    public void characters(char[] ch, int start, int length) throws SAXException {
                        super.characters(ch, start, length);
                    }
                };

                saxParser.parse(urlConnection.getInputStream(), handler);

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

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MyAdapter<String> adapter = new MyAdapter<String>(new ArrayList<String>());
        adapter.add("lolka\nlolka\nlalka");

        setListAdapter(adapter);

        new Downloader(getListView()).execute("http://stackoverflow.com/feeds/tag/android");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
}
