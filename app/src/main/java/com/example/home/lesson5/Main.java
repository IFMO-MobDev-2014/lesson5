package com.example.home.lesson5;

import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class Main extends ListActivity {

    public class MyAdapter<T> extends BaseAdapter {

        List<T> list;

        public MyAdapter(List<T> list) {
            this.list = list;
        }

        public void add(T item) {
            list.add(item);
            notifyDataSetChanged();
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
            if (view == null)
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.my_item_1, viewGroup, false);
            ((TextView) view.findViewById(R.id.title)).setText(((String[]) getItem(i))[0]);
            ((TextView) view.findViewById(R.id.content)).setText(Html.fromHtml(((String[]) getItem(i))[1]));
            if (i % 2 == 1)
                view.setBackgroundColor(0xFFDDDDDD);
            else
                view.setBackgroundColor(0xFFFFFFFF);
            return view;
        }
    }

    @SuppressWarnings("unchecked")
    public void listAdd(String[] s) {
        ((MyAdapter<String[]>) getListAdapter()).add(s);
    }

    public class Downloader extends AsyncTask<String, Void, String[][]> {

        ListView listView;

        public Downloader(ListView listView) {
            this.listView = listView;
        }

        @Override
        protected void onPostExecute(String[][] s) {
            super.onPostExecute(s);
            final String[][] r = s;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    for (String[] aR : r) listAdd(aR);
                }
            });
        }

        @Override
        protected String[][] doInBackground(String... strings) {

            String rssUrl = strings[0];
            String[][] s = new String[0][];

            int index = 0;

            try {
                URL url = new URL(rssUrl);

                Document xmlResponse = DocumentBuilderFactory.newInstance()
                        .newDocumentBuilder().parse((InputStream) url.getContent());

                xmlResponse.getDocumentElement().normalize();

                NodeList list = xmlResponse.getElementsByTagName("item");

                s = new String[list.getLength()][];

                for (int i = 0; i < list.getLength(); i++) {

                    Node node = list.item(i);

                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        Element element = (Element) node;

                        s[index++] = new String[] {element.getElementsByTagName("title").item(0).getFirstChild().getNodeValue(),
                                                   element.getElementsByTagName("description").item(0).getFirstChild().getNodeValue(),
                                                   element.getElementsByTagName("link").item(0).getFirstChild().getNodeValue()};
                    }

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

            return s;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MyAdapter<String[]> adapter = new MyAdapter<String[]>(new ArrayList<String[]>());

        setListAdapter(adapter);

        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(Main.this, WebActivity.class);
                intent.putExtra("link", ((String[]) getListView().getAdapter().getItem(i))[2]);
                startActivity(intent);
            }
        });

        new Downloader(getListView()).execute("http://bash.im/rss/");
    }

}
