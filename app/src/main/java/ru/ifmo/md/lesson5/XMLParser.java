package ru.ifmo.md.lesson5;

import android.util.Log;

import org.xml.sax.InputSource;

import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by Svet on 19.10.2014.
 */
public class XMLParser {

    static ArrayList<Message> parse(String source, String s) {
        ArrayList<Message> res = new ArrayList<Message>();
        SAXParserFactory factory = SAXParserFactory.newInstance();
        try {
            SAXParser parser = factory.newSAXParser();
            XmlHandler handler = new XmlHandler(source);
            InputSource input = new InputSource(new StringReader(s));
            parser.parse(input, handler);
            res = handler.getValues();
        } catch(Exception e) {
            e.printStackTrace();
            Log.e("ERROR", "Error in XmlParser");
        }
        return res;
    }
}
