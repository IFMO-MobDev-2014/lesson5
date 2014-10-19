package ru.ifmo.md.lesson5;

import android.app.ListActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;


public class MyActivity extends ListActivity {
    EditText input;
    SimpleDateFormat format;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        format = new SimpleDateFormat(getResources().getString(R.string.date_format), Locale.ENGLISH);
        input = (EditText) findViewById(R.id.editText1);

        input.setOnKeyListener(new View.OnKeyListener() {

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    updateList();
                    return true;
                }
                return false;
            }
        });
    }

    public void onClick(View view) {
        updateList();
    }

    public void updateList() {
        String rssStringURL = input.getText().toString();
        if(!rssStringURL.startsWith("http://")){
            rssStringURL = "http://" + rssStringURL;
        }
        new DownloadAndParseXMLTask().execute(rssStringURL);
        input.setText("");
    }

    class DownloadAndParseXMLTask extends AsyncTask<String, Void, ArrayList<RSSItem>> {
        private boolean canConnect = true;
        private boolean canOpen = true;
        private boolean canParse = true;
        private boolean isSorted = true;

        @Override
        protected ArrayList<RSSItem> doInBackground(String... arg0) {
            String rssStringURL = arg0[0];
            ArrayList<RSSItem> rssItems = new ArrayList<RSSItem>();
            StringBuilder content = new StringBuilder();
            publishProgress();

            try {
                URL rssURL = new URL(rssStringURL);
                URLConnection urlConnection = rssURL.openConnection();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    content.append(line + "\n");
                }
                bufferedReader.close();

                SAXParserFactory mySAXParserFactory = SAXParserFactory.newInstance();
                SAXParser mySAXParser = mySAXParserFactory.newSAXParser();
                XMLReader myXMLReader = mySAXParser.getXMLReader();
                RSSHandler myRSSHandler = new RSSHandler();
                myXMLReader.setContentHandler(myRSSHandler);
                InputSource myInputSource = new InputSource(new StringReader(content.toString()));
                myXMLReader.parse(myInputSource);

                rssItems = myRSSHandler.getRssItems();
            } catch (SAXException e) {
                canParse = false;
                e.printStackTrace();
                return null;
            } catch (ParserConfigurationException e) {
                canParse = false;
                e.printStackTrace();
                return null;
            } catch (MalformedURLException e) {
                canConnect = false;
                e.printStackTrace();
                return null;
            } catch (IOException e) {
                canOpen = false;
                e.printStackTrace();
                return null;
            }

            if (dateParse(rssItems.get(0).getPubdate()) != null) {
                Collections.sort(rssItems, new RSSItemComparator());
                Collections.reverse(rssItems);
            } else {
                isSorted = false;
            }

            return rssItems;
        }

        @Override
        protected void onProgressUpdate(Void... arg0) {
            showToast(R.string.waiting, Toast.LENGTH_SHORT);
        }

        @Override
        protected void onPostExecute(ArrayList<RSSItem> result) {
            if (!isSorted) {
                showToast(R.string.cannot_parse_date, Toast.LENGTH_LONG);
            }
            if (!canConnect) {
                showToast(R.string.cannot_connect, Toast.LENGTH_LONG);
            } else if (!canOpen) {
                showToast(R.string.cannot_open, Toast.LENGTH_LONG);
            } else if (!canParse) {
                showToast(R.string.cannot_parse, Toast.LENGTH_LONG);
            } else {
                ArrayAdapter<RSSItem> adapter = new RSSArrayAdapter(getApplicationContext(), result);
                setListAdapter(adapter);
            }
        }
    }

    class RSSItemComparator implements Comparator<RSSItem> {
        public int compare(RSSItem firstItem, RSSItem secondItem) {
            Date firstDate = dateParse(firstItem.getPubdate());
            Date secondDate = dateParse(secondItem.getPubdate());

            return firstDate.compareTo(secondDate);
        }
    }

    public Date dateParse(String str) {
        try {
            return format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void showToast(int message, int time) {
        Toast toast = Toast.makeText(getApplicationContext(), message, time);
        toast.show();
    }
}