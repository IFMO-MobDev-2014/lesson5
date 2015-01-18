package com.example.dima_2.lesson5;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import java.io.*;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by Dima_2 on 08.01.2015.
 */
public class RSSLoader extends IntentService {
    public String link;
    public RSSList list = new RSSList();

    public RSSLoader() {
        super("RSSLoader");
    }

    public RSSLoader(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String intentUrl = intent.getStringExtra("url");
        try {
            URL url = new URL(intentUrl);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            String contentType = connection.getHeaderField("Content-Type");
            String encoding = "utf-8";
            if (contentType != null && contentType.contains("charset=")) {
                Matcher matcher = Pattern.compile("charset=([^\\s]+)").matcher(contentType);
                matcher.find();
                encoding = matcher.group(1);
            }
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, encoding);
            InputSource inputSource = new InputSource(inputStreamReader);
            SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
            SAXHandler handler = new SAXHandler();
            if (list == null || handler == null) {
                throw new IOException();
            }
            parser.parse(inputSource, handler);
            if (list == null || handler == null) {
                throw new IOException();
            }
            list.list = handler.rssItems;
            link = handler.channelName;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(Table.COLUMN_NAME, link);
        contentValues.put(Table.COLUMN_LINK, intentUrl);

        Uri nChannel = getContentResolver().insert(DatabaseContentProvider.CONTENT_URI_CHANNELS, contentValues);
        Cursor cursor = getContentResolver().query(nChannel, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int newChannelID = cursor.getInt(cursor.getColumnIndexOrThrow(Table.COLUMN_ID));
            if (list != null) {
                    for (RSSElement element : list.list) {
                            contentValues = element.getContentValues();
                            contentValues.put(ItemsTable.COLUMN_CHANNEL_ID, newChannelID);
                            getContentResolver().insert(DatabaseContentProvider.CONTENT_URI_ITEMS, contentValues);
                        }
                }
            cursor.close();
        }
    }
}
