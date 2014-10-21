package com.example.kirill.rssreader;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import java.net.URL;

/**
 * Created by Kirill on 19.10.2014.
 */
public class RSSLoader {

    Context mContext;

    public RSSLoader(Context mContext) {
        this.mContext = mContext;
    }

    public static boolean isOnline(Context mContext) {
        ConnectivityManager cm =
                (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    public String load(String urlString) {

        InputStream is = null;
        String rv = null;
        URL url = null;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {}
        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            is = urlConnection.getInputStream();
            rv = new Scanner(is, "windows-1251").useDelimiter("\\A").next();
            byte[] temp = convertEncoding(rv.getBytes("windows-1251"), "windows-1251", "UTF-8");
            rv = new String(temp, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();
        }
        return rv;
    }

    public static byte[] convertEncoding(byte[] bytes, String from, String to) throws UnsupportedEncodingException {
        return new String(bytes, from).getBytes(to);
    }
}
