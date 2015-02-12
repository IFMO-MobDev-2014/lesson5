package com.example.rssreader;

import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ListView;

import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by Яна on 04.02.2015.
 */
public class MyLoader extends AsyncTask<String, Void, ArrayList<Item> > {

    MyListAdapter adapter;
    MyLoader(MyListAdapter adapter) {
        this.adapter = adapter;
    }
    @Override
    protected ArrayList<Item> doInBackground(String... params) {
        try {
            URL target = new URL(params[0]);
            ArrayList<Item> items = new Parser(target).parse();
            Log.i("loader", items.size() + "");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<Item> items){
        adapter.setItems(items);
    }
}
