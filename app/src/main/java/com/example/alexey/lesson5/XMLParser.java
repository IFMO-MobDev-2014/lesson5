package com.example.alexey.lesson5;

/**
 * Created by Alexey on 20.10.2014.
 */
import android.util.Log;
import android.widget.ImageView;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.InputStream;
import java.util.Vector;

public class XMLParser {

    public void  parse(InputStream c) {

        try {

            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();

            DefaultHandler handler = new DefaultHandler() {

                boolean bfname = false;
                boolean blname = false;
                boolean bnname = false;
                boolean bsalary = false;

                public void startElement(String uri, String localName,String qName,
                                         Attributes attributes) throws SAXException {

//                    System.out.println("Start Element :" + qName);

                    if (qName.equalsIgnoreCase("TITLE")) {
                        bfname = true;
                    }

                    if (qName.equalsIgnoreCase("description")) {
                        blname = true;
                    }

                    if (qName.equalsIgnoreCase("link")) {
                        bnname = true;
                    }

                    if (qName.equalsIgnoreCase("SALAsdsdRY")) {
                        bsalary = true;
                    }

                }

                public void endElement(String uri, String localName,
                                       String qName) throws SAXException {

                   // System.out.println("End Element :" + qName);

                }

                public void characters(char ch[], int start, int length) throws SAXException {

                    if (bfname) {
                        MyActivity.a.addElement(new String(ch, start, length));
                        //Log.i("title",new String(ch, start, length));
                        bfname = false;
                    }

                    if (blname) {
                        MyActivity.b.addElement(new String(ch, start, length));
                        //Log.i("id",new String(ch, start, length));
                        blname = false;
                    }

                    if (bnname) {
                        MyActivity.c.addElement(new String(ch, start, length));
                        bnname = false;
                    }

                    if (bsalary) {
                        System.out.println("Salary : " + new String(ch, start, length));
                        bsalary = false;
                    }

                }

            };

            saxParser.parse(c, handler);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }



}