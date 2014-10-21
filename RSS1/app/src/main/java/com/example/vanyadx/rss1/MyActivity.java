package com.example.vanyadx.rss1;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.view.*;


@SuppressLint("SimpleDateFormat")
@SuppressWarnings("unused")
public class MyActivity extends Activity {

    InputStream is;
    ListView listview;
    final ArrayList usmessage = new ArrayList<String>();
    ArrayAdapter<String> adapter;
    final ArrayList address = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        listview = (ListView) findViewById(R.id.listView1);
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, usmessage);

        listview.setAdapter(adapter);
        final Intent intent = new Intent(MyActivity.this, WebActivity.class);
        MyAsyncTask my = new MyAsyncTask();
        my.execute();
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public  void onItemClick(AdapterView<?> parent, View p, int position,long id){
                intent.putExtra("newAddress",address.get(position).toString());
                System.out.println(address.get(position).toString());
                startActivity(intent);

            }
        });




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    public class MyAsyncTask extends AsyncTask<Void, Void, Void> {
        List<Message> printnew;

        @Override
        public Void doInBackground(Void... params) {
            DomFeedParser news = new DomFeedParser(
                    "http://stackoverflow.com/feeds/tag/android");

            printnew = news.parse();
            return null;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void onPostExecute(Void result) {
            for (int i = 0; i < printnew.size(); i++) {
                usmessage.add(printnew.get(i).title + " "
                        + printnew.get(i).date);
                address.add(printnew.get(i).address);
            }

            adapter.notifyDataSetChanged();

        }

        public class Message  {

            private String title;
            private URL link;
            private String description;
            private String date;
            private String address;

            public void setTitle(String title) {
                this.title = title;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public void setAddress(String address){
                this.address = address;
            }

            public void setDate(String data) {
                String res = "";
                for (int i = 0; i < data.length(); i++) {
                    if (i == 10 || i == data.length() - 1) {
                        res += " ";
                    } else {
                        res += data.charAt(i);
                    }
                }
                this.date = res;
            }


            public String getTitle() {
                return title;
            }




        }

        public class DomFeedParser {
            URL feedUrl;
            static final String PUB_DATE = "published";
            static final String ITEM = "entry";
            static final String TITLE = "title";
            static final String DESCRIPTION = "description";
            static final String LINK = "link";
            static final String ADDRESS = "id";
            DomFeedParser(String t) {
                try {
                    feedUrl = new URL(t);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }

            protected InputStream getInputStream() {
                try {
                    return feedUrl.openConnection().getInputStream();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            public List<Message> parse() {
                DocumentBuilderFactory factory = DocumentBuilderFactory
                        .newInstance();
                List<Message> messages = new ArrayList<Message>();
                try {
                    DocumentBuilder builder = factory.newDocumentBuilder();
                    Document dom = builder.parse(this.getInputStream());
                    Element root = dom.getDocumentElement();
                    NodeList items = root.getElementsByTagName(ITEM);
                    for (int i = 0; i < items.getLength(); i++) {
                        Message message = new Message();
                        Node item = items.item(i);
                        NodeList properties = item.getChildNodes();
                        for (int j = 0; j < properties.getLength(); j++) {
                            Node property = properties.item(j);
                            String name = property.getNodeName();
                            if (!name.equals("")) {
                                if (name.equalsIgnoreCase(TITLE)) {

                                    message.setTitle(property.getFirstChild()
                                            .getNodeValue());
                                } else if (name.equalsIgnoreCase(PUB_DATE)) {

                                    message.setDate(property.getFirstChild()
                                            .getNodeValue());
                                } else if (name.equalsIgnoreCase(ADDRESS)){
                                    message.setAddress(property.getFirstChild()
                                            .getNodeValue());
                                }
                            }
                        }

                        messages.add(message);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return messages;
            }
        }
    }

}
