package ru.eugene.listviewpractice2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;


public class RssFeed extends Activity {
    public static final String LOG = "RssFeed";
    private ListView descriptions = null;
    private Context context;
    List<ItemObject> itemsData = null;
    HandlerRSS handlerRSS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rss_feed);
        context = this;
        descriptions = (ListView) findViewById(R.id.descriptions);

        ConnectivityManager cntManager = (ConnectivityManager)
                getSystemService(CONNECTIVITY_SERVICE);

        NetworkInfo info = cntManager.getActiveNetworkInfo();

        if (info != null && info.isConnected()) {
            String urlAddress = getIntent().getStringExtra("url");
            new DownloadWebpageTask().execute(urlAddress);
        } else {
            Log.i(LOG, "No network connection available");
        }

        descriptions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (itemsData != null) {
                    Intent showFeed = new Intent(context, ShowFeed.class);
                    showFeed.putExtra("url", itemsData.get(i).getLink());
                    startActivity(showFeed);
                }
            }
        });
    }

    private class DownloadWebpageTask extends AsyncTask<String, Void, Void> {
        Boolean somethingGoWrong = false;

        @Override
        protected Void doInBackground(String... urls) {

            // params comes from the execute() call: params[0] is the url.
            try {
                InputStream result = downloadUrl(urls[0]);
                SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
                SAXParser saxParser = saxParserFactory.newSAXParser();
                handlerRSS = new HandlerRSS();

                Reader isr = new InputStreamReader(result);
                InputSource is = new InputSource();
                is.setCharacterStream(isr);
//                is.setEncoding("windows-1251");

                saxParser.parse(is, handlerRSS);
                return null;
            } catch (Exception e) {
                Toast toast = Toast.makeText(getApplicationContext(), "IOException", Toast.LENGTH_SHORT);
                toast.show();
                somethingGoWrong = true;

                return null;
            }
        }

        @Override
        protected void onPostExecute(Void a) {
            itemsData = handlerRSS.getItemsData();
            List<String> itemsDescription = new ArrayList<String>();
            for (ItemObject it : itemsData) {
                itemsDescription.add(it.getDescription());
            }

            descriptions.setAdapter(new MyAdapter(context, R.layout.rowlayout, itemsDescription));

        }
    }

    private InputStream downloadUrl(String myurl) throws IOException {
        InputStream is = null;

        URL url = new URL(myurl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000);
        conn.setConnectTimeout(15000);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        conn.connect();
        is = conn.getInputStream();

        return is;
    }
}
