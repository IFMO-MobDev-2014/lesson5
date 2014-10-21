package ru.ifmo.ctddev.soloveva.rssreader;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import ru.ifmo.ctddev.soloveva.rssreader.rss.Entry;
import ru.ifmo.ctddev.soloveva.rssreader.rss.RssParser;


public class MainActivity extends Activity {
    private static final String RSS_URL = "http://stackoverflow.com/feeds/tag/android";

    private ListView view;
    private ArrayAdapter<Object> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = new ListView(this);
        adapter = new ArrayAdapter<>(MainActivity.this, R.layout.item, new ArrayList<>());
        view.setAdapter(adapter);
        setContentView(view);
        new GetRssTask().execute(RSS_URL);
    }

    private class GetRssTask extends AsyncTask<String, Void, List<Entry>> {
        private Exception exception;

        @Override
        protected List<Entry> doInBackground(String... params) {
            RssParser parser = null;
            try {
                parser = new RssParser();
                return parser.fetchEntries(params[0]);
            } catch (IOException | SAXException | ParserConfigurationException e) {
                exception = e;
            } finally {
                if (parser != null) {
                    try {
                        parser.close();
                    } catch (IOException e) {
                        Log.e("RSS", "can't close", e);
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(final List<Entry> entries) {
            if (exception != null) {
                Toast.makeText(MainActivity.this, R.string.error, Toast.LENGTH_LONG).show();
                Log.e("RSS", "can't read", exception);
            } else {
                adapter.addAll(entries);
                view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(MainActivity.this, ContentActivity.class);
                        intent.putExtra(ContentActivity.HTML_KEY, entries.get(position).getHtml());
                        startActivity(intent);
                    }
                });
            }
        }
    }
}
