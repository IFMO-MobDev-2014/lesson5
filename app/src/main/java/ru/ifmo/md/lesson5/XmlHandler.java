package ru.ifmo.md.lesson5;

import android.util.Log;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

/**
 * Created by Svet on 20.10.2014.
 */
public class XmlHandler extends DefaultHandler {
    ArrayList<Message> values;
    Message current;
    String input;
    String source;

    XmlHandler(String source) {
        this.source = source;
        current = null;
    }

    public ArrayList<Message> getValues() {
        return values;
    }

    @Override
    public void characters(char [] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
        if(current != null) {
            input = input.concat(new String(ch, start, length));
        }
    }

    @Override
    public void endElement(String uri, String localName, String name) throws SAXException{
        super.endElement(uri, localName, name);
        if(current != null) {
            if(localName.equalsIgnoreCase("item")) {
                values.add(current);
                current = null;
            }
            if(localName.equalsIgnoreCase("title")) {
                current.title = input.toString();
            }
            if(localName.equalsIgnoreCase("description")) {
                current.description = input.toString();
            }
            if(localName.equalsIgnoreCase("link")) {
                current.link = input.toString();
            }
            input = "";
        }
    }

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
        values = new ArrayList<Message>();
        input = "";
    }

    @Override
    public void startElement(String uri, String localName, String name,
                             Attributes attributes) throws SAXException {
        super.startElement(uri, localName, name, attributes);
        if(localName.equalsIgnoreCase("item")) {
//            Log.i("MY", "item created");
            current = new Message();
            current.source = source;
        }
    }
}
