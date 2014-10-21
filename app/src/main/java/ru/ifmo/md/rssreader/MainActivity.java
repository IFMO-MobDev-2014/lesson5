package ru.ifmo.md.rssreader;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.impl.cookie.DateUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends Activity {

    private EditText urlText;
    private ListView newsList;
    private Handler h;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        urlText = (EditText) findViewById(R.id.urlText);
        newsList = (ListView) findViewById(R.id.newsList);
        h = new Handler();
    }

    public void okClicked(View view) {
        String stringUrl = urlText.getText().toString();
        XmlTask xmlTask = new XmlTask();
        xmlTask.execute(stringUrl);
    }

    private class XmlTask extends AsyncTask<String, Void, Void> {

        private final ArrayList<RssEntry> entries = new ArrayList<RssEntry>();

        @Override
        protected Void doInBackground(String... stringUrl) {
            try {
                final URL url = new URL(stringUrl[0]);
                XmlPullParser xpp = XmlPullParserFactory.newInstance().newPullParser();

                xpp.setInput(url.openStream(), null);
                int eventType = xpp.getEventType();
                String tag = null;
                String title = null;
                String description = null;
                String link = null;
                Date pubDate = null;
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_TAG) {
                        tag = xpp.getName();
                    } else if (eventType == XmlPullParser.END_TAG) {
                        if (xpp.getName().equals("item") || xpp.getName().equals("entry")) {
                            entries.add(new RssEntry(title, description, link, pubDate));
                        }
                        tag = null;
                    } else if (eventType == XmlPullParser.TEXT) {
                        if ("title".equals(tag)) {
                            title = xpp.getText();
                        } else if ("description".equals(tag)) {
                            description = xpp.getText();
                        } else if ("link".equals(tag)) {
                            link = xpp.getText();
                        } else if ("pubDate".equals(tag)) {
                            pubDate = DateUtils.parseDate(xpp.getText());
                        }
                    }
                    eventType = xpp.next();
                }
            } catch (Exception e) {
                h.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), R.string.exception, Toast.LENGTH_SHORT).show();
                    }
                });
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            newsList.setAdapter(new RssEntryAdapter(entries));
            newsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int ind, long l) {
                    Intent webIntent = new Intent(getApplicationContext(), WebActivity.class);
                    webIntent.putExtra("link", entries.get(ind).link);
                    webIntent.putExtra("title", entries.get(ind).title);
                    webIntent.putExtra("pubDate", android.text.format.DateUtils.getRelativeDateTimeString(
                            getApplicationContext(),
                            entries.get(ind).pubDate.getTime(),
                            android.text.format.DateUtils.MINUTE_IN_MILLIS,
                            android.text.format.DateUtils.WEEK_IN_MILLIS,
                            android.text.format.DateUtils.FORMAT_ABBREV_RELATIVE));
                    startActivity(webIntent);
                }
            });
        }
    }
}