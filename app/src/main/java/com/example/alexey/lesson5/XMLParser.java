package com.example.alexey.lesson5;

/**
 * Created by Alexey on 20.10.2014.
 */

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


                public void startElement(String uri, String localName,String qName,
                                         Attributes attributes) throws SAXException {



                    if (qName.equalsIgnoreCase("TITLE")) {
                        bfname = true;
                    }

                    if (qName.equalsIgnoreCase("description")) {
                        blname = true;
                    }

                    if (qName.equalsIgnoreCase("link")) {
                        bnname = true;
                    }


                }

                public void endElement(String uri, String localName,
                                       String qName) throws SAXException {


                }

                public void characters(char ch[], int start, int length) throws SAXException {

                    if (bfname) {
                        MyActivity.a.addElement(new String(ch, start, length));
                        bfname = false;
                    }

                    if (blname) {
                        MyActivity.b.addElement(new String(ch, start, length));
                        blname = false;
                    }

                    if (bnname) {
                        MyActivity.c.addElement(new String(ch, start, length));
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