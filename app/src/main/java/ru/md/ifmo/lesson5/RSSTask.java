package ru.md.ifmo.lesson5;


import android.os.AsyncTask;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class RSSTask extends AsyncTask <String, Integer, ArrayList<ItemMaster>> {

    Exception exception = null;

    @Override
    protected ArrayList<ItemMaster> doInBackground(String... strings) {
        String link = strings[0];
        try {

            SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
            SAXParser saxParser = saxParserFactory.newSAXParser();
            XMLReader xmlReader = saxParser.getXMLReader();

            ItemXMLHandler myXMLHandler = new ItemXMLHandler();
            xmlReader.setContentHandler(myXMLHandler);

            StringBuilder builder = new StringBuilder();

            URLConnection connection = new URL(link).openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = reader.readLine())!=null) {
                builder.append(line+"\n");
            }
            reader.close();
            InputSource inStream = new InputSource(new StringReader(builder.toString()));

            xmlReader.parse(inStream);


            return myXMLHandler.getItemsList();


        }  catch (Exception e) {
            this.exception = e;
        }
        return null;
    }

}
