package com.example.alexey.lesson5;

/**
 * Created by Alexey on 20.10.2014.
 */

import android.content.ContentValues;
import android.util.Log;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.InputStream;
import java.util.Vector;

public class XMLParser {
    ContentValues cv;

    public void  parse(InputStream c) {

        try {
            cv = new ContentValues();
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            DefaultHandler handler = new DefaultHandler() {

                boolean bfname = false;
                boolean blname = false;
                boolean bnname = false;


                public void startElement(String uri, String localName,String qName,
                                         Attributes attributes) throws SAXException {



                    if (qName.equalsIgnoreCase("TITLE")) {
                        bfname = true;
                    }

                    if (qName.equalsIgnoreCase("description")) {
                        blname = true;
                    }

                    if (qName.equalsIgnoreCase("LINK")) {
                        bnname = true;
                    }


                }

                public void endElement(String uri, String localName,
                                       String qName) throws SAXException {


                }

                public void characters(char ch[], int start, int length) throws SAXException {

                    if (bfname) {
                        cv.put(DataBase.POSTNAME, new String(ch, start, length));
                        bfname = false;
                    }

                    if (blname) {
                        blname = false;
                    }

                    if (bnname) {
                        Log.i("get",new String(ch, start, length));
                        cv.put(DataBase.EMAIL, new String(ch, start, length));

                        MyActivity.sqdb.insert(DataBase.TABLE_NAME, null, cv);
                        cv = new ContentValues();
                        bnname = false;
                    }

                }

            };

            saxParser.parse(c, handler);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }



}