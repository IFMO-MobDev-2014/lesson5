package ru.ifmo.md.lesson2;

import org.w3c.dom.*;

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
import android.widget.TextView;

import java.io.InputStream;
import java.lang.String;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;

public class MyActivity extends ListActivity {

    public final String channel = "http://bash.im/rss/";

    public class RssNode {
        String title, summary, url;

        RssNode(String title, String summary, String url) {
            this.title = title;
            this.summary = summary;
            this.url = url;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setListAdapter(new RssAdapter<RssNode>(new ArrayList<RssNode>()));
        new RssGrabbingProcess().execute(channel);

        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MyActivity.this, WebActivity.class);
                intent.putExtra("url", ((RssNode) getListView().getAdapter().getItem(i)).url);
                startActivity(intent);
            }
        });

    }

    public class RssAdapter<RssNode> extends BaseAdapter {

        List<RssNode> bank;

        public RssAdapter(List<RssNode> bank) {
            this.bank = bank;
        }

        public void add(RssNode item) {
            bank.add(item);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return bank.size();
        }

        @Override
        public RssNode getItem(int i) {
            return bank.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.single_element, viewGroup, false);
            }

            TextView title = (TextView) view.findViewById(R.id.node_title);
            TextView content = (TextView) view.findViewById(R.id.node_content);

            title.setText(((MyActivity.RssNode) getItem(i)).title);
            content.setText(Html.fromHtml(((MyActivity.RssNode) getItem(i)).summary));

            return view;
        }
    }

    public class RssGrabbingProcess extends AsyncTask<String, Void, RssNode[]> {

        @Override
        protected RssNode[] doInBackground(String ... args) {


            RssNode result[] = new RssNode[1];
            result[0] = new RssNode("Failed during downloading", "check the internet connection", "");

            Document grabbedDoc;

            try {
                grabbedDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse((InputStream) (new URL(args[0])).getContent());
                grabbedDoc.getDocumentElement().normalize();

            } catch (Exception e) {
                result[0].summary = e.getMessage();
                return result;
            }


            final String ChannelLabel = ((Element) grabbedDoc.getElementsByTagName("channel").item(0)).getElementsByTagName("title").item(0).getFirstChild().getNodeValue();
            final String ChannelDescription = ((Element) grabbedDoc.getElementsByTagName("channel").item(0)).getElementsByTagName("description").item(0).getFirstChild().getNodeValue();
            RssNode top = new RssNode(ChannelLabel, ChannelDescription, "");

            NodeList list = grabbedDoc.getElementsByTagName("item");
            result = new RssNode[list.getLength()+1];
            int count = 0;

            for (int i = 0; i < list.getLength(); i++) {
                Node item = list.item(i);
                if (item.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) item;
                    String title = element.getElementsByTagName("title").item(0).getFirstChild().getNodeValue();
                    String summary = element.getElementsByTagName("description").item(0).getFirstChild().getNodeValue();
                    String url = element.getElementsByTagName("link").item(0).getFirstChild().getNodeValue();
                    result[count] = new RssNode(title, summary, url);
                    count++;
                }
            }
            result[list.getLength()] = top;

            return result;
        }


        @Override
        protected void onPostExecute(RssNode[] s) {
            super.onPostExecute(s);

            for (int i = 0; i < s.length; i++) {
                ((RssAdapter<RssNode>) getListAdapter()).add(s[i]);
            }

            TextView ChannelLabel = (TextView) findViewById(R.id.channel_label);
            TextView ChannelDescription = (TextView) findViewById(R.id.channel_description);

            ChannelLabel.setText(s[s.length-1].title);
            ChannelDescription.setText(s[s.length-1].summary);
        }

    }

}
