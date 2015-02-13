package com.example.rssreader;

import android.content.Context;
import android.os.AsyncTask;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by Яна on 04.02.2015.
 */
public class MyLoader extends AsyncTask<String, Void, ArrayList<Item>> {

    MyListAdapter adapter;
    Context context;

    MyLoader(MyListAdapter adapter, Context context) {
        this.adapter = adapter;
        this.context = context;
    }

    @Override
    protected ArrayList<Item> doInBackground(String... params) {
        URL target;
        try {
            target = new URL(params[0]);
            return new Parser(target).parse();
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<Item> items) {
        adapter.setItems(items);
        adapter.notifyDataSetChanged();
    }
}
