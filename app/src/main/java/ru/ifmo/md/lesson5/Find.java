package ru.ifmo.md.lesson5;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.content.Context;
import android.content.Intent;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.net.*;
import android.widget.*;
import android.os.AsyncTask;
import android.view.ViewGroup;

public class Find extends Activity {

    Adapter adapter;
    Document article;
    LinearLayout parsedArticles;
    TextView head[];
    String articles[];
    ListView list;
    TextView text;
    String link="http://bash.im/rss/";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        text = (TextView) findViewById(R.id.txt);
        list = new ListView(this);
        adapter = new Adapter(this, R.layout.list_item);

        list.setAdapter(adapter);
        parsedArticles = (LinearLayout) findViewById(R.id.parsescreen);
        new Loading().execute(link);

    }

    public class Adapter extends ArrayAdapter<TextView> {

        @Override
        public View getView(int i, View v, ViewGroup p) {
            return head[i];
        }

        @Override
        public TextView getItem(int i) {
            return head[i];
        }

        public Adapter(Context context, int data) {
            super(context, data);
        }

    }

    public void showArticle(int i) {

        Intent intent = new Intent(this, Show.class);
        intent.putExtra("link", articles[i]);
        startActivity(intent);

    }


    public class Loading extends AsyncTask<String, Void, Document> {

        @Override
        protected Document doInBackground(String... v) {
            try {
                text.setText("Downloading...");
                DocumentBuilder doc = DocumentBuilderFactory.newInstance().newDocumentBuilder();

                URL ref = new URL(link);
                URLConnection connect = ref.openConnection();

                article = doc.parse(connect.getInputStream());
                connect.getInputStream().close();
                 return article;

            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(Document res) {
            article = res;
            out();
        }
    }

    public void out(){
        text.setText("Chosen: " + link);

        if (article.getElementsByTagName("rss").getLength() > 0) {

            NodeList res = ((Element) ((Element) (article.getElementsByTagName("rss")).item(0)).getElementsByTagName("channel").item(0)).getElementsByTagName("item");
            head = new TextView[res.getLength()];
            articles = new String[res.getLength()];

            for (int i = 0; i < res.getLength(); i++) {
                head[i] = new TextView(this);
                String s1 = (((Element) res.item(i)).getElementsByTagName("title").item(0)).getChildNodes().item(0).getNodeValue();
                String s2 = (((Element) res.item(i)).getElementsByTagName("pubDate").item(0)).getChildNodes().item(0).getNodeValue();
                articles[i] = (((Element) res.item(i)).getElementsByTagName("description").item(0)).getChildNodes().item(0).getNodeValue();

                head[i].setText(s1 + "\n" + s2);
                adapter.add(head[i]);

            }
        } else if (article.getElementsByTagName("feed").getLength() > 0) {

            NodeList res = ((Element) (article.getElementsByTagName("feed")).item(0)).getElementsByTagName("entry");
            head = new TextView[res.getLength()];
            articles = new String[res.getLength()];

            for (int i = 0; i < res.getLength(); i++) {
                head[i] = new TextView(this);
                String s1 = (((Element) res.item(i)).getElementsByTagName("title").item(0)).getChildNodes().item(0).getNodeValue();
                String s2 = (((Element) res.item(i)).getElementsByTagName("updated").item(0)).getChildNodes().item(0).getNodeValue();
                articles[i] = (((Element) res.item(i)).getElementsByTagName("summary").item(0)).getChildNodes().item(0).getNodeValue();

                head[i].setText(s1 + "\n" + s2);
                adapter.add(head[i]);
            }
        } else if (article.getElementsByTagName("rdf").getLength() > 0) {

            NodeList res = ((Element) (article.getElementsByTagName("rdf")).item(0)).getElementsByTagName("item");
            head = new TextView[res.getLength()];
            articles = new String[res.getLength()];

            for (int i = 0; i < res.getLength(); i++) {
                head[i] = new TextView(this);
                String s1 = (((Element) res.item(i)).getElementsByTagName("title").item(0)).getChildNodes().item(0).getNodeValue();
                articles[i] = (((Element) res.item(i)).getElementsByTagName("description").item(0)).getChildNodes().item(0).getNodeValue();

                head[i].setText(s1);
                adapter.add(head[i]);
            }
        }

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                showArticle(i);
            }
        });

        parsedArticles.addView(list);
    }

}
