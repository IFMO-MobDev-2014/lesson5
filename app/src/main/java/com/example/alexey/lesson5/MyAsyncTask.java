package com.example.alexey.lesson5;

import android.content.res.XmlResourceParser;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.SAXParser;

class MyAsyncTask extends AsyncTask<String, Integer, Integer> {
    @Override
    protected void onProgressUpdate(Integer... progress) {

    }

    @Override
    protected void onPostExecute(Integer n) {

    }
    public XMLParser xp=new XMLParser();


    @Override
    protected Integer doInBackground(String... parameter) {

        int result = 1;
        URL url = null;
        try {
            url = new URL("http://feeds.bbci.co.uk/news/rss.xml");

            URLConnection urlConnection = url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());


            xp.parse(in);

            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        MyActivity.h = 1;
        return result;
    }

    private int min(int size, int size1) {
        if (size<size1) return size;
        return size1;
    }
}